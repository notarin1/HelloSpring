package com.example.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;
import com.example.util.UrlBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@Secured("IS_AUTHENTICATED_REMEMBERED")
public class AdminController {
    @Autowired
    private AccountService account;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UrlBuilder urlBuilder;

    @RequestMapping("/admin")
    public String getAdmin(@AuthenticationPrincipal User user, @NonNull Model model) {
	if (user == null) {
	    return "redirect:/";
	}
	model.addAttribute("helper", new ViewHelper(urlBuilder, "hello spring!!", account.getAccounts(), user));
	return "admin";
    }

    @RequestMapping(value = "/admin/accounts/create", method = RequestMethod.GET)
    public String getCreateAccount(Model model) {
	model.addAttribute("helper", new AccountCreateViewHelper(urlBuilder));
	model.addAttribute("form", new AccountForm());
	return "admin/create_account";
    }

    @RequestMapping(value = "/admin/accounts/create", method = RequestMethod.POST)
    public String postCreateAccount(@ModelAttribute("form") @Valid AccountForm form, BindingResult result,
	    Model model) {
	if (result.hasErrors()) {
	    model.addAttribute("helper", new AccountCreateViewHelper(urlBuilder));
	    model.addAttribute("form", form);
	    return "admin/create_account";
	}
	
	// Account作成
	accountService.create(form.accountOf());
	return "redirect:/";
    }

    @RequestMapping(value = "/admin/accounts/{id}/edit", method = RequestMethod.GET)
    public String getEditAccount(@PathVariable("id") int id, Model model) {
	Account account = accountService.findUser(id)
		.orElseThrow(() -> new IllegalStateException("Could not find account " + id));

	model.addAttribute("helper", new AccountEditViewHelper(urlBuilder, account));
	model.addAttribute("form", new AccountForm(account.getName(), account.getPassword(), account.getRole()));
	return "admin/edit_account";
    }

    // 入力の検証を行う引数に対して@Validを付与する。検証結果は引数のBindingResultで受け取る。
    @RequestMapping(value = "/admin/accounts/{id}/update", method = RequestMethod.POST)
    public String postUpdateAccount(@PathVariable("id") int id, @ModelAttribute("form") @Valid AccountForm form,
	    BindingResult result, Model model) {
	// Check Account ID
	Account account = accountService.findUser(id)
		.orElseThrow(() -> new IllegalStateException("Could not find account " + id));

	if (result.hasErrors()) {
	    model.addAttribute("helper", new AccountEditViewHelper(urlBuilder, account));
	    model.addAttribute("form", form);
	    return "admin/edit_account";
	}

	// 更新
	accountService.update(form.accountOf(id));
	return "redirect:/";
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class ViewHelper {
	@NonNull
	private UrlBuilder urlBuilder;
	@Getter
	@NonNull
	private final String title;
	@NonNull
	private final List<Account> accounts;
	@NonNull
	private final User user;

	@Getter
	@Builder
	public static class AccountRow {
	    private final String name;
	    private final String editUrl;
	}

	public List<AccountRow> getAccountRows() {
	    return accounts.stream().map(
		    a -> AccountRow.builder().name(a.getName()).editUrl(urlBuilder.editAccountUrl(a.getId())).build())
		    .collect(Collectors.toList());
	}
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    static class AccountCreateViewHelper {
	@NonNull
	private UrlBuilder urlBuilder;

	public String getCreateUrl() {
	    return urlBuilder.createAccountUrl();
	}
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    static class AccountEditViewHelper {
	@NonNull
	private UrlBuilder urlBuilder;
	@NonNull
	private final Account account;

	public String getUpdateUrl() {
	    return urlBuilder.updateAccountUrl(account.getId());
	}
    }

    @Data
    @NoArgsConstructor // これがないと怒られる！
    @AllArgsConstructor
    static class AccountForm {
	@NotBlank(message = "may not be null")
	@Size(max = 20, message = "max 20 characters!")
	private String name;
	@NotBlank
	private String password;
	@NotBlank
	private String role;
	
	public Account accountOf() {
	    return Account.builder()
		    .name(name)
		    .password(password)
		    .role(role)
		    .build();
	}

	public Account accountOf(int id) {
	    return Account.builder()
		    .id(id)
		    .name(name)
		    .password(password)
		    .role(role)
		    .build();
	}
    }
}
