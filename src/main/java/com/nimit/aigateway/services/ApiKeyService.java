package com.nimit.aigateway.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.nimit.aigateway.model.ApiKey;
import com.nimit.aigateway.model.User;
import com.nimit.aigateway.repository.ApiKeyRepository;
import com.nimit.aigateway.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiKeyService {
  private static final String KEY_PREFIX = "pk_";

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Autowired
  private UserRepository userRepository;

  public String generateApiKeyForUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));

    String rawKey = generateSecureKey();
    String keyHash = hashKey(rawKey);

    ApiKey apiKey = new ApiKey();
    apiKey.setUser(user);
    apiKey.setKeyHash(keyHash);
    apiKey.setCreatedAt(LocalDateTime.now());
    apiKey.setIsActive(true);
    apiKey.setRateLimitQuota(1000);

    apiKeyRepository.save(apiKey);

    return rawKey;
  }

  private String generateSecureKey() {
    UUID keyId = UUID.randomUUID();
    byte[] randomBytes = new byte[32];
    new SecureRandom().nextBytes(randomBytes);
    String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    return KEY_PREFIX + keyId.toString() + "_" + secret;
  }

  private String hashKey(String key) {
    return BCrypt.hashpw(key, BCrypt.gensalt());
  }
}
