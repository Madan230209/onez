<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>

	<div class="container">
		<div class="sidebar">
			<ul class="nav">
				<li><a href="${contextPath}/dashboard"><span class="icon">🏠</span>
						Dashboard</a></li>
				<li><a href="${contextPath}/adminOrder"><span class="icon">📊</span>
						Order List</a></li>
				<li><a href="${contextPath}/modifyStudents"><span
						class="icon">💳</span> Students</a></li>
				<li><a href="${contextPath}/studentUpdate"><span
						class="icon">✏️</span> Update Student</a></li>
			</ul>
			<div class="logout">
				<form action="${contextPath}/logout" method="post">
					<input type="submit" class="nav-button" value="Logout" />
				</form>
			</div>
		</div>

		<div class="content">
			<div class="header">
				<div class="info-box">
					<h3>Total Students</h3>
					<p>${empty total ? 0 : total}</p>
				</div>
				<div class="info-box">
					<h3>Computing</h3>
					<p>${empty computing ? 0 : computing}</p>
				</div>
				<div class="info-box">
					<h3>Multimedia</h3>
					<p>${empty multimedia ? 0 : multimedia}</p>
				</div>
				<div class="info-box">
					<h3>Networking</h3>
					<p>${empty networking ? 0 : networking}</p>
				</div>
			</div>


			<div class="card">
				<div>
					<h2>Welcome, Admins!</h2>
					<p>School Management Dashboard</p>
					<br /> <br />
					<p>We're excited to have you onboard. Manage your school's data
						efficiently and effortlessly with our user-friendly interface.
						From student records to financial data, everything you need is
						just a few clicks away.</p>
				</div>
				<img src="${contextPath}/resource/system/college.jpg"
					alt="college">
			</div>

			<div class="table-container">
				<h3>Recently Enrolled Student</h3>
				<table>
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Email</th>
							<th>Number</th>
						</tr>
					</thead>
					<tbody>
						<!-- Using JSTL forEach loop to display student data -->
						<c:forEach var="student" items="${studentList}">
							<tr>
								<td>${student.id}</td>
								<td>${student.firstName}${student.lastName}</td>
								<td>${student.email}</td>
								<td>${student.number}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</body>
</html>