<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
  <title>Edit Profile</title>

  <!-- Set contextPath variable -->
  <c:set var="contextPath" value="${pageContext.request.contextPath}" />

  <!-- Link to external CSS -->
  <link rel="stylesheet" type="text/css" href="${contextPath}/css/userDashboard.css" />
</head>
<body>

<!-- Sidebar Navigation -->
<nav class="sidebar">
  <div>
    <a href="${contextPath}/home"><div><img src="${contextPath}/resources/logo/logoWhite.png" alt="ONEZ Logo" class="logo"/></div></a>
  </div>
  <a href="${contextPath}/userDashboard" class="no-style"><div><p>Account details</p></div></a>
  <a href="${contextPath}/orderHistory" class="no-style"><div><p>Orders</p></div></a>
  <a href="${contextPath}/wishlist" class="no-style"><div><p>Wishlist</p></div></a>
  <a href="${contextPath}/cart" class="no-style"><div><p>Cart</p></div></a>

  <!-- Logout Button -->
  <form action="${contextPath}/logout" method="post" class="logout-form">
    <button type="submit" class="sidebar-button">Logout</button>
  </form>
</nav>

<!-- Main Content -->
<div class="main-content">
  <div class="content-wrapper">
    <div class="top-section">
      <h3>Manage My Account</h3>
      <button id="editToggle" class="btn btn-edit">Edit Profile</button>
    </div>

    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="account-details">
      <form id="profileForm" action="${pageContext.request.contextPath}/userDashboard" method="post" style="display: none;">
        <div class="form-group">
          <div class="profile-icon">
            <img src="${contextPath}/resources/user/${user.imageUrl}" width="100" height="100"
                 onerror="this.src='${contextPath}/resources/logo/onez.svg'" />
          </div>
        </div>

        <div class="form-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" name="firstName" value="${user.firstName}" required />
        </div>

        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" name="lastName" value="${user.lastName}" required />
        </div>

        <div class="form-group">
          <label for="dob">Date of Birth</label>
          <input type="date" id="dob" name="dob" value="${user.dob}" />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" value="${user.email}" required />
        </div>

        <div class="form-group">
          <label for="number">Phone Number</label>
          <input type="tel" id="number" name="number" value="${user.number}" />
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Update Profile</button>
          <button type="button" id="cancelEdit" class="btn btn-secondary">Cancel</button>
        </div>
      </form>

      <div id="profileView">
        <div class="profile-icon">
          <img src="${contextPath}/resources/user/${user.imageUrl}" width="100" height="100"
               onerror="this.src='${contextPath}/resources/logo/onez.svg'" />
        </div>
        
        <div class="profile-info">
          <div class="info-row">
            <span class="info-label">First Name:</span>
            <span class="info-value">${user.firstName}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Last Name:</span>
            <span class="info-value">${user.lastName}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Date of Birth:</span>
            <span class="info-value">${user.dob}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Email:</span>
            <span class="info-value">${user.email}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Phone Number:</span>
            <span class="info-value">${user.number}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const editToggle = document.getElementById('editToggle');
    const cancelEdit = document.getElementById('cancelEdit');
    const profileForm = document.getElementById('profileForm');
    const profileView = document.getElementById('profileView');

    editToggle.addEventListener('click', function() {
        profileView.style.display = 'none';
        profileForm.style.display = 'block';
        editToggle.style.display = 'none';
    });

    cancelEdit.addEventListener('click', function() {
        profileForm.style.display = 'none';
        profileView.style.display = 'block';
        editToggle.style.display = 'block';
    });
});
</script>

</body>
</html>