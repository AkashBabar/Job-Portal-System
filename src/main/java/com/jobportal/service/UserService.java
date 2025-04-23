package com.jobportal.service;

import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Register a new user with password encoding.
	 */
	public User registerUser(User user) {
		if (user == null) {
			logger.error("🔴 User object is null during registration attempt");
			throw new IllegalArgumentException("User cannot be null");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			logger.error("🔴 Password is empty or null for user: {}", user.getEmail());
			throw new IllegalArgumentException("Password cannot be empty");
		}

		// Encrypt password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		try {
			return userRepo.save(user);
		} catch (Exception e) {
			logger.error("❌ Error registering user: {}", user.getEmail(), e);
			throw new RuntimeException("User registration failed", e);
		}
	}

	/**
	 * Check if an email already exists in the system.
	 */
	public boolean emailExists(String email) {
		return userRepo.existsByEmailIgnoreCase(email);
	}

	/**
	 * Get user by email (returns an Optional).
	 */
	public Optional<User> getByEmail(String email) {
		return userRepo.findByEmailIgnoreCase(email);
	}

	/**
	 * Get user by email (throws an exception if not found).
	 */
	public User getUserByEmail(String email) {
		Optional<User> optionalUser = userRepo.findByEmailIgnoreCase(email);
		if (optionalUser.isEmpty()) {
			logger.warn("⚠️ User not found for email: {}", email);
		} else {
			logger.info("✅ User found: {}", optionalUser.get().getEmail());
		}
		return optionalUser.orElse(null); // Returns null if not found
	}

	/**
	 * Get all registered users.
	 */
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	/**
	 * Save or update user information (ensures password is encoded).
	 */
	public User saveUser(User user) {
		if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
			if (user.getPassword().isEmpty()) {
				logger.error("🔴 Password is empty for user: {}", user.getEmail());
				throw new IllegalArgumentException("Password cannot be empty");
			}
			// Encode password before saving
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return userRepo.save(user);
	}

	/**
	 * Delete user by ID.
	 */
	public void deleteUserById(Long id) {
		if (!userRepo.existsById(id)) {
			logger.warn("⚠️ Attempted to delete a non-existing user with ID: {}", id);
			throw new RuntimeException("User not found with ID: " + id);
		}
		userRepo.deleteById(id);
		logger.info("✅ User with ID: {} deleted successfully", id);
	}

	/**
	 * Get user by ID (returns an Optional).
	 */
	public Optional<User> getUserById(Long id) {
		return userRepo.findById(id);
	}
}
