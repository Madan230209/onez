package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.onez.model.FeedbackModel;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.service.FeedbackService;
import com.onez.service.ProductService;
import com.onez.util.RedirectionUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/feedback", "/submitFeedback" })
public class FeedbackController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final FeedbackService feedbackService = new FeedbackService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer productId = request.getParameter("productId") != null ? 
            Integer.parseInt(request.getParameter("productId")) : null;

        if (productId != null) {
            request.setAttribute("product", productService.getProductById(productId));
        }
        request.getRequestDispatcher(RedirectionUtil.feedbackUrl).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        Integer productId = request.getParameter("productId") != null ? 
            Integer.parseInt(request.getParameter("productId")) : null;
        int rating = Integer.parseInt(request.getParameter("rating"));
        String feedbackDetails = request.getParameter("feedbackDetails");

        if (userId == null || productId == null) {
            request.setAttribute("error", "User or product information missing");
            request.getRequestDispatcher(RedirectionUtil.feedbackUrl).forward(request, response);
            return;
        }

        // Create minimal UserModel with just the ID
        UserModel user = new UserModel();
        user.setId(userId);

        ProductModel product = productService.getProductById(productId);

        FeedbackModel feedback = new FeedbackModel();
        feedback.setRating(rating);
        feedback.setFeedbackDetails(feedbackDetails);
        feedback.setUser(user);
        feedback.setProduct(product);

        boolean feedbackSubmitted = feedbackService.submitFeedback(feedback);
        
        if (feedbackSubmitted) {
            request.getSession().setAttribute("success", "Thank you for your feedback!");
        } else {
            request.getSession().setAttribute("error", "Failed to submit feedback. Please try again.");
        }
        
        response.sendRedirect(request.getContextPath() + "/products?productId=" + productId);
    }
}