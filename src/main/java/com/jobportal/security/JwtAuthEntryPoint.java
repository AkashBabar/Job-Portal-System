package com.jobportal.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// Log the authentication exception for better traceability
		logger.error("Unauthorized access attempt: {}", authException.getMessage());

		// Send detailed error response with HTTP status 401 (Unauthorized)
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		// Create a JSON error response
		String errorResponse = "{ \"error\": \"Unauthorized\", \"message\": \"Token is missing or invalid.\" }";
		response.getWriter().write(errorResponse);
	}
}
