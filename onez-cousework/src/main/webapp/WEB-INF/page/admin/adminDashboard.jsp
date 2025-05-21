<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<%
// Initialize necessary objects and variables
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
pageContext.setAttribute("currentUser", currentUser);
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/adminDashboard.css" />
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
    <!-- header jsp file connection -->
    <jsp:include page="adminHeader.jsp"/>

    <h1>Welcome to Dashboard!</h1>

    <div class="maindash">
        <div class="section1">
            <div class="product">
                <i class="fa-solid fa-box"></i>
                <h2>${not empty totalProduct ? totalProduct : 0}</h2>
                <p>Total Products</p>
            </div>
        </div>

        <div class="section2">
            <div class="product">
                <i class="fa-solid fa-money-bill-trend-up"></i>
                <h2>Rs. ${not empty totalsales ? totalsales : "0.00"}</h2>
                <p>Total Sales</p>
            </div>
        </div>
        
        <div class="section3">
            <div class="product">
                <i class="fa-solid fa-user-plus"></i>
                <h2>${not empty total ? total : 0}</h2>
                <p>Total Customer</p>
            </div>
        </div>
    </div>

    <div class="recent-orders">
        <h2>Recent Orders</h2>
        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Order Id</th>
                        <th>Status</th>
                        <th>Address</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty orderList}">
                            <c:forEach var="order" items="${orderList}">
                                <tr>
                                    <td>${order.user.firstName} ${order.user.lastName}</td>
                                    <td>${order.orderId}</td>
                                    <td>${order.orderStatus}</td>
                                    <td>${order.user.address.name}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4">No recent orders found</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>


</div>
</body>
      
      
      
      
      
      
      
    