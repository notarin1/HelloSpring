package com.example.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.entity.LoginUserDetail;

@Controller
public class RootController {
    @RequestMapping("/")
    public String getIndex(@AuthenticationPrincipal User user) {
	if (user == null) {
	    return "index";
	}

	LoginUserDetail userDetail = (LoginUserDetail) user;

	if (userDetail.isAdmin()) {
	    return "redirect:/admin";
	} else {
	    return "redirect:/home";
	}
    }
}
