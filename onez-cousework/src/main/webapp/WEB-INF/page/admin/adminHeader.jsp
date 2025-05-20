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
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/adminHeader.css" />
<header>
          <div class="add-product">
            <div class="admin-header">
              <img src="${contextPath}/resources/user/${user.imageUrl}" width="40" height="40" style="border-radius: 10px;"
                    onerror="this.src='${contextPath}/resources/logo/onez.svg'">
              <p>${username}</p>
            </div>
          </div>
        </header>