<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Job Portal - Home</title>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f0f2f5;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100vh;
	margin: 0;
}

.container {
	background-color: #ffffff;
	padding: 40px 60px;
	border-radius: 12px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
	text-align: center;
	width: 90%;
	max-width: 500px;
}

h1 {
	color: #333;
	margin-bottom: 25px;
	font-size: 26px;
	font-weight: 600;
}

.links {
	display: flex;
	justify-content: center;
	gap: 20px;
	flex-wrap: wrap;
	margin-top: 20px;
}

.links a {
	display: inline-block;
	padding: 12px 25px;
	font-size: 16px;
	color: #fff;
	background-color: #007bff;
	border-radius: 6px;
	text-decoration: none;
	transition: background-color 0.3s ease, transform 0.2s ease;
}

.links a:hover {
	background-color: #0056b3;
	transform: scale(1.05);
}

/* Responsive design */
@media ( max-width : 768px) {
	.container {
		padding: 30px 40px;
	}
	h1 {
		font-size: 22px;
	}
	.links a {
		font-size: 14px;
	}
}
</style>
</head>
<body>
	<div class="container">
		<h1>Welcome to the Job Portal System</h1>
		<div class="links">
			<a href="${pageContext.request.contextPath}/login"
				aria-label="Login to your account">Login</a> <a
				href="${pageContext.request.contextPath}/register"
				aria-label="Register a new account">Register</a>
		</div>
	</div>
</body>
</html>
