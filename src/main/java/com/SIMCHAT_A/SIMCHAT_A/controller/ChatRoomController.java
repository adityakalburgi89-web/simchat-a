package com.SIMCHAT_A.SIMCHAT_A.controller;

import com.SIMCHAT_A.SIMCHAT_A.dto.CreateRoomRequest;
import com.SIMCHAT_A.SIMCHAT_A.dto.JoinRoomRequest;
import com.SIMCHAT_A.SIMCHAT_A.dto.SendMessageRequest;
import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import com.SIMCHAT_A.SIMCHAT_A.service.ChatRoomService;
import com.SIMCHAT_A.SIMCHAT_A.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;


@RestController 
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    // => => Post/chatrooms -> create room 
        @PostMapping
    public ResponseEnitity<String> createRoom(@vaild  @RequestBody CreateRoomRequest request){

        String roomId = request.roomName().toLowerCase();
        chatRoomService.createRoom(roomId, request.roomName());
        return new ResponseEntity<>("Chat room"+request.roomName()+"create successfully with ID :"+roomId, HttpStatus.CREATED);

    }
    // => Post/chatroom/{roomId} join -> joined
    @PostMapping("/{roomId}/join")
        public ResponseEntity<MessageRoom> sendMessage(@PathVariable String roomId, @Vaild @RequestBody joinRoomRequest request){

            chatRoomService.joinChatRoom(roomId, request.username());
            return ResponseEntity.ok("user"+request.username+"successfully"+roomId+"");

           // Message message = messageService.sendMessage(roomId request.username(), request.message());


        }
        @PostMapping("/{roomId}/message") public ResponseEntity<MessageRoom>sendMessage(@PathVariable String roomId @Valid @ResponseBody sendMessageRequest request){
            Message message = messageService.seendMessage(roomId,request.username(), request.message());
            return new ResponseEntity<>(message, HttpSattus.CREATED);
        }
    }

