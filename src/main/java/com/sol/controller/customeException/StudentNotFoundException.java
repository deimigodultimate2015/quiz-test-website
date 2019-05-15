package com.sol.controller.customeException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5778055952115677164L;

	public StudentNotFoundException(String message) {
		super(message);
	}
}
