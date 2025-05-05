<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Feedback</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/feedback.css">
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <main class="main-content">
        <div class="feedback-container">
            <h1><i class="fas fa-comment-alt"></i> Share Your Feedback</h1>
            
            <c:if test="${not empty success}">
                <div class="success-message">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <form action="${contextPath}/submitFeedback" method="post">
                <div class="form-group">
                    <label for="rating">Rating:</label>
                    <div class="star-rating">
                        <input type="radio" id="star5" name="rating" value="5" />
                        <label for="star5" title="5 stars"></label>
                        <input type="radio" id="star4" name="rating" value="4" />
                        <label for="star4" title="4 stars"></label>
                        <input type="radio" id="star3" name="rating" value="3" checked />
                        <label for="star3" title="3 stars"></label>
                        <input type="radio" id="star2" name="rating" value="2" />
                        <label for="star2" title="2 stars"></label>
                        <input type="radio" id="star1" name="rating" value="1" />
                        <label for="star1" title="1 star"></label>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="comments">Comments:</label>
                    <textarea id="comments" name="comments" rows="5" 
                              placeholder="Tell us about your experience..."></textarea>
                </div>
                
                <button type="submit" class="submit-btn">
                    <i class="fas fa-paper-plane"></i> Submit Feedback
                </button>
            </form>
            
            <a href="${contextPath}/home" class="back-to-home">
                <i class="fas fa-home"></i> Back to Home
            </a>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>
</body>
</html>