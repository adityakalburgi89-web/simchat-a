package com.SIMCHAT_A.SIMCHAT_A.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank(message = "participant is required")
        @Size(min = 3, max = 30, message = "participant must be between 3 and 30 characters")
        String participant,

        @NotBlank(message = "message is required")
        @Size(max = 1000, message = "message cannot exceed 1000 characters")
        String message
) {
}
