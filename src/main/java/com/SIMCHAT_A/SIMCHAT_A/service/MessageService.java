package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.RoomNotFoundException;
import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import com.SIMCHAT_A.SIMCHAT_A.repository.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRoomRepository messageRoomRepository;

    // => send msg -> save to redis -> pub/sub channel
    public MessageRoom sendMessage(String roomId, String participant, String content) {
        String normalizedRoomId = roomId.trim().toLowerCase();

        // check room exist ?
        if (!chatRoomRepository.exists(normalizedRoomId)) {
            throw new RoomNotFoundException("Chat room '" + roomId + "' does not exist.");
        }

        chatRoomRepository.addParticipant(normalizedRoomId, participant.trim());

        // create msg model obj
        MessageRoom message = MessageRoom.builder()
                .participant(participant.trim())
                .message(content.trim())
                .timestamp(Instant.now().toString())
                .build();

        // save to redis list
        messageRoomRepository.saveAndPublishMessage(normalizedRoomId, message);
        return message;
    }

    // => get message history
    public List<MessageRoom> getChatHistory(String roomId, Integer limit) {
        String normalizedRoomId = roomId.trim().toLowerCase();

        if (!chatRoomRepository.exists(normalizedRoomId)) {
            throw new RoomNotFoundException("Chat room '" + roomId + "' does not exist.");
        }

        int effectiveLimit = (limit == null || limit <= 0) ? 20 : Math.min(limit, 100);
        return messageRoomRepository.getMessages(normalizedRoomId, effectiveLimit);
    }
}
