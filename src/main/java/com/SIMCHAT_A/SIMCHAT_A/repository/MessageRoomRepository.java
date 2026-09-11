package com.SIMCHAT_A.SIMCHAT_A.repository;

import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRoomRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String ROOM_KEY_PREFIX = "chatroom:";
    private static final String MESSAGES_SUFFIX = ":messages";
    private static final String CHANNEL_SUFFIX = ":channel";

    // => message in redis saved RPUSH
    public String saveAndPublishMessage(String roomId, MessageRoom message) {
        String messageKey = ROOM_KEY_PREFIX + roomId + MESSAGES_SUFFIX;
        String channelKey = ROOM_KEY_PREFIX + roomId + CHANNEL_SUFFIX;

        try {
            // serilize || deserialize
            String jsonMessage = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(messageKey, jsonMessage);
            redisTemplate.convertAndSend(channelKey, jsonMessage);
            return jsonMessage;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize", e);
        }
    }

    // => get last N message from Redis List (LRANGE)
    public List<MessageRoom> getMessages(String roomId, int limit) {
        String messageKey = ROOM_KEY_PREFIX + roomId + MESSAGES_SUFFIX;
        List<String> rawMessages = redisTemplate.opsForList().range(messageKey, -limit, -1);
        List<MessageRoom> messages = new ArrayList<>();
        if (rawMessages != null) {
            for (String json : rawMessages) {
                try {
                    MessageRoom msg = objectMapper.readValue(json, MessageRoom.class);
                    messages.add(msg);
                } catch (JsonProcessingException e) {
                    // skip invaild
                }
            }
        }
        return messages;
    }
}