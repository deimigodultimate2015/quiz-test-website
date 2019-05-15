package com.sol.model.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "students", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username")
})

@JsonFilter("studentFilter")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 15)
	private String username;
	
	@Nationalized
	@NotBlank(message = "Student name is required")
	@Size(min = 3, max = 255, message = "Student name must contain at least 3 characters and max is 255 characters")
	@Column(length = 255)
	private String name;
	
	@Column
	private boolean gender;
	
	@NotNull(message = "Birthday is required")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
	private Set<Activity> activities = new HashSet<>();
	
	public Student() {}

	public Student(@NotBlank String name, @NotNull boolean gender, @NotNull Date birthday) {
		super();
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
	}

	public Student(Long id, @NotBlank String username, @NotBlank String name, @NotNull boolean gender,
			@NotNull Date birthday, Set<Activity> activities) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.activities = activities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
}
