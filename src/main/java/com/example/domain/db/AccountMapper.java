package com.example.domain.db;

import java.util.List;

import com.example.domain.entity.Account;

public interface AccountMapper {
	void insertAccount(Account account);

	void updateAccount(Account account);

	void deleteAccount(Account account);

	List<Account> selectAccount();

	Account findByName(String name);
}
