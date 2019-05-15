package com.sol.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name = "subjects", uniqueConstraints = {
		@UniqueConstraint(columnNames = "name")
})

@JsonFilter("subjectFilter")
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nationalized
	@NotBlank(message = "Subject name is required")
	@Size(min = 3, max =255, message = "Subject name must contain at least 3 characters and max is 255 characters")
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
	private Set<Quiz> quizs = new HashSet<>();
	
	public Subject() {}
	
	public Subject(
			@NotBlank(message = "Subject name is required") @Size(min = 3, max = 255, message = "Subject name must contain at least 3 characters and max is 255 characters") String name) {
		super();
		this.name = name;
	}

	public Subject(Long id, @NotBlank @Size(min = 3, max = 255) String name) {
		super();
		this.id = id;
		this.name = name;
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

	public Set<Quiz> getQuizs() {
		return quizs;
	}

	public void setQuizs(Set<Quiz> quizs) {
		this.quizs = quizs;
	};
	
	
}
