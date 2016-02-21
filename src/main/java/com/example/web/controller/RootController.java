package com.example.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
	@RequestMapping("/")
	public String getIndex(@AuthenticationPrincipal User user) {
		if (user == null) {
			return "redirect:/login";
		}
		return "redirect:/home";
	}
}
