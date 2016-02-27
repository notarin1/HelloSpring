package com.example.web.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.NonNull;

@Controller
@Secured("IS_AUTHENTICATED_REMEMBERED")
@RequestMapping("/")
public class HomeController {
    @RequestMapping("home")
    public String getHome(@AuthenticationPrincipal User user, @NonNull Model model) {
	return "home";
    }
}
