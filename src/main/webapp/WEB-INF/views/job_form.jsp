<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Post New Job - Job Portal</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f8f9fa;
	padding: 40px 20px;
	margin: 0;
}

.container {
	max-width: 600px;
	margin: auto;
	background-color: white;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
	text-align: center;
	color: #333;
	margin-bottom: 30px;
}

form label {
	display: block;
	margin-bottom: 6px;
	font-weight: bold;
	color: #444;
}

input[type="text"], textarea, select {
	width: 100%;
	padding: 12px;
	margin-bottom: 18px;
	border: 1px solid #ccc;
	border-radius: 6px;
	box-sizing: border-box;
	font-size: 15px;
}

textarea {
	resize: vertical;
	min-height: 120px;
}

button {
	width: 100%;
	padding: 14px;
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 6px;
	font-size: 16px;
	cursor: pointer;
	transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
	background-color: #0056b3;
	transform: scale(1.05);
}

.cancel-link {
	display: block;
	text-align: center;
	margin-top: 10px;
	color: #007bff;
	text-decoration: none;
}

.cancel-link:hover {
	text-decoration: underline;
}

/* Responsive design */
@media screen and (max-width: 600px) {
	.container {
		padding: 20px;
	}
}
</style>
</head>
<body>

	<div class="container">
		<h2>Post a New Job</h2>

		<form action="/company-dashboard/job/update" method="post">
			<!-- CSRF Token (if Spring Security is used) -->
			<c:if test="${not empty _csrf}">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</c:if>

			<!-- Hidden field for Job ID -->
			<input type="hidden" name="id" value="${job.id}" />

			<!-- Job Title -->
			<label for="title">Job Title:</label> <input type="text" id="title"
				name="title" value="${job.title}" placeholder="e.g. Java Developer"
				required />

			<!-- Job Description -->
			<label for="description">Job Description:</label>
			<textarea id="description" name="description" required>${job.description}</textarea>

			<!-- Location -->
			<label for="location">Location:</label> <input type="text"
				id="location" name="location" value="${job.location}"
				placeholder="e.g. Mumbai, India" required />

			<!-- Required Skills -->
			<label for="skillsRequired">Required Skills:</label> <input
				type="text" id="skillsRequired" name="skillsRequired"
				value="${job.skillsRequired}"
				placeholder="e.g. Java, Spring Boot, SQL" required />

			<!-- Job Type -->
			<label for="jobType">Job Type:</label> <select id="jobType"
				name="jobType" required>
				<option value="">--Select Type--</option>
				<option value="Full-time"
					${job.jobType == 'Full-time' ? 'selected' : ''}>Full-time</option>
				<option value="Part-time"
					${job.jobType == 'Part-time' ? 'selected' : ''}>Part-time</option>
				<option value="Internship"
					${job.jobType == 'Internship' ? 'selected' : ''}>Internship</option>
				<option value="Contract"
					${job.jobType == 'Contract' ? 'selected' : ''}>Contract</option>
			</select>

			<!-- Salary -->
			<label for="salaryRange">Salary:</label> <input type="text"
				id="salaryRange" name="salaryRange" value="${job.salaryRange}"
				placeholder="e.g. ₹6,00,000 per annum" />

			<!-- Submit Button -->
			<button type="submit">Update Job</button>
		</form>

		<!-- Cancel Link -->
		<a href="/company-dashboard" class="cancel-link">Cancel</a>
	</div>

</body>
</html>
