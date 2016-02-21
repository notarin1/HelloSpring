package com.example.domain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.domain.db.AccountMapper;
import com.example.domain.entity.Account;

import lombok.NonNull;

@Repository
public class AccountRepository {
    @Autowired
    private AccountMapper accountMapper;

    public List<Account> getAccounts() {
	return accountMapper.selectAccount();
    }

    public Account findByName(@NonNull String name) {
	return accountMapper.findByName(name);
    }

    public void add(@NonNull Account account) {
	accountMapper.insertAccount(account);
    }

    public void update(@NonNull Account account) {
	accountMapper.updateAccount(account);
    }

    public void delete(@NonNull Account account) {
	accountMapper.deleteAccount(account);
    }
}
