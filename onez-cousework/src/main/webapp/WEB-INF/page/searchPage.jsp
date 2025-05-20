<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
    <title>Search Results | oneZ</title>
    <!-- Set contextPath variable -->
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/css/home.css" />
    <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/css/search.css" />
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
    <link href="https://cdn.lineicons.com/5.0/lineicons.css" rel="stylesheet" />
</head>

<body>
    <jsp:include page="header.jsp" />
    
    <!-- Display error message if available -->
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <!-- Display success message if available -->
    <c:if test="${not empty success}">
        <p class="success-message">${success}</p>
    </c:if>

    <!-- Search Results Section -->
    <section class="search-results">
        <h3 class="search-title">Search Results for "${param.search}"</h3>
        
        <c:choose>
            <c:when test="${not empty products}">
                <div class="product-box">
                    <c:forEach var="product" items="${products}">
                        <div class="product-container">
                            <div class="product-image">
                                <a href="${contextPath}/viewProduct?productId=${product.productId}">
                                    <img src="${contextPath}/resources/product/${product.productImage}" 
                                         alt="${product.productName}" 
                                         onerror="this.src='${contextPath}/resources/logo/onez.svg'">
                                </a>
                            </div>
                            <div class="product-info">
                                <h4>${product.productName}</h4>
                                <p>Category: ${product.category}</p>
                                <h5>Rs. ${product.price}</h5>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-results">
                    <p>No products found matching your search.</p>
                    <a href="${contextPath}/home" class="back-to-home">Back to Home</a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
    
    <jsp:include page="footer.jsp" />
</body>
</html>