package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.onez.model.CartModel;
import com.onez.model.OrderModel;
import com.onez.service.OrderService;
import com.onez.util.RedirectionUtil;

/**
 * OrderController is responsible for handling order requests. It interacts with
 * the OrderService to manage order data according to users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { 
    "/order-history",  // Shows past orders
    "/order",          // Shows checkout page
    "/processOrder"    // Processes new orders
})
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
	 * Handles GET requests to the order/ orderHistory page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if ("/order-history".equals(path)) {
            showOrderHistory(request, response);
        } else if ("/order".equals(path)) {
            showCheckoutPage(request, response);
        }
    }

    /** 			
	 * Handles POST requests for processing order.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/processOrder".equals(request.getServletPath())) {
            processNewOrder(request, response);
        }
    }

    /**
	 * Shows order history by setting attributes and forwarding to the orderHistory
	 * page.
	 *
	 * @param req         HttpServletRequest object
	 * @param resp        HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        // Check for and transfer success message from session to request
        HttpSession session = request.getSession();
        String orderSuccess = (String) session.getAttribute("orderSuccess");
        if (orderSuccess != null) {
            request.setAttribute("orderSuccess", orderSuccess);
            session.removeAttribute("orderSuccess"); // Remove after displaying
        }

        try (OrderService orderService = new OrderService()) {
            List<OrderModel> orders = orderService.getUserOrders(userId);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher(RedirectionUtil.orderHistoryUrl).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading order history: " + e.getMessage());
            request.getRequestDispatcher(RedirectionUtil.orderHistoryUrl).forward(request, response);
        }
    }

    /**
	 * Shows order details by setting attributes and forwarding to the checkout/ order
	 * page.
	 *
	 * @param req         HttpServletRequest object
	 * @param resp        HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    private void showCheckoutPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try (OrderService orderService = new OrderService()) {
            CartModel cart = orderService.getCartForUser(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                request.setAttribute("error", "Your cart is empty");
            }
            request.setAttribute("cart", cart);
            request.getRequestDispatcher(RedirectionUtil.orderUrl).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading cart: " + e.getMessage());
            request.getRequestDispatcher(RedirectionUtil.orderUrl).forward(request, response);
        }
    }

    /**
	 * Processes new orders by setting attributes and forwarding to the orderHistory 
	 * page for success and showCheckoutPage method for error.
	 *
	 * @param req         HttpServletRequest object
	 * @param resp        HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    private void processNewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        String paymentMethod = request.getParameter("paymentMethod");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try (OrderService orderService = new OrderService()) {
            OrderModel order = orderService.processOrder(userId, paymentMethod);
            
            if (order == null) {
                request.setAttribute("error", "Failed to process your order");
                showCheckoutPage(request, response);
            } else {
                // Clear the cart from session after successful order
                request.getSession().removeAttribute("cart");
                
                // Set success message with order details (as a String)
                String successMessage = "Your order #" + order.getOrderId() + " has been placed successfully!";
                request.getSession().setAttribute("orderSuccess", successMessage);
                
                // Redirect to order history
                response.sendRedirect(request.getContextPath() + "/order-history");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Order processing failed: " + e.getMessage());
            showCheckoutPage(request, response);
        }
    }
}