package com.example.supportservice.service;

import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.model.MessageEntity;
import com.example.supportservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;

    public void saveMessage( ChatMessage message) {
        MessageEntity entity = MessageEntity.builder()
                                            .sender(message.getSender())
                                            .receiver(message.getReceiver())
                                            .content(message.getContent())
                                            .timestamp(LocalDateTime.now())
                                            .build();
        messageRepository.save(entity);
        log.info("Chat message saved for {} -> {}", message.getSender(), message.getReceiver());
    }

    public List<MessageEntity> getChatHistory(String sender, String receiver) {
        log.info("Retrieving chat history between {} and {}", sender, receiver);
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
}
