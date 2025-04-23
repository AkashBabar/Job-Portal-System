<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Student Dashboard - Job Portal</title>
<style>
body {
	font-family: 'Arial', sans-serif;
	background-color: #f4f7f6;
	margin: 0;
	padding: 0;
	color: #333;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	background-color: #007bff;
	color: white;
	padding: 15px 30px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h2 {
	margin: 0;
	font-size: 24px;
}

.logout-btn {
	background-color: #dc3545;
	color: white;
	border: none;
	padding: 10px 20px;
	border-radius: 6px;
	cursor: pointer;
	font-size: 16px;
	transition: background-color 0.3s ease;
}

.logout-btn:hover {
	background-color: #c82333;
}

.message {
	text-align: center;
	font-weight: bold;
	margin: 20px 0;
}

.success {
	color: green;
}

.error {
	color: red;
}

table {
	width: 100%;
	margin-top: 20px;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
	overflow-x: auto;
}

th, td {
	padding: 15px;
	text-align: left;
	border-bottom: 1px solid #ccc;
}

th {
	background-color: #007bff;
	color: white;
	font-size: 16px;
}

tr:hover {
	background-color: #f1f1f1;
}

.apply-btn {
	padding: 8px 16px;
	background-color: #28a745;
	color: white;
	border: none;
	border-radius: 6px;
	cursor: pointer;
	font-size: 14px;
	transition: background-color 0.3s ease;
}

.apply-btn:hover {
	background-color: #218838;
}

.applied {
	color: #28a745;
	font-weight: bold;
	text-transform: uppercase;
}

.no-data {
	text-align: center;
	color: #777;
	font-size: 16px;
	padding: 20px;
}

.job-action-btn {
	display: flex;
	justify-content: center;
	align-items: center;
}

@media ( max-width : 768px) {
	.header {
		flex-direction: column;
		align-items: flex-start;
		padding: 10px 20px;
	}
	table {
		width: 100%;
		margin-top: 15px;
		border-radius: 5px;
	}
	th, td {
		padding: 12px;
		font-size: 14px;
	}
	.logout-btn {
		margin-top: 10px;
	}
	.apply-btn {
		padding: 6px 12px;
		font-size: 12px;
	}
}
</style>
</head>
<body>

	<div class="header">
		<h2>Welcome to Student Dashboard</h2>
		<a
			href="${pageContext.request.contextPath}/student-dashboard/my_applications"
			style="margin-left: 50px; font-weight: bold; text-decoration: none; color: rgb(255, 255, 255);">
			📄 My Applications </a>
		<form action="${pageContext.request.contextPath}/logout" method="post">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<button class="logout-btn">Logout</button>
		</form>
	</div>

	<!-- Flash Messages -->
	<c:if test="${not empty successMessage}">
		<div class="message success">${successMessage}</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="message error">${errorMessage}</div>
	</c:if>

	<!-- Job Listing Table -->
	<table>
		<thead>
			<tr>
				<th>Title</th>
				<th>Company</th>
				<th>Location</th>
				<th>Description</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty jobs}">
				<c:forEach var="job" items="${jobs}">
					<tr>
						<td>${job.title}</td>
						<td>${job.company}</td>
						<td>${job.location}</td>
						<td>${job.description}</td>
						<td class="job-action-btn"><c:set var="alreadyApplied"
								value="false" /> <c:forEach var="appliedJob"
								items="${appliedJobs}">
								<c:if test="${appliedJob.job.id == job.id}">
									<c:set var="alreadyApplied" value="true" />
								</c:if>
							</c:forEach> <c:choose>
								<c:when test="${alreadyApplied}">
									<span class="applied">Applied</span>
								</c:when>
								<c:otherwise>
									<form
										action="${pageContext.request.contextPath}/apply/${job.id}"
										method="post" style="margin: 0;">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" />
										<button type="submit" class="apply-btn">Apply</button>
									</form>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${empty jobs}">
				<tr>
					<td colspan="5" class="no-data">No jobs available at the
						moment.</td>
				</tr>
			</c:if>
		</tbody>
	</table>

</body>
</html>
