package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.JobService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	private final JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	// 🔐 Inject the currently logged-in user into all views
	@ModelAttribute("loggedInUser")
	public User getLoggedInUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			Object principal = auth.getPrincipal();
			if (principal instanceof CustomUserDetails customUserDetails) {
				return customUserDetails.getUser();
			}
		}
		return null;
	}

	// 📝 Show job posting form (Only accessible by company users)
	@GetMapping("/company-dashboard/job/new")
	public String showJobForm(@ModelAttribute("loggedInUser") User user, Model model,
			RedirectAttributes redirectAttributes) {
		if (!isCompany(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Access denied! Please log in as a company.");
			return "redirect:/login";
		}
		model.addAttribute("job", new Job());
		return "job_form";
	}

	// 💾 Save job after validation
	@PostMapping("/company-dashboard/job/save")
	public String saveJob(@Valid @ModelAttribute("job") Job job, BindingResult result,
			@ModelAttribute("loggedInUser") User user, RedirectAttributes redirectAttributes) {
		if (!isCompany(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access!");
			return "redirect:/login";
		}

		if (result.hasErrors()) {
			return "job_form";
		}

		job.setCompany(user.getName());
		job.setPostedBy(user);
		jobService.saveJob(job);

		logger.info("Company [{}] posted a new job: {}", user.getEmail(), job.getTitle());
		redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully!");
		return "redirect:/company-dashboard";
	}

	// ✏️ Edit job - accessible only if user owns the job
	@GetMapping("/company-dashboard/job/edit/{id}")
	public String editJob(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails,
			Model model, RedirectAttributes attributes) {
		User user = customUserDetails.getUser();
		try {
			Job job = jobService.getJobIfOwner(id, user.getId());
			model.addAttribute("job", job);
			return "job_form";
		} catch (RuntimeException e) {
			logger.warn("Edit denied for job {}: {}", id, e.getMessage());
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/company-dashboard";
		}
	}

	// 🔁 Update job if the user is the owner
	@PostMapping("/company-dashboard/job/update")
	public String updateJob(@ModelAttribute("job") Job updatedJob,
			@AuthenticationPrincipal CustomUserDetails customUserDetails, RedirectAttributes attributes) {
		User user = customUserDetails.getUser();
		try {
			jobService.updateJobIfOwner(updatedJob, user.getId());
			logger.info("Job [{}] updated by {}", updatedJob.getId(), user.getEmail());
			attributes.addFlashAttribute("successMessage", "Job updated successfully!");
		} catch (RuntimeException e) {
			logger.error("Error updating job {}: {}", updatedJob.getId(), e.getMessage());
			attributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/company-dashboard";
	}

	// ❌ Delete job if the user is the owner
	@GetMapping("/company-dashboard/job/delete/{id}")
	public String deleteJob(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails,
			RedirectAttributes attributes) {
		User user = customUserDetails.getUser();
		try {
			jobService.deleteJobIfOwner(id, user.getId());
			logger.info("Job [{}] deleted by {}", id, user.getEmail());
			attributes.addFlashAttribute("successMessage", "Job deleted successfully!");
		} catch (RuntimeException e) {
			logger.warn("Delete denied for job {}: {}", id, e.getMessage());
			attributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/company-dashboard";
	}

	// ✅ Helper: Check if user has COMPANY role
	private boolean isCompany(User user) {
		return user != null && User.Role.COMPANY.equals(user.getRole());
	}
}
