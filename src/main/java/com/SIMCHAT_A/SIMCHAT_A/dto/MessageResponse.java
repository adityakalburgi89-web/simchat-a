package com.SIMCHAT_A.SIMCHAT_A.dto;

import java.time.*;

public  record MessageResponse (

    String username,
    String message,
    LocalDateTime timestamp
)
{}
