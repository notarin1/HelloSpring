package com.example.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;

import lombok.Data;
import lombok.NonNull;

@Controller
@RequestMapping("/")
public class MainController {
	@Autowired
	private AccountService account;

	@RequestMapping("/home")
	public String getHome(@NonNull Model model) {
		model.addAttribute("helper", new ViewHelper("hello spring!!", account.getAccounts()));
		return "home";
	}

	@Data
	public static class ViewHelper {
		@NonNull
		private final String title;
		@NonNull
		private final List<Account> accounts;
	}
}
