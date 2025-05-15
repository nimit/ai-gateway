package com.nimit.aigateway.controller;

import com.nimit.aigateway.model.User;
import com.nimit.aigateway.services.ApiKeyService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/keys")
@SecurityRequirement(name = "bearerAuth")
public class ApiKeyController {

  @Autowired
  private ApiKeyService apiKeyService;

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Map<String, String>> generateApiKey(
      @AuthenticationPrincipal UserDetails userDetails) {
    System.out.println("DEBUG: IN generateApiKey");
    String apiKey = apiKeyService.generateApiKeyForUser(
        ((User) userDetails).getId());

    Map<String, String> response = new HashMap<>();
    response.put("apiKey", apiKey);
    response.put("message", "Store this API key securely. It won't be shown again.");

    return ResponseEntity.ok(response);
  }
}
