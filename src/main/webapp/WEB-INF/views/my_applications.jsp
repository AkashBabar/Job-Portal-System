<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Applications - Job Portal</title>
<style>
/* General Styles */
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f1f4f8;
	margin: 0;
	padding: 40px;
	color: #333;
}

.container {
	max-width: 1000px;
	margin: auto;
	background: #ffffff;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
	color: #333;
	text-align: center;
	margin-bottom: 30px;
	font-size: 28px;
	font-weight: 600;
}

a.back-link {
	display: inline-block;
	margin-bottom: 20px;
	text-decoration: none;
	color: #007bff;
	font-weight: bold;
	transition: color 0.2s;
}

a.back-link:hover {
	color: #0056b3;
}

/* Table Styling */
table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

th, td {
	padding: 15px;
	border: 1px solid #e0e0e0;
	text-align: center;
	font-size: 14px;
}

th {
	background-color: #007bff;
	color: white;
	text-transform: uppercase;
}

tr:nth-child(even) {
	background-color: #f9f9f9;
}

tr:hover {
	background-color: #f1f1f1; /* Adding hover effect for rows */
}

td:first-child {
	font-weight: bold;
	color: #007bff;
}

/* Empty Message Styling */
.empty-message {
	font-size: 18px;
	font-weight: bold;
	color: #dc3545;
	background-color: #f8d7da;
	padding: 15px;
	border-radius: 5px;
	margin-top: 30px;
}

/* Responsive Design */
@media screen and (max-width: 768px) {
	body {
		padding: 20px;
	}
	table {
		width: 100%;
		font-size: 12px;
	}
	th, td {
		padding: 10px;
	}
}
</style>
</head>
<body>

	<div class="container">
		<h2>My Job Applications</h2>

		<a href="/student-dashboard" class="back-link">← Back to Dashboard</a>

		<c:choose>
			<c:when test="${empty applications}">
				<p class="empty-message">You haven't applied to any jobs yet.</p>
			</c:when>
			<c:otherwise>
				<table>
					<thead>
						<tr>
							<th>Job Title</th>
							<th>Company</th>
							<th>Location</th>
							<th>Job Type</th>
							<th>Salary Range</th>
							<th>Applied On</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="app" items="${applications}">
							<tr>
								<td>${app.job.title}</td>
								<td>${app.job.company}</td>
								<td>${app.job.location}</td>
								<td>${app.job.jobType}</td>
								<td>${app.job.salaryRange}</td>
								<td><fmt:formatDate value="${app.appliedDateAsDate}"
										pattern="dd MMM yyyy" /></td>

								<td><c:choose>
										<c:when test="${app.status == 'APPLIED'}">Applied</c:when>
										<c:when test="${app.status == 'SHORTLISTED'}">Shortlisted</c:when>
										<c:when test="${app.status == 'REJECTED'}">Rejected</c:when>
										<c:otherwise>Unknown</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>
