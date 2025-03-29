package com.example.supportservice.controller;


import com.example.supportservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatService chatService;

    /**
     * Retrieves chat history between two users
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory( @RequestParam String sender, @RequestParam String receiver) {
        log.info("Fetching chat history for {} <-> {}", sender, receiver);
        return ResponseEntity.ok(chatService.getChatHistory(sender, receiver));
    }
}
