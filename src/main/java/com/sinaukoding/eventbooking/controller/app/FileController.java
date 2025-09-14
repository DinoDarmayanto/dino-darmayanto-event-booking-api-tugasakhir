package com.sinaukoding.eventbooking.controller.app;

import com.sinaukoding.eventbooking.model.enums.TipeUpload;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.service.app.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "FILE API")
public class FileController {

    private final FileService fileService;

    @PostMapping(path = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<?> uploadFile(@RequestPart MultipartFile file,
                                      @RequestParam TipeUpload tipeUpload,
                                      @RequestParam(required = false) String eventId,
                                      @RequestParam(defaultValue = "false") boolean setPrimary) {
        return fileService.upload(file, tipeUpload, eventId, setPrimary);
    }


    @GetMapping("view")
    public void viewFile(@RequestParam String pathFile, HttpServletResponse response) throws IOException {
        Resource resource = fileService.loadFileAsResource(pathFile);
        IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    @PostMapping("delete")
    public BaseResponse<?> deleteFile(@RequestParam String pathFile) {
        return fileService.delete(pathFile);
    }

}
