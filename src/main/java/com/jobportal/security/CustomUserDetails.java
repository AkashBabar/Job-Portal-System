package com.jobportal.security;

import com.jobportal.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails interface. This class
 * wraps the User entity to provide authentication details including
 * authorities, username, and password.
 */
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final User user;

	// Constructor to initialize the user
	public CustomUserDetails(User user) {
		this.user = user;
	}

	// Getter for the underlying User entity
	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Assign role as 'ROLE_' prefix to ensure Spring Security works with it
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
	}

	@Override
	public String getPassword() {
		return user.getPassword(); // Return the encrypted password from the User entity
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // Using email as the username (login identifier)
	}

	@Override
	public boolean isAccountNonExpired() {
		// This can be adjusted if you have expiration logic
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Can be customized based on account lock logic
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Can be adjusted if tracking credential expiration
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Modify if you have a flag for account activation/deactivation
		return true;
	}

	/**
	 * Optionally, you can override toString() if you need logging or debugging
	 * information.
	 */
	@Override
	public String toString() {
		return "CustomUserDetails{email=" + user.getEmail() + ", role=" + user.getRole() + "}";
	}
}
