package com.example.supportservice.dto;

import lombok.*;

@Data
//@AllArgsConstructor
@NoArgsConstructor

public class ChatMessage {
    private String sender;
    private String receiver;



    private String content;
    private String timestamp;
    private MessageType type;

    public ChatMessage(String sender, String receiver, MessageType type, String content, String timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
    }

    public ChatMessage(String sender, String receiver,  String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;

    }



    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
