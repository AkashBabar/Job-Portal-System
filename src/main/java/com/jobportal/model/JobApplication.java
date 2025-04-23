package com.jobportal.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class JobApplication {

	public enum Status {
		APPLIED, SHORTLISTED, REJECTED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "applied_date", nullable = false)
	private LocalDate appliedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id", nullable = false)
	private Job job;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User applicant;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Status status;

	// ──────── Constructors ────────

	public JobApplication() {
		this.status = Status.APPLIED;
	}

	// Automatically set the applied date just before persisting
	@PrePersist
	protected void onCreate() {
		this.appliedDate = LocalDate.now();
	}

	// ──────── Getters and Setters ────────

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getAppliedDate() {
		return appliedDate;
	}

	public Date getAppliedDateAsDate() {
		return java.sql.Date.valueOf(appliedDate);
	}

	public void setAppliedDateAsDate(Date appliedDate) {
		this.appliedDate = appliedDate.toLocalDate();
	}

	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public User getApplicant() {
		return applicant;
	}

	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	// ──────── ToString ────────

	@Override
	public String toString() {
		return "JobApplication{" + "id=" + id + ", appliedDate=" + appliedDate + ", jobId="
				+ (job != null ? job.getId() : null) + ", applicantId=" + (applicant != null ? applicant.getId() : null)
				+ ", status=" + status + '}';
	}
}
