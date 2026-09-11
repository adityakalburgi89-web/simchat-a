package com.SIMCHAT_A.SIMCHAT_A.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class MessageRoom {
    private String participant;
    private String message;
    private String timestamp;
}