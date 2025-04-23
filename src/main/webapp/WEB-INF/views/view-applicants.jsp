<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Applicants - ${job.title}</title>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f6f9;
	margin: 0;
	padding: 30px;
}

h2 {
	text-align: center;
	color: #2c3e50;
	margin-bottom: 30px;
}

.applicant-card {
	background-color: #ffffff;
	border-radius: 10px;
	box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
	padding: 20px;
	margin: 20px auto;
	width: 85%;
	max-width: 800px;
	transition: box-shadow 0.3s;
}

.applicant-card:hover {
	box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.applicant-card p {
	margin: 10px 0;
	color: #444;
	font-size: 15px;
}

.applicant-card strong {
	color: #2c3e50;
}

a.view-resume {
	display: inline-block;
	background-color: #007bff;
	color: #fff;
	padding: 8px 14px;
	border-radius: 5px;
	text-decoration: none;
	font-weight: bold;
	transition: background-color 0.3s;
	margin-top: 8px;
}

a.view-resume:hover {
	background-color: #0056b3;
}

.no-applicants {
	text-align: center;
	color: #888;
	font-style: italic;
	font-size: 16px;
	margin-top: 50px;
}

.back-link {
	display: block;
	margin: 40px auto 0;
	width: fit-content;
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	font-weight: bold;
	transition: background-color 0.3s;
}

.back-link:hover {
	background-color: #0056b3;
}

.status-applied {
	color: green;
}

.status-reviewed {
	color: blue;
}

.status-rejected {
	color: red;
}
</style>
</head>
<body>

	<h2>Applicants for: ${job.title}</h2>

	<c:choose>
		<c:when test="${empty applicants}">
			<p class="no-applicants">No applicants have applied for this job
				yet.</p>
		</c:when>
		<c:otherwise>
			<c:forEach var="application" items="${applicants}">
				<div class="applicant-card">
					<p>
						<strong>Name:</strong> ${application.applicant.name}
					</p>
					<p>
						<strong>Email:</strong> ${application.applicant.email}
					</p>
					<p>
						<strong>Applied On:</strong>
						<fmt:formatDate value="${application.appliedDateAsDate}"
							pattern="dd MMM yyyy" />
					</p>

					<c:if test="${not empty application.applicant.resumeLink}">
						<p>
							<a href="${application.applicant.resumeLink}" target="_blank"
								class="view-resume"
								aria-label="View Resume for ${application.applicant.name}"
								title="View the resume of ${application.applicant.name}"> 📄
								View Resume </a>
						</p>
					</c:if>

				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>

	<a href="${pageContext.request.contextPath}/company-dashboard"
		class="back-link">← Back to Dashboard</a>

</body>
</html>
