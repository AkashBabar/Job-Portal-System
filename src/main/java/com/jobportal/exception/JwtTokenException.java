package com.jobportal.exception;

public class JwtTokenException extends RuntimeException {
	public JwtTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
