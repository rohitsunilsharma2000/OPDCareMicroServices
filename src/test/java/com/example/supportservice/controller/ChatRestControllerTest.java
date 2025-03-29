package com.example.supportservice.controller;


import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.model.MessageEntity;
import com.example.supportservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
class ChatRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetChatHistory() throws Exception {
        // Given
        MessageEntity msg1 = MessageEntity.builder()
                                          .sender("john")
                                          .receiver("alice")
                                          .content("Hi Alice")
                                          .timestamp(LocalDateTime.now())
                                          .build();

        MessageEntity msg2 = MessageEntity.builder()
                                          .sender("alice")
                                          .receiver("john")
                                          .content("Hi John")
                                          .timestamp(LocalDateTime.now())
                                          .build();

        List<MessageEntity> history = List.of(msg1, msg2);

        Mockito.when(chatService.getChatHistory(eq("john"), eq("alice"))).thenReturn(history);

        // When + Then
        mockMvc.perform(get("/api/chat/history")
                                .param("sender", "john")
                                .param("receiver", "alice")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].sender", is("john")))
               .andExpect(jsonPath("$[0].receiver", is("alice")))
               .andExpect(jsonPath("$[1].sender", is("alice")))
               .andExpect(jsonPath("$[1].receiver", is("john")));
    }
}
