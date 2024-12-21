package com.nimit.aigateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nimit.aigateway.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
  Optional<ApiKey> findByKeyHashAndIsActiveTrue(String keyHash);

  List<ApiKey> findByUserIdAndIsActiveTrue(Long userId);
}
