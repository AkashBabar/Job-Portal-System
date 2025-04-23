package com.jobportal.repository;

import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	// ✅ Updated: Case-insensitive fetch user by email
	Optional<User> findByEmailIgnoreCase(String email);

	// ✅ Updated: Case-insensitive check if email exists
	boolean existsByEmailIgnoreCase(String email);

	// 🔍 Find users by role (STUDENT, COMPANY, ADMIN)
	List<User> findByRole(User.Role role);

	// 🔍 Search users by name containing a keyword (case insensitive)
	List<User> findByNameContainingIgnoreCase(String name);

	// 🔍 Search users by both role and name keyword (case insensitive)
	List<User> findByRoleAndNameContainingIgnoreCase(User.Role role, String name);
}
