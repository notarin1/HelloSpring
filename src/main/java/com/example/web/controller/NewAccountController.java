package com.example.web.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.entity.Account;
import com.example.domain.service.AccountService;
import com.example.domain.service.MailService;
import com.example.util.UrlBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Controller
public class NewAccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UrlBuilder urlBuilder;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private MailService mailService;

    @RequestMapping(value = "/new_account/agreement", method = RequestMethod.GET)
    public String getIndex() {
	return "new_account/agreement";
    }

    @RequestMapping(value = "/new_account/input_account", method = RequestMethod.GET)
    public String getInputAccount(Model model) {
	return renderInputAccount(new AccountForm(), model);
    }

    @RequestMapping(value = "/new_account/send_mail", method = RequestMethod.POST)
    public String getSendMail(@ModelAttribute("form") @Valid AccountForm form, BindingResult result, Model model) {
	if (result.hasErrors()) {
	    return renderInputAccount(form, model);
	}

	// 同一メアドチェック(但し同一メアドで登録中のAccountは除く)
	if (accountService.isExistRegularAccountOf(form.getName())) {
	    result.rejectValue("name", "同じメールアドレスが既に登録されています");
	    return renderInputAccount(form, model);
	}

	// TOTP TOKEN作成&Account仮登録
	String token = "abcde"; // TODO Time based one time token生成をする
	accountService.createProvisionalAccount(form.accountOf(passwordEncoder, token));

	// メール送信
//	mailService.sendEmail(form.getName(), "");

	return "new_account/send_mail";
    }

    @RequestMapping(value = "/new_account/confirm_account", method = RequestMethod.GET)
    public String getConfirmAccount(@RequestParam String token, Model model) {
	return accountService.findByToken(token).map(a -> {
	    accountService.update(a.regularOf());
	    return "new_account/registration_complete";
	}).orElse("new_account/error");
    }

    private String renderInputAccount(AccountForm form, Model model) {
	model.addAttribute("form", form);
	return "new_account/input_account";
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

	public Account accountOf(@NonNull PasswordEncoder encoder, @NonNull String newToken) {
	    return Account.builder().name(name).password(encoder.encode(password)).role("ROLE_USER").token(newToken)
		    .build();
	}
    }
}
