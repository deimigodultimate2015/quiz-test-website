package com.sol.controller.message.response;

import java.util.HashSet;
import java.util.Set;

import com.sol.model.entities.Activity;
import com.sol.model.entities.Subject;

public class QuizList {
	
	private Long id;
	
	private String name;
	
	private Subject subject;
	
	private Set<Activity> activities = new HashSet<>();
	
	private Long timeLimit ;
	
	private int totalQuestion;
	
	private int pass;
	
	public QuizList() {}

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

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
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
	
	
	
}
