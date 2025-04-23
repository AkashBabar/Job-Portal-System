package com.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.jobportal.model.JwtResponse;
import com.jobportal.model.LoginRequest;
import com.jobportal.security.JwtUtil;
import com.jobportal.service.UserDetailsServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			// Authenticate the user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			// Load user details
			UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

			// Generate JWT token using the UserDetails object
			String token = jwtUtil.generateToken(userDetails);

			// Return the JWT response
			return ResponseEntity.ok(new JwtResponse(token));
		} catch (BadCredentialsException e) {
			// Log invalid login attempt
			logger.warn("Invalid login attempt for username: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		} catch (Exception e) {
			// Handle other exceptions
			logger.error("Error during authentication: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
		}
	}
}
