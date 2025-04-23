package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.User;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	/**
	 * Fetch all jobs
	 */
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	/**
	 * Save or update a job
	 */
	public Job saveJob(Job job) {
		return jobRepository.save(job);
	}

	/**
	 * Get a job by ID
	 */
	public Job getJobById(Long id) {
		return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
	}

	/**
	 * Delete a job by ID
	 */
	public void deleteJobById(Long id) {
		if (!jobRepository.existsById(id)) {
			throw new RuntimeException("Job not found with ID: " + id);
		}
		jobRepository.deleteById(id);
	}

	/**
	 * Search jobs by keyword in the title
	 */
	public List<Job> searchJobs(String keyword) {
		return jobRepository.findByTitleContainingIgnoreCase(keyword);
	}

	/**
	 * Get jobs posted by a specific user
	 */
	public List<Job> getJobsByUser(User user) {
		return jobRepository.findByPostedBy(user);
	}

	/**
	 * Get applicants for a specific job
	 */
	public List<JobApplication> getApplicantsForJob(Job job) {
		return jobApplicationRepository.findByJob(job);
	}

	/**
	 * Filter jobs by job type (e.g., Full-time, Part-time)
	 */
	public List<Job> getJobsByType(String jobType) {
		return jobRepository.findByJobTypeIgnoreCase(jobType);
	}

	/**
	 * Filter jobs by location keyword
	 */
	public List<Job> getJobsByLocation(String location) {
		return jobRepository.findByLocationContainingIgnoreCase(location);
	}

	/**
	 * Filter jobs by skill keyword
	 */
	public List<Job> getJobsBySkill(String skill) {
		return jobRepository.findBySkillsRequiredContainingIgnoreCase(skill);
	}

	/**
	 * Filter jobs by salary keyword
	 */
	public List<Job> getJobsBySalaryRange(String salaryKeyword) {
		return jobRepository.findBySalaryRangeContainingIgnoreCase(salaryKeyword);
	}

	/**
	 * 🔍 Filter jobs using multiple optional criteria
	 */
	public List<Job> searchJobsByFilters(String keyword, String location, String jobType, String skills,
			String salary) {
		List<Job> jobs = jobRepository.findAll();

		return jobs.stream()
				.filter(job -> keyword == null || keyword.isEmpty()
						|| job.getTitle().toLowerCase().contains(keyword.toLowerCase()))
				.filter(job -> location == null || location.isEmpty()
						|| job.getLocation().toLowerCase().contains(location.toLowerCase()))
				.filter(job -> jobType == null || jobType.isEmpty() || job.getJobType().equalsIgnoreCase(jobType))
				.filter(job -> skills == null || skills.isEmpty()
						|| job.getSkillsRequired().toLowerCase().contains(skills.toLowerCase()))
				.filter(job -> salary == null || salary.isEmpty()
						|| job.getSalaryRange().toLowerCase().contains(salary.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * Ensure only the job owner can delete the job
	 */
	public void deleteJobIfOwner(Long jobId, Long companyId) {
		Job job = jobRepository.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

		if (!job.getPostedBy().getId().equals(companyId)) {
			throw new RuntimeException("Unauthorized to delete this job.");
		}

		jobRepository.deleteById(jobId);
	}

	/**
	 * Get the job if the user is the owner
	 */
	public Job getJobIfOwner(Long jobId, Long userId) {
		Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

		if (!job.getPostedBy().getId().equals(userId)) {
			throw new RuntimeException("Unauthorized access: You are not the owner of this job");
		}

		return job;
	}

	/**
	 * Update the job if the user is the owner
	 */
	@Transactional
	public void updateJobIfOwner(Job updatedJob, Long userId) {
		Job existingJob = jobRepository.findById(updatedJob.getId())
				.orElseThrow(() -> new RuntimeException("Job not found"));

		// Check if the logged-in user is the owner of the job
		if (!existingJob.getPostedBy().getId().equals(userId)) {
			throw new RuntimeException("You are not authorized to edit this job");
		}

		// If ownership is confirmed, proceed with update
		existingJob.setTitle(updatedJob.getTitle());
		existingJob.setDescription(updatedJob.getDescription());
		existingJob.setLocation(updatedJob.getLocation());
		existingJob.setSalaryRange(updatedJob.getSalaryRange());
		existingJob.setJobType(updatedJob.getJobType());
		existingJob.setSkillsRequired(updatedJob.getSkillsRequired());
		// Add any other fields that need to be updated

		jobRepository.save(existingJob); // Save the updated job
	}

}
