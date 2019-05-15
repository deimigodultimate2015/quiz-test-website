package com.sol.controller.message.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StudentCreator {
	
	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 255, message = "Name size between 3 - 255")
	private String name;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	
	@NotNull
	private boolean gender;
	
	public StudentCreator() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	
}
