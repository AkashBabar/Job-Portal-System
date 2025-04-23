package com.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Job Portal System application. This class bootstraps the
 * Spring Boot application.
 */
@SpringBootApplication
public class JobPortalSystemApplication {

	/**
	 * The main method that launches the Spring Boot application.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(JobPortalSystemApplication.class, args);
		System.out.println("✅ Job Portal System is up and running at: http://localhost:9091");
	}
}
