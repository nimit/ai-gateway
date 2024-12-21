package com.nimit.aigateway.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String secretKey;

  public String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 30 days
        .sign(Algorithm.HMAC256(secretKey));
  }

  public String validateToken(String token) {
    try {
      return JWT.require(Algorithm.HMAC256(secretKey))
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException e) {
      return null;
    }
  }
}
