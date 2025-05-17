package com.onez.filter;

import java.io.IOException;

import com.onez.util.CookieUtil;
import com.onez.util.SessionUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";
    private static final String HOME = "/home";
    private static final String CATEGORY = "/viewCategory";
    private static final String DESCRIPTION = "/viewProduct";
    private static final String SEARCH = "/search";
    private static final String ROOT = "/";
    private static final String ORDER = "/order";
    private static final String ADD_ORDER = "/order-history";
    private static final String PROCESS_ORDER = "/processOrder";
    private static final String CART = "/cart";
    private static final String CARTADD = "/cart/add";
    private static final String CARTUPDATE = "/cart/update";
    private static final String CARTREMOVE = "/cart/remove";
    private static final String WISHLIST = "/wishlist";
    private static final String WISHLIST_ADD = "/wishlist/add";
    private static final String WISHLIST_DELETE = "/wishlist/remove";
    
    private static final String ORDER_HISTORY = "/orderHistory";
    private static final String ORDER_DELETE = "/orderHistory/delete";
    private static final String USER_DASHBOARD = "/userDashboard";
    private static final String ADMIN_DASHBOARD = "/adminDashboard";
    private static final String PRODUCT = "/products";
    private static final String ORDERS = "/admin/orders";
    private static final String MODIFY_USERS = "/modifyUsers";
    private static final String ABOUTUS = "/aboutUs";
    private static final String CONTACT = "/contact";
    private static final String PRIVACY = "/privacy";
    private static final String RETURN = "/return";
    private static final String TERMS = "/terms";
    private static final String WARRANTY = "/warranty";


    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	// Initialization logic, if required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

    	// Allow access to resources
 		if (uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".svg") || uri.endsWith(".jpeg")) {
 			chain.doFilter(request, response);
 			return;
 		}

 		if (uri.endsWith("/logout")) {
            chain.doFilter(request, response);
            return;
        }
 		
        boolean isLoggedIn = SessionUtil.getAttribute(req, "username") != null;
        String userRole = CookieUtil.getCookie(req, "role") != null ? CookieUtil.getCookie(req, "role").getValue(): null;
        
        
        
        if ("admin".equals(userRole)) {
			// Admin is logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
				res.sendRedirect(req.getContextPath() + ADMIN_DASHBOARD);
			} else if (uri.endsWith(ADMIN_DASHBOARD) || uri.endsWith(MODIFY_USERS) || uri.endsWith(PRODUCT)
					|| uri.endsWith(ORDERS) || uri.endsWith(HOME) || uri.endsWith(ROOT)
					|| uri.endsWith(CATEGORY) || uri.endsWith(DESCRIPTION) || uri.endsWith(SEARCH) 
					|| uri.endsWith(ABOUTUS)|| uri.endsWith(CONTACT)|| uri.endsWith(PRIVACY)|| 
					uri.endsWith(RETURN) || uri.endsWith(TERMS)|| uri.endsWith(WARRANTY)) {
				chain.doFilter(request, response);
			} else if (uri.endsWith(ORDER) || uri.endsWith(CART) || uri.endsWith(WISHLIST) || uri.endsWith(ORDER_HISTORY) || uri.endsWith(USER_DASHBOARD)) {
				res.sendRedirect(req.getContextPath() + ADMIN_DASHBOARD);
			} else {
				res.sendRedirect(req.getContextPath() + ADMIN_DASHBOARD);
			}
		} else if ("customer".equals(userRole)) {
			// User is logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
				res.sendRedirect(req.getContextPath() + HOME);
			} else if (uri.endsWith(HOME) || uri.endsWith(ROOT) || uri.endsWith(ORDER) || uri.endsWith(ADD_ORDER)|| uri.endsWith(PROCESS_ORDER)
					|| uri.endsWith(CART) || uri.endsWith(CARTADD) || uri.endsWith(CARTREMOVE) || uri.endsWith(CARTUPDATE) | uri.endsWith(USER_DASHBOARD) 
					|| uri.endsWith(ORDER_HISTORY) || uri.endsWith(WISHLIST)|| uri.endsWith(WISHLIST_ADD)|| uri.endsWith(WISHLIST_DELETE)||
					uri.endsWith(CATEGORY) || uri.endsWith(DESCRIPTION) || uri.endsWith(SEARCH) || uri.endsWith(ORDER_DELETE)
					|| uri.endsWith(ABOUTUS)|| uri.endsWith(CONTACT)|| uri.endsWith(PRIVACY)|| 
					uri.endsWith(RETURN) || uri.endsWith(TERMS)|| uri.endsWith(WARRANTY)) {
				chain.doFilter(request, response);
			} else if (uri.endsWith(ADMIN_DASHBOARD) || uri.endsWith(MODIFY_USERS) || uri.endsWith(PRODUCT)
					|| uri.endsWith(ORDERS)) {
				res.sendRedirect(req.getContextPath() + HOME);
			} else {
				res.sendRedirect(req.getContextPath() + HOME);
			}
		} else{
			// Not logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(HOME) || uri.endsWith(ROOT) || 
					uri.endsWith(CATEGORY) || uri.endsWith(DESCRIPTION) || uri.endsWith(SEARCH)
					|| uri.endsWith(ABOUTUS)|| uri.endsWith(CONTACT)|| uri.endsWith(PRIVACY)|| uri.endsWith(RETURN)
					|| uri.endsWith(TERMS)|| uri.endsWith(WARRANTY)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath() + LOGIN);
			}
		}
	}

    @Override
    public void destroy() {
    	// Cleanup logic, if required
    }
}