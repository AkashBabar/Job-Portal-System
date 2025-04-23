package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.User;
import com.jobportal.repository.JobApplicationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(JobApplicationService.class);

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	/**
	 * Save a job application directly with validation.
	 */
	@Transactional
	public JobApplication apply(JobApplication application) {
		if (application == null || application.getApplicant() == null || application.getJob() == null) {
			throw new IllegalArgumentException("Application, Applicant, or Job must not be null");
		}

		application.setAppliedDate(LocalDate.now());
		application.setStatus(JobApplication.Status.APPLIED);

		logger.info("Saving application for job '{}' by user '{}'", application.getJob().getTitle(),
				application.getApplicant().getEmail());

		return jobApplicationRepository.save(application);
	}

	/**
	 * Apply for a job only if not already applied.
	 */
	@Transactional
	public Optional<JobApplication> applyForJob(User student, Job job) {
		if (student == null || job == null) {
			throw new IllegalArgumentException("Student or Job cannot be null");
		}

		if (!hasApplied(student, job)) {
			JobApplication application = new JobApplication();
			application.setApplicant(student);
			application.setJob(job);
			application.setAppliedDate(LocalDate.now());
			application.setStatus(JobApplication.Status.APPLIED);

			logger.info("User '{}' applied to job '{}'", student.getEmail(), job.getTitle());
			return Optional.of(jobApplicationRepository.save(application));
		} else {
			logger.info("User '{}' has already applied to job '{}'", student.getEmail(), job.getTitle());
			return Optional.empty();
		}
	}

	/**
	 * Check if a user has already applied to a specific job.
	 */
	public boolean hasApplied(User student, Job job) {
		if (student == null || job == null)
			return false;
		return jobApplicationRepository.existsByApplicantAndJob(student, job);
	}

	/**
	 * Get all job applications for a given user.
	 */
	public List<JobApplication> getApplicationsByUser(User user) {
		if (user == null)
			return Collections.emptyList();
		return jobApplicationRepository.findByApplicant(user);
	}

	/**
	 * Get all job applications for a specific job.
	 */
	public List<JobApplication> getApplicantsForJob(Job job) {
		if (job == null)
			return Collections.emptyList();
		return jobApplicationRepository.findByJob(job);
	}

	/**
	 * Count number of applications for a job.
	 */
	public long countApplicationsForJob(Job job) {
		if (job == null)
			return 0;
		return jobApplicationRepository.countByJob(job);
	}

	/**
	 * Update the status of a job application.
	 */
	@Transactional
	public void updateApplicationStatus(Long applicationId, JobApplication.Status status) {
		if (applicationId == null || status == null) {
			throw new IllegalArgumentException("Application ID and Status must not be null");
		}

		Optional<JobApplication> optionalApp = jobApplicationRepository.findById(applicationId);
		optionalApp.ifPresentOrElse(app -> {
			app.setStatus(status);
			jobApplicationRepository.save(app);
			logger.info("Updated application ID {} status to {}", applicationId, status);
		}, () -> {
			logger.warn("Application ID {} not found for status update", applicationId);
		});
	}
}
