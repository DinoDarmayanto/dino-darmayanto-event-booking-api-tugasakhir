package com.sinaukoding.eventbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinaukoding.eventbooking.controller.managementuser.UserController;
import com.sinaukoding.eventbooking.model.enums.Status;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import com.sinaukoding.eventbooking.service.managementuser.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void saveUserTest() throws Exception {
        UserRequestRecord request = new UserRequestRecord(
                "1",
                "dino123",
                "dinodarmayanto22@yopmail.com",
                "secret123",
                "Dino",
                "Darmayanto",
                "08123456789",
                Status.AKTIF,
                "ADMIN"
        );

        // Gunakan any() untuk menghindari UnnecessaryStubbingException
        doNothing().when(userService).add(any(UserRequestRecord.class));

        mockMvc.perform(post("/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Data berhasil disimpan"));
    }
}