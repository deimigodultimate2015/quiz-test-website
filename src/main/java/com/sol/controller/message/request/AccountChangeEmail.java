package com.sol.controller.message.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AccountChangeEmail {
	
	@NotBlank(message = "Username is required")
	public String username;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email")
	public String email;
	
	public AccountChangeEmail () {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
