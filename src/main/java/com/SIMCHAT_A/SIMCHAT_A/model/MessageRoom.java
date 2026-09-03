package com.SIMCHAT_A.SIMCHAT_A.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class MessageRoom {

    private String username;
    private String message;
    private LocalDateTime timestamp;
    
}