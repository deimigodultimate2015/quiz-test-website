package com.sol.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "activites")
@JsonFilter("activityFilter")
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quiz_id")
	private Quiz quiz;
	
	@NotNull(message = "Date is required")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date testDate;
	
	@Min(value = 0, message = "Percent complete must bigger or equal 100")
	@Max(value = 100, message = "Percent complete must smaller or equal 100")
	private byte percentComplete;
	
	private boolean status;
	
	public Activity() {}

	public Activity(Long id, Student student, Quiz quiz, @NotNull Date testDate, @Min(0) byte percentComplete,
			@NotNull boolean status) {
		super();
		this.id = id;
		this.student = student;
		this.quiz = quiz;
		this.testDate = testDate;
		this.percentComplete = percentComplete;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
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
	};
	
	
	
	
}
