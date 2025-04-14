package com.onez.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.service.DashboardService;

/**
 * Servlet implementation for handling dashboard-related HTTP requests.
 * 
 * This servlet manages interactions with the DashboardService to fetch user
 * information, handle updates, and manage user data. It forwards requests to
 * appropriate JSP pages or handles POST actions based on the request
 * parameters.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/dashboard" })
public class DashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Instance of DashboardService for handling business logic
	private DashboardService dashboardService;

	/**
	 * Default constructor initializes the DashboardService instance.
	 */
	public DashboardController() {
		this.dashboardService = new DashboardService();
	}

	/**
	 * Handles HTTP GET requests by retrieving user information and forwarding
	 * the request to the dashboard JSP page.
	 * 
	 * @param request  The HttpServletRequest object containing the request data.
	 * @param response The HttpServletResponse object used to return the response.
	 * @throws ServletException If an error occurs during request processing.
	 * @throws IOException      If an input or output error occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve all user information from the DashboardService
		request.setAttribute("userList", dashboardService.getRecentUsers());

		request.setAttribute("total", dashboardService.getTotalUsers());
		request.setAttribute("computing", dashboardService.getComputingUsers());
		request.setAttribute("multimedia", dashboardService.getMultimediaUsers());
		request.setAttribute("networking", dashboardService.getNetworkingUsers());

		// Forward the request to the dashboard JSP for rendering
		request.getRequestDispatcher("/WEB-INF/page/admin/dashboard.jsp").forward(request, response);
	}

}