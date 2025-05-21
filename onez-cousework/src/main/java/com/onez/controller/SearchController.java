package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.onez.model.ProductModel;
import com.onez.service.ProductService;
import com.onez.util.RedirectionUtil;

/**
 * SearchController is responsible for handling search requests. It interacts with
 * the ProductService to filter products.
 */

@WebServlet(asyncSupported = true, urlPatterns = { "/search" })
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ProductService productService = new ProductService();
    
    /**
	 * Handles GET requests to the search page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String search = request.getParameter("search");
        List<ProductModel> productList = new ArrayList<>();
        
        if (search != null && !search.trim().isEmpty()) {
            productList = productService.getProductsBySearch(search);
        }
        
        request.setAttribute("products", productList);
        request.getRequestDispatcher(RedirectionUtil.searchUrl).forward(request, response);
	}

	/**
	 * Handles POST requests for search.
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
