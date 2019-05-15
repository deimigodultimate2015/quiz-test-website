package com.sol.controller.message.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ActivityCreator {
	
	@NotBlank(message = "Student Username is required")
	private String studentUsername;
	
	@NotNull(message = "Quiz ID is required")
	private Long quizID;
	
	@Min(value = 0, message = "Percent complete must bigger or equal 100")
	@Max(value = 100, message = "Percent complete must smaller or equal 100")
	private byte percentComplete;
	
	private boolean status;
	
	public ActivityCreator() {}

	public String getStudentUsername() {
		return studentUsername;
	}

	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}

	public Long getQuizID() {
		return quizID;
	}

	public void setQuizID(Long quizID) {
		this.quizID = quizID;
	}

	public byte getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(byte percentComplete) {
		this.percentComplete = percentComplete;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
