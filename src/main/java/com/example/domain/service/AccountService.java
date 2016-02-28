package com.example.domain.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public List<Account> getRegularAccounts() {
	return repository.getAccounts().stream().filter(a -> StringUtils.isEmpty(a.getToken())).collect(toList());
    }

    public Optional<Account> findUser(int id) {
	return repository.getAccounts().stream().filter(x -> x.getId() == id).findAny();
    }

    public Optional<Account> findByName(@NonNull String name) {
	return Optional.ofNullable(repository.findByName(name));
    }

    public Optional<Account> findByToken(@NonNull String token) {
	return Optional.ofNullable(repository.findByToken(token));
    }

    public boolean isExistRegularAccountOf(@NonNull String name) {
	return findByName(name).filter(a -> StringUtils.isEmpty(a.getToken())).isPresent();
    }

    public void create(@NonNull Account account) {
	repository.add(account);
    }

    public void createProvisionalAccount(@NonNull Account account) {
	// 念のため正規のAcconutがあったら処理を中断するチェック
	if (isExistRegularAccountOf(account.getName())) {
	    throw new IllegalStateException("Valid account has been exist!" + account.getName());
	}

	// 既に仮登録済みのAccountがあれば一旦抹消する
	findByName(account.getName()).ifPresent(a -> {
	    delete(a);
	});

	// Token付きの仮登録Accountを作成する
	create(account);
    }

    public void update(@NonNull Account account) {
	repository.update(account);
    }

    public void delete(@NonNull Account account) {
	repository.delete(account);
    }

    public void deleteByToken(@NonNull String token) {
	findByToken(token).ifPresent(a -> delete(a));
    }
}
