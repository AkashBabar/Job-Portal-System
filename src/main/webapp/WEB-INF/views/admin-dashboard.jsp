<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard - Job Portal</title>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f7fc;
	margin: 0;
	padding: 30px;
}

.container {
	width: 90%;
	margin: 0 auto;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-bottom: 20px;
	border-bottom: 2px solid #ddd;
}

h2 {
	font-size: 1.8rem;
	color: #333;
	text-align: left;
}

.message {
	text-align: center;
	font-weight: bold;
	margin-top: 10px;
}

.success {
	color: green;
}

.error {
	color: red;
}

table {
	width: 100%;
	margin: 30px 0;
	border-collapse: collapse;
	background-color: #fff;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

th, td {
	padding: 12px 18px;
	border: 1px solid #ddd;
	text-align: left;
	vertical-align: top;
}

th {
	background-color: #007bff;
	color: white;
}

td {
	background-color: #f9f9f9;
}

a.delete-link {
	color: red;
	text-decoration: none;
	font-weight: bold;
}

a.delete-link:hover {
	text-decoration: underline;
}

.logout-btn {
	background-color: #dc3545;
	color: white;
	padding: 10px 20px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.logout-btn:hover {
	background-color: #c82333;
}

.skill-badge {
	display: inline-block;
	background-color: #17a2b8;
	color: white;
	padding: 5px 12px;
	margin: 3px;
	border-radius: 12px;
	font-size: 14px;
}

.logout-form {
	text-align: center;
	margin-top: 20px;
}

/* Responsive Design */
@media ( max-width : 768px) {
	.header {
		flex-direction: column;
		align-items: flex-start;
	}
	h2 {
		font-size: 1.5rem;
		margin-bottom: 10px;
	}
	table {
		font-size: 0.9rem;
	}
	td, th {
		padding: 10px;
	}
	.skill-badge {
		font-size: 12px;
	}
}
</style>
</head>
<body>

	<div class="container">
		<!-- Header -->
		<div class="header">
			<h2>Admin Dashboard - Registered Users</h2>
		</div>

		<!-- Flash Messages -->
		<c:if test="${not empty successMessage}">
			<div class="message success">${successMessage}</div>
		</c:if>
		<c:if test="${not empty errorMessage}">
			<div class="message error">${errorMessage}</div>
		</c:if>

		<!-- Users Table -->
		<c:if test="${not empty users}">
			<table>
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>Role</th>
						<th>Skills</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${users}">
						<tr>
							<td>${user.id}</td>
							<td>${user.name}</td>
							<td>${user.email}</td>
							<td>${user.role}</td>
							<td><c:forEach var="skill"
									items="${fn:split(user.skills, ',')}">
									<span class="skill-badge">${fn:trim(skill)}</span>
								</c:forEach></td>
							<td><a
								href="${pageContext.request.contextPath}/admin-dashboard/delete/${user.id}"
								class="delete-link"
								onclick="return confirm('Are you sure you want to delete this user? This action cannot be undone!');">
									Delete </a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>

		<c:if test="${empty users}">
			<div class="message error">No users found!</div>
		</c:if>

		<!-- Logout Form -->
		<div class="logout-form">
			<form action="${pageContext.request.contextPath}/logout"
				method="post">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<button type="submit" class="logout-btn">Logout</button>
			</form>
		</div>
	</div>

</body>
</html>
