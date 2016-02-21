package com.example;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class HelloSpringApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		// メッセージソースの指定 default: messages.properties
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		return messageSource;
	}
}
