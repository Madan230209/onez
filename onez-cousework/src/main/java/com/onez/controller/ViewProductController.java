package com.onez.controller;

import com.onez.model.ProductModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.onez.service.ProductService;
import com.onez.util.RedirectionUtil;

/**
 * ViewProductController is responsible for handling viewProduct requests. It interacts with
 * the ProdductService to authenticate users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/viewProduct" })
public class ViewProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ProductService productService = new ProductService();

    /**
	 * Handles GET requests to the viewProduct page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        List<ProductModel> productList = productService.getAllProducts();

        request.setAttribute("products", productList);
        
        List<ProductModel> recentProductList = productService.getRecentProducts();

        //Set in request scope for JSP
        request.setAttribute("recentProducts", recentProductList);
        
        int productId = Integer.parseInt(request.getParameter("productId"));
        ProductModel product = productService.getProductById(productId);
        
        if (product != null) {
            request.setAttribute("product", product);
            request.getRequestDispatcher(RedirectionUtil.viewProductUrl).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/home?error=ProductNotFound");
        }
    }
}
