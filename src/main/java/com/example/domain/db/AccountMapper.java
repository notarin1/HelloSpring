package com.example.domain.db;

import java.util.List;

import com.example.domain.entity.Account;

public interface AccountMapper {
	List<Account> selectAccount();

	Account findByName(String name);
}
