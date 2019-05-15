package com.sol.model.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sol.model.entities.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>{

	boolean existsById(Long id);
	
	@Query("select a from Activity a where a.student.name like CONCAT('%',:studentName,'%')")
	Page<Activity> findByStudentNameContaining(@Param("studentName")String studentName, Pageable pageable);
	
	@Query("select a from Activity a where a.quiz.name like CONCAT('%',:quizName,'%')")
	Page<Activity> findByQuizNameContaining(@Param("quizName")String quizname, Pageable pageable);
	
	@Query("select a from Activity a where a.id like CONCAT('%',:id,'%')")
	Page<Activity> findByIdContaining(@Param("id")String id, Pageable pageable);
	
	@Query("select a from Activity a where a.testDate like CONCAT('%',:testDate,'%')")
	Page<Activity> findByTestDateContaining(@Param("testDate")String testDate, Pageable pageable);
	
}
