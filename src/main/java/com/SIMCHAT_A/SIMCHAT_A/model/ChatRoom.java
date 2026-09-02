package com.SIMCHAT_A.SIMCHAT_A.model;

import java.util.HashSet;
import java.util.*;

public class ChatRoom {

    private String name;

    private Set<String> users = new HashSet<>();

    public ChatRoom() {

    }

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public Set<String> GetUsers() {
        return this.users;
    }

    public void addUser(String username) {
        users.add(username);
    }

    public Boolean hasUser(String username) {
        return users.contains(username);
    }

}
