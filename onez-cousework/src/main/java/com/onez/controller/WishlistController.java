package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import com.onez.model.UserModel;
import com.onez.service.WishlistService;
import com.onez.util.RedirectionUtil;

/**
 * WishlistController is responsible for handling wishlist, wishlist/add, wishlist/remove requests. It interacts with
 * the WishlistService to manage products to and from wishlist.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/wishlist", "/wishlist/add", "/wishlist/remove" })
public class WishlistController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WishlistService wishlistService;
      
    /**
	 * Constructor initializes the WishlistService.
	 */
    public WishlistController() {
        super();
        try {
			this.wishlistService = new WishlistService();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
	 * Handles GET requests to the wishlist page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(RedirectionUtil.loginUrl + "?redirect=wishlist");
            return;
        }
        
        try {
			request.setAttribute("wishlist", wishlistService.getWishlistByUser(user));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        request.getRequestDispatcher(RedirectionUtil.wishlistUrl).forward(request, response);
    }

    
    /**
	 * Handles POST requests for wishlist/add and wishlist/remove.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");
        String path = request.getServletPath();
        
        if (user == null) {
            response.sendRedirect(RedirectionUtil.loginUrl);
            return;
        }
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            if (path.equals("/wishlist/add")) {
                try {
					wishlistService.addToWishlist(user, productId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else if (path.equals("/wishlist/remove")) {
                try {
					wishlistService.removeFromWishlist(user, productId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            response.sendRedirect(path.equals("/wishlist/add") ? 
            	    request.getContextPath() + "/wishlist": // Stay on same page after add
            	    request.getContextPath() + "/wishlist"); // Go to wishlist after remove
        } catch (NumberFormatException e) {
            response.sendRedirect(RedirectionUtil.homeUrl);
        }
    }
}