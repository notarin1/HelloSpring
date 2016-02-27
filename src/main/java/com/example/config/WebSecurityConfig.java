package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.domain.service.LoginUserDetailService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // Spring Security がデフォルトで設定する
						 // Basic認証が機能しなくなります。
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginUserDetailService userDetailService;
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
		// 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
		// 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
		// .antMatchers("/country/**").permitAll()
		.antMatchers("/", "/news", "new_acount").permitAll()
		.antMatchers("/assets/**", "/css/**", "/custom/**", "/dist/**", "/fonts/**", "/js/**").permitAll()
		// TODO: ADMIN roleじゃないと/adminには入れない
		.antMatchers("/home").hasRole("USER")
		.antMatchers("/admin").hasRole("ADMIN")
		// それ以外は匿名アクセス禁止
		.anyRequest().authenticated();
	/*
	 * 現在の認証で認可する場合は、.fullyAuthenticated()を使用。
	 * Remember-me認証で認可する場合は、.authenticated()を使用。
	 */
	http.formLogin().loginPage("/login")
		// templateで指定するパラメータ名
		.usernameParameter("username").passwordParameter("password").permitAll()
		// Required to use GET method for logout
		// ログアウトのリンクをformでなくアンカー(GET)で作る場合に必要。これがないと、/logoutにアクセスした時にCSRFトークンのチェックに引っ掛かる。
		.and().logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.permitAll()
		// ログイン状態を覚えておく設定
		.and().rememberMe().tokenRepository(jdbcTokenRepository()).tokenValiditySeconds(86400 * 7);
    }

    // パスワードの暗号化方式
    @Bean
    public PasswordEncoder passwordEncoder() {
	return new StandardPasswordEncoder(); // BCryptPasswordEncoder
					      // StandardPasswordEncoder
					      // NoOpPasswordEncoder
    }

    // loginで入力されたpasswordの暗号化方法を指定する(=passwordEncoder())
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    /**
     * Remember Me 認証に利用するトークンのリポジトリ DBテーブルは persistent_logins
     * 固定、DDLも指定されたものを使うこと。 loginテンプレートのRemember-Meフィールドも指定されたものを使うこと。 あとはSpring
     * Securityが勝手にDBアクセスしてくれる。
     * 
     * @return
     */
    @Bean
    public PersistentTokenRepository jdbcTokenRepository() {
	JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
	repository.setDataSource(dataSource);
	// repository.setCreateTableOnStartup(true);
	return repository;
    }
}
