package com.example.supportservice.controller;


import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.service.ChatService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import(ChatController.class) // 👈 explicitly add the controller
public class ChatControllerMockTest {

    @Autowired
    private ChatController chatController;

    @MockBean
    private ChatService chatService;

    @Test
    @Order(1)
    @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
    void testSendMessage() {
        ChatMessage message = new ChatMessage();
        message.setSender("john@example.com");
        message.setContent("Hello World");
        message.setType(ChatMessage.MessageType.CHAT);

        ChatMessage result = chatController.sendMessage(message);

        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatService, times(1)).saveMessage(captor.capture());

        ChatMessage saved = captor.getValue();
        assertThat(saved.getContent()).isEqualTo("Hello World");
        assertThat(saved.getSender()).isEqualTo("john@example.com");
        assertThat(result.getType()).isEqualTo(ChatMessage.MessageType.CHAT);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
    void testNewUserJoin() {
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setSender("john@example.com");

        ChatMessage result = chatController.newUser(joinMessage);

        assertThat(result.getSender()).isEqualTo("john@example.com");
        assertThat(result.getType()).isEqualTo(ChatMessage.MessageType.JOIN);
    }
}

