package com.jobportal.security;

import com.jobportal.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		// Check if the Authorization header contains "Bearer <token>"
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwtToken = authHeader.substring(7); // Extract token from the header

			try {
				username = jwtUtil.extractUsername(jwtToken);
			} catch (Exception e) {
				logger.warn("JWT Token extraction failed: " + e.getMessage());
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
				return; // Stop the filter chain if token extraction fails
			}
		}

		// Validate token and set authentication context if token is valid
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// Validate the token and authenticate if valid
			if (jwtUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set authentication in the context
				SecurityContextHolder.getContext().setAuthentication(authToken);
				logger.info("User authenticated successfully: {}", username);
			}
		}

		// Continue the filter chain for the next filters
		filterChain.doFilter(request, response);
	}
}
