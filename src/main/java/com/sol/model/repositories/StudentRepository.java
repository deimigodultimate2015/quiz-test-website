package com.sol.model.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sol.model.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	public boolean existsByUsername(String username) ;
	
	@Query(value = "SELECT u FROM Student u WHERE u.id LIKE CONCAT('%',:id,'%')")
	public Page<Student> findByIdContaining(@Param("id") String id, Pageable pageable);
	
	public Optional<Student> findByUsername(String username);
	
	public Page<Student> findByUsernameContaining(String username, Pageable pageable);
	public Page<Student> findByNameContaining(String name, Pageable pageable);
	
	@Query(value = "SELECT s FROM Student s WHERE s.birthday LIKE CONCAT('%',:birthday,'%')")
	public Page<Student> findByBirthdayContaining(@Param("birthday") String birthday, Pageable pageable);
}
