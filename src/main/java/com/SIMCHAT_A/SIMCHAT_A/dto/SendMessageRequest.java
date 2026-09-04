package com.SIMCHAT_A.SIMCHAT_A.dto;
import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {


    @NotBlank(message = "username is required")
        String username;
    @NotBlank(message = "message is ")
        String message;
}
