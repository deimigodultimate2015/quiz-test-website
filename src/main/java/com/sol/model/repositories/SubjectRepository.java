package com.sol.model.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sol.model.entities.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{
	boolean existsByName(String name);
	
	@Query(value = "select s from Subject s where s.id like CONCAT('%',:id,'%')")
	Page<Subject> findByIdContaining(@Param("id") String id, Pageable pageable);
	
	Page<Subject> findByNameContaining(String name, Pageable pageable);
}
