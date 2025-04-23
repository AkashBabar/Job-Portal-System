package com.jobportal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs", indexes = { @Index(name = "idx_company", columnList = "company"),
		@Index(name = "idx_title", columnList = "title") })
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String company;

	private String location;

	@Column(length = 1000)
	private String description;

	private String skillsRequired;

	private String jobType; // e.g., Full-time, Part-time, Internship

	private String salaryRange; // Optional field for displaying job salary

	@Column(name = "posted_date", nullable = false)
	private LocalDate postedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posted_by", nullable = false)
	private User postedBy;

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobApplication> applications = new ArrayList<>();

	// ──────── Lifecycle Callback ────────
	@PrePersist
	public void onCreate() {
		this.postedDate = LocalDate.now();
	}

	// ──────── Getters and Setters ────────

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSkillsRequired() {
		return skillsRequired;
	}

	public void setSkillsRequired(String skillsRequired) {
		this.skillsRequired = skillsRequired;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getSalaryRange() {
		return salaryRange;
	}

	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}

	public LocalDate getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public List<JobApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<JobApplication> applications) {
		this.applications = applications;
	}

	public void addApplication(JobApplication application) {
		applications.add(application);
		application.setJob(this);
	}

	public void removeApplication(JobApplication application) {
		applications.remove(application);
		application.setJob(null);
	}
}
