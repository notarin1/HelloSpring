package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NewAccountController {
    @RequestMapping(value = "/new_account/agreement", method = RequestMethod.GET)
    public String getIndex() {
	return "new_account/agreement";
    }

    @RequestMapping(value = "/new_account/input_account", method = RequestMethod.GET)
    public String getInputAccount() {
	return "new_account/input_account";
    }

    @RequestMapping(value = "/new_account/send_mail", method = RequestMethod.POST)
    public String getSendMail() {
	return "new_account/send_mail";
    }
}
