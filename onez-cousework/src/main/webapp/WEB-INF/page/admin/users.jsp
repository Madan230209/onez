<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<%
// Initialize necessary objects and variables
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
// need to add data in attribute to select it in JSP code using JSTL core tag
pageContext.setAttribute("currentUser", currentUser);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/user.css" />
	<script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
</head>
<body>

	<div class="mainparent">
		
		<!-- Sidebar jsp file connection -->
		
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
        <main class="dashboard" >
        <jsp:include page="adminHeader.jsp"/>
			<div class="maindash">
	          
	          <div class="section1">
	            <div class="product">
	              <h2>${empty Kathmandu ? 0 : Kathmandu}</h2>
	              <p>Kathmandu</p>
	            </div>
	          </div>
	
	          <div class="section2">
	            <div class="product">
	              <h2>${empty Lalitpur ? 0 : Lalitpur}</h2>
	              <p>Lalitpur</p>
	            </div>
	          </div>
	          
	          <div class="section3">
	            <div class="product">
	              <h2>${empty Bhaktapur ? 0 : Bhaktapur}</h2>
	              <p>Bhaktapur</p>
	            </div>
	          </div>
        </div>

			
				<!-- Display error message if available -->
				<c:if test="${not empty error}">
					<p class="error-message">${error}</p>
				</c:if>

				<!-- Display success message if available -->
				<c:if test="${not empty success}">
					<p class="success-message">${success}</p>
				</c:if>
				
		<div class="recent-orders">
		<h3 style="font-size: 30px; margin-bottom:5px; text-align:center;"> User List</h3>
			<div class="table-container">
				 <table class="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Address</th>
							<th>Email</th>
							<th>Number</th>
						</tr>
					</thead>
					<tbody>
						<!-- Using JSTL forEach loop to display user data -->
						<c:forEach var="user" items="${userList}">
							<tr>
								<td>${user.id}</td>
								<td>${user.firstName} ${user.lastName}</td>
								<td>${user.address.name}</td>
								<td>${user.email}</td>
								<td>${user.number}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
			</main>
		</div>

</body>
</html>