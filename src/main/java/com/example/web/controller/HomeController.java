package com.example.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private AccountService account;

	@RequestMapping("home")
	public String getHome(@AuthenticationPrincipal User user,
							@NonNull Model model) {
		if (user == null) {
			return "redirect:/login";
		}
		model.addAttribute("helper", new ViewHelper("hello spring!!", 
													account.getAccounts(),
													user));
		return "home";
	}

	@Value
	public static class ViewHelper {
		@NonNull
		private final String title;
		@NonNull
		private final List<Account> accounts;
		@NonNull
		private final User user;
	}
}
