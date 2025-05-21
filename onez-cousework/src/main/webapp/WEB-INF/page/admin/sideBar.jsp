<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<%
// Initialize necessary objects and variables
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
// need to add data in attribute to select it in JSP code using JSTL core tag
pageContext.setAttribute("currentUser", currentUser);
%>
<script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
<link rel="stylesheet" type="text/css"
href="${pageContext.request.contextPath}/css/sidebar.css" />
<!-- Sidebar Navigation -->
<body>
<nav class="sidebar">
    <div>    
            <img src="${contextPath}/resources/logo/logoWhite.png" alt="ONEZ Logo" class="logo"/>    
    </div>
    
    <a href="${contextPath}/adminDashboard" class="side-nav">
        <div>
            <p>Dashboard</p>
            <i class="fa-solid fa-table-columns"></i>
        </div>
    </a>
    
    <a href="${contextPath}/admin/orders" class="side-nav">
        <div>
           <p>Orders</p>
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
    </a>
    
    <a href="${contextPath}/modifyUsers" class="side-nav">
        <div>
            <p>Customer Details</p>
            <i class="fa-solid fa-gear"></i>
        </div>
    </a>
    
    <a href="${contextPath}/products" class="side-nav">
        <div>
           	<p>Manage Products</p>
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
    </a>
    
    
    <div>
 		 <form action="${contextPath}/logout" method="post">
                    <input type="submit" class="logout-btn" value="Logout" />
                </form>
 	</div>
</nav>
</body>