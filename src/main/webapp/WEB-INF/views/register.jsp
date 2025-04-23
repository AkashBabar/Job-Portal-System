<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Register - Job Portal</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f8f9fa;
	margin: 0;
	padding: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
}

.container {
	background: #ffffff;
	padding: 30px;
	width: 100%;
	max-width: 500px;
	border-radius: 10px;
	box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

h2 {
	text-align: center;
	color: #333;
	margin-bottom: 25px;
}

form label {
	display: block;
	margin: 12px 0 6px;
	font-weight: bold;
}

form input, form select {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 6px;
	font-size: 15px;
	box-sizing: border-box;
}

button {
	width: 100%;
	background-color: #007bff;
	color: white;
	padding: 12px;
	border: none;
	border-radius: 6px;
	margin-top: 20px;
	font-size: 16px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

button:hover {
	background-color: #0056b3;
}

.error, .success {
	margin-top: 20px;
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
	text-align: center;
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

@media ( max-width : 768px) {
	.container {
		padding: 20px;
	}
	button {
		font-size: 14px;
	}
}
</style>

<script>
	function validateForm() {
		let password = document.getElementById("password").value;
		let resumeLink = document.getElementById("resumeLink").value;

		// Check if password is at least 6 characters long
		if (password.length < 6) {
			alert("Password must be at least 6 characters long!");
			return false;
		}

		// Validate resume link if provided
		if (resumeLink.trim() !== "" && !/^https?:\/\/.+/.test(resumeLink)) {
			alert("Resume link must start with http:// or https://");
			return false;
		}

		// Disable the submit button to prevent multiple clicks
		document.getElementById("submitBtn").disabled = true;
		document.getElementById("submitBtn").textContent = "Registering...";
		return true;
	}
</script>

</head>
<body>

	<div class="container">
		<h2>Create Your Account</h2>

		<form action="${pageContext.request.contextPath}/register"
			method="post" onsubmit="return validateForm()">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <label for="name">Name:</label> <input
				type="text" id="name" name="name" required value="${param.name}" />

			<label for="email">Email:</label> <input type="email" id="email"
				name="email" required value="${param.email}" /> <label
				for="password">Password:</label> <input type="password"
				id="password" name="password" required
				placeholder="Minimum 6 characters" /> <label for="skills">Skills:</label>
			<input type="text" id="skills" name="skills"
				placeholder="e.g. Java, Spring Boot, React" value="${param.skills}" />

			<label for="resumeLink">Resume Link:</label> <input type="url"
				id="resumeLink" name="resumeLink"
				placeholder="https://yourdomain.com/resume.pdf"
				value="${param.resumeLink}" /> <label for="role">Role:</label> <select
				name="role" id="role" required>
				<option value="" disabled
					<c:if test="${empty param.role}">selected</c:if>>Select
					Role</option>
				<c:forEach var="role" items="${roles}">
					<option value="${role}"
						<c:if test="${param.role == role}">selected</c:if>>${role}</option>
				</c:forEach>
			</select>

			<button type="submit" id="submitBtn">Register</button>
		</form>

		<p>
			Already have an account? <a
				href="${pageContext.request.contextPath}/login">Login here</a>
		</p>

		<!-- Error and success messages -->
		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>

		<c:if test="${not empty message}">
			<div class="success">${message}</div>
		</c:if>
	</div>

</body>
</html>
