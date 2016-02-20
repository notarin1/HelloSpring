package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;

import lombok.Value;

@Controller
public class AdminController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/admin")
    public String getAdmin() {
	return "admin";
    }

    @RequestMapping("/admin/accounts/{id}/edit")
    public String getEditAccount(@PathVariable("id") int id, Model model) {
	Account account = accountService.findUser(id)
		.orElseThrow(() -> new IllegalStateException("Could not find account " + id));

	model.addAttribute("helper", new AccountEditViewHelper(account));
	model.addAttribute("form", new EditForm(account.getName(), account.getPassword()));
	return "admin/edit_account";
    }

    @Value
    static class AccountEditViewHelper {
	private final Account account;
    }

    @Value
    static class EditForm {
	private String name;
	private String password;
    }
}
