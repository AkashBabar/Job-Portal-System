<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Login - Job Portal</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap"
	rel="stylesheet">
<style>
body {
	font-family: 'Roboto', sans-serif;
	background-color: #f4f4f4;
	margin: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
}

.login-container {
	background: #ffffff;
	padding: 35px 30px;
	width: 100%;
	max-width: 400px;
	border-radius: 10px;
	box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
	text-align: center;
}

h2 {
	color: #333;
	margin-bottom: 25px;
}

form label {
	display: block;
	text-align: left;
	font-weight: bold;
	margin-top: 12px;
	margin-bottom: 6px;
}

input[type="text"], input[type="password"] {
	width: 100%;
	padding: 12px 15px;
	border: 1px solid #ccc;
	border-radius: 8px;
	box-sizing: border-box;
	font-size: 15px;
}

button {
	width: 100%;
	background-color: #007bff;
	color: white;
	padding: 12px;
	border: none;
	border-radius: 8px;
	font-size: 16px;
	margin-top: 15px;
	cursor: pointer;
	transition: background-color 0.3s ease;
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 8px;
}

button:hover {
	background-color: #0056b3;
}

.error, .success {
	margin-top: 15px;
	padding: 10px;
	border-radius: 5px;
	font-size: 14px;
}

.error {
	color: #721c24;
	background-color: #f8d7da;
	border: 1px solid #f5c6cb;
}

.success {
	color: #155724;
	background-color: #d4edda;
	border: 1px solid #c3e6cb;
}

p {
	margin-top: 20px;
	font-size: 14px;
}

a {
	color: #007bff;
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

@media ( max-width : 600px) {
	.login-container {
		width: 90%;
		padding: 25px 20px;
	}
}
</style>
</head>
<body>

	<div class="login-container">
		<h2>Login</h2>

		<form action="${pageContext.request.contextPath}/login" method="post"
			id="loginForm">
			<label for="username">Email:</label> <input type="text"
				name="username" id="username" placeholder="Enter your email"
				autocomplete="email" required value="${param.username}" /> <label
				for="password">Password:</label> <input type="password"
				name="password" id="password" placeholder="Enter your password"
				autocomplete="current-password" required /> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" />

			<button type="submit" id="submitBtn">
				<span id="loader" style="display: none;">🔄</span> <span
					id="loginText">Login</span>
			</button>
		</form>

		<c:if test="${param.error != null}">
			<div class="error">Invalid credentials. Please try again.</div>
		</c:if>

		<c:if test="${param.logout != null}">
			<div class="success">You have logged out successfully.</div>
		</c:if>

		<p>
			Don't have an account? <a
				href="${pageContext.request.contextPath}/register">Register here</a>
		</p>
	</div>

	<script>
        document.getElementById("loginForm").addEventListener("submit", function () {
            const submitBtn = document.getElementById("submitBtn");
            const loader = document.getElementById("loader");
            const loginText = document.getElementById("loginText");

            submitBtn.disabled = true;
            loader.style.display = "inline-block";
            loginText.style.display = "none";
        });
    </script>

</body>
</html>
