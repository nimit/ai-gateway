package com.nimit.aigateway.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@RedisHash("session")
public class UserSession implements Serializable {
  private long id;
  private String email;
  private Set<String> roles;
  private Map<String, Object> preferences;
  private LocalDateTime lastAccess;
}
