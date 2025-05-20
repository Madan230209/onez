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
 * Servlet implementation class Search
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/search" })
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ProductService productService = new ProductService();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String search = request.getParameter("search");
        List<ProductModel> productList = new ArrayList<>();
        
        if (search != null && !search.trim().isEmpty()) {
            productList = productService.getProductsBySearch(search);
        }
        
        request.setAttribute("products", productList);
        request.getRequestDispatcher(RedirectionUtil.searchUrl).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
