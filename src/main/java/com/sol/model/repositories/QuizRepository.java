package com.sol.model.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sol.model.entities.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
	boolean existsById(Long id);
	boolean existsByName(String name);
	
	@Query("select q from Quiz q where q.id like CONCAT('%',:id,'%')")
	Page<Quiz> findByIdContaining(@Param("id") String id, Pageable pageable);
	
	Page<Quiz> findByNameContaining(String name, Pageable pageable);
	
	@Query("select q from Quiz q where q.subject.name like CONCAT('%',:name,'%')")
	Page<Quiz> findBySubjectNameContaining(@Param("name")String subjectName, Pageable pageable);
}
