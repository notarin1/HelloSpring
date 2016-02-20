package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.domain.service.LoginUserDetailService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // Spring Security がデフォルトで設定する
													// Basic認証が機能しなくなります。
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private LoginUserDetailService service;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
				// 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
				// 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
				// .antMatchers("/country/**").permitAll()
				.antMatchers("/", "/home").permitAll()
				.antMatchers("/assets/**", "/css/**", "/custom/**", "/dist/**", "/fonts/**", "/js/**").permitAll()
				.antMatchers("/admin").hasRole("ADMIN")
	            .anyRequest().authenticated();
        http
            .formLogin()
            	.loginPage("/login")
            	.permitAll()
                .and()
            .logout()
				// Required to use GET method for logout
				// ログアウトのリンクをformでなくアンカー(GET)で作る場合に必要。これがないと、/logoutにアクセスした時にCSRFトークンのチェックに引っ掛かる。
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
    }

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(userDetailService)
		// .passwordEncoder(new StandardPasswordEncoder());
		auth.userDetailsService(service);
	}
}
