package com.nimit.aigateway.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
// @RequestMapping("/api/v1")
public class AIModelController {
@GetMapping("/ping")
public String ping() {
  return "Pong!";
}

// @PostMapping("/completion")
// public ResponseEntity<?> getCompletion(@RequestBody CompletionRequest
// request) {
// // Implementation for AI model API integration
// return ResponseEntity.ok().build();
// }
}