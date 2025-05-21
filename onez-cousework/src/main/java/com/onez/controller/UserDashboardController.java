package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.onez.model.UserModel;
import com.onez.service.UserDashboardService;
import com.onez.util.RedirectionUtil;

/**
 * UserDashboardController is responsible for handling profile requests. It interacts with
 * the UserDashboardService to manipulate the logged user details.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/userDashboard" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 15     // 15 MB
)
public class UserDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDashboardService dashboardService;

    /**
	 * Constructor initializes the UserDashboardService.
	 */
    public UserDashboardController() {
        this.dashboardService = new UserDashboardService();
    }

    /**
	 * Handles GET requests to the userDashboard page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        UserModel user = dashboardService.getUserInfo(userId);
        if (user != null) {
            transferSessionMessagesToRequest(request);
            request.setAttribute("user", user);
            request.getRequestDispatcher(RedirectionUtil.userDashboardUrl).forward(request, response);
        } else {
            request.setAttribute("error", "User information not found");
            request.getRequestDispatcher(RedirectionUtil.userDashboardUrl).forward(request, response);
        }
    }

    /**
	 * Handles POST requests for user info update.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String number = request.getParameter("number");
            String dob = request.getParameter("dob");

            UserModel existingUser = dashboardService.getUserInfo(userId);
            String currentImage = (existingUser != null) ? existingUser.getImageUrl() : null;

            // Handle profile image upload
            Part filePart = request.getPart("profilePicture");
            String fileName = (filePart != null) ? Paths.get(filePart.getSubmittedFileName()).getFileName().toString() : null;

            String newFileName = null;
            if (filePart != null && fileName != null && !fileName.isEmpty()) {
                newFileName = "user_" + userId + "_" + System.currentTimeMillis() + "_" + fileName;

                // Upload path: /resources/user
                String uploadPath = getServletContext().getRealPath("") + File.separator + "resources" + File.separator + "user";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // creates the directory if not already there
                }

                filePart.write(uploadPath + File.separator + newFileName);
            }

            // Set updated user info
            UserModel updatedUser = new UserModel();
            updatedUser.setId(userId);
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setEmail(email);
            updatedUser.setNumber(number);

            if (dob != null && !dob.isEmpty()) {
                try {
                    updatedUser.setDob(java.time.LocalDate.parse(dob));
                } catch (Exception e) {
                    updatedUser.setDob(null);
                }
            }

            if (newFileName != null) {
                updatedUser.setImageUrl(newFileName);
            } else {
                updatedUser.setImageUrl(currentImage);
            }

            boolean success = dashboardService.updateUserInfo(updatedUser);

            if (success) {
                request.getSession().setAttribute("successMessage", "Profile updated successfully!");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update profile. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Something went wrong. Please check your inputs.");
        }

        response.sendRedirect(request.getContextPath() + "/userDashboard");
    }

    /**
	 * Transfers session messages to request by setting attributes and 
	 * setting success and error messages.
	 *
	 * @param req         HttpServletRequest object
	 */
    private void transferSessionMessagesToRequest(HttpServletRequest request) {
        String successMessage = (String) request.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            request.getSession().removeAttribute("successMessage");
        }

        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage");
        }
    }
}

