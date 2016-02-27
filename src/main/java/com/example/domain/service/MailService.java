package com.example.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class MailService {
    @Autowired
    private JavaMailSender sender;

    public SimpleMailMessage sendEmail(@NonNull String email, @NonNull String subject, @NonNull String content) {
	SimpleMailMessage mailMessage = new SimpleMailMessage();

	mailMessage.setTo(email);
	mailMessage.setFrom("this_service@gmail.com");
	mailMessage.setSubject(subject);
	mailMessage.setText(content);

	sender.send(mailMessage);

	return mailMessage;
    }
}
