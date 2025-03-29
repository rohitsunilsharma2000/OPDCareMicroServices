package com.example.supportservice.service;

import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.model.MessageEntity;
import com.example.supportservice.repository.MessageRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void testSaveMessage_shouldSaveToRepository() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        ChatService chatService = new ChatService(messageRepository);

        ChatMessage message = new ChatMessage("alice", "bob", "Hello");

        chatService.saveMessage(message);

        verify(messageRepository, times(1)).save(any(MessageEntity.class));
    }

    @Test
    void testGetChatHistory_shouldCallRepository() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        ChatService chatService = new ChatService(messageRepository);

        List<MessageEntity> expected = List.of(
                MessageEntity.builder().sender("alice").receiver("bob").content("Hi").build()
        );

        when(messageRepository.findBySenderAndReceiver("alice", "bob")).thenReturn(expected);

        List<MessageEntity> actual = chatService.getChatHistory("alice", "bob");

        assertEquals(expected, actual);
        verify(messageRepository, times(1)).findBySenderAndReceiver("alice", "bob");
    }
}
