<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Default Dashboard</title>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f9f9f9;
	margin: 0;
	padding: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	color: #333;
}

.dashboard-container {
	background: #fff;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
	text-align: center;
	max-width: 500px;
	width: 90%;
	transition: transform 0.3s ease;
}

.dashboard-container:hover {
	transform: translateY(-10px);
}

h1 {
	color: #333;
	margin-bottom: 20px;
	font-size: 24px;
}

p {
	color: #666;
	font-size: 16px;
	margin-bottom: 20px;
}

a {
	display: inline-block;
	padding: 10px 20px;
	background-color: #007bff;
	color: #fff;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s ease, transform 0.2s ease;
}

a:hover {
	background-color: #0056b3;
	transform: scale(1.05);
}

/* Responsive Design */
@media ( max-width : 768px) {
	.dashboard-container {
		padding: 30px;
	}
	h1 {
		font-size: 20px;
	}
	p {
		font-size: 14px;
	}
	a {
		font-size: 14px;
	}
}
</style>
</head>
<body>

	<div class="dashboard-container">
		<h1>Welcome to the Default Dashboard</h1>
		<p>
			No specific role has been assigned to your account.<br> Please
			contact the administrator for access.
		</p>
		<a href="<%=request.getContextPath()%>/logout">Logout</a>
	</div>

</body>
</html>
