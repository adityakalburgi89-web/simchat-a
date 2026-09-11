package com.SIMCHAT_A.SIMCHAT_A.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String message;
    private String status;
    private String roomId;

    public static ApiResponse success(String message) {
        return ApiResponse.builder()
                .message(message)
                .status("success")
                .build();
    }

    public static ApiResponse success(String message, String roomId) {
        return ApiResponse.builder()
                .message(message)
                .roomId(roomId)
                .status("success")
                .build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder()
                .message(message)
                .status("error")
                .build();
    }
}
