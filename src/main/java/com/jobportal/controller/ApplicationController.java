package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.JobApplicationService;
import com.jobportal.service.JobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ApplicationController {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	@Autowired
	private JobApplicationService applicationService;

	@Autowired
	private JobService jobService;

	// 🔹 Student applies for a job
	@PostMapping("/jobs/apply/{jobId}")
	public String applyToJob(@PathVariable Long jobId, @AuthenticationPrincipal CustomUserDetails customUserDetails,
			RedirectAttributes redirectAttributes) {

		User user = customUserDetails != null ? customUserDetails.getUser() : null;

		// Ensure only students can apply
		if (user == null || user.getRole() != User.Role.STUDENT) {
			redirectAttributes.addFlashAttribute("error", "Please log in as a student to apply.");
			return "redirect:/login";
		}

		try {
			Job job = jobService.getJobById(jobId);

			// Check if the student has already applied
			if (applicationService.hasApplied(user, job)) {
				redirectAttributes.addFlashAttribute("message", "You have already applied for this job.");
			} else {
				JobApplication application = new JobApplication();
				application.setJob(job);
				application.setApplicant(user);
				// application.setAppliedDate(LocalDate.now()); // No longer needed — set via
				// @PrePersist

				applicationService.apply(application);
				logger.info("User {} applied for job '{}'", user.getEmail(), job.getTitle());

				redirectAttributes.addFlashAttribute("message", "Application submitted successfully.");
			}

		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", "Error while applying: " + e.getMessage());
		}

		return "redirect:/student-dashboard/my-applications";
	}

	// 🔹 Student views their job applications
	@GetMapping("/student-dashboard/my_applications")
	public String myApplications(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
		User user = customUserDetails.getUser();

		// Fetch applications only for the logged-in student
		List<JobApplication> myApplications = applicationService.getApplicationsByUser(user);

		model.addAttribute("applications", myApplications);
		return "my_applications";
	}

	// 🔹 Company views applicants for a specific job they posted
	@GetMapping("/company-dashboard/job/view-applicants/{jobId}")
	public String viewApplicants(@PathVariable Long jobId, @AuthenticationPrincipal CustomUserDetails customUserDetails,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			User companyUser = customUserDetails.getUser();

			// Ensure the job belongs to the logged-in company user
			Job job = jobService.getJobIfOwner(jobId, companyUser.getId());

			// Fetch all applicants for this job
			List<JobApplication> applicants = applicationService.getApplicantsForJob(job);

			model.addAttribute("applicants", applicants);
			model.addAttribute("job", job);

			return "view-applicants"; // Update this path as per your JSP structure

		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/company-dashboard";
		}
	}

}
