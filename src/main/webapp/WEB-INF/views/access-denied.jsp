<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="Access Denied - 403 | You are not authorized to view this page.">
<meta name="keywords"
	content="403, Access Denied, Unauthorized, Error Page">
<meta name="author" content="Job Portal">
<title>Access Denied - 403</title>
<style>
body {
	margin: 0;
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #fef2f2;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
}

.container {
	text-align: center;
	background-color: #fff;
	padding: 40px 30px;
	border-radius: 12px;
	box-shadow: 0 4px 12px rgba(255, 0, 0, 0.15);
	max-width: 500px;
	width: 90%;
}

.container img {
	width: 180px;
	margin-bottom: 20px;
}

h1 {
	color: #d9534f;
	font-size: 2rem;
	margin-bottom: 10px;
}

p {
	color: #555;
	font-size: 1.1rem;
	margin-bottom: 20px;
}

a, button {
	background-color: #337ab7;
	color: white;
	padding: 10px 20px;
	border: none;
	text-decoration: none;
	border-radius: 6px;
	font-weight: bold;
	cursor: pointer;
	transition: background-color 0.2s ease;
}

a:hover, button:hover {
	background-color: #286090;
}

.email-info {
	font-size: 0.9rem;
	color: #888;
	margin-bottom: 15px;
}
</style>
</head>
<body>
	<div class="container">
		<img src="https://cdn-icons-png.flaticon.com/512/179/179386.png"
			alt="403 Forbidden Icon - Access Denied" />
		<h1>🚫 Access Denied</h1>
		<p>You are not authorized to view this page.</p>

		<c:if test="${not empty pageContext.request.userPrincipal}">
			<div class="email-info">
				Logged in as: <strong>${pageContext.request.userPrincipal.name}</strong>
			</div>
		</c:if>

		<a href="/">🏠 Go to Home</a> <br /> <br />
		<button onclick="history.back()">🔙 Go Back</button>
	</div>
</body>
</html>
