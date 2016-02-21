package com.example.web.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UrlBuilder urlBuilder;

    @RequestMapping("/admin")
    public String getAdmin() {
	return "admin";
    }

    @RequestMapping(value = "/admin/accounts/{id}/edit", method = RequestMethod.GET)
    public String getEditAccount(@PathVariable("id") int id, Model model) {
	Account account = accountService.findUser(id)
		.orElseThrow(() -> new IllegalStateException("Could not find account " + id));

	model.addAttribute("helper", new AccountEditViewHelper(urlBuilder, account));
	model.addAttribute("form", new EditForm(account.getName(), account.getRole()));
	return "admin/edit_account";
    }

    // 入力の検証を行う引数に対して@Validを付与する。検証結果は引数のBindingResultで受け取る。
    @RequestMapping(value = "/admin/accounts/{id}/update", method = RequestMethod.POST)
    public String postUpdateAccount(@PathVariable("id") int id, @ModelAttribute("form") @Valid EditForm form,
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
	account.setName(form.getName());
	account.setRole(form.getRole());
	accountService.update(account);

	return "redirect:/home";
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
    static class EditForm {
	@NotBlank(message = "may not be null")
	@Size(max = 20, message = "max 20 characters!")
	private String name;
	@NotBlank
	private String role;
    }
}
