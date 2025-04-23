package com.jobportal.security;

import com.jobportal.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final UserDetailsServiceImpl userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthEntryPoint jwtEntryPoint;

	public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter,
			JwtAuthEntryPoint jwtEntryPoint) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtEntryPoint = jwtEntryPoint;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint)) // Handle
																												// JWT
																												// auth
																												// errors
				.authorizeHttpRequests(auth -> auth
						// JWT Public Endpoints
						.requestMatchers("/api/auth/**").permitAll()

						// JSP Public Endpoints
						.requestMatchers("/", "/login", "/register", "/access-denied", "/css/**", "/js/**",
								"/resources/**", "/WEB-INF/views/**")
						.permitAll()

						// Role Based Access Control
						.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/company/**").hasRole("COMPANY")
						.requestMatchers("/student/**").hasRole("STUDENT")

						// Any other requests require authentication
						.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
						.successHandler(this::customSuccessHandler).failureUrl("/login?error=true").permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/login?logout").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID").permitAll())
				.authenticationProvider(authenticationProvider()).userDetailsService(userDetailsService);

		// Add JWT filter BEFORE the UsernamePasswordAuthenticationFilter
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Custom success handler to redirect users based on their roles.
	 */
	private void customSuccessHandler(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		try {
			var roles = authentication.getAuthorities().stream().map(auth -> auth.getAuthority()).toList();
			logger.debug("Authenticated user roles: {}", roles); // Change to DEBUG level for production

			if (roles.contains("ROLE_ADMIN")) {
				response.sendRedirect("/admin-dashboard");
			} else if (roles.contains("ROLE_COMPANY")) {
				response.sendRedirect("/company-dashboard");
			} else if (roles.contains("ROLE_STUDENT")) {
				response.sendRedirect("/student-dashboard");
			} else {
				response.sendRedirect("/access-denied");
			}
		} catch (Exception e) {
			logger.error("Error in custom success handler", e);
		}
	}
}
