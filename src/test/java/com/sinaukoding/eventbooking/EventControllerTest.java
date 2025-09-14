package com.sinaukoding.eventbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinaukoding.eventbooking.controller.event.EventController;
import com.sinaukoding.eventbooking.model.request.EventRequestRecord;
import com.sinaukoding.eventbooking.service.event.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void saveEventTest() throws Exception {
        EventRequestRecord request = new EventRequestRecord(
                "1",
                "Workshop DevOps",
                "Belajar CI/CD, Docker, dan Kubernetes",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                80,
                new BigDecimal("200000.00"),
                "Bandung",
                true,
                List.of("Workshop"),
                List.of("DevOps"),
                "447906bc-8693-4cad-b565-872b33a50d7b"
        );

        doNothing().when(eventService).add(any(EventRequestRecord.class));

        mockMvc.perform(post("/event/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event berhasil disimpan"));
    }
}
