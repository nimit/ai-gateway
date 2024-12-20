package com.nimit.aigateway.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // TODO: MAKE THIS NON-OPTIONAL
    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMagicLink(String email, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("test@demomailtrap.com");
        helper.setTo(email);
        helper.setSubject("Your Magic Link");
        helper.setText("Click here to login: " + baseUrl + "/auth/verify?token=" + token);
        mailSender.send(message);
    }
}
