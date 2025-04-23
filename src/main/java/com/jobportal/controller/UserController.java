package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.User.Role;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.JobApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String REDIRECT_STUDENT_DASHBOARD = "redirect:/student-dashboard";
	private static final String REDIRECT_COMPANY_DASHBOARD = "redirect:/company-dashboard";
	private static final String REDIRECT_ADMIN_DASHBOARD = "redirect:/admin-dashboard";

	private final UserService userService;
	private final JobApplicationService jobApplicationService;
	private final JobService jobService;

	public UserController(UserService userService, JobApplicationService jobApplicationService, JobService jobService) {
		this.userService = userService;
		this.jobApplicationService = jobApplicationService;
		this.jobService = jobService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Role.class, new java.beans.PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(Role.valueOf(text.toUpperCase()));
			}
		});
	}

	@ModelAttribute("roles")
	public Role[] roles() {
		return Role.values();
	}

	@ModelAttribute("loggedInUser")
	public User populateLoggedInUser() {
		return getLoggedInUser();
	}

	@GetMapping("/")
	public String home() {
		User user = getLoggedInUser();
		return (user != null) ? redirectToDashboard(user) : "index";
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		User user = getLoggedInUser();
		if (user != null) {
			return redirectToDashboard(user);
		}
		model.addAttribute("roles", Role.values());
		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", Role.values());
		return "register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
		if (user.getEmail() == null || !user.getEmail().contains("@")) {
			model.addAttribute("error", "Invalid email address.");
			return "register";
		}
		if (user.getRole() == null) {
			model.addAttribute("error", "Please select a role.");
			return "register";
		}
		if (userService.emailExists(user.getEmail())) {
			model.addAttribute("error", "Email already registered!");
			return "register";
		}

		userService.registerUser(user);
		logger.info("New user registered: {}", user.getEmail());
		redirectAttributes.addFlashAttribute("message", "Registered successfully! Please login.");
		return REDIRECT_LOGIN;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin-dashboard")
	public String adminDashboard(@ModelAttribute("loggedInUser") User user, Model model) {
		if (!isAuthorized(user, Role.ADMIN, model))
			return "access-denied";

		model.addAttribute("users", userService.getAllUsers());
		return "admin-dashboard";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin-dashboard/delete/{id}")
	public String deleteUser(@PathVariable Long id, @ModelAttribute("loggedInUser") User admin, Model model) {
		if (!isAuthorized(admin, Role.ADMIN, model))
			return "access-denied";

		userService.deleteUserById(id);
		logger.info("Admin {} deleted user ID: {}", admin.getEmail(), id);
		return REDIRECT_ADMIN_DASHBOARD;
	}

	@PreAuthorize("hasRole('COMPANY')")
	@GetMapping("/company-dashboard")
	public String companyDashboard(@ModelAttribute("loggedInUser") User user, Model model) {
		if (!isAuthorized(user, Role.COMPANY, model))
			return "access-denied";

		model.addAttribute("jobs", jobService.getJobsByUser(user));
		return "company-dashboard";
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/student-dashboard")
	public String studentDashboard(@ModelAttribute("loggedInUser") User user, Model model) {
		if (!isAuthorized(user, Role.STUDENT, model))
			return "access-denied";

		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("appliedJobs", jobApplicationService.getApplicationsByUser(user));
		return "student-dashboard";
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PostMapping("/apply/{jobId}")
	public String applyForJob(@PathVariable Long jobId, @ModelAttribute("loggedInUser") User student, Model model) {
		if (!isAuthorized(student, Role.STUDENT, model))
			return "access-denied";

		try {
			Job job = jobService.getJobById(jobId);
			if (job == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");

			if (jobApplicationService.hasApplied(student, job)) {
				model.addAttribute("message", "You have already applied for this job.");
			} else {
				jobApplicationService.applyForJob(student, job);
				logger.info("Student {} applied for job ID: {}", student.getEmail(), jobId);
			}
		} catch (Exception e) {
			logger.error("Error applying for job ID {}: {}", jobId, e.getMessage());
			model.addAttribute("error", "An error occurred while applying for the job.");
		}

		return studentDashboard(student, model);
	}

	@GetMapping("/logout")
	public String logout() {
		SecurityContextHolder.clearContext();
		return "redirect:/";
	}

	@GetMapping("/access-denied")
	public String accessDeniedPage() {
		return "access-denied";
	}

	private User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof CustomUserDetails customUserDetails) {
				User user = customUserDetails.getUser();
				logger.debug("Logged in user: {}", user.getEmail());
				return user;
			}
			logger.warn("Unexpected principal type: {}", principal);
		}
		return null;
	}

	private boolean isAuthorized(User user, Role requiredRole, Model model) {
		if (user == null || user.getRole() != requiredRole) {
			model.addAttribute("error", "Access Denied! Required Role: " + requiredRole);
			logger.warn("Unauthorized access attempt by user: {}", (user != null) ? user.getEmail() : "Unknown");
			return false;
		}
		return true;
	}

	private String redirectToDashboard(User user) {
		return switch (user.getRole()) {
		case STUDENT -> REDIRECT_STUDENT_DASHBOARD;
		case COMPANY -> REDIRECT_COMPANY_DASHBOARD;
		case ADMIN -> REDIRECT_ADMIN_DASHBOARD;
		default -> {
			logger.warn("Unknown role for user: {}", user.getEmail());
			yield "redirect:/access-denied";
		}
		};
	}
}
