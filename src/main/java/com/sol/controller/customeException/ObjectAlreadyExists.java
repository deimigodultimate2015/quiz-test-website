package com.sol.controller.customeException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ObjectAlreadyExists extends RuntimeException {
	
	private static final long serialVersionUID = -2804759651876455561L;

	public ObjectAlreadyExists(String message) {
		super(message);
	}
}
