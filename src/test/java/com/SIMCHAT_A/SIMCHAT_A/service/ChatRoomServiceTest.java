package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.RoomAlreadyExistsException;
import com.SIMCHAT_A.SIMCHAT_A.exception.RoomNotFoundException;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    void createChatRoom_Success() {
        when(chatRoomRepository.saveRoomAtomic("general", "general")).thenReturn(true);

        String roomId = chatRoomService.createChatRoom("general");

        assertEquals("general", roomId);
        verify(chatRoomRepository).saveRoomAtomic("general", "general");
    }

    @Test
    void createChatRoom_DuplicateRoom_ThrowsException() {
        when(chatRoomRepository.saveRoomAtomic("general", "general")).thenReturn(false);

        assertThrows(RoomAlreadyExistsException.class, () -> chatRoomService.createChatRoom("general"));
    }

    @Test
    void joinChatRoom_Success() {
        when(chatRoomRepository.exists("general")).thenReturn(true);

        assertDoesNotThrow(() -> chatRoomService.joinChatRoom("general", "guest_user"));
        verify(chatRoomRepository).addParticipant("general", "guest_user");
    }

    @Test
    void joinChatRoom_NonExistentRoom_ThrowsException() {
        when(chatRoomRepository.exists("unknown_room")).thenReturn(false);

        assertThrows(RoomNotFoundException.class, () -> chatRoomService.joinChatRoom("unknown_room", "guest_user"));
    }

    @Test
    void getParticipants_Success() {
        when(chatRoomRepository.exists("general")).thenReturn(true);
        when(chatRoomRepository.getParticipants("general")).thenReturn(Set.of("user1", "user2"));

        Set<String> participants = chatRoomService.getParticipants("general");

        assertEquals(2, participants.size());
        assertTrue(participants.contains("user1"));
    }
}
