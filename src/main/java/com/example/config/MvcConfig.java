package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
	// // 通常ページ
	// registry.addViewController("/home").setViewName("home");
	// registry.addViewController("/").setViewName("home");
	// registry.addViewController("/login").setViewName("login");
	// // 管理者ページの追加
	// registry.addViewController("/admin").setViewName("admin");
    }
}