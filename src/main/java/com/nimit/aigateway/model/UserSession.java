package com.nimit.aigateway.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@RedisHash("session")
public class UserSession implements Serializable {
  private Long id;
  private String email;
  private Map<String, Object> preferences;
  private LocalDateTime lastAccess;
}
