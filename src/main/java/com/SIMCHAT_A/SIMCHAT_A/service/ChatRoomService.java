package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.ChatRoomNotFound;
import com.SIMCHAT_A.SIMCHAT_A.exception.DuplicateChatRoom;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // => create a new chat room
    public void createChatRoom(String roomId, String name) {
        if (chatRoomRepository.exist(roomId)) {

            throw new DuplicateChatRoom("chat room with id " + roomId + " already exist");

        }
        chatRoomRepository.saveRoom(roomId, name);

    }

    public void joinChatRoom(String roomId, String username) {
        if (!chatRoomRepository.exist(roomId)) {
            throw new ChatRoomNotFound("Chat room with id " + roomId + "not foound");

        }
        chatRoomRepository.addParticipants(roomId, username);
    }
    // => get all participant in chat

    public Set<String> getParticipants(String roomId) {
        if (!chatRoomRepository.exist(roomId)) {

            throw new ChatRoomNotFound("chat room with id" + roomId + "not found");
        }
        return chatRoomRepository.getParticipant(roomId);
    }
}
