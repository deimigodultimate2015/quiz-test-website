package com.sol.controller.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangeAccountPassword {
	
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
