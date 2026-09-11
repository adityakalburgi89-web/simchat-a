package com.SIMCHAT_A.SIMCHAT_A.service;

import com.SIMCHAT_A.SIMCHAT_A.exception.RoomNotFoundException;
import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.SIMCHAT_A.SIMCHAT_A.repository.ChatRoomRepository;
import com.SIMCHAT_A.SIMCHAT_A.repository.MessageRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MessageRoomRepository messageRoomRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void sendMessage_Success() {
        when(chatRoomRepository.exists("general")).thenReturn(true);

        MessageRoom msg = messageService.sendMessage("general", "guest_user", "Hello!");

        assertNotNull(msg);
        assertEquals("guest_user", msg.getParticipant());
        assertEquals("Hello!", msg.getMessage());
        assertNotNull(msg.getTimestamp());
        verify(messageRoomRepository).saveAndPublishMessage(eq("general"), any(MessageRoom.class));
    }

    @Test
    void sendMessage_NonExistentRoom_ThrowsException() {
        when(chatRoomRepository.exists("unknown_room")).thenReturn(false);

        assertThrows(RoomNotFoundException.class, () -> messageService.sendMessage("unknown_room", "guest_user", "Hello!"));
    }

    @Test
    void getChatHistory_Success() {
        when(chatRoomRepository.exists("general")).thenReturn(true);
        MessageRoom msg1 = new MessageRoom("user1", "hi", "2024-01-01T10:00:00Z");
        when(messageRoomRepository.getMessages("general", 10)).thenReturn(List.of(msg1));

        List<MessageRoom> history = messageService.getChatHistory("general", 10);

        assertEquals(1, history.size());
        assertEquals("user1", history.get(0).getParticipant());
    }

    @Test
    void getChatHistory_DefaultLimit() {
        when(chatRoomRepository.exists("general")).thenReturn(true);

        messageService.getChatHistory("general", null);

        verify(messageRoomRepository).getMessages("general", 20);
    }
}
