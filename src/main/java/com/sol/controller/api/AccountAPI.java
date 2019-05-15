package com.sol.controller.api;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sol.controller.customeException.ObjectAlreadyExists;
import com.sol.controller.customeException.StudentNotFoundException;
import com.sol.controller.message.request.AccountChangeEmail;
import com.sol.controller.message.request.AccountRegister;
import com.sol.controller.message.request.ChangeAccountPassword;
import com.sol.model.entities.Account;
import com.sol.model.entities.Role;
import com.sol.model.entities.RoleName;
import com.sol.model.repositories.AccountRepository;
import com.sol.model.repositories.RoleRepository;
import com.sol.model.repositories.StudentRepository;
import com.sol.model.utils.CheckValueAndSetDefault;
import com.sol.model.utils.CustomPageable;

@RestController
@CrossOrigin("*")
@RequestMapping("/manager")
public class AccountAPI {
	
	@Autowired
	AccountRepository accountRepositiory ;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CheckValueAndSetDefault checkValue;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	CustomPageable cPageable;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PutMapping(value = "/account/password")
	@PreAuthorize("hasRole('SYSADMIN')")
	public ResponseEntity<Object> updatePassword(@Validated @RequestBody ChangeAccountPassword changePassword) {
		
		Optional<Account> account = accountRepositiory.findByUsername(changePassword.getUsername());
		
		if(! account.isPresent()) {
			throw new ObjectAlreadyExists("Not found username: "+changePassword.getUsername());
		} else {
			account.get().setPassword(passwordEncoder.encode(changePassword.getPassword()));
			accountRepositiory.save(account.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(value = "/account")
	@PreAuthorize("hasRole('SYSADMIN')")
	public ResponseEntity<Object> addAccount(@Validated @RequestBody AccountRegister accountR) {
		if(accountRepositiory.existsByUsername(accountR.getUsername())) {
			throw new ObjectAlreadyExists("Username already exist");
		} else if (accountRepositiory.existsByEmail(accountR.getEmail())) {
			throw new ObjectAlreadyExists("Email already taken");
		}
		
		Account account = new Account();
		account.setEmail(accountR.getEmail());
		account.setUsername(accountR.getUsername());
		account.setPassword(passwordEncoder.encode(accountR.getPassword()));
		
		Set<Role> roles = new HashSet<>();
		
		switch (accountR.getRole()) {
		case "sysadmin":
			Role sysadminRole = roleRepository.findByName(RoleName.ROLE_SYSADMIN).orElseThrow(
					() -> new StudentNotFoundException("Not found sysadmin role"));
			roles.add(sysadminRole);
			break;

		case "admin":
			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(
					() -> new StudentNotFoundException("not found admin role"));
			roles.add(adminRole);
			break;
			
		default:
			if(studentRepository.existsByUsername(accountR.getUsername())) {
				Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(
						() -> new StudentNotFoundException("not found user role"));
				roles.add(userRole);
				break;
			} else {
				throw new StudentNotFoundException("Not found student with username: "+accountR.getUsername());
			}
		}
		
		account.setRoles(roles);
		Account account2 = accountRepositiory.save(account);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(account2.getId()).toUri();
		
		return ResponseEntity.created(location).build();
		
	}
	
	@PutMapping(value = "/account/{id}")
	public void updateAccount(@Validated @RequestBody AccountChangeEmail accountCE) {
		Optional<Account> account = accountRepositiory.findByUsername(accountCE.getUsername());
		if(!account.isPresent()) {
			throw new StudentNotFoundException("Account username: "+accountCE.username);
		}
		
		account.get().setEmail(accountCE.getEmail());
		
		accountRepositiory.save(account.get());
		
	}
	
	@GetMapping("/account/{id}")
	@PreAuthorize("hasRole('SYSADMIN')")
	public Account getAccount(@PathVariable("id") Long id) {
		Optional<Account> account = accountRepositiory.findById(id);
		if( account.isPresent() ) {
			return account.get() ;
		} else {
			throw new StudentNotFoundException("Account id: "+id);
		}
	}
	
	@GetMapping("/accounts")
	@PreAuthorize("hasRole('SYSADMIN')")
	public Page<Account> searchAccount(@RequestParam(name = "searchColumn", defaultValue = "id") String searchColumn,
									   @RequestParam(name = "searchText", 	defaultValue = "") String searchText,
									   @RequestParam(name = "sortColumn", 	defaultValue = "id") String sortColumn,
									   @RequestParam(name = "sortType", 	defaultValue = "ASC") String sortType,
									   @RequestParam(name = "pageSize", 	defaultValue = "5") Integer pageSize,
									   @RequestParam(name = "pageNumber",	defaultValue = "0") Integer pageNumber ) {
		
		searchColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(searchColumn,new String[] {"id","username","email"}, "id");		
		sortColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(sortColumn,new String[] {"id","username","email"}, "id");
		sortType = checkValue.CheckValueAndSetDefaultWithoutCaseSensitive(sortType, new String[] {"ASC", "DESC"}, "ASC").toUpperCase();
		searchText = checkValue.fromEqualToContain(searchText);
		
		if(pageNumber < 0) pageNumber = 0;
		if(pageSize < 0) pageSize = 0;
		
		Pageable pageable = cPageable.pageableWithStringSortType(pageNumber, pageSize, sortColumn, sortType);
		
		switch ( searchColumn ) {
		
		case "id":
			return accountRepositiory.findByIdContaining(searchText, pageable);
		case "username":
			return accountRepositiory.findByUsernameContaining(searchText, pageable);
		case "email":
			return accountRepositiory.findByEmailContaining(searchText, pageable);
		default:
			return accountRepositiory.findAll(pageable);
		}
			
	}
}
