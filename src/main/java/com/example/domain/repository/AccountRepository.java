package com.example.domain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.example.domain.db.AccountMapper;
import com.example.domain.entity.Account;

import lombok.NonNull;

@Repository
public class AccountRepository {
    @Autowired
    private AccountMapper accountMapper;

    @Cacheable(value = "cache.day", key="'List<Account>'")
    public List<Account> getAccounts() {
	return accountMapper.selectAccount();
    }

    @Cacheable(value = "cache.day", key="'Account/' + #name")
    public Account findByName(@NonNull String name) {
	return accountMapper.findByName(name);
    }

    // allEntries:Whether all the entries inside the cache(s) are removed. 
    @CacheEvict(value = "cache.day", allEntries = true)	
    public void add(@NonNull Account account) {
	accountMapper.insertAccount(account);
    }

    @CacheEvict(value = "cache.day", allEntries = true)
    public void update(@NonNull Account account) {
	accountMapper.updateAccount(account);
    }

    @CacheEvict(value = "cache.day", allEntries = true)
    public void delete(@NonNull Account account) {
	accountMapper.deleteAccount(account);
    }
}
