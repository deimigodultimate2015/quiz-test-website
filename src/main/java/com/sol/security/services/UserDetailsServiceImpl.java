package com.sol.security.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sol.model.entities.Account;
import com.sol.model.repositories.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//It just simple if not found account with username then throw an runtime Exception
		Account account = accountRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Account not found with this username: "+username));
		
		return UserPrinciple.build(account);
	}

}
