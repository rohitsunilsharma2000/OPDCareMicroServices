package com.example.supportservice.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMessageTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ChatMessage.MessageType type = ChatMessage.MessageType.CHAT;
        ChatMessage message = new ChatMessage("Alice", "Bob", type, "Hello Bob!", "2025-03-24T10:00:00");

        assertThat(message.getSender()).isEqualTo("Alice");
        assertThat(message.getReceiver()).isEqualTo("Bob");
        assertThat(message.getType()).isEqualTo(ChatMessage.MessageType.CHAT);
        assertThat(message.getContent()).isEqualTo("Hello Bob!");
        assertThat(message.getTimestamp()).isEqualTo("2025-03-24T10:00:00");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ChatMessage message = new ChatMessage();
        message.setSender("John");
        message.setReceiver("Jane");
        message.setType(ChatMessage.MessageType.JOIN);
        message.setContent("Joined the room");
        message.setTimestamp("2025-03-24T11:00:00");

        assertThat(message.getSender()).isEqualTo("John");
        assertThat(message.getReceiver()).isEqualTo("Jane");
        assertThat(message.getType()).isEqualTo(ChatMessage.MessageType.JOIN);
        assertThat(message.getContent()).isEqualTo("Joined the room");
        assertThat(message.getTimestamp()).isEqualTo("2025-03-24T11:00:00");
    }

    @Test
    void testEnumValues() {
        assertThat(ChatMessage.MessageType.valueOf("CHAT")).isEqualTo(ChatMessage.MessageType.CHAT);
        assertThat(ChatMessage.MessageType.valueOf("JOIN")).isEqualTo(ChatMessage.MessageType.JOIN);
        assertThat(ChatMessage.MessageType.valueOf("LEAVE")).isEqualTo(ChatMessage.MessageType.LEAVE);
    }
}
