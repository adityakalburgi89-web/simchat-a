package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.RoomAlreadyExistsException;
import com.SIMCHAT_A.SIMCHAT_A.exception.RoomNotFoundException;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // => create a new chat room
    public String createChatRoom(String roomName) {
        String normalizedRoomName = roomName.trim();
        String roomId = normalizedRoomName.toLowerCase();

        boolean created = chatRoomRepository.saveRoomAtomic(roomId, normalizedRoomName);
        if (!created) {
            throw new RoomAlreadyExistsException("Chat room '" + normalizedRoomName + "' already exists.");
        }
        return roomId;
    }

    // => join chat room
    public void joinChatRoom(String roomId, String participant) {
        String normalizedRoomId = roomId.trim().toLowerCase();
        if (!chatRoomRepository.exists(normalizedRoomId)) {
            throw new RoomNotFoundException("Chat room '" + normalizedRoomId + "' does not exist.");
        }
        chatRoomRepository.addParticipant(normalizedRoomId, participant.trim());
    }

    // => get all participant in chat
    public Set<String> getParticipants(String roomId) {
        String normalizedRoomId = roomId.trim().toLowerCase();
        if (!chatRoomRepository.exists(normalizedRoomId)) {
            throw new RoomNotFoundException("Chat room '" + normalizedRoomId + "' does not exist.");
        }
        return chatRoomRepository.getParticipants(normalizedRoomId);
    }

    // => delete chat room
    public void deleteChatRoom(String roomId) {
        String normalizedRoomId = roomId.trim().toLowerCase();
        boolean deleted = chatRoomRepository.deleteRoom(normalizedRoomId);
        if (!deleted) {
            throw new RoomNotFoundException("Chat room '" + normalizedRoomId + "' does not exist.");
        }
    }

    public boolean roomExists(String roomId) {
        return chatRoomRepository.exists(roomId.trim().toLowerCase());
    }
}
