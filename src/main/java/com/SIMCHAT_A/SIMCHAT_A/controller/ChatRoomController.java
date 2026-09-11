package com.SIMCHAT_A.SIMCHAT_A.controller;

import com.SIMCHAT_A.SIMCHAT_A.dto.*;
import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.SIMCHAT_A.SIMCHAT_A.service.ChatRoomService;
import com.SIMCHAT_A.SIMCHAT_A.service.MessageService;
import com.SIMCHAT_A.SIMCHAT_A.service.SseEmitterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/chatapp/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SseEmitterService sseEmitterService;

    // => => Post/chatrooms -> create room 
    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        String roomId = chatRoomService.createChatRoom(request.roomName());
        ApiResponse response = ApiResponse.builder()
                .message("Chat room '" + request.roomName().trim() + "' created successfully.")
                .roomId(roomId)
                .status("success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // => Post/chatroom/{roomId} join -> joined
    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse> joinRoom(@PathVariable String roomId,
                                                @Valid @RequestBody JoinRoomRequest request) {
        chatRoomService.joinChatRoom(roomId, request.participant());
        ApiResponse response = ApiResponse.builder()
                .message("User '" + request.participant().trim() + "' joined chat room '" + roomId.trim().toLowerCase() + "'.")
                .status("success")
                .build();
        return ResponseEntity.ok(response);
    }

    // => Post/chatroom/{roomId}/messages -> send message
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse> sendMessage(@PathVariable String roomId,
                                                   @Valid @RequestBody SendMessageRequest request) {
        messageService.sendMessage(roomId, request.participant(), request.message());
        ApiResponse response = ApiResponse.builder()
                .message("Message sent successfully.")
                .status("success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // => Get/chatroom/{roomId}/messages -> get message history
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ChatHistoryResponse> getChatHistory(@PathVariable String roomId,
                                                              @RequestParam(required = false) Integer limit) {
        List<MessageRoom> messages = messageService.getChatHistory(roomId, limit);
        return ResponseEntity.ok(new ChatHistoryResponse(messages));
    }

    // => Get/chatroom/{roomId}/subscribe -> sse real time stream
    @GetMapping(value = "/{roomId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeRoom(@PathVariable String roomId) {
        return sseEmitterService.subscribe(roomId.trim().toLowerCase());
    }

    // => Delete/chatroom/{roomId} -> delete room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable String roomId) {
        chatRoomService.deleteChatRoom(roomId);
        ApiResponse response = ApiResponse.builder()
                .message("Chat room '" + roomId.trim().toLowerCase() + "' deleted successfully.")
                .status("success")
                .build();
        return ResponseEntity.ok(response);
    }
}
