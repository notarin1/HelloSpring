package com.example.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.entity.Account;
import com.example.domain.repository.AccountRepository;

import lombok.NonNull;

@Service
@Transactional
public class AccountService {
	@Autowired
	private AccountRepository repository;

	public List<Account> getAccounts() {
		return repository.getAccounts();
	}

	public Optional<Account> findUser(int id) {
		return repository.getAccounts().stream().filter(x -> x.getId() == id).findAny();
	}

	public Account findByName(@NonNull String name) {
		Account account = repository.findByName(name);
		if (account == null) {
			throw new IllegalStateException("Account not found");
		}
		return account;
	}
}
