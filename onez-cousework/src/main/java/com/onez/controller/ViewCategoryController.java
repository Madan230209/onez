package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.onez.model.ProductModel;
import com.onez.service.ProductService;
import com.onez.util.RedirectionUtil;

/**
 * ViewCategoryController is responsible for handling categorizing requests. It interacts with
 * the ProductService to retrieve product details.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/viewCategory" })
public class ViewCategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService = new ProductService();

    /**
	 * Handles GET requests to the viewCategory page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
        List<ProductModel> productList;
        
        if (category != null && !category.isEmpty() && !category.equals("All")) {
            productList = productService.getProductsByCategory(category);
        } else {
            productList = productService.getSomeProducts();
        }
        
        request.setAttribute("products", productList);
        request.getRequestDispatcher(RedirectionUtil.viewCategoryUrl).forward(request, response);
    }

    /**
	 * Handles POST requests for viewCategory page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}