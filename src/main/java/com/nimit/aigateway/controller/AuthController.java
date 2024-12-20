package com.nimit.aigateway.controller;

import com.nimit.aigateway.services.MagicLinkService;
import com.nimit.aigateway.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MagicLinkService magicLinkService;
    private final EmailService emailService;

    public AuthController(MagicLinkService magicLinkService, EmailService emailService) {
        System.out.println("AuthController Instantiated");
        this.magicLinkService = magicLinkService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email) {
        String token = magicLinkService.generateToken(email);
        try {
            emailService.sendMagicLink(email, token);
        } catch (MessagingException err) {
            return ResponseEntity.status(500)
                    .body("Magic link cannot be sent. Problem with the email service.\n" + err);
        }
        return ResponseEntity.ok().body("Magic link sent to your email");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        return magicLinkService.validateToken(token)
                .map(user -> ResponseEntity.ok().body("Login successful"))
                .orElse(ResponseEntity.badRequest().body("Invalid or expired token"));
    }
}
