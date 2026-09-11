package com.SIMCHAT_A.SIMCHAT_A.dto;

import com.SIMCHAT_A.SIMCHAT_A.model.MessageRoom;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryResponse {
    private List<MessageRoom> messages;
}
