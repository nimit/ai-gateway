package com.aigateway.service;

import com.aigateway.model.User;
import com.aigateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MagicLinkService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final String MAGIC_LINK_PREFIX = "magic_link:";
    private static final Duration TOKEN_VALIDITY = Duration.ofMinutes(5);

    @Transactional
    public String generateToken(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        String token = UUID.randomUUID().toString();
        
        redisTemplate.opsForValue().set(
            MAGIC_LINK_PREFIX + token,
            email,
            TOKEN_VALIDITY
        );
        
        return token;
    }

    @Transactional
    public Optional<User> validateToken(String token) {
        String email = redisTemplate.opsForValue().get(MAGIC_LINK_PREFIX + token);
        
        if (email != null) {
            redisTemplate.delete(MAGIC_LINK_PREFIX + token);
            
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
            // The last_login update trigger will handle the timestamp
            user.setLastLogin(LocalDateTime.now());
            return Optional.of(userRepository.save(user));
        }
        
        return Optional.empty();
    }
}
