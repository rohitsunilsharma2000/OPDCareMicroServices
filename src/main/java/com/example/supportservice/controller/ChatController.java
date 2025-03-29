package com.example.supportservice.controller;


import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;



@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Receives message from client and broadcasts via /topic/public or /topic/user
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage( @Payload ChatMessage message) {
        log.info("Message received from {}: {}", message.getSender(), message.getContent());
        chatService.saveMessage(message);
        return message;
    }

    /**
     * Notify others when someone joins
     */
    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public ChatMessage newUser(@Payload ChatMessage message) {
        message.setType(ChatMessage.MessageType.JOIN);
        log.info("{} joined the chat.", message.getSender());
        return message;
    }
}
