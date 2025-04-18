package com.onez.controller.admin;

import java.io.IOException;

import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.service.UpdateService;
import com.onez.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for handling user update operations.
 * 
 * This servlet processes HTTP requests for updating user information.
 * It interacts with the UpdateService to perform database operations and 
 * forwards requests to the appropriate JSP page for user interaction.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/userUpdate" })
public class UpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Service for updating user information
    private UpdateService updateService;

    /**
     * Default constructor initializes the UpdateService instance.
     */
    public UpdateController() {
        this.updateService = new UpdateService();
    }

    /**
     * Handles HTTP GET requests by retrieving user information from the session 
     * and forwarding the request to the update JSP page.
     * 
     * @param req The HttpServletRequest object containing the request data.
     * @param resp The HttpServletResponse object used to return the response.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException If an input or output error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Retrieve and set user information from the session if available
        if (req.getSession().getAttribute("user") != null) {
            UserModel user = (UserModel) SessionUtil.getAttribute(req, "user");
            SessionUtil.removeAttribute(req, "user");
            req.setAttribute("user", user);
        }

        // Forward to the update JSP page
        req.getRequestDispatcher("/WEB-INF/page/admin/update.jsp").forward(req, resp);
    }

    /**
     * Handles HTTP POST requests for updating user information.
     * Retrieves user data from the request parameters, updates the user record 
     * in the database using UpdateService, and redirects to the dashboard or 
     * handles update failure.
     * 
     * @param req The HttpServletRequest object containing the request data.
     * @param resp The HttpServletResponse object used to return the response.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException If an input or output error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Retrieve user data from request parameters
        int userId = Integer.parseInt(req.getParameter("userId"));
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String number = req.getParameter("number");

        // Create ProductModel object
        ProductModel product = new ProductModel();
        product.setName(req.getParameter("subject"));

        // Create UserModel object with updated data
        UserModel user = new UserModel(userId, firstName, 
                lastName, product, email, number);

        // Attempt to update user information in the database
        Boolean result = updateService.updateUserInfo(user);
        if (result != null && result) {
            resp.sendRedirect(req.getContextPath() + "/modifyUsers"); // Redirect to dashboard on success
        } else {
            req.getSession().setAttribute("user", user);
            handleUpdateFailure(req, resp, result); // Handle failure
        }
    }

    /**
     * Handles update failures by setting an error message and forwarding the request 
     * back to the update page.
     * 
     * @param req The HttpServletRequest object containing the request data.
     * @param resp The HttpServletResponse object used to return the response.
     * @param loginStatus Indicates the result of the update operation.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException If an input or output error occurs.
     */
    private void handleUpdateFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
            throws ServletException, IOException {
        // Determine error message based on update result
        String errorMessage;
        if (loginStatus == null) {
            errorMessage = "Our server is under maintenance. Please try again later!";
        } else {
            errorMessage = "Update Failed. Please try again!";
        }
        req.setAttribute("error", errorMessage);
        req.getRequestDispatcher(req.getContextPath() + "/update").forward(req, resp);
    }
}