package com.sinaukoding.eventbooking;

import com.sinaukoding.eventbooking.controller.app.FileController;
import com.sinaukoding.eventbooking.model.enums.TipeUpload;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.service.app.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    // Helper ServletOutputStream untuk streaming test
    private ServletOutputStream createServletOutputStream(ByteArrayOutputStream outputStream) {
        return new ServletOutputStream() {
            @Override
            public void write(int b) {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        };
    }

    @Test
    void uploadFileTest() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy image content".getBytes()
        );

        BaseResponse<String> response = BaseResponse.ok("File uploaded", "test-image.jpg");

        // pakai any() + cast supaya Mockito nggak error
        when(fileService.upload(any(), eq(TipeUpload.IMAGE), any(), anyBoolean()))
                .thenAnswer(invocation -> response); // pakai thenAnswer, lebih fleksibel

        mockMvc.perform(multipart("/file/upload")
                        .file(mockFile)
                        .param("tipeUpload", TipeUpload.IMAGE.name())
                        .param("setPrimary", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File uploaded"))
                .andExpect(jsonPath("$.data").value("test-image.jpg"));
    }

    @Test
    void deleteFileTest() throws Exception {
        BaseResponse<String> response = BaseResponse.ok("File deleted", "test-image.jpg");

        when(fileService.delete(anyString()))
                .thenAnswer(invocation -> response); // pakai thenAnswer

        mockMvc.perform(post("/file/delete")
                        .param("pathFile", "test-image.jpg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File deleted"))
                .andExpect(jsonPath("$.data").value("test-image.jpg"));
    }

    @Test
    void viewFileTest() throws Exception {
        byte[] content = "Hello World".getBytes();
        Resource resource = new ByteArrayResource(content);
        when(fileService.loadFileAsResource(anyString())).thenReturn(resource);

        HttpServletResponse response = org.mockito.Mockito.mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(createServletOutputStream(outputStream));

        // langsung panggil controller
        fileController.viewFile("test-image.jpg", response);

        assertThat(outputStream.toString()).isEqualTo("Hello World");
    }

}
