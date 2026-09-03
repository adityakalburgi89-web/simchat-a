package com.SIMCHAT_A.SIMCHAT_A.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinRoomRequest(
        @NotBlank(message = "username is rquired") String username) {
}
