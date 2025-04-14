<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<%
// Initialize necessary objects and variables
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
// need to add data in attribute to select it in JSP code using JSTL core tag
pageContext.setAttribute("currentUser", currentUser);
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/profile.css" />
</head>
<body>
<div class="container">
    <div class="sidebar">
        <ul>
            <li class="active">Basic Information</li>
            <li>Linked Social Account</li>
            <li>Password</li>
            <li>Popup Guides</li>
        </ul>
    </div>

    <div class="main">
        <h2>Basic Information</h2>
        <form action="${contextPath}/updateProfile" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" value="${fullName}" />
            </div>

            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" value="${email}" readonly />
                <span class="email-note">
                    This is your primary email address and will be used to send notification emails. 
                    <a href="${contextPath}/changeEmail">Change Email Address</a>
                </span>
            </div>

            <div class="form-group">
                <label>Location</label>
                <input type="text" name="location" value="${location}" readonly />
            </div>

            <div class="form-group">
                <label>School</label>
                <input type="text" name="school" value="${school}" maxlength="128" />
            </div>

            <div class="form-group">
                <label>Profile Image</label>
                <input type="file" name="profileImage" accept="image/*" />
                <small>Max Image Size = 1 MB</small>
            </div>

            <a href="${contextPath}/home"><button type="submit">Save Changes</button></a>
        </form>
    </div>
</div>
</body>
</html>
