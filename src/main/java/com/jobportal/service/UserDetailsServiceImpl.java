package com.jobportal.service;

import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Load user details by email for authentication.
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String trimmedEmail = email.trim();
		Optional<User> optionalUser;

		try {
			optionalUser = userRepository.findByEmailIgnoreCase(trimmedEmail);
		} catch (DataAccessException e) {
			logger.error("Error accessing the database while loading user: {}", trimmedEmail, e);
			throw new UsernameNotFoundException("Database error while loading user: " + trimmedEmail, e);
		}

		// Log the successful retrieval of the user
		return optionalUser.map(this::buildUserDetails).orElseThrow(() -> {
			logger.error("No user found with email: {}", trimmedEmail);
			return new UsernameNotFoundException("No user found with email: " + trimmedEmail);
		});
	}

	/**
	 * Build user details with the necessary information for authentication.
	 */
	private UserDetails buildUserDetails(User user) {
		if (user.getRole() == null) {
			logger.error("User role is null for user: {}", user.getEmail());
			throw new IllegalStateException("User role is null for user: " + user.getEmail());
		}

		// Log the loading of the user with role
		logger.info("Successfully loaded user: {} with role: {}", user.getEmail(), user.getRole().name());

		// Return the custom user details object with relevant information for security
		// context
		return new CustomUserDetails(user);
	}
}
