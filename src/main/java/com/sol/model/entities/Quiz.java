package com.sol.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name = "quizs")
@JsonFilter(value = "quizFilter")
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nationalized
	@NotBlank(message = "Quiz name is required")
	@Size(min = 3, max = 255, message=  "Quiz name must contain at least 3 characters and max is 255 characters")
	private String name;
	
	@NotNull(message ="Subject is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private Subject subject;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
	private Set<Activity> activities = new HashSet<>();
	
	@Min(value = 0, message = "Time limit must bigger or equal 0")
	private Long timeLimit ;
	
	@Min(value = 0, message = "Total question must bigger or equal 0")
	private int totalQuestion;
	
	@Min(value = 0, message = "Pass score must bigger or equal 0")
	private int pass;
	
	@Column(length = 20000)
	private String quizQuestion;
	
	public Quiz() {}
	
	public Quiz(Long id, @NotBlank @Size(min = 3, max = 255) String name, Subject subject, @Min(0) Long timeLimit,
			@Min(0) int totalQuestion, @Min(0) int pass, String quizQuestion) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.timeLimit = timeLimit;
		this.totalQuestion = totalQuestion;
		this.pass = pass;
		this.quizQuestion = quizQuestion;
	}

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

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
}
