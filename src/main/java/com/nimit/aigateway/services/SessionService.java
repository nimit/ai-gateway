package com.nimit.aigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nimit.aigateway.model.UserSession;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
  @Autowired
  private final RedisTemplate<String, UserSession> redisTemplate;
  private static final long SESSION_TTL = 6L * 60 * 60; // 6 hours in seconds

  public SessionService(RedisTemplate<String, UserSession> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void saveUserSession(String email, UserSession session) {
    String key = "session:" + email;
    redisTemplate.opsForValue().set(key, session);
    redisTemplate.expire(key, SESSION_TTL, TimeUnit.SECONDS);
  }

  public Optional<UserSession> getUserSession(String email) {
    String key = "session:" + email;
    return Optional.ofNullable((UserSession) redisTemplate.opsForValue().get(key));
  }
}
