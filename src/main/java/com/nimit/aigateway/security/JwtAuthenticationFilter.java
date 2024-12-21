package com.nimit.aigateway.security;

import com.nimit.aigateway.model.UserSession;
import com.nimit.aigateway.services.JwtService;
import com.nimit.aigateway.services.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final SessionService sessionService;

  @Autowired
  public JwtAuthenticationFilter(JwtService jwtService, SessionService sessionService) {
    this.jwtService = jwtService;
    this.sessionService = sessionService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);
      String email = jwtService.validateToken(jwt);

      if (email != null) {
        Optional<UserSession> session = sessionService.getUserSession(email);
        if (session.isPresent()) {
          // Set authentication in SecurityContext
          SecurityContextHolder.getContext()
              .setAuthentication(createAuthentication(session.get()));
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  private Authentication createAuthentication(UserSession session) {
    List<GrantedAuthority> authorities = session.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    return new UsernamePasswordAuthenticationToken(
        session.getEmail(), null, authorities);
  }
}
