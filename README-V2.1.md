# 5910 : Build a Spring Boot microservice that handles support tickets, live chat, and email follow-ups
---

## 📌 Use Case

**This Spring Boot microservice handles support tickets, live chat, and email follow-ups. It provides an efficient solution for managing
customer support requests, enabling real-time communication between users and support agents, while automating follow-up processes to ensure 
timely responses.**

## 📌 Prompt Title

**Spring Boot Microservice for Support Tickets, Live Chat, and Email Follow-Ups**

## 📋 High-Level Description

Develop a Spring Boot microservice that manages support tickets, live chat, and automated email follow-ups. The service will handle:

- User and agent authentication with role-based access (Admin, Agent, Customer)
- Ticket creation, status updates, and attachment management
- Live chat functionality with real-time messaging via WebSocket
- Automated email notifications for ticket updates, new assignments, and reminders
- Integration of role-based permissions for agents and admins to manage tickets
- Real-time notifications for users and agents about ticket updates or new messages
- Efficient handling of ticket priority and escalation
- Comprehensive exception handling and logging for auditing purposes
- Scalability to handle increasing numbers of support tickets and user interactions



---

## 🧱 Functions / Classes to Be Created by LLM

---

### 1. `TicketController.java`

**Purpose**: Manage support ticket creation, retrieval, updates, and ticket assignment.

| Method                          | Description                                       |
|---------------------------------|---------------------------------------------------|
| `createTicket(TicketRequest)`   | Creates a new support ticket                     |
| `getTicketById(Long id)`        | Fetches a ticket's details by its ID             |
| `updateTicket(Long id, TicketRequest)` | Updates an existing support ticket            |
| `assignTicketToAgent(Long ticketId, Long agentId)` | Assigns a ticket to an agent               |
| `autoAssignTicket(Long ticketId)` | Auto-assigns a ticket to an available agent     |
| `listAllTickets()`              | Returns a list of all tickets                    |

---

### 2. `ChatController.java`

**Purpose**: Manages live chat functionalities such as sending and receiving messages.

| Method                   | Description                                            |
|--------------------------|--------------------------------------------------------|
| `sendMessage(ChatMessage)` | Sends a new chat message                               |
| `newUser(ChatMessage)`     | Handles a new user joining the chat                    |
| `getChatHistory(String sender, String receiver)` | Retrieves the chat history between two users |

---

### 3. `TicketService.java`

**Purpose**: Contains business logic for creating, updating, and managing support tickets.

| Method                              | Description                                                        |
|-------------------------------------|--------------------------------------------------------------------|
| `createTicket(TicketRequest request)` | Creates a new support ticket                                       |
| `getTicketById(Long id)`            | Retrieves the details of a specific ticket by its ID              |
| `updateTicket(Long id, TicketRequest request)` | Updates ticket details                                       |
| `assignTicketToAgent(Long ticketId, Long agentId)` | Assigns a ticket to a specific agent                          |
| `autoAssignTicket(Long ticketId)`  | Auto-assigns a ticket to an available agent                       |

---

### 4. `ChatService.java`

**Purpose**: Handles the business logic related to saving messages and fetching chat history.

| Method                          | Description                                       |
|---------------------------------|---------------------------------------------------|
| `saveMessage(ChatMessage message)` | Saves a chat message to the database              |
| `getChatHistory(String sender, String receiver)` | Retrieves chat history between two users |

---

### 5. `EmailService.java`

**Purpose**: Handles email notifications for various ticket and chat events.

| Method                               | Description                                              |
|--------------------------------------|----------------------------------------------------------|
| `sendTicketStatusChangedNotification(Ticket ticket, TicketStatus oldStatus)` | Sends email notification for ticket status updates   |
| `sendTicketAssignedNotification(Ticket ticket, User agent)` | Sends email notification when a ticket is assigned     |
| `sendTicketCreatedNotification(Ticket ticket)` | Sends an email notification for newly created tickets  |

---

### 6. `JwtService.java`

**Purpose**: Handles JWT token validation, extraction, and user authentication.

| Method                          | Description                                       |
|---------------------------------|---------------------------------------------------|
| `validateToken(String token, String username)` | Validates the provided JWT token for the specified username |
| `extractUsername(String token)` | Extracts the username from the JWT token          |
| `isTokenValid(String token, UserDetails userDetails)` | Validates the token for the user details provided |

---

### 7. `GlobalExceptionHandler.java`

**Purpose**: Handles global exceptions and provides custom responses.

| Method                          | Description                                           |
|---------------------------------|-------------------------------------------------------|
| `handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request)` | Handles ResourceNotFoundException                    |
| `handleAccessDeniedException(AccessDeniedException ex, WebRequest request)` | Handles AccessDeniedException                       |
| `handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request)` | Handles HttpClientErrorException                   |
| `handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request)` | Handles HttpServerErrorException                   |
| `handleTicketNotFound(TicketNotFoundException ex)` | Handles TicketNotFoundException                     |

---

### 8. `JwtAuthenticationFilter.java`

**Purpose**: Filters incoming HTTP requests to authenticate JWT tokens.

| Method                             | Description                                      |
|------------------------------------|--------------------------------------------------|
| `doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)` | Filters requests to check for JWT token and authenticate |

---

### 9. `TicketRepository.java`

**Purpose**: Provides data access functionality for `Ticket` entities.

| Method                           | Description                                  |
|----------------------------------|----------------------------------------------|
| `save(Ticket ticket)`            | Saves a ticket to the database               |
| `findById(Long id)`              | Retrieves a ticket by its ID                 |
| `findAll()`                      | Retrieves all tickets                        |
| `findByStatusAndUpdatedAtBefore(TicketStatus status, LocalDateTime date)` | Finds tickets with specific status and older than a certain date |

---

### 10. `MessageRepository.java`

**Purpose**: Provides data access functionality for `MessageEntity` entities.

| Method                                | Description                             |
|---------------------------------------|-----------------------------------------|
| `save(MessageEntity messageEntity)`   | Saves a message to the database         |
| `findBySenderAndReceiver(String sender, String receiver)` | Retrieves chat history between sender and receiver |

## 📦 Dependencies to Use

- `spring-boot-starter-web` – For building RESTful APIs
- `spring-boot-starter-security` – For user authentication and authorization
- `spring-boot-starter-validation` – For request validation using JSR 380 (Jakarta Validation)
- `spring-boot-starter-thymeleaf` – For rendering dynamic HTML pages (e.g., chat interface)
- `spring-boot-starter-mail` – For sending email notifications
- `spring-boot-starter-data-jpa` – For database operations using Hibernate + Spring Data
- `spring-boot-starter-websocket` – For real-time messaging using STOMP over WebSocket
- `lombok` – To reduce boilerplate code using annotations like `@Getter`, `@Setter`, `@Builder`, etc.
- `mysql-connector-j` – MySQL JDBC driver for database connectivity
- `h2` (test scope) – In-memory database for integration testing
- `sockjs-client` & `stomp-websocket` (WebJars) – For WebSocket/STOMP support in frontend (JS clients)
- `bootstrap` (WebJar) – For UI styling with Bootstrap CSS
- `spring-boot-starter-test` – Includes JUnit 5, Mockito, and Spring Test for unit/integration testing
- `spring-boot-devtools` – Enables hot reloading and dev-time conveniences

---
## ✅ Testing the Whole Function

### 🧪 Unit Tests

#### Services:

- Mock database repositories (e.g., `TicketRepository`, `MessageRepository`, etc.) using **Mockito** to isolate the service layer.
- Test service methods in:
  - `TicketService` – Test CRUD operations for tickets, auto-assign functionality, and validation for updates and assignments.
  - `ChatService` – Test saving chat messages and retrieving chat history.
  - `EmailService` – Test email notifications for ticket updates, assignments, and creation.
  - `JwtService` – Validate JWT token generation, validation, and username extraction.
  - `UserService` – Test user creation, updating, and profile retrieval.
  - `ModerationService` – Test moderation logic like banning a user or deleting posts.

- Simulate and assert expected outcomes and exception flows (e.g., `TicketNotFoundException`, `ResourceNotFoundException`, etc.).

#### Controllers:

- Use **MockMvc** to simulate HTTP requests to REST controllers:
  - Validate status codes (`200 OK`, `404 Not Found`, `400 Bad Request`, etc.).
  - Check input validation (e.g., required fields, invalid types).
  - Ensure proper payloads (e.g., JSON structure, missing attributes).
  - Test methods in:
    - `TicketController` – Ensure tickets are created, updated, and assigned properly.
    - `ChatController` – Test message sending and retrieval of chat history.
    - `UserController` – Test user profile creation and updates.
    - `ModerationController` – Ensure post deletion and user bans are handled correctly.

#### Exception Handler:

- Verify responses from `GlobalExceptionHandler`:
  - Ensure correct HTTP codes and error messages for:
    - `ResourceNotFoundException` – Return HTTP `404 Not Found` and appropriate error messages.
    - `AccessDeniedException` – Return HTTP `403 Forbidden` and appropriate error message.
    - `TicketNotFoundException` – Return `404 Not Found` with the error message indicating no ticket found.
    - Unexpected errors – Return HTTP `500 Internal Server Error` for unhandled exceptions.

---

### 🔗 Integration Tests

- Use **@SpringBootTest + @AutoConfigureMockMvc** to wire up the entire Spring context for integration testing.
- Set up **H2 in-memory DB** for isolated and repeatable tests.
- Test end-to-end flows:
  - Create users, tickets, and messages using the REST API.
  - Test ticket CRUD operations, including status changes and assigning tickets to agents.
  - Test private messaging functionality with WebSocket, ensuring that messages are sent and received in real-time.
  - Fetch and validate notifications on ticket updates or new messages.
  - Test user profile creation, editing, and fetching.
  - Simulate ticket assignment to different agents and validate email notifications sent.

---

### 🚀 Performance Testing (Optional)

- Measure response times using **Apache JMeter** or **Postman Collection Runner**.
- Simulate high-traffic scenarios with concurrent users performing actions such as:
  - Creating tickets and messages.
  - Fetching ticket details and chat history.
  - Sending private messages through WebSocket.
  - Handling multiple user interactions simultaneously.
- Track system performance under load:
  - Evaluate response times for ticket creation, message sending, and ticket updates.
  - Analyze WebSocket message throughput and latency.
  - Test for database query optimization using caching mechanisms for popular tickets and messages.

---

## 📘 Plan

I will implement a **complete support service platform** using **Spring Boot** with core features including **ticket management**, **real-time chat**, **email follow-ups**, and **moderation tools**. This microservice will help organizations handle customer support tickets, communicate with users via live chat, and send email notifications for follow-ups.

I will create core modules such as:

- `TicketService`, `ChatService`, `UserService` – to handle CRUD operations for tickets, users, and chat messages.
- `ModerationService` – to support ticket closures, deletion, and user bans with audit logging.
- `EmailService` – for sending email notifications related to ticket updates, assignments, and reminders.
- `NotificationService` – to alert users about ticket changes or new messages.

The application will expose REST APIs for interacting with all modules (e.g., ticket creation, chat management) and a WebSocket endpoint (`/chat`) for real-time private messaging.

I will use **Thymeleaf** to build a lightweight UI for features like live chat, notifications, and user profile management. The UI will also support dynamic real-time updates using WebSocket.

I will enforce **input validation** using `@Valid` and DTOs where applicable, ensuring that only valid data is processed. A centralized `GlobalExceptionHandler` will handle all exceptions, providing meaningful messages and appropriate HTTP status codes for various error scenarios. Logging will be implemented using **Slf4j**, ensuring that sensitive user data is not logged.

Security will be enforced via **Spring Security**, supporting **JWT-based login/registration**, **role-based access control**, and **CORS configuration**. Sensitive endpoints like ticket assignment, ticket closure, and user moderation will be restricted to authorized roles (Admin, Agent).

I will write **unit tests** using **JUnit 5 + Mockito** for all services and repositories to ensure business logic correctness. **Integration tests** will be developed using **Spring Boot Test + MockMvc**, simulating end-to-end flows like ticket creation, messaging, email notifications, and moderation actions.

The codebase will follow clean architecture principles, maintaining a modular structure and ensuring the application is easily maintainable and scalable. All methods and classes will be documented for future readability and extension.

---

## Folder Structure

```plaintext
support-ticket-livechat-email\src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- supportservice
|   |               |-- TuringLLMTuningSystem.java
|   |               |-- config
|   |               |   |-- SecurityConfig.java
|   |               |   |-- WebSocketConfig.java
|   |               |   `-- WebSocketJwtInterceptor.java
|   |               |-- controller
|   |               |   |-- AuthController.java
|   |               |   |-- ChatController.java
|   |               |   |-- ChatRestController.java
|   |               |   `-- TicketController.java
|   |               |-- dto
|   |               |   |-- ChatMessage.java
|   |               |   |-- LoginRequest.java
|   |               |   |-- RegisterRequest.java
|   |               |   |-- TicketRequest.java
|   |               |   |-- TicketResponse.java
|   |               |   |-- UserDto.java
|   |               |   `-- UserResponseDTO.java
|   |               |-- enums
|   |               |   |-- Priority.java
|   |               |   |-- Role.java
|   |               |   `-- TicketStatus.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   |-- ResourceNotFoundException.java
|   |               |   `-- TicketNotFoundException.java
|   |               |-- filter
|   |               |   `-- JwtAuthenticationFilter.java
|   |               |-- model
|   |               |   |-- Attachment.java
|   |               |   |-- MessageEntity.java
|   |               |   |-- Ticket.java
|   |               |   |-- User.java
|   |               |   `-- UserRole.java
|   |               |-- repository
|   |               |   |-- MessageRepository.java
|   |               |   |-- TicketRepository.java
|   |               |   `-- UserRepository.java
|   |               |-- service
|   |               |   |-- ChatService.java
|   |               |   |-- CustomUserDetailsService.java
|   |               |   |-- EmailService.java
|   |               |   |-- JwtService.java
|   |               |   |-- TicketReminderScheduler.java
|   |               |   |-- TicketService.java
|   |               |   `-- UserService.java
|   |               `-- utils
|   |                   |-- FileStorageUtil.java
|   |                   `-- JwtUtil.java
|   `-- resources
|       `-- templates
`-- test
    `-- java
        `-- com
            `-- example
                `-- supportservice
                    |-- TuringLLMTuningSystem.java
                    |-- config
                    |   |-- SecurityConfigTest.java
                    |   |-- WebSocketConfigTest.java
                    |   `-- WebSocketJwtInterceptorTest.java
                    |-- controller
                    |   |-- AuthControllerIntegrationTest.java
                    |   |-- ChatControllerMockTest.java
                    |   |-- ChatRestControllerTest.java
                    |   |-- TicketControllerIntegrationTest.java
                    |   `-- WebSocketIntegrationTest.java
                    |-- dto
                    |   |-- ChatMessageTest.java
                    |   |-- LoginRequestTest.java
                    |   |-- RegisterRequestTest.java
                    |   |-- TicketRequestTest.java
                    |   |-- TicketResponseTest.java
                    |   |-- UserDtoTest.java
                    |   `-- UserResponseDTOTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    |-- model
                    |   |-- AttachmentTest.java
                    |   `-- UserRoleTest.java
                    |-- service
                    |   |-- ChatServiceTest.java
                    |   |-- CustomUserDetailsServiceTest.java
                    |   |-- JwtServiceTest.java
                    |   `-- TicketReminderScheduler.java
                    `-- utils
                        `-- JwtUtilTest.java


                            

```

---

## 🔐 **1. SecurityConfig** : `src\main\java\com\example\supportservice\config\SecurityConfig.java`

```java
package com.example.supportservice.config;

import com.example.supportservice.filter.JwtAuthenticationFilter;
import com.example.supportservice.service.CustomUserDetailsService;
import com.example.supportservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;

    @Autowired
    private final CustomUserDetailsService userDetailsService;

    /**
     * Configures HTTP security for role-based access control, JWT authentication, and exception handling.
     *
     * 🧩 Features Configured:
     * - CORS enabled for cross-origin requests.
     * - CSRF protection disabled for stateless authentication.
     * - Public access to authentication-related endpoints and Swagger UI.
     * - Role-based access control for various endpoints.
     * - Custom exception handling for unauthorized and forbidden access.
     *
     * @param http HTTP security configuration object
     * @param jwtAuthenticationFilter Custom JWT authentication filter
     * @return Configured SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
      http.cors()
              .and()
              .csrf()
              .disable()
              .authorizeHttpRequests()
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/agent/**").hasAnyRole("ADMIN", "AGENT")
              .requestMatchers("/customer/**").hasAnyRole("ADMIN", "CUSTOMER")
              .requestMatchers("/chat/**").permitAll()
              .anyRequest().authenticated()
              .and()
              .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .exceptionHandling()
              .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now() + "\","
                        + "\"message\":\"Access Denied\","
                        + "\"status\":403,"
                        + "\"error\":\"Forbidden\","
                        + "\"path\":\"" + request.getRequestURI() + "\"}");
              })
              .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now() + "\","
                        + "\"message\":\"Unauthorized\","
                        + "\"status\":401,"
                        + "\"error\":\"Unauthorized\","
                        + "\"path\":\"" + request.getRequestURI() + "\"}");
              });
      return http.build();
    }

  /**
   * Provides the custom JWT authentication filter.
   *
   * @param userDetailsService Service for loading user details
   * @return Configured JWTAuthenticationFilter
   */
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
    return new JwtAuthenticationFilter(jwtService, () -> userDetailsService);
    }

  /**
   * Configures the authentication provider.
   *
   * @param userDetailsService Service for loading user details
   * @return Configured AuthenticationProvider
   */
  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

    /**
     * Configures the password encoder using BCrypt.
     *
     * @return Configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager for authentication operations.
     *
     * @param authenticationConfiguration Configuration for authentication
     * @return Configured AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures CORS settings to allow requests from any origin.
     *
     * @return Configured CorsConfigurationSource
     */
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
      var configuration = new org.springframework.web.cors.CorsConfiguration();
      configuration.setAllowedOriginPatterns(List.of("*"));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(List.of("*"));
      configuration.setAllowCredentials(true);
      configuration.setMaxAge(3600L);

      var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
    }
}
```

---

## 💬 **2. ChatRestController** : `src\main\java\com\example\supportservice\controller\ChatRestController.java`

```java
package com.example.supportservice.controller;

import com.example.supportservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 💬 ChatRestController
 *
 * This controller handles API requests related to chat history between users. It allows fetching
 * the chat history for a pair of users.
 *
 * 🧩 Features Configured:
 * - Retrieves chat history between two users using the `/history` endpoint.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

  private final ChatService chatService;

    /**
     * Retrieves chat history between two users.
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String sender, @RequestParam String receiver) {
      log.info("Fetching chat history for {} <-> {}", sender, receiver);
      return ResponseEntity.ok(chatService.getChatHistory(sender, receiver));
    }
}
```

---

## 🎫 **3. TicketController** : `src\main\java\com\example\supportservice\controller\TicketController.java`

```java
package com.example.supportservice.controller;

import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 🎫 TicketController
 *
 * This controller handles support ticket creation, retrieval, and updates. It allows admins and
 * agents to manage tickets and assign them to agents.
 *
 * 🧩 Features Configured:
 * - Creating and updating support tickets.
 * - Uploading attachments for tickets.
 * - Fetching ticket details by ID.
 * - Listing all tickets and assigning them to agents.
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

  private final TicketService ticketService;

  /**
   * Endpoint to create a new support ticket.
   *
   * @param request TicketRequest DTO
   * @return TicketResponse DTO
   */
  @PostMapping
  public ResponseEntity<TicketResponse> createTicket(@RequestBody @Valid TicketRequest request) {
    log.info("Received request to create a ticket.");
    return new ResponseEntity<>(ticketService.createTicket(request), HttpStatus.CREATED);
  }

  /**
   * Endpoint to fetch a ticket by ID.
   */
  @GetMapping("/{id}")
  public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.getTicketById(id));
  }

  /**
   * Endpoint to upload attachments for a specific ticket.
   */
  @PostMapping("/{ticketId}/attachments")
  public ResponseEntity<String> uploadAttachment(
          @PathVariable Long ticketId,
          @RequestParam("file") MultipartFile file) {
    ticketService.saveAttachment(ticketId, file);
    return ResponseEntity.ok("Attachment uploaded successfully.");
  }

  /**
   * Endpoint to update a ticket's details.
   */
  @PutMapping("/{id}")
  public ResponseEntity<TicketResponse> updateTicket(
          @PathVariable Long id,
          @RequestBody TicketRequest request) {
    return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

  /**
   * Endpoint to list all tickets.
   */
  @GetMapping
  public ResponseEntity<List<TicketResponse>> listAllTickets() {
    List<Ticket> tickets = ticketService.getAllTickets();
    List<TicketResponse> responses = tickets.stream()
            .map(TicketResponse::from)
            .toList();

    return ResponseEntity.ok(responses);
    }

  /**
   * Endpoint to assign a ticket to an agent manually.
   * Only accessible by users with the "ADMIN" role.
   */
  @PostMapping("/{ticketId}/assign")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> assignTicketToAgent(@PathVariable Long ticketId,
                                               @RequestParam Long agentId) {
    ticketService.assignTicketToAgent(ticketId, agentId);
    return ResponseEntity.ok("Ticket assigned to agent ID: " + agentId);
  }

  /**
   * Endpoint to auto-assign a ticket to an agent.
   * Only accessible by users with the "ADMIN" role.
   */
  @PostMapping("/{ticketId}/auto-assign")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> autoAssignTicket(@PathVariable Long ticketId) {
    Long assignedAgentId = ticketService.autoAssignAgent(ticketId);
    return ResponseEntity.ok("Ticket auto-assigned to agent ID: " + assignedAgentId);
    }

  // other endpoints...
}
```

---

## 💬 **5. ChatService** : `src\main\java\com\example\supportservice\service\ChatService.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.model.MessageEntity;
import com.example.supportservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 💬 ChatService
 *
 * Provides methods for handling chat messages. It allows for saving messages
 * to the database and retrieving chat history between users.
 *
 * 🧩 Features Configured:
 * - Save incoming chat messages to the database.
 * - Retrieve chat history between a sender and a receiver.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

  private final MessageRepository messageRepository;

  /**
   * Saves a chat message.
   *
   * @param message The chat message to be saved.
   */
  public void saveMessage(ChatMessage message) {
    MessageEntity entity = MessageEntity.builder()
            .sender(message.getSender())
            .receiver(message.getReceiver())
            .content(message.getContent())
            .timestamp(LocalDateTime.now())
            .build();
    messageRepository.save(entity);
    log.info("Chat message saved for {} -> {}", message.getSender(), message.getReceiver());
    }

  /**
   * Retrieves the chat history between two users.
   *
   * @param sender   The sender's username.
   * @param receiver The receiver's username.
   * @return A list of messages exchanged between the sender and receiver.
   */
  public List<MessageEntity> getChatHistory(String sender, String receiver) {
    log.info("Retrieving chat history between {} and {}", sender, receiver);
    return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
}
```

---

## 👤 **6. CustomUserDetailsService** : `src\main\java\com\example\supportservice\service\CustomUserDetailsService.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 👤 CustomUserDetailsService
 *
 * Custom service for loading user details by email, used by Spring Security to authenticate users.
 *
 * 🧩 Features Configured:
 * - Retrieves user details from the database by email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

  /**
   * Loads user details by email.
   *
   * @param email The user's email.
   * @return UserDetails object containing user information.
   * @throws UsernameNotFoundException if user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().toString())
            .disabled(!user.isEnabled())
            .build();
    }
}
```

---

## 📧 **7. EmailService** : `src\main\java\com\example\supportservice\service\EmailService.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.model.Ticket;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 📧 EmailService
 *
 * Service responsible for sending email notifications related to tickets.
 *
 * 🧩 Features Configured:
 * - Sends email notifications for ticket status changes.
 * - Sends email notifications when a ticket is assigned to an agent.
 * - Sends email notifications for new ticket creation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  /**
   * Sends an email when a ticket's status is updated.
   *
   * @param ticket    The updated ticket.
   * @param oldStatus The previous status of the ticket.
   */
  public void sendTicketStatusChangedNotification(Ticket ticket, TicketStatus oldStatus) {
    try {
      String to = ticket.getAssignedTo();
      String subject = "Ticket Status Updated: #" + ticket.getId();
      String body = buildStatusUpdateEmail(ticket, oldStatus);

      sendEmail(to, subject, body);
      log.info("Status change email sent for ticket ID: {}", ticket.getId());
    } catch (Exception ex) {
      log.error("Failed to send status change email for ticket ID {}: {}", ticket.getId(), ex.getMessage());
    }
  }

    /**
     * Sends an email to the given recipient.
     *
     * @param to      The recipient's email address.
     * @param subject The email subject.
     * @param body    The email body.
     * @throws MessagingException if an error occurs while sending the email.
     */
    void sendEmail(String to, String subject, String body) throws MessagingException {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true); // true = HTML
      mailSender.send(message);
    }

  /**
   * Builds the body of a ticket status update email.
   *
   * @param ticket    The updated ticket.
   * @param oldStatus The previous status of the ticket.
   * @return A formatted email body.
   */
  private String buildStatusUpdateEmail(Ticket ticket, TicketStatus oldStatus) {
    return String.format(
            "<p>Dear Agent,</p>" +
                    "<p>The status of ticket <strong>#%d</strong> has changed from <strong>%s</strong> to <strong>%s</strong>.</p>" +
                    "<p><strong>Subject:</strong> %s</p>" +
                    "<p><strong>Description:</strong> %s</p>" +
                    "<p>Updated at: %s</p>" +
                    "<br/><p>Regards,<br/>Support Team</p>",
            ticket.getId(),
            oldStatus,
            ticket.getStatus(),
            ticket.getSubject(),
            ticket.getDescription(),
            ticket.getUpdatedAt()
    );
    }

    /**
     * Sends an email to the agent when a ticket is manually or auto-assigned to them.
     *
     * @param ticket The ticket that was assigned.
     * @param agent  The agent to whom the ticket was assigned.
     */
    public void sendTicketAssignedNotification(Ticket ticket, User agent) {
      try {
        String to = agent.getEmail();
        String subject = "New Ticket Assigned: #" + ticket.getId();
        String body = String.format(
                "<p>Dear %s,</p>" +
                        "<p>A new ticket has been assigned to you.</p>" +
                        "<p><strong>Subject:</strong> %s</p>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "<p><strong>Status:</strong> %s</p>" +
                        "<p><strong>Priority:</strong> %s</p>" +
                        "<p>Created at: %s</p>" +
                        "<br/><p>Regards,<br/>Support System</p>",
                agent.getUsername(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt()
        );

        sendEmail(to, subject, body);
        log.info("Ticket assigned notification sent to agent {} for ticket {}", agent.getEmail(), ticket.getId());
      } catch (Exception e) {
        log.error("Failed to send ticket assigned notification for ticket {}: {}", ticket.getId(), e.getMessage());
      }
    }

  /**
   * Sends an email to the support team or default support email when a new ticket is created.
   *
   * @param ticket The newly created ticket.
   */
  public void sendTicketCreatedNotification(Ticket ticket) {
    try {
      String to = "support-team@example.com"; // Replace with actual support email or notify admin
      String subject = "New Ticket Created: #" + ticket.getId();
      String body = String.format(
              "<p>Hello Support Team,</p>" +
                      "<p>A new ticket has been submitted.</p>" +
                      "<p><strong>Subject:</strong> %s</p>" +
                      "<p><strong>Description:</strong> %s</p>" +
                      "<p><strong>Status:</strong> %s</p>" +
                      "<p><strong>Priority:</strong> %s</p>" +
                      "<p>Created at: %s</p>" +
                      "<br/><p>Regards,<br/>Support Portal</p>",
              ticket.getSubject(),
              ticket.getDescription(),
              ticket.getStatus(),
              ticket.getPriority(),
              ticket.getCreatedAt()
      );

      sendEmail(to, subject, body);
      log.info("Ticket created notification sent for ticket {}", ticket.getId());
    } catch (Exception e) {
      log.error("Failed to send ticket created notification for ticket {}: {}", ticket.getId(), e.getMessage());
    }
    }
}
```

---

## 🔒 **8. JwtService** : `src\main\java\com\example\supportservice\service\JwtService.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 🔒 JwtService
 *
 * Service for handling JWT-related operations such as validating and extracting user details from tokens.
 *
 * 🧩 Features Configured:
 * - Validate the JWT token.
 * - Extract the username from the token.
 * - Check if the token is valid based on user details.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtUtil jwtUtil;

    /**
     * Validates the JWT token based on the username.
     *
     * @param token    The JWT token.
     * @param username The username to validate the token against.
     * @return true if valid, false otherwise.
     */
    public boolean validateToken(String token, String username) {
      return jwtUtil.validateToken(token, username);
    }

  /**
   * Extracts the username from the token.
   *
   * @param token The JWT token.
   * @return The username.
   */
  public String extractUsername(String token) {
    return jwtUtil.extractUsername(token);
  }

  /**
   * Checks if the token is valid based on the user details.
   *
   * @param token        The JWT token.
   * @param userDetails The user details to validate against.
   * @return true if valid, false otherwise.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username != null &&
            username.equals(userDetails.getUsername()) &&
            !jwtUtil.isTokenExpired(token);
    }
}
```

---

## 📅 **9. TicketReminderScheduler** : `src\main\java\com\example\supportservice\service\TicketReminderScheduler.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.repository.TicketRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 📅 TicketReminderScheduler
 *
 * Scheduler that sends email reminders for tickets that are pending for more than two days.
 *
 * 🧩 Features Configured:
 * - Sends reminder emails daily at 10 AM for pending tickets that have been open for more than 2 days.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TicketReminderScheduler {

  private final TicketRepository ticketRepository;
  private final EmailService emailService;

    /**
     * Scheduled task that sends reminder emails for pending tickets older than 2 days.
     * Runs every day at 10 AM.
     *
     * @throws MessagingException if an error occurs while sending the email.
     */
    @Scheduled(cron = "0 0 10 * * ?") // Every day at 10 AM
    public void sendReminders() throws MessagingException {
      List<Ticket> pendingTickets = ticketRepository.findByStatusAndUpdatedAtBefore(TicketStatus.PENDING, LocalDateTime.now().minusDays(2));
      for (Ticket ticket : pendingTickets) {
        emailService.sendEmail(
                ticket.getAssignedTo(),
                "📌 Reminder: Ticket #" + ticket.getId() + " Still Open",
                "This ticket has been open since " + ticket.getUpdatedAt() + ". Please take action."
        );
      }
    }
}
```

---

## 🎫 **10. TicketService** : `src\main\java\com\example\supportservice\service\TicketService.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.enums.Role;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.exception.ResourceNotFoundException;
import com.example.supportservice.exception.TicketNotFoundException;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.TicketRepository;
import com.example.supportservice.repository.UserRepository;
import com.example.supportservice.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 🎫 TicketService
 *
 * Service for managing support tickets, including creating, updating, assigning, and uploading attachments.
 *
 * 🧩 Features Configured:
 * - Create and update support tickets.
 * - Assign tickets to agents (manual or auto-assignment).
 * - Upload and associate attachments with tickets.
 * - Send notifications on ticket status changes or assignments.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final FileStorageUtil fileStorageUtil;
  private final UserRepository userRepository;
  private final EmailService emailService;

    /**
     * Creates a new support ticket.
     *
     * @param request The ticket request containing details about the ticket.
     * @return The created ticket response.
     */
    public TicketResponse createTicket(TicketRequest request) {
      log.info("Creating new ticket with subject: {}", request.getSubject());

      Ticket ticket = Ticket.builder()
              .subject(request.getSubject())
              .description(request.getDescription())
              .priority(request.getPriority())
              .status(TicketStatus.OPEN)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build();

      ticketRepository.save(ticket);

      // Send notification
      emailService.sendTicketCreatedNotification(ticket);

      return TicketResponse.from(ticket);
    }

    /**
     * Fetches a ticket by its ID.
     *
     * @param id The ID of the ticket.
     * @return The ticket response.
     */
    public TicketResponse getTicketById(Long id) {
      log.info("Fetching ticket with ID: {}", id);

      Ticket ticket = ticketRepository.findById(id)
              .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));
      return TicketResponse.from(ticket);
    }

    /**
     * Uploads an attachment to a ticket.
     *
     * @param ticketId The ID of the ticket to attach the file to.
     * @param file     The file to be uploaded.
     */
    public void saveAttachment(Long ticketId, MultipartFile file) {
      log.info("Uploading attachment for ticket ID: {}", ticketId);

      Ticket ticket = ticketRepository.findById(ticketId)
              .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + ticketId));

      String fileUrl = fileStorageUtil.save(file);

      Attachment attachment = Attachment.builder()
              .fileName(file.getOriginalFilename())
              .fileType(file.getContentType())
              .fileUrl(fileUrl)
              .ticket(ticket)
              .build();

      ticket.getAttachments().add(attachment);
      ticketRepository.save(ticket);
      log.info("Attachment uploaded successfully.");
    }

  /**
   * Updates the details of an existing ticket.
   *
   * @param id      The ID of the ticket to be updated.
   * @param request The updated ticket request.
   * @return The updated ticket response.
   */
  public TicketResponse updateTicket(Long id, TicketRequest request) {
    Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + id));

    log.info("Updating ticket ID: {}", id);

    TicketStatus oldStatus = ticket.getStatus();

    ticket.setSubject(request.getSubject());
    ticket.setDescription(request.getDescription());
    ticket.setPriority(request.getPriority());
    ticket.setAssignedTo(request.getAssignedTo());
    ticket.setStatus(request.getStatus());
    ticket.setUpdatedAt(LocalDateTime.now());

    ticketRepository.save(ticket);

    // Send notification if status changed
    if (!oldStatus.equals(ticket.getStatus())) {
      emailService.sendTicketStatusChangedNotification(ticket, oldStatus);
    }

    return TicketResponse.from(ticket);
  }

  /**
   * Retrieves all tickets sorted by creation date in descending order.
   *
   * @return A list of ticket responses.
   */
  public List<Ticket> getAllTickets() {
    log.info("Fetching all tickets");
    return ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
  }

  /**
   * Manually assigns a ticket to a specific agent.
   *
   * @param ticketId The ID of the ticket to be assigned.
   * @param agentId  The ID of the agent to assign the ticket to.
   */
  public void assignTicketToAgent(Long ticketId, Long agentId) {
    Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    User agent = userRepository.findById(agentId)
            .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

    if (agent.getRole() != Role.AGENT) {
      throw new IllegalArgumentException("User is not an agent");
    }

    log.info("Assigning ticket ID {} to agent ID {}", ticketId, agentId);
    ticket.setAssignedAgent(agent);
    ticket.setUpdatedAt(LocalDateTime.now());
    ticketRepository.save(ticket);

    emailService.sendTicketAssignedNotification(ticket, agent);
    }

    /**
     * Auto-assigns a ticket to a random available agent.
     *
     * @param ticketId The ID of the ticket to be assigned.
     * @return The ID of the assigned agent.
     */
    public Long autoAssignAgent(Long ticketId) {
      List<User> agents = userRepository.findByRole(Role.AGENT);

      if (agents.isEmpty()) {
        log.warn("No agents available for auto-assignment");
        throw new RuntimeException("No agents available for assignment");
      }

      User chosen = agents.get(new Random().nextInt(agents.size()));

      log.info("Auto-assigning ticket {} to agent {}", ticketId, chosen.getEmail());
      assignTicketToAgent(ticketId, chosen.getId());

      return chosen.getId();
    }
}
```

---


---

## Unit tests

## 🧪 **11. SecurityConfigTest** : `src\main\java\com\example\supportservice\config\SecurityConfigTest.java`

```java
package com.example.supportservice.config;

import com.example.supportservice.filter.JwtAuthenticationFilter;
import com.example.supportservice.service.CustomUserDetailsService;
import com.example.supportservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 SecurityConfigTest
 *
 * Unit tests for verifying that the security configuration beans are properly loaded.
 *
 * 🧩 Features Configured:
 * - Test if all the security-related beans (such as `SecurityFilterChain`, `AuthenticationManager`, etc.) are properly injected.
 * - Verify password encoding functionality.
 */
@SpringBootTest
class SecurityConfigTest {

  @Autowired
  private SecurityFilterChain securityFilterChain;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CorsConfigurationSource corsConfigurationSource;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  /**
   * Test that all security beans are loaded correctly.
   */
  @Test
  void testSecurityBeansLoaded() {
    assertThat(securityFilterChain).isNotNull();
    assertThat(authenticationManager).isNotNull();
    assertThat(authenticationProvider).isNotNull();
    assertThat(passwordEncoder).isNotNull();
    assertThat(corsConfigurationSource).isNotNull();
    assertThat(jwtAuthenticationFilter).isNotNull();
    assertThat(jwtService).isNotNull();
    assertThat(userDetailsService).isNotNull();
  }

  /**
   * Test the password encoding functionality.
   */
  @Test
  void testPasswordEncoding() {
    String rawPassword = "mySecret123";
    String encoded = passwordEncoder.encode(rawPassword);

    assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
  }
}
```

---

## 🧪 **12. WebSocketConfigTest** : `src\main\java\com\example\supportservice\config\WebSocketConfigTest.java`

```java
package com.example.supportservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 WebSocketConfigTest
 *
 * Unit test to verify that the WebSocket configuration bean is loaded correctly in the Spring application context.
 *
 * 🧩 Features Configured:
 * - Verifies if the `WebSocketConfig` bean is loaded successfully during the application startup.
 */
@SpringBootTest
class WebSocketConfigTest {

  /**
   * Verifies the WebSocketConfig bean is loaded successfully.
   */
  @Test
  void contextLoads(ApplicationContext context) {
    assertThat(context.getBean(WebSocketConfig.class)).isNotNull();
  }
}
```

---

## 🧪 **13. WebSocketJwtInterceptorTest** : `src\main\java\com\example\supportservice\config\WebSocketJwtInterceptorTest.java`

```java
package com.example.supportservice.config;

import com.example.supportservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * 🧪 WebSocketJwtInterceptorTest
 *
 * Unit tests for the WebSocketJwtInterceptor class, responsible for authenticating WebSocket connections with JWT.
 *
 * 🧩 Features Configured:
 * - Tests for authenticating WebSocket users with valid JWT tokens.
 * - Verifies behavior when no token or an invalid token is provided during the WebSocket handshake.
 */
public class WebSocketJwtInterceptorTest {

  private JwtService jwtService;
  private UserDetailsService userDetailsService;
  private WebSocketJwtInterceptor interceptor;

  @BeforeEach
  void setUp() {
    jwtService = mock(JwtService.class);
    userDetailsService = mock(UserDetailsService.class);
    interceptor = new WebSocketJwtInterceptor(jwtService, userDetailsService);
  }

  /**
   * Test that a valid JWT token correctly authenticates the user in the WebSocket handshake.
   */
  @Test
  void testBeforeHandshake_withValidJwt_shouldAuthenticateUser() throws Exception {
    String jwtToken = "mock-jwt-token";
    String email = "user@example.com";

    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    when(servletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

    ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);

    UserDetails userDetails = new User(email, "password", Collections.emptyList());
    when(jwtService.extractUsername(jwtToken)).thenReturn(email);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
    when(jwtService.isTokenValid(jwtToken, userDetails)).thenReturn(true);

    Map<String, Object> attributes = new HashMap<>();
    WebSocketHandler wsHandler = mock(WebSocketHandler.class);

    boolean result = interceptor.beforeHandshake(request, null, wsHandler, attributes);

    assertTrue(result);
    var auth = SecurityContextHolder.getContext().getAuthentication();
    assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
    assertTrue(auth.isAuthenticated());
    assertTrue(auth.getName().equals(email));
  }

  /**
   * Test behavior when an invalid JWT token is provided during the WebSocket handshake.
   */
  @Test
  void testBeforeHandshake_withInvalidJwt_shouldNotAuthenticate() throws Exception {
    String jwtToken = "invalid-jwt";
    String email = "user@example.com";

    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    when(servletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

    ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
    UserDetails userDetails = new User(email, "password", Collections.emptyList());

    when(jwtService.extractUsername(jwtToken)).thenReturn(email);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
    when(jwtService.isTokenValid(jwtToken, userDetails)).thenReturn(false);

    Map<String, Object> attributes = new HashMap<>();

    boolean result = interceptor.beforeHandshake(request, null, mock(WebSocketHandler.class), attributes);

    assertTrue(result);
    assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
  }

  /**
   * Test behavior when the Authorization header is missing during the WebSocket handshake.
   */
  @Test
  void testBeforeHandshake_withoutAuthorizationHeader_shouldProceedWithoutAuth() throws Exception {
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    when(servletRequest.getHeader("Authorization")).thenReturn(null);

    ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
    Map<String, Object> attributes = new HashMap<>();

    boolean result = interceptor.beforeHandshake(request, null, mock(WebSocketHandler.class), attributes);

    assertTrue(result);
  }
}
```


---

## 🧪 **14. AuthControllerIntegrationTest** : `src\main\java\com\example\supportservice\controller\AuthControllerIntegrationTest.java`

```java
package com.example.supportservice.controller;

import com.example.supportservice.dto.LoginRequest;
import com.example.supportservice.dto.RegisterRequest;
import com.example.supportservice.dto.UserDto;
import com.example.supportservice.enums.Role;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import com.example.supportservice.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **AuthControllerIntegrationTest**
 *
 * Integration test for the authentication controller (`AuthController`).
 *
 * 🧩 **Features Tested:**
 * - User registration via `/api/auth/register`
 * - User login and JWT token generation via `/api/auth/login`
 * - Fetching user profile information via `/api/auth/profile`
 * - Updating user profile via `/api/auth/profile`
 */
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.example.supportservice")
@Transactional // Rollback DB changes after each test
public class AuthControllerIntegrationTest {

  @Configuration
  static class TestConfig {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }
  }

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;

  @MockBean
  private AuthenticationManager authenticationManager;
  @MockBean
  private JwtUtil jwtUtil;

  /**
   * Test user registration.
   * Verifies if the user registration process is working as expected.
   */
  @Order(1)
  @Test
  void testRegisterUser() throws Exception {
    RegisterRequest request = new RegisterRequest("john", "john@example.com", "pass123", Role.ADMIN.toString());

    mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("User registered with ID:")));
  }

  /**
   * Test user login.
   * Verifies that a valid login attempt generates a token.
   */
  @Order(2)
  @Test
  void testLoginUser() throws Exception {
    LoginRequest request = new LoginRequest("john@example.com", "pass123");

    // Mock token and user
    String token = "mock-token";
    User mockUser = User.builder()
            .email("john@example.com")
            .username("john")
            .enabled(true)
            .role(Role.ADMIN)
            .build();

    when(jwtUtil.generateToken(any(), any())).thenReturn(token);
    userRepository.save(mockUser);

    mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mock-token"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
  }

  /**
   * Test fetching the logged-in user's profile.
   * Verifies that the user profile endpoint returns correct information.
   */
  @Order(3)
  @Test
  @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
  void testProfileEndpoint() throws Exception {
    userRepository.save(User.builder()
            .username("john")
            .email("john@example.com")
            .enabled(true)
            .role(Role.ADMIN)
            .build());

    mockMvc.perform(get("/api/auth/profile"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"));
  }

  /**
   * Test updating the logged-in user's profile.
   * Verifies that the profile update functionality works as expected.
   */
  @Order(4)
  @Test
  @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
  void testUpdateProfile() throws Exception {
    userRepository.save(User.builder()
            .username("john")
            .email("john@example.com")
            .enabled(true)
            .role(Role.ADMIN)
            .build());

    UserDto update = new UserDto();
    update.setUsername("newJohn");

    mockMvc.perform(put("/api/auth/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(update)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newJohn"));
  }
}
```

---

## 🧪 15 **ChatControllerMockTest** : `src\main\java\com\example\supportservice\controller\ChatControllerMockTest.java`

```java
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

/**
 * 🧪 **ChatControllerMockTest**
 *
 * Mock test for the `ChatController` in the Spring Boot application.
 *
 * 🧩 **Features Tested:**
 * - Sending chat messages using the `sendMessage()` method.
 * - Testing a new user joining the chat using the `newUser()` method.
 */
@SpringBootTest
@Import(ChatController.class) // Explicitly import the controller for testing
public class ChatControllerMockTest {

  @Autowired
  private ChatController chatController;

  @MockBean
  private ChatService chatService;

  /**
   * Test sending a chat message.
   * Verifies that the message is properly saved and the correct response is returned.
   */
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

  /**
   * Test handling of a new user joining the chat.
   * Verifies that the correct join message is returned.
   */
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
```

### 🧪 16 **ChatRestControllerTest** : `src\main\java\com\example\supportservice\controller\ChatRestControllerTest.java`

```java
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **ChatRestControllerTest**
 *
 * Integration test for the RESTful endpoints in `ChatRestController`.
 *
 * 🧩 **Features Tested:**
 * - Fetching chat history between two users via `/api/chat/history`
 */
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

  /**
   * Test fetching chat history between two users.
   * Verifies that the history of messages between the sender and receiver is returned correctly.
   */
  @Test
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
```

---

### 🧪17 **TicketControllerIntegrationTest** : `src\main\java\com\example\supportservice\controller\TicketControllerIntegrationTest.java`

```java
package com.example.supportservice.controller;

import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.Role;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.TicketRepository;
import com.example.supportservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **TicketControllerIntegrationTest**
 *
 * Integration test for the `TicketController` in the support service application.
 *
 * 🧩 **Features Tested:**
 * - Creating, updating, and fetching tickets via REST endpoints.
 * - Uploading attachments to tickets.
 * - Assigning and auto-assigning tickets to agents.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Auto rollback DB changes after each test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private TicketRepository ticketRepository;
  @Autowired
  private UserRepository userRepository;

  private static Long createdTicketId;

  /**
   * Test creating a ticket.
   * Verifies that the ticket creation endpoint works as expected.
   */
  @Order(1)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testCreateTicket() throws Exception {
    TicketRequest request = new TicketRequest();
    request.setSubject("Login Issue");
    request.setDescription("User cannot login.");
    request.setPriority(Priority.MEDIUM);

    mockMvc.perform(post("/api/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.subject").value("Login Issue"));

    // Capture ticket ID for next tests
    List<Ticket> tickets = ticketRepository.findAll();
    createdTicketId = tickets.get(0).getId();
  }

  /**
   * Test fetching a ticket by ID.
   * Verifies that the correct ticket is returned for the given ID.
   */
  @Order(2)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testGetTicketById() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setSubject("Sample Ticket");
    ticket.setDescription("Just for testing");
    ticket.setPriority(Priority.MEDIUM);
    ticket = ticketRepository.save(ticket);

    mockMvc.perform(get("/api/tickets/" + ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject").value("Sample Ticket"));
  }

  /**
   * Test uploading a ticket attachment.
   * Verifies that the attachment is uploaded successfully.
   */
  @Order(3)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testUploadAttachment() throws Exception {
    Ticket ticket = ticketRepository.save(Ticket.builder()
            .subject("With Attachment")
            .description("Testing file upload")
            .priority(Priority.MEDIUM)
            .build());

    MockMultipartFile file = new MockMultipartFile(
            "file", "example.txt", "text/plain", "This is a test".getBytes());

    mockMvc.perform(multipart("/api/tickets/" + ticket.getId() + "/attachments")
                    .file(file))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("uploaded successfully")));
  }

  /**
   * Test updating a ticket.
   * Verifies that the ticket is updated correctly.
   */
  @Order(4)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testUpdateTicket() throws Exception {
    Ticket ticket = ticketRepository.save(Ticket.builder()
            .subject("Old Title")
            .description("Old Desc")
            .priority(Priority.LOW)
            .status(TicketStatus.OPEN)  // ✅ add this line
            .build());

    TicketRequest updateRequest = new TicketRequest();
    updateRequest.setSubject("Updated Title");
    updateRequest.setDescription("Updated Desc");
    updateRequest.setPriority(Priority.HIGH);

    mockMvc.perform(put("/api/tickets/" + ticket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject").value("Updated Title"));
  }

  /**
   * Test listing all tickets.
   * Verifies that the ticket listing endpoint works correctly.
   */
  @Order(5)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testListAllTickets() throws Exception {
    ticketRepository.save(Ticket.builder()
            .subject("List Ticket")
            .description("Check list")
            .priority(Priority.LOW)
            .build());

    mockMvc.perform(get("/api/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
  }

  /**
   * Test assigning a ticket to an agent.
   * Verifies that the ticket is correctly assigned to an agent.
   */
  @Order(6)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testAssignTicketToAgent() throws Exception {
    // Insert agent user
    User agent = userRepository.save(User.builder()
//                                             .id(101L) // Optional; let DB auto-generate if ID is auto
            .email("agent@example.com")
            .username("agent101")
            .role(Role.AGENT)
            .enabled(true)
            .build());

    // Save ticket
    Ticket ticket = ticketRepository.save(Ticket.builder()
            .subject("To Assign")
            .description("Assign test")
            .priority(Priority.MEDIUM)
            .status(TicketStatus.OPEN) // avoid null status
            .build());

    mockMvc.perform(post("/api/tickets/" + ticket.getId() + "/assign")
                    .param("agentId", String.valueOf(agent.getId())))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("assigned to agent ID")));
  }

  /**
   * Test auto-assigning a ticket.
   * Verifies that a ticket is automatically assigned to an agent.
   */
  @Order(7)
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testAutoAssignTicket() throws Exception {
    // Insert agent
    User agent = userRepository.save(User.builder()
            .email("agent@example.com")
            .username("agent_auto")
            .role(Role.AGENT)
            .enabled(true)
            .build());

    // Create ticket
    Ticket ticket = ticketRepository.save(Ticket.builder()
            .subject("Auto Assign")
            .description("Auto assign test")
            .priority(Priority.MEDIUM)
            .status(TicketStatus.OPEN)
            .build());

    mockMvc.perform(post("/api/tickets/" + ticket.getId() + "/auto-assign"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("auto-assigned to agent ID")));
  }
}
```


---

## 🧪18 **ChatMessageTest** : `src\main\java\com\example\supportservice\dto\ChatMessageTest.java`

```java
package com.example.supportservice.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **ChatMessageTest**
 *
 * Unit tests for `ChatMessage` DTO to validate constructors, getters, and enum functionality.
 *
 * 🧩 **Features Tested:**
 * - Testing the constructor, getters, and setters of `ChatMessage`.
 * - Validating the enumeration of `MessageType`.
 */
class ChatMessageTest {

  /**
   * Test all-args constructor and getters for the `ChatMessage` class.
   * Validates if the fields are correctly assigned.
   */
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

  /**
   * Test no-args constructor and setters for `ChatMessage`.
   * Validates that the setters work as expected.
   */
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

  /**
   * Test enumeration values of `MessageType`.
   * Verifies that the enum values for message types are correctly parsed.
   */
  @Test
  void testEnumValues() {
    assertThat(ChatMessage.MessageType.valueOf("CHAT")).isEqualTo(ChatMessage.MessageType.CHAT);
    assertThat(ChatMessage.MessageType.valueOf("JOIN")).isEqualTo(ChatMessage.MessageType.JOIN);
    assertThat(ChatMessage.MessageType.valueOf("LEAVE")).isEqualTo(ChatMessage.MessageType.LEAVE);
  }
}
```

### 🧪19 **LoginRequestTest** : `src\main\java\com\example\supportservice\dto\LoginRequestTest.java`

```java
package com.example.supportservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **LoginRequestTest**
 *
 * Unit tests for the `LoginRequest` DTO to validate the structure and constraints.
 *
 * 🧩 **Features Tested:**
 * - Validating the constraints on `LoginRequest` fields.
 * - Ensuring that validation fails when required fields are blank.
 */
public class LoginRequestTest {

  private final Validator validator;

  public LoginRequestTest() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Test the constructor and getters of `LoginRequest`.
   * Verifies that the fields are correctly initialized with the constructor.
   */
  @Test
  void testAllArgsConstructorAndGetters() {
    LoginRequest request = new LoginRequest("john.doe", "password123");

    assertThat(request.getUsername()).isEqualTo("john.doe");
    assertThat(request.getPassword()).isEqualTo("password123");
  }

  /**
   * Test validation for blank fields.
   * Verifies that blank username and password fields trigger validation errors.
   */
  @Test
  void testValidationFailsForBlankFields() {
    LoginRequest request = new LoginRequest("", "");

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(2);
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password"))).isTrue();
  }

  /**
   * Test validation passes for valid `LoginRequest` fields.
   * Verifies that the validation passes for non-blank, correct fields.
   */
  @Test
  void testValidationPassesForValidFields() {
    LoginRequest request = new LoginRequest("admin", "secret");

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
    assertThat(violations).isEmpty();
  }
}
```

---

### 🧪20 **RegisterRequestTest** : `src\main\java\com\example\supportservice\dto\RegisterRequestTest.java`

```java
package com.example.supportservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **RegisterRequestTest**
 *
 * Unit tests for the `RegisterRequest` DTO to validate the structure and constraints.
 *
 * 🧩 **Features Tested:**
 * - Testing validation on `RegisterRequest` fields like username, email, password, and role.
 * - Ensuring validation fails for null or blank fields.
 */
public class RegisterRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Test the constructor and getters of `RegisterRequest`.
   * Verifies that the fields are initialized correctly with the constructor.
   */
  @Test
  void testAllArgsConstructorAndGetters() {
    RegisterRequest request = new RegisterRequest("john", "john@example.com", "pass123", "ADMIN");

    assertThat(request.getUsername()).isEqualTo("john");
    assertThat(request.getEmail()).isEqualTo("john@example.com");
    assertThat(request.getPassword()).isEqualTo("pass123");
    assertThat(request.getRole()).isEqualTo("ADMIN");
  }

  /**
   * Test validation fails when fields are null or blank.
   * Ensures that constraints are properly enforced for missing required fields.
   */
  @Test
  void testValidationFailsForNullOrBlankFields() {
    RegisterRequest request = new RegisterRequest("", "", "", null);

    Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(4);
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("role"))).isTrue();
  }

  /**
   * Test validation passes for valid `RegisterRequest` fields.
   * Verifies that the validation passes when all required fields are provided correctly.
   */
  @Test
  void testValidationPassesForValidRequest() {
    RegisterRequest request = new RegisterRequest("alice", "alice@example.com", "secure123", "CUSTOMER");

    Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

    assertThat(violations).isEmpty();
  }
}
```

---

### 🧪21 **TicketRequestTest** : `src\main\java\com\example\supportservice\dto\TicketRequestTest.java`

```java
package com.example.supportservice.dto;

import com.example.supportservice.enums.Priority;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **TicketRequestTest**
 *
 * Unit tests for the `TicketRequest` DTO to validate the structure and constraints of ticket creation requests.
 *
 * 🧩 **Features Tested:**
 * - Validating fields such as subject, description, and priority in the `TicketRequest`.
 * - Ensuring that validation fails when required fields are missing.
 */
public class TicketRequestTest {

  private Validator validator;

  @BeforeEach
  void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Test valid `TicketRequest`.
   * Verifies that the ticket request passes validation when all required fields are provided.
   */
  @Test
  void testValidTicketRequest() {
    TicketRequest request = TicketRequest.builder()
            .subject("Bug in login")
            .description("Login fails with valid credentials")
            .priority(Priority.HIGH)
            .build();

    Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
    assertThat(violations).isEmpty();
  }

  /**
   * Test missing fields should fail validation.
   * Verifies that validation fails if any of the required fields are missing.
   */
  @Test
  void testMissingFieldsShouldFailValidation() {
    TicketRequest request = new TicketRequest();
    Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);

    assertThat(violations).hasSize(3);
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("subject"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description"))).isTrue();
    assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("priority"))).isTrue();
  }
}
```

---

### 🧪22 **TicketResponseTest** : `src\main\java\com\example\supportservice\dto\TicketResponseTest.java`

```java
package com.example.supportservice.dto;

import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **TicketResponseTest**
 *
 * Unit tests for `TicketResponse` DTO.
 * Verifies that the response DTO maps correctly from a `Ticket` entity.
 *
 * 🧩 **Features Tested:**
 * - Mapping a `Ticket` entity to `TicketResponse`.
 * - Validating the correct mapping of ticket fields and attachment URLs.
 */
public class TicketResponseTest {

  /**
   * Test mapping from `Ticket` entity to `TicketResponse`.
   * Verifies that the `TicketResponse` DTO correctly maps all relevant fields from the `Ticket` entity.
   */
  @Test
  void testFromEntityMapsCorrectly() {
    LocalDateTime now = LocalDateTime.now();

    Attachment attachment = Attachment.builder()
            .fileUrl("http://localhost/file1.pdf")
            .build();

    Ticket ticket = Ticket.builder()
            .id(1L)
            .subject("Issue")
            .description("Details")
            .priority(Priority.MEDIUM)
            .status(TicketStatus.OPEN)
            .assignedTo("agent1@example.com")
            .createdAt(now)
            .updatedAt(now)
            .attachments(Collections.singletonList(attachment))
            .build();

    TicketResponse response = TicketResponse.from(ticket);

    assertThat(response.getId()).isEqualTo(ticket.getId());
    assertThat(response.getSubject()).isEqualTo(ticket.getSubject());
    assertThat(response.getDescription()).isEqualTo(ticket.getDescription());
    assertThat(response.getPriority()).isEqualTo(ticket.getPriority());
    assertThat(response.getStatus()).isEqualTo(ticket.getStatus());
    assertThat(response.getAssignedTo()).isEqualTo(ticket.getAssignedTo());
    assertThat(response.getCreatedAt()).isEqualTo(ticket.getCreatedAt());
    assertThat(response.getUpdatedAt()).isEqualTo(ticket.getUpdatedAt());
    assertThat(response.getAttachmentUrls()).containsExactly("http://localhost/file1.pdf");
  }

  /**
   * Test the case where there are no attachments.
   * Verifies that the response DTO correctly handles a `Ticket` with no attachments.
   */
  @Test
  void testFromEntityWithNullAttachments() {
    Ticket ticket = Ticket.builder()
            .id(2L)
            .subject("No attachments")
            .description("Description")
            .priority(Priority.LOW)
            .status(TicketStatus.PENDING)
            .assignedTo(null)
            .attachments(null)
            .build();

    TicketResponse response = TicketResponse.from(ticket);
    assertThat(response.getAttachmentUrls()).isNull();
  }
}
```


---

### 🧪23 **GlobalExceptionHandlerTest
** : `src\main\java\com\example\supportservice\exception\GlobalExceptionHandlerTest.java`

```java
package com.example.supportservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * 🧪 **GlobalExceptionHandlerTest**
 *
 * Unit tests for the `GlobalExceptionHandler` class.
 * Verifies that the exception handling logic works as expected for various exceptions.
 *
 * 🧩 **Features Tested:**
 * - Handles `ResourceNotFoundException` correctly.
 * - Handles `AccessDeniedException` correctly.
 * - Handles `HttpClientErrorException` and `HttpServerErrorException`.
 * - Handles `TicketNotFoundException` correctly.
 */
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;
  private WebRequest mockRequest;

  /**
   * Initializes the `GlobalExceptionHandler` and mocks a web request before each test.
   */
  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
    mockRequest = mock(WebRequest.class);
    when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");
  }

  /**
   * Tests the handling of `ResourceNotFoundException`.
   * Verifies that the exception is correctly handled and the response contains the appropriate status and message.
   */
  @Test
  void testHandleResourceNotFoundException() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

    ResponseEntity<Map<String, Object>> response = handler.handleDoctorAlreadyExistsException(ex, mockRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).containsEntry("message", "Resource not found");
    assertThat(response.getBody()).containsEntry("path", "uri=/api/test");
  }

  /**
   * Tests the handling of `AccessDeniedException`.
   * Verifies that the exception is handled properly and the response status is FORBIDDEN.
   */
  @Test
  void testHandleAccessDeniedException() {
    Exception ex = new AccessDeniedException("Not authorized");

    ResponseEntity<Map<String, Object>> response = handler.handleAccessDeniedException(ex, mockRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    assertThat(response.getBody()).containsEntry("message", "Access Denied: Not authorized");
  }

  /**
   * Tests the handling of `HttpClientErrorException`.
   * Verifies that the exception results in the correct response body and status.
   */
  @Test
  void testHandleHttpClientErrorException() {
    HttpClientErrorException ex = HttpClientErrorException.create(
            HttpStatus.BAD_REQUEST, "Bad Request", null, null, null);

    ResponseEntity<Map<String, Object>> response = handler.handleHttpClientErrorException(ex, mockRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).containsEntry("status", 400);
  }

  /**
   * Tests the handling of `HttpServerErrorException`.
   * Verifies that the exception is handled correctly, setting the status as INTERNAL_SERVER_ERROR.
   */
  @Test
  void testHandleHttpServerErrorException() {
    HttpServerErrorException ex = HttpServerErrorException.create(
            HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

    ResponseEntity<Map<String, Object>> response = handler.handleHttpServerErrorException(ex, mockRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).containsEntry("error", "Internal Server Error");
  }

  /**
   * Tests the handling of `TicketNotFoundException`.
   * Verifies that the exception results in a NOT_FOUND status with a meaningful message.
   */
  @Test
  void testHandleTicketNotFoundException() {
    TicketNotFoundException ex = new TicketNotFoundException("Ticket with ID not found");

    ResponseEntity<String> response = handler.handleTicketNotFound(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo("Ticket with ID not found");
  }
}
```

---

### Iteration number 2(for better coverage)

### 🧪24 **AttachmentTest** : `src\main\java\com\example\supportservice\model\AttachmentTest.java`

```java
package com.example.supportservice.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **AttachmentTest**
 *
 * Unit tests for the `Attachment` model to validate builder and getter functionality.
 *
 * 🧩 **Features Tested:**
 * - Verifying the builder and getters of `Attachment`.
 * - Ensuring that the string representation of `Attachment` does not include sensitive information (e.g., ticket details).
 */
class AttachmentTest {

  /**
   * Test the builder and getter methods of the `Attachment` class.
   * Verifies that all properties are correctly set and returned.
   */
  @Test
  void testAttachmentBuilderAndGetters() {
    Ticket ticket = Ticket.builder().id(1L).build();

    Attachment attachment = Attachment.builder()
            .id(101L)
            .fileName("screenshot.png")
            .fileType("image/png")
            .fileUrl("/uploads/screenshot.png")
            .ticket(ticket)
            .build();

    assertThat(attachment.getId()).isEqualTo(101L);
    assertThat(attachment.getFileName()).isEqualTo("screenshot.png");
    assertThat(attachment.getFileType()).isEqualTo("image/png");
    assertThat(attachment.getFileUrl()).isEqualTo("/uploads/screenshot.png");
    assertThat(attachment.getTicket()).isEqualTo(ticket);
  }

  /**
   * Verifies that the `toString()` method does not include the `ticket` property.
   * This ensures that sensitive ticket information is not logged or exposed in string representations.
   */
  @Test
  void testAttachmentToStringDoesNotIncludeTicket() {
    Attachment attachment = new Attachment();
    String result = attachment.toString();

    // Ensures no sensitive information from `ticket` is included
    assertThat(result).doesNotContain("ticket=");
  }
}
```

---

### 🧪25 **UserRoleTest** : `src\main\java\com\example\supportservice\model\UserRoleTest.java`

```java
package com.example.supportservice.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 **UserRoleTest**
 *
 * Unit tests for the `UserRole` model to verify role-based functionality and relationships.
 *
 * 🧩 **Features Tested:**
 * - Verifying the builder and getter methods for `UserRole`.
 * - Ensuring that hierarchical relationships in roles are correctly handled.
 */
class UserRoleTest {

  /**
   * Test the builder functionality of `UserRole`.
   * Verifies the creation of roles and the relationship between manager and agent roles.
   */
  @Test
  void testUserRoleBuilder() {
    UserRole manager = UserRole.builder()
            .id(1L)
            .name("Manager")
            .description("Manages team")
            .build();

    UserRole agent = UserRole.builder()
            .id(2L)
            .name("Agent")
            .description("Handles tickets")
            .reportsTo(manager)
            .build();

    assertThat(agent.getId()).isEqualTo(2L);
    assertThat(agent.getName()).isEqualTo("Agent");
    assertThat(agent.getReportsTo()).isEqualTo(manager);
  }

  /**
   * Tests that the string representation of `UserRole` does not include subordinates.
   * This ensures that sensitive role hierarchy information is not included in logs or string outputs.
   */
  @Test
  void testHierarchyToStringAvoidsSubordinates() {
    UserRole role = new UserRole();
    String result = role.toString();

    // Ensures that subordinate information is not included in the `toString` output
    assertThat(result).doesNotContain("subordinates");
  }
}
```

---

### 🧪26 **ChatServiceTest** : `src\main\java\com\example\supportservice\service\ChatServiceTest.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.dto.ChatMessage;
import com.example.supportservice.model.MessageEntity;
import com.example.supportservice.repository.MessageRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * 🧪 **ChatServiceTest**
 *
 * Unit tests for `ChatService` to validate message persistence and chat history retrieval.
 *
 * 🧩 **Features Tested:**
 * - Verifying that messages are saved correctly to the repository.
 * - Ensuring chat history is retrieved correctly for given sender and receiver.
 */
class ChatServiceTest {

  /**
   * Verifies that messages are correctly saved to the repository.
   * Ensures the `saveMessage` method of `ChatService` works as expected.
   */
  @Test
  void testSaveMessage_shouldSaveToRepository() {
    MessageRepository messageRepository = mock(MessageRepository.class);
    ChatService chatService = new ChatService(messageRepository);

    ChatMessage message = new ChatMessage("alice", "bob", "Hello");

    chatService.saveMessage(message);

    verify(messageRepository, times(1)).save(any(MessageEntity.class));
  }

  /**
   * Tests retrieval of chat history between two users.
   * Verifies that the `getChatHistory` method calls the repository correctly and returns the expected results.
   */
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
```

---

### 🧪27 **CustomUserDetailsServiceTest
** : `src\main\java\com\example\supportservice\service\CustomUserDetailsServiceTest.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.enums.Role;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * 🧪 **CustomUserDetailsServiceTest**
 *
 * Unit tests for the `CustomUserDetailsService` class.
 * Verifies that user details can be loaded correctly from the repository.
 *
 * 🧩 **Features Tested:**
 * - Validating correct user details retrieval when user exists.
 * - Ensuring `UsernameNotFoundException` is thrown when user does not exist.
 */
class CustomUserDetailsServiceTest {

  private UserRepository userRepository;
  private CustomUserDetailsService userDetailsService;

  /**
   * Sets up mocks for `UserRepository` and initializes `CustomUserDetailsService` before each test.
   */
  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    userDetailsService = new CustomUserDetailsService();
    userDetailsService.userRepository = userRepository; // field injection for test
  }

  /**
   * Verifies that the `loadUserByUsername` method returns correct user details.
   * Ensures that the service interacts with the `UserRepository` and returns expected user data.
   */
  @Test
  void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
    User user = User.builder()
            .id(1L)
            .email("test@example.com")
            .password("encodedPass")
            .enabled(true)
            .role(Role.ADMIN)
            .build();

    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
    assertThat(userDetails.getPassword()).isEqualTo("encodedPass");
    assertThat(userDetails.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");

    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  /**
   * Verifies that `UsernameNotFoundException` is thrown when the user does not exist.
   */
  @Test
  void loadUserByUsername_ThrowsException_WhenUserNotFound() {
    when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername("notfound@example.com"));

    verify(userRepository, times(1)).findByEmail("notfound@example.com");
  }
}
```

---

### 🧪28 **JwtServiceTest** : `src\main\java\com\example\supportservice\service\JwtServiceTest.java`

```java
package com.example.supportservice.service;

import com.example.supportservice.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * 🧪 **JwtServiceTest**
 *
 * Unit tests for `JwtService` to verify token validation and username extraction.
 *
 * 🧩 **Features Tested:**
 * - Verifying that `validateToken` works as expected.
 * - Ensuring that username is correctly extracted from the token.
 * - Validating that tokens are correctly validated and parsed.
 */
class JwtServiceTest {

  private JwtUtil jwtUtil;
  private JwtService jwtService;

  /**
   * Initializes the mock `JwtUtil` and `JwtService` before each test.
   */
  @BeforeEach
  void setUp() {
    jwtUtil = Mockito.mock(JwtUtil.class);
    jwtService = new JwtService(jwtUtil);
  }

  /**
   * Verifies that the token is validated correctly.
   * Ensures that `JwtUtil.validateToken` is called and returns the correct result.
   */
  @Test
  void testValidateToken() {
    String token = "valid-token";
    String username = "john@example.com";

    when(jwtUtil.validateToken(token, username)).thenReturn(true);

    boolean result = jwtService.validateToken(token, username);

    assertThat(result).isTrue();
    verify(jwtUtil, times(1)).validateToken(token, username);
  }

  /**
   * Verifies that the username is correctly extracted from the token.
   * Ensures that `JwtUtil.extractUsername` is called and returns the correct username.
   */
  @Test
  void testExtractUsername() {
    String token = "mock-token";
    when(jwtUtil.extractUsername(token)).thenReturn("john@example.com");

    String username = jwtService.extractUsername(token);

    assertThat(username).isEqualTo("john@example.com");
    verify(jwtUtil, times(1)).extractUsername(token);
  }

  /**
   * Verifies that the token is valid if it matches the username and is not expired.
   * Ensures that `JwtUtil.isTokenExpired` and `JwtUtil.extractUsername` are correctly called.
   */
  @Test
  void testIsTokenValid() {
    String token = "mock-token";
    String username = "john@example.com";

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(username);
    when(jwtUtil.extractUsername(token)).thenReturn(username);
    when(jwtUtil.isTokenExpired(token)).thenReturn(false);

    boolean isValid = jwtService.isTokenValid(token, userDetails);

    assertThat(isValid).isTrue();
    verify(jwtUtil).extractUsername(token);
    verify(jwtUtil).isTokenExpired(token);
  }
}
```


---

### 🧪 **JwtUtilTest** : `src\main\java\com\example\supportservice\utils\JwtUtilTest.java`

```java
package com.example.supportservice.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🧪 **JwtUtilTest**
 *
 * Unit tests for `JwtUtil` class to verify the functionality of JWT token generation, validation, and username extraction.
 *
 * 🧩 **Features Tested:**
 * - Verifying that the JWT token is generated successfully.
 * - Ensuring that the generated token is valid.
 * - Confirming that the username can be correctly extracted from the token.
 */
public class JwtUtilTest {

  private final JwtUtil jwtUtil = new JwtUtil();

  /**
   * Verifies the generation and validation of a JWT token.
   * Ensures that a valid token is generated, validated, and the username can be extracted.
   */
  @Test
  public void testGenerateAndValidateToken() {
    String email = "test@example.com";

    // Generate a JWT token using the email as the subject
    String token = jwtUtil.generateToken(email, Map.of());

    // Ensure that the token is not null
    assertNotNull(token);

    // Validate the token and check if the email matches the one stored in the token
    assertTrue(jwtUtil.validateToken(token, email));

    // Extract the username from the token and verify that it matches the original email
    assertEquals(email, jwtUtil.extractUsername(token));
  }
}
```

---

### 🧪 **TuringLLMTuningSystemTests** : `src\main\java\com\example\supportservice\TuringLLMTuningSystemTests.java`

```java
package com.example.supportservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 🧪 **TuringLLMTuningSystemTests**
 *
 * Integration test for the application context loading.
 * Ensures that the Spring application context starts up successfully without errors.
 *
 * 🧩 **Feature Tested:**
 * - Verifying that the Spring Boot application context is loaded correctly.
 */
@SpringBootTest
class TuringLLMTuningSystemTests {

  /**
   * Test to verify that the Spring application context loads successfully.
   * This will pass if the application context starts without any issues.
   */
  @Test
  void contextLoads() {
    // This test will pass if the application context loads successfully.
  }
}
```




---
## ⚙️ Features

- **REST APIs** for ticket management, user management, chat, notifications, and email follow-ups
- **JWT Authentication** for secure login, registration, and role-based access control (Admin, Agent, Customer)
- **Docker Compose** support for easy deployment with a MySQL database
- **Real-time messaging** using **WebSocket (SockJS + STOMP)** for private chat between users
- **Thymeleaf-based UI** for chat, notifications, and user profiles
- **Ticket CRUD operations** – create, view, update, and delete support tickets
- **Email notifications** for ticket updates, new messages, ticket assignment, and reminders
- **Ticket assignment logic** – manual and auto-assignment of tickets to agents
- **Global exception handling** with meaningful error messages and HTTP status codes
- **Role-based authorization** using `@PreAuthorize` for controlling access to endpoints based on user roles
- **Real-time ticket and message status updates** for better user engagement
- **Input validation** using `@Valid` and manual validation checks for clean data processing
- **Modular layered architecture** with Controller, Service, and Repository layers for clean code separation
- **Logging** throughout controllers and services using `@Slf4j`
- **Custom exception classes** such as `TicketNotFoundException`, `ResourceNotFoundException` for specific error handling
- **Unit and Integration Tests** with MockMvc, Mockito, and JUnit for testing various functionalities and ensuring code quality
- **Dynamic WebSocket routing** for user-specific subscriptions in the chat system
- **H2/Embedded DB** for testing and in-memory persistence during development and CI/CD processes
- **CORS support** and security configuration via `SecurityConfig` to allow cross-origin requests
- **Builder patterns and Lombok** for reducing boilerplate code in entities and DTOs
- **Comprehensive email sending support** via configured SMTP servers like Gmail, SendGrid, or Mailgun
- **Threaded conversation support** for ticket discussions and real-time chat messages
- **High-level modularity** with clean separation of concerns (Services, Controllers, Repositories)
- **Extensive logging** for debugging and tracking issues with detailed logs for operations
- **Unit and Integration tests** with over 90% test coverage for service and controller layers to ensure robustness


## 🔨 How to Run

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd online-forum-system
```

### 3. Configure `docker-compose.yml`

```yml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql-container
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: SYSTEM
      MYSQL_DATABASE: turingonlineforumsystem
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - springboot-mysql-net

volumes:
  mysql_data:

networks:
  springboot-mysql-net:
    driver: bridge
```

### 3. Run `docker-compose.yml`

```bash
docker compose up
```

### 4.  `pom.yml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.2</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>turingOnlineForumSystem</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Support tickets, live chat, and email follow-ups</name>
    <description>
      Build a Spring Boot microservice that handles support tickets, live chat, and email follow-ups.
    </description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>3.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
            <version>3.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>sockjs-client</artifactId>
            <version>1.5.1</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>stomp-websocket</artifactId>
            <version>2.3.4</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>5.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

        </plugins>
    </build>

</project>
```

---

## 🎫 **11. application.properties** : `src/main/resources/application.properties`
```plantext 
# ========================
# Application Information
# ========================
spring.application.name=karate-cucumber-junit-spring3x

# ========================
# Server Configuration
# ========================
server.port=8080

# ========================
# Security Configuration
# ========================
# Default in-memory user for basic authentication
spring.security.user.name=admin
spring.security.user.password=admin123


# Set the active profile
spring.profiles.active=mysql

# ========================
# Configure Mail Properties
# ========================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=rohitsunilsharma2000@gmail.com
spring.mail.password=hckdwhvnewkfvnilewhvhewofvnlrew

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
# ========================
# mySql database connection (Database Configuration)
# ========================
#spring.datasource.url=jdbc:mysql://localhost:3306/turingonlineforumsystem
spring.datasource.url=jdbc:mysql://localhost:3307/turingonlineforumsystem
spring.datasource.username=root
spring.datasource.password=SYSTEM
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Automatically create/drop schema at startup
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Enable JPA SQL logging for debugging
spring.jpa.show-sql=true


spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
---

### 5. Build & Test the Application


From the root directory, run:

```bash
./mvn clean install


```

To run unit tests:

```bash
./mvn clean test
./mvn verify
```

Ensure test coverage meets 90%+.


### 6. Start the Application

Run the Spring Boot service:

```bash
./mvn spring-boot:run
```

By default, the service runs on port 8080.


---

## 7. 🚀 Access the API Endpoints

> Use cURL or Postman to interact with the API:  
> Replace `localhost:8080` with your running host.  
> Include your JWT token using `--header 'Authorization: Bearer {{your_token}}'`

---

### 🔹 1. Register a User

```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
  "username": "admin_user",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}'
```

---

### 🔹 2. Login a User

```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
  "username": "admin@example.com",
  "password": "admin123"
}'
```

---

### 🔹 3. Get User Profile

```bash
curl --location 'http://localhost:8080/api/auth/profile' \
--header 'Authorization: Bearer {{your_token}}'
```

---

### 🔹 4. Update User Profile

```bash
curl --location --request PUT 'http://localhost:8080/api/auth/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {{your_token}}' \
--data-raw '{
  "username": "updated_name",
  "email": "updated_email@example.com"
}'
```

---

### 🔹 5. Create a Ticket

```bash
curl --location 'http://localhost:8080/api/tickets' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {{your_token}}' \
--data '{
  "subject": "App crash on startup",
  "description": "The app crashes every time I try to open it.",
  "priority": "HIGH"
}'
```

---

### 🔹 6. Get a Ticket by ID

```bash
curl --location 'http://localhost:8080/api/tickets/1' \
--header 'Authorization: Bearer {{your_token}}'
```

---

### 🔹 7. Upload File Attachment to a Ticket

```bash
curl --location --request POST 'http://localhost:8080/api/tickets/1/attachments' \
--header 'Authorization: Bearer {{your_token}}' \
--form 'file=@"path/to/your/file.png"'
```

---

### 🔹 8. Manual Assignment API (Admin Only)

```bash
curl --location --request POST 'http://localhost:8080/api/tickets/1/assign?agentId=2' \
--header 'Authorization: Bearer {{your_token}}'
```

---

### 🔹 9. Auto Assignment API (Admin Only)

```bash
curl --location --request POST 'http://localhost:8080/api/tickets/1/auto-assign' \
--header 'Authorization: Bearer {{your_token}}'
```

---

 🎯 **WebSocket Chat API Endpoints** :-

---

## 8. 🔌 WebSocket Chat Endpoints

> WebSocket URL:  
> `ws://localhost:8080/chat/websocket`  
> Add JWT as header: `Authorization: Bearer {{your_token}}`  
> Use [Postman WebSocket](https://learning.postman.com/docs/sending-requests/supported-api-frameworks/websocket/) or frontend clients.

---

### 🔹 1. Connect to WebSocket

```
WebSocket URL:
ws://localhost:8080/chat/websocket
```

Headers:

```json
{
  "Authorization": "Bearer {{your_token}}"
}
```

---

### 🔹 2. Subscribe to Public Chat Topic

```json
{
  "command": "subscribe",
  "headers": {
    "id": "sub-0",
    "destination": "/topic/public"
  }
}
```

✅ This subscribes you to broadcasted messages (public chat or notifications).

---

### 🔹 3. Send a Chat Message

```json
{
  "command": "send",
  "headers": {
    "destination": "/app/chat.send"
  },
  "body": "{\"sender\":\"customer@example.com\",\"receiver\":\"agent@example.com\",\"content\":\"Hello Agent!\",\"timestamp\":\"2025-03-23T21:00:00Z\",\"type\":\"CHAT\"}"
}
```

🧠 Remember: the `body` is a **JSON string inside a JSON object**.

---

### 🔹 4. Send a Join Notification

```json
{
  "command": "send",
  "headers": {
    "destination": "/app/chat.newUser"
  },
  "body": "{\"sender\":\"customer@example.com\",\"type\":\"JOIN\"}"
}
```

👥 Notifies that a user has joined the chat.

---

### 🔹 5. Receive Chat Message (example response)

```json
{
  "sender": "agent@example.com",
  "receiver": "customer@example.com",
  "content": "Hello, how can I assist?",
  "timestamp": "2025-03-23T21:00:10Z",
  "type": "CHAT"
}
```

---

###  ⏱ Time and Space Complexity Analysis 🧠


---


### 🔹 1. Register User

- **Time Complexity:** `O(1)` – inserts a user into the DB.
- **Space Complexity:** `O(1)` – uses constant memory.

---

### 🔹 2. Login User (JWT Auth)

- **Time Complexity:** `O(1)` – user lookup and JWT token generation.
- **Space Complexity:** `O(1)` – small space for token generation.

---

### 🔹 3. Get User Profile

- **Time Complexity:** `O(1)` – fetch user by email (indexed field).
- **Space Complexity:** `O(1)`

---

### 🔹 4. Update User Profile

- **Time Complexity:** `O(1)` – single record update.
- **Space Complexity:** `O(1)`

---

### 🔹 5. Create a Ticket

- **Time Complexity:** `O(1)` – inserts a new ticket with initial fields.
- **Space Complexity:** `O(1)`

---

### 🔹 6. Get Ticket by ID

- **Time Complexity:** `O(1)` – primary key lookup.
- **Space Complexity:** `O(1)`

---

### 🔹 7. Upload File Attachment to a Ticket

- **Time Complexity:**
  - File saving: `O(n)` where `n = file size`.
  - DB update: `O(1)`
- **Space Complexity:** `O(n)` – depends on file size.

---

### 🔹 8. Manual Ticket Assignment to Agent

- **Time Complexity:** `O(1)` – update ticket with agent ID.
- **Space Complexity:** `O(1)`

---

### 🔹 9. Auto Assignment to Agent

- **Time Complexity:**
  - Agent lookup: `O(k)` where `k = number of agents`.
  - Random pick + update: `O(1)`
- **Space Complexity:** `O(1)`

---

### 🔹 10. WebSocket Chat – Send Message

- **Time Complexity:** `O(1)` – publish to destination.
- **Space Complexity:** `O(1)`

---

### 🔹 11. WebSocket Chat – Subscribe to Topic

- **Time Complexity:** `O(1)` – subscription registration.
- **Space Complexity:** `O(1)`

---

### 🔹 12. WebSocket Chat – Message History Retrieval

- **Time Complexity:** `O(m)` – fetch all messages for user or pair.
- **Space Complexity:** `O(m)` – message list stored in memory.

---

### 🔹 13. Email Notifications (Ticket Created / Status Changed / Assigned)

- **Time Complexity:**
  - Template load + send email: `O(1)`
- **Space Complexity:** `O(1)`

---

## 📊 Code Coverage Reports

- ✅ 1st
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1l95Eoz2u6XCmRoQtE0nMBid1GAndU6T8/view?usp=drive_link)
- ✅ 2nd
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1swsVQzhJXqczKt8ljiBPF5z5LcL_AV-v/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/1V_ggn0gIMbjGr2tudKLUfNrC2GjLvnFi/view?usp=drive_link)


---

## 🧠 Conclusion

The **Spring Boot Support Ticket System** efficiently manages user registration, ticketing, real-time messaging, and email notifications with **constant to linear time complexity** across most endpoints. The system:

✅ **Supports scalable user interaction** with optimized indexed database queries and role-based access control.  
✅ **Ensures data integrity and low latency**, leveraging lazy loading and transactional consistency across ticket and chat workflows.  
✅ **Delivers real-time communication and notifications** with minimal overhead using **WebSocket + STOMP**, and asynchronous event-driven logic for responsiveness.  
✅ **Handles file uploads and large datasets** efficiently through streaming and controlled attachment handling.  
✅ **Provides maintainability and testability**, with modularized services, proper logging, exception handling, and JWT-based security.

With proper **database indexing**, optional **caching (e.g., Redis)**, **async email/event processing**, and a robust **microservice-friendly design**, the application architecture ensures **high performance and scalability** for production-ready, enterprise-level use cases. 🚀

---


