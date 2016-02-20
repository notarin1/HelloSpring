package com.example.domain.service;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.entity.Account;
import com.example.domain.entity.LoginUserDetail;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Inject
	private AccountService accountService;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account account = accountService.findByName(username); // (3)
			return new LoginUserDetail(account); // (4)
		} catch (Exception e) {
			throw new UsernameNotFoundException("user not found", e); // (5)
		}
	}
}
