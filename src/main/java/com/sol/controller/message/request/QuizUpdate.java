package com.sol.controller.message.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

public class QuizUpdate {
	
	@NotNull(message = "Quiz id is required")
	private Long id;
	
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
	
	public QuizUpdate() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
