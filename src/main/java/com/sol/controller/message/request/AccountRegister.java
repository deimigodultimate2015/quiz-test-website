package com.sol.controller.message.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AccountRegister {
	@NotBlank(message = "Username is required")
	@Size(min =3, max =255, message="Username must contain at least 3 and max is 255 characters")
	public String username;
	
	@NotBlank(message = "Password is required")
	@Size(min =6, max =100, message="Password must contain at least 6 and max is 100 characters")
	public String password;
	
	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	@Size(max = 255, message = "Email max length is 255 characters")
	public String email;
	
	public String role;
	
	public AccountRegister() {}
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
