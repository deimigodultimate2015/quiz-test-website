package com.sol.model.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sol.model.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
	
	Optional<Account> findByUsername(String username);
	
	Page<Account> findByEmailContaining(String email, Pageable pageable);
	Page<Account> findByUsernameContaining(String username, Pageable pageable);
	
	@Query("select a from Account a where a.id like CONCAT('%',:id,'%')")
	Page<Account> findByIdContaining(@Param("id") String id, Pageable pageable);
	
}
