package com.example.supportservice.controller;


import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    private String jwtToken;

    @Autowired
    private JwtUtil jwtUtil;

    private WebSocketStompClient stompClient;

    private static final String CHAT_ENDPOINT = "/chat";
    private static final String SEND_ENDPOINT = "/app/chat.send";
    private static final String SUBSCRIBE_TOPIC = "/topic/public";

    @BeforeEach
    public void setup() {
        jwtToken = "Bearer " + jwtUtil.generateToken("customer@example.com", Map.of());
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testChatMessageExchange() throws Exception {
        String url = "ws://localhost:" + port + CHAT_ENDPOINT;
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", jwtToken);

        BlockingQueue<ChatMessage> blockingQueue = new LinkedBlockingQueue<>();

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe(SUBSCRIBE_TOPIC, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ChatMessage.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        blockingQueue.offer((ChatMessage) payload);
                    }
                });

                ChatMessage message = new ChatMessage();
                message.setSender("customer@example.com");
                message.setReceiver("agent@example.com");
                message.setContent("Hello from integration test");
                message.setType(ChatMessage.MessageType.valueOf("CHAT"));

                session.send(SEND_ENDPOINT, message);
            }
        };

        ListenableFuture<StompSession> future = stompClient.connect(url, headers, connectHeaders, sessionHandler);
//        StompSession session = future.get(1, TimeUnit.SECONDS);
//
//        ChatMessage received = blockingQueue.poll(5, TimeUnit.SECONDS);
//        assertNotNull(received);
//        assertEquals("Hello from integration test", received.getContent());
//        assertEquals("customer@example.com", received.getSender());
    }
}
