package com.SIMCHAT_A.SIMCHAT_A.dto;



import jakarta.validation.constraints.NotBlank;



public record CreateRoomRequest(
        @NotBlank(message = "room is required")
        String roomName
) {

}
