package com.jobportal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jobportal.exception.JwtTokenException;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	private static final String CLAIM_ROLES = "roles";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	private Key secretKey;

	@PostConstruct
	public void init() {
		logger.info("=== JWT CONFIGURATION ===");
		logger.info("JWT Secret: {}", secret != null ? "[PROTECTED]" : "null");
		logger.info("JWT Expiration (ms): {}", jwtExpiration);

		if (secret == null || secret.isEmpty()) {
			throw new IllegalStateException("JWT secret is not configured!");
		}

		// Use the Keys.secretKeyFor method to generate a secure key for HS512
		this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(userDetails, jwtExpiration);
	}

	public String generateToken(UserDetails userDetails, long expirationMillis) {
		String roles = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority())
				.collect(Collectors.joining(","));

		return Jwts.builder().setSubject(userDetails.getUsername()).claim(CLAIM_ROLES, roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
				.signWith(secretKey, SignatureAlgorithm.HS512).compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public List<String> extractRoles(String token) {
		String roles = extractClaim(token, claims -> claims.get(CLAIM_ROLES, String.class));
		if (roles == null || roles.isBlank()) {
			return List.of();
		}
		return List.of(roles.split(","));
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).setAllowedClockSkewSeconds(60) // Allow 1 minute of
																								// clock skew
					.build().parseClaimsJws(token).getBody();
		} catch (JwtException | IllegalArgumentException e) {
			logger.error("JWT token parsing error: {}", e.getMessage());
			throw new JwtTokenException("Invalid or expired JWT token", e);
		}
	}
}
