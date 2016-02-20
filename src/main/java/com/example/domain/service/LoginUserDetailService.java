package com.example.domain.service;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.entity.Account;
import com.example.domain.entity.LoginUserDetail;

/**
 * ユーザの詳細情報を永続化層(DB)から取得する
 * 
 * @author notar_000
 *
 */
@Service
public class LoginUserDetailService implements UserDetailsService {
	@Inject
	private AccountService accountService;

	/**
	 * username: ユーザ名
	 */
	@Override
	public UserDetails loadUserByUsername(String userNameArg) throws UsernameNotFoundException {
		try {
			Account account = accountService.findByName(userNameArg); // (3)
			return new LoginUserDetail(account); // (4)
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Account not found.");
		}
	}
}
