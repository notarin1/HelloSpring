package com.example.domain.entity;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class LoginUserDetail extends User {// (1)
	private static final long serialVersionUID = 1L;

	private final Account account;// (2)

	public LoginUserDetail(Account account) {
		// (3)
		super(account.getName(), account.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER")); // (4)
		this.account = account;
	}

	public Account getAccount() { // (5)
		return account;
	}

	// (1)
	// org.springframework.security.core.userdetails.UserDetailsインタフェースを実装する。
	// ここではUserDetailsを実装したorg.springframework.security.core.userdetails.User
	// クラスを継承し、本プロジェクト用のUserDetailsクラスを実装する。
	// (2) Springの認証ユーザークラスに、本プロジェクトのアカウント情報を保持させる。
	// (3) Userクラスのコンストラクタを呼び出す。第1引数はユーザー名、第2引数はパスワード、第3引数は権限リストである。
	// (4) 簡易実装として、"ROLE_USER"というロールのみ持つ権限を作成する。
	// (5) アカウント情報のgetterを用意する。これにより、ログインユーザーのAccountオブジェクトを取得することができる。
}
