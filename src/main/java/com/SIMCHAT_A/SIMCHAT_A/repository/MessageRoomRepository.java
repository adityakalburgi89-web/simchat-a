package com.SIMCHAT_A.SIMCHAT_A.repository;

import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRoomRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String ROOM_KEY_PREFIX = "chatroom";
    private static final String MESSAGE_KEY_SUFFIX = ":messages";

    public MessageRoomRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();

        // serilize || deserialize
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // => message in redis saved RPUSH
    public void saveMessage(String roomId, MessageRoom messageRoom) {
        String messageKey = ROOM_KEY_PREFIX + roomId + MESSAGE_KEY_SUFFIX;

        try {
            String jsonMessage = objectMapper.writeValueAsString(messageRoom);
            redisTemplate.opsForList().rightPush(messageKey, jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize", e);
        }

    }

    // => get last N message from Redis List (LRANGE)
    public List<MessageRoom> getMessage(String roomId, int limit) {
        String messageKey = ROOM_KEY_PREFIX + ":" + roomId + MESSAGE_KEY_SUFFIX;

        List<String> rawMessages = redisTemplate.opsForList().range(messageKey, -limit, -1);
        List<MessageRoom> messages = new ArrayList<>();
        if (rawMessages != null) {
            for (String rawjson : rawMessages) {
                try {
                    MessageRoom message = objectMapper.readValue(rawjson, MessageRoom.class);
                    messages.add(message);
                } catch (JsonProcessingException e) {
                    // skip invaild
                }
            }
        }
        return messages;
    }

}