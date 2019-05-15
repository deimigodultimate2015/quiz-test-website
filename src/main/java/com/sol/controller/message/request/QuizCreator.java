package com.sol.controller.message.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

public class QuizCreator {
	
	@Nationalized
	@NotBlank(message = "Quiz name is required")
	@Size(min = 3, max = 255, message=  "Quiz name must contain at least 3 characters and max is 255 characters")
	private String name;
	
	@NotNull(message ="Subject is required")
	private Long subjectID;
	
	@Min(value = 0, message = "Time limit must bigger or equal 0")
	private Long timeLimit ;
	
	@Min(value = 0, message = "Total question must bigger or equal 0")
	private int totalQuestion;
	
	@Min(value = 0, message = "Pass score must bigger or equal 0")
	private int pass;
	
	private String quizQuestion;
	
	public QuizCreator() {}
	
	public QuizCreator(
			@NotBlank(message = "Quiz name is required") @Size(min = 3, max = 255, message = "Quiz name must contain at least 3 characters and max is 255 characters") String name,
			@NotNull(message = "Subject is required") Long subjectID,
			@Min(value = 0, message = "Time limit must bigger or equal 0") Long timeLimit,
			@Min(value = 0, message = "Total question must bigger or equal 0") int totalQuestion,
			@Min(value = 0, message = "Pass score must bigger or equal 0") int pass, String quizQuestion) {
		super();
		this.name = name;
		this.subjectID = subjectID;
		this.timeLimit = timeLimit;
		this.totalQuestion = totalQuestion;
		this.pass = pass;
		this.quizQuestion = quizQuestion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}

	public Long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public String getQuizQuestion() {
		return quizQuestion;
	}

	public void setQuizQuestion(String quizQuestion) {
		this.quizQuestion = quizQuestion;
	}
}
