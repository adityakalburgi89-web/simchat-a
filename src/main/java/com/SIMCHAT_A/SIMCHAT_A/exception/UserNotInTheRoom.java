package com.SIMCHAT_A.SIMCHAT_A.exception;

public class UserNotInTheRoom extends RuntimeException {

    public UserNotInTheRoom(String message){
        super(message);
    }
}
