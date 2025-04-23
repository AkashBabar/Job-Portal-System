package com.jobportal.repository;

import com.jobportal.model.Job;
import com.jobportal.model.JobApplication;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

	/**
	 * 📄 Get all job applications submitted by a specific user (student).
	 */
	List<JobApplication> findByApplicant(User applicant);

	/**
	 * ❌ Check if an application already exists for a given job and student.
	 */
	boolean existsByApplicantAndJob(User applicant, Job job);

	/**
	 * 📂 Get all applications submitted for a specific job.
	 */
	List<JobApplication> findByJob(Job job);

	/**
	 * 🔍 Fetch a specific application for a student and job.
	 */
	JobApplication findByApplicantAndJob(User applicant, Job job);

	/**
	 * 🔢 Count total number of applications for a given job.
	 */
	long countByJob(Job job);
}
