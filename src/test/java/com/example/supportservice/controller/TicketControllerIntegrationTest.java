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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // ✅ Auto rollback
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;

    private static Long createdTicketId;

    @Order(1)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateTicket() throws Exception {
        TicketRequest request = new TicketRequest();
        request.setSubject("Login Issue");
//        request.setAssignedTo(2L);
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
