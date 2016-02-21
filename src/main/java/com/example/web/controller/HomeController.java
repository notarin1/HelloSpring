package com.example.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;
import com.example.util.UrlBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private AccountService account;
    @Autowired
    private UrlBuilder urlBuilder;

    @RequestMapping("home")
    public String getHome(@AuthenticationPrincipal User user, @NonNull Model model) {
	if (user == null) {
	    return "redirect:/login";
	}
	model.addAttribute("helper", new ViewHelper(urlBuilder, "hello spring!!", account.getAccounts(), user));
	return "home";
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
}
