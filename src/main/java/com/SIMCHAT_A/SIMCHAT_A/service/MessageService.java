package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.ChatRoomNotFound;
import com.SIMCHAT_A.SIMCHAT_A.exception.UserNotInTheRoom;
import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import com.SIMCHAT_A.SIMCHAT_A.repository.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CHANNEL_PREFIX = "channel : chatroom:";

    // => send msg -> save to redis -> pub/sub channel

    public MessageRoom sendMessage(String roomId, String username, String content) {
        // check room exist ?
        if (!chatRoomRepository.exist(roomId)) {

            throw new ChatRoomNotFound("chat room with id " + roomId + "not found");
        }

        if (!chatRoomRepository.isParticipantinRoom(roomId, username)) {

            throw new UserNotInTheRoom("user" + username + "ïs not in the room " + roomId + "!");

        }

        // create msg model obj
        MessageRoom message = MessageRoom.builder()
                .username(username)
                .message(content)
                .timestamp(LocalDateTime.now())
                .build();

        // save to redis list
        messageRoomRepository.saveMessage(roomId, message);

        // publish to pub/sub channel
        String channel = CHANNEL_PREFIX + roomId;
        redisTemplate.convertAndSend(channel, username + ":" + content);

        return message;

    }

    // => get message history

    public List<MessageRoom> getMessageHistory(String roomId, int limit) {
        if (!chatRoomRepository.exist(roomId)) {
            throw new ChatRoomNotFound("chat room with id" + roomId + "hot found!");
        }

        return messageRoomRepository.getMessage(roomId, limit);
    }

}
