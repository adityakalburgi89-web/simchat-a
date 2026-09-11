package com.SIMCHAT_A.SIMCHAT_A.controller;

import com.SIMCHAT_A.SIMCHAT_A.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // => root path welcome endpoint
    @GetMapping("/")
    public ResponseEntity<ApiResponse> home() {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Welcome to SIMCHAT Redis Chat API! Base endpoint is /api/chatapp/chatrooms")
                .status("success")
                .build());
    }
}
