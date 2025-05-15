package com.nimit.aigateway.controller;

import com.nimit.aigateway.services.MagicLinkService;
import com.nimit.aigateway.services.SessionService;
import com.nimit.aigateway.model.User;
import com.nimit.aigateway.model.UserSession;
import com.nimit.aigateway.services.EmailService;
import com.nimit.aigateway.services.JwtService;
import jakarta.mail.MessagingException;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MagicLinkService magicLinkService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SessionService sessionService;

    public AuthController(MagicLinkService magicLinkService, EmailService emailService, JwtService jwtService,
            SessionService sessionService) {
        System.out.println("DEBUG: AuthController Instantiated");
        this.magicLinkService = magicLinkService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
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
        User user;
        try {
            user = magicLinkService.validateToken(token);
            String email = user.getEmail();
            // Generate JWT
            String jwt = jwtService.generateToken(email);

            // Create and save session
            UserSession session = user.createSession();
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    session.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            sessionService.saveUserSession(email, session);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body("Login successful");
        } catch (Exception err) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
}
