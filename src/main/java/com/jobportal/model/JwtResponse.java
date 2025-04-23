package com.jobportal.model;

public class JwtResponse {

	private String token;

	// Default constructor
	public JwtResponse() {
	}

	// Constructor with token
	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		// Avoid printing the token in logs, as it's sensitive information
		return "JwtResponse{token='*****'}";
	}
}
