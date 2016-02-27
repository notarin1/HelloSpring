package com.example.domain.service;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * userNameArg: ユーザ名
     */
    @Override
    public UserDetails loadUserByUsername(String userNameArg) throws UsernameNotFoundException {
	try {
	    // DBからユーザ名でAccount情報を取得する
	    Account account = accountService.findByName(userNameArg);
	    String notarin1 = passwordEncoder.encode("notarin1");

	    // ユーザROLE情報をDBの権限情報から作成する
	    List<GrantedAuthority> authorities = StringUtils.equals(account.getRole(), "ROLE_ADMIN")
		    ? Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"))
		    : Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

	    // TODO テストのためこんな所でpasswordEncoder使ってるが、Account.createの所で使ってDBに記録する
	    return new LoginUserDetail(account, account.getPassword(), authorities); // (4)
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new UsernameNotFoundException("Account not found.");
	}
    }
}
