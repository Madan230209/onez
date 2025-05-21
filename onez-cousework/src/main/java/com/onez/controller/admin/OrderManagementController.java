package com.onez.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.onez.model.OrderModel;
import com.onez.service.OrderService;
import com.onez.util.RedirectionUtil;

/**
 * OrderManagementController is responsible for handling admin/orders requests. It interacts with
 * the OrderService to manage orders.
 */
@WebServlet(asyncSupported = true, urlPatterns ={"/admin/orders"})
public class OrderManagementController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
	 * Handles GET requests by retrieving orders information and 
	 * forward request to the orderManagement page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (OrderService orderService = new OrderService()) {
            List<OrderModel> orders = orderService.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher(RedirectionUtil.orderManagementUrl).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            request.getRequestDispatcher(RedirectionUtil.orderManagementUrl).forward(request, response);
            e.printStackTrace();
        }
    }

    /**
	 * Handles POST requests by retrieving actions value and update 
	 * status of order and forward response to /admin/orders .
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String newStatus = request.getParameter("status");
                
                try (OrderService orderService = new OrderService()) {
                    boolean updated = orderService.updateOrderStatus(orderId, newStatus);
                    if (updated) {
                        request.getSession().setAttribute("message", 
                            "Order #" + orderId + " status updated to " + newStatus);
                    } else {
                        request.getSession().setAttribute("error", 
                            "Failed to update order #" + orderId + ". It may not exist.");
                    }
                } catch (SQLException e) {
                    request.getSession().setAttribute("error", 
                        "Database error updating order: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid order ID format");
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}