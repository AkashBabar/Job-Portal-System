<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Company Dashboard - Job Portal</title>
<link
	href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap"
	rel="stylesheet">
<style>
body {
	font-family: 'Roboto', sans-serif;
	padding: 20px;
	background-color: #f8f9fa;
	margin: 0;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	background-color: #007bff;
	color: white;
	padding: 10px 20px;
	border-radius: 5px;
}

.header h2 {
	margin: 0;
}

.logout-btn {
	padding: 8px 16px;
	background-color: #d9534f;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
}

.logout-btn:hover {
	background-color: #c9302c;
}

.message-box {
	padding: 15px;
	margin: 15px 0;
	border-radius: 5px;
	font-weight: 500;
	text-align: center;
}

.message-box.success {
	background-color: #d4edda;
	border: 1px solid #c3e6cb;
	color: #155724;
}

.message-box.error {
	background-color: #f8d7da;
	border: 1px solid #f5c6cb;
	color: #721c24;
}

.job-form {
	background: white;
	padding: 20px;
	border-radius: 6px;
	margin-bottom: 30px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.job-form input[type="text"], .job-form textarea {
	width: 100%;
	padding: 12px;
	margin: 8px 0;
	border-radius: 4px;
	border: 1px solid #ccc;
}

.field-error {
	color: red;
	font-size: 0.9em;
}

.btn-submit {
	background-color: #28a745;
	color: white;
	padding: 12px 24px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
	display: block;
	width: 100%;
	margin-top: 15px;
}

.btn-submit:hover {
	background-color: #218838;
}

table {
	width: 100%;
	border-collapse: collapse;
	background: white;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	margin-top: 30px;
}

table th, table td {
	padding: 12px;
	border: 1px solid #ddd;
	text-align: left;
}

.skill-badge {
	display: inline-block;
	background-color: #e0e0e0;
	color: #333;
	padding: 5px 10px;
	margin: 3px;
	border-radius: 4px;
	font-size: 0.9em;
	max-width: 120px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.delete-link, .edit-link, .view-applicants-link {
	color: #007bff;
	text-decoration: none;
	font-weight: bold;
	margin-right: 8px;
}

.delete-link:hover, .edit-link:hover, .view-applicants-link:hover {
	text-decoration: underline;
}

@media ( max-width : 768px) {
	.header {
		flex-direction: column;
		align-items: flex-start;
	}
	.logout-btn {
		margin-top: 10px;
	}
}
</style>
</head>
<body>

	<!-- Header -->
	<div class="header">
		<h2>Company Dashboard</h2>
		<!-- Logout Button -->
		<form action="${pageContext.request.contextPath}/logout" method="post">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<button class="logout-btn" type="submit">Logout</button>
		</form>
	</div>

	<!-- Flash Messages -->
	<c:if test="${not empty successMessage}">
		<div class="message-box success">${successMessage}</div>
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="message-box error">${errorMessage}</div>
	</c:if>

	<!-- Post Job Form -->
	<form class="job-form"
		action="${pageContext.request.contextPath}/company-dashboard/job/save"
		method="post" onsubmit="return validateForm()">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<h3>Post a New Job</h3>
		<input type="text" name="title" placeholder="Job Title" required />
		<textarea name="description" placeholder="Job Description" rows="4"
			required></textarea>
		<input type="text" name="location" placeholder="Location" required />
		<input type="text" name="skillsRequired"
			placeholder="Skills Required (comma-separated)" required /> <input
			type="text" name="jobType"
			placeholder="Job Type (e.g., Full-time, Part-time)" required /> <input
			type="text" name="salaryRange"
			placeholder="Salary Range (e.g., 30k-50k)" required />
		<button type="submit" class="btn-submit">Post Job</button>
	</form>

	<!-- Job Listings -->
	<c:choose>
		<c:when test="${not empty jobs}">
			<h3>My Job Postings</h3>
			<table>
				<thead>
					<tr>
						<th>ID</th>
						<th>Title</th>
						<th>Company</th>
						<th>Description</th>
						<th>Location</th>
						<th>Skills</th>
						<th>Type</th>
						<th>Salary</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="job" items="${jobs}">
						<tr>
							<td>${job.id}</td>
							<td>${job.title}</td>
							<td>${job.company}</td>
							<td>${job.description}</td>
							<td>${job.location}</td>
							<td><c:forEach var="skill"
									items="${fn:split(job.skillsRequired, ',')}">
									<span class="skill-badge">${fn:trim(skill)}</span>
								</c:forEach></td>
							<td>${job.jobType}</td>
							<td>${job.salaryRange}</td>
							<td>
								<!-- Link to View Applicants for this job --> <a
								href="${pageContext.request.contextPath}/company-dashboard/job/view-applicants/${job.id}"
								class="view-applicants-link">View Applicants</a> <a
								href="${pageContext.request.contextPath}/company-dashboard/job/edit/${job.id}"
								class="edit-link">Edit</a> <a
								href="${pageContext.request.contextPath}/company-dashboard/job/delete/${job.id}"
								class="delete-link"
								onclick="return confirm('Are you sure you want to delete this job?');">Delete</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div class="message-box"
				style="background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404;">
				No jobs have been posted yet.</div>
		</c:otherwise>
	</c:choose>

	<script>
        function validateForm() {
            const title = document.querySelector("input[name='title']").value;
            const description = document.querySelector("textarea[name='description']").value;
            const location = document.querySelector("input[name='location']").value;
            const skillsRequired = document.querySelector("input[name='skillsRequired']").value;
            const jobType = document.querySelector("input[name='jobType']").value;
            const salaryRange = document.querySelector("input[name='salaryRange']").value;

            if (!title || !description || !location || !skillsRequired || !jobType || !salaryRange) {
                alert("Please fill in all fields.");
                return false;
            }
            return true;
        }
    </script>

</body>
</html>
