package com.jobportal.repository;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

	/**
	 * 🔍 Search jobs by title keyword (case-insensitive).
	 */
	List<Job> findByTitleContainingIgnoreCase(String keyword);

	/**
	 * 🏢 Get all jobs posted by a specific user (typically a company).
	 */
	List<Job> findByPostedBy(User user);

	/**
	 * 📍 Filter jobs by location (case-insensitive).
	 */
	List<Job> findByLocationContainingIgnoreCase(String location);

	/**
	 * 🧠 Filter jobs by skills required (case-insensitive).
	 */
	List<Job> findBySkillsRequiredContainingIgnoreCase(String skill);

	/**
	 * 💼 Filter jobs by job type (e.g., Full-time, Part-time, Remote).
	 */
	List<Job> findByJobTypeIgnoreCase(String jobType);

	/**
	 * 💰 Filter jobs by salary range (case-insensitive).
	 */
	List<Job> findBySalaryRangeContainingIgnoreCase(String salaryRange);
}
