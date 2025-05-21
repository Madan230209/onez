<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<%
// Initialize necessary objects and variables
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
pageContext.setAttribute("currentUser", currentUser);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
    <title>Order Management</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/orderManagement.css" />
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
    
</head>
<body>
<div class="mainparent">
      
      <nav class="sidebar">
      
       <div class="side-bar-content">   
   		 <div class="logo">    
            <img src="${contextPath}/resources/logo/logoWhite.png" alt="ONEZ Logo" class="logo-img"/>    
    	</div> 
    	
    	<a href="${contextPath}/adminDashboard" class="side-nav">
        <div class="content">
            <p>Dashboard</p>
            <i class="fa-solid fa-table-columns"></i>
        </div>
    </a>
    
    <a href="${contextPath}/admin/orders" class="side-nav">
        <div class="content">
           <p>Orders</p>
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
    </a>
    
    <a href="${contextPath}/modifyUsers" class="side-nav">
        <div class="content">
            <p>Customer Details</p>
            <i class="fa-solid fa-gear"></i>
        </div>
    </a>
    
    <a href="${contextPath}/products" class="side-nav">
       <div class="content">
           	<p>Manage Products</p>
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
    </a>
    </div>  
    
    <div>
 		 <form action="${contextPath}/logout" method="post">
                    <input type="submit" class="logout-btn" value="Logout" />
                </form>
 	</div>
</nav>
    
    
    <!-- Main Content -->
    <main class="dashboard">
    <jsp:include page="adminHeader.jsp"/>
    
        <h1>Recent Orders</h1>
        
        <!-- Display messages -->
        <c:if test="${not empty message}">
            <div class="alert success">${message}</div>
            <c:remove var="message" scope="session"/>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>
        
        <table>
            <thead>
                <tr>
                	<th>Product List </th>
                    <th>Customer Name</th>
                    <th>Order ID</th>
                    <th>Status</th>
                    <th>Address</th>
                    <th>Order Date</th>
                    <th>Payment Method</th>
                    <th>Total Price</th>
                </tr>
            </thead>
            <tbody>
            	
                <c:forEach var="order" items="${orders}">
                    <tr>
                   		 <td ><c:forEach var="item" items="${order.items}">
			                    <div class="order-item">
			                        <img src="${pageContext.request.contextPath}/resources/product/${item.product.productImage}"  width="60" height="60"
			                             alt="${item.product.productName}" 
			                             class="item-image">
			                    </div>
			                </c:forEach></td>
                        <td>${order.user.firstName} ${order.user.lastName}</td>
                        <td>${order.orderId}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="status-form">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <select name="status" class="status-dropdown" onchange="this.form.submit()">
                                    <option value="Pending" ${order.orderStatus eq 'Pending' ? 'selected' : ''}>Pending</option>
                                    <option value="Processing" ${order.orderStatus eq 'Processing' ? 'selected' : ''}>Processing</option>
                                    <option value="Shipped" ${order.orderStatus eq 'Shipped' ? 'selected' : ''}>Shipped</option>
                                    <option value="Completed" ${order.orderStatus eq 'Completed' ? 'selected' : ''}>Completed</option>
                                    <option value="Cancelled" ${order.orderStatus eq 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </form>
                        </td>
                        <td>
						    <c:if test="${not empty order.user.address}">
						        ${order.user.address.name}
						    </c:if>
						</td>
                        <td>${order.orderDate}</td>
                        <td>${order.paymentMethod}</td>
                        <td>
						    <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="Rs."/>
						</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>