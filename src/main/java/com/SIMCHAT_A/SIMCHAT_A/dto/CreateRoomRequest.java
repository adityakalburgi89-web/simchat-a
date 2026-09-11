package com.SIMCHAT_A.SIMCHAT_A.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(
        @NotBlank(message = "roomName is required")
        @Size(min = 3, max = 50, message = "roomName must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "roomName must contain only letters, digits, underscores, or hyphens")
        String roomName
) {
}
