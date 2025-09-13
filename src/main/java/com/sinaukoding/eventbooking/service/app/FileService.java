package com.sinaukoding.eventbooking.service.app;

import com.sinaukoding.eventbooking.model.enums.TipeUpload;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    // Upload file ke event tertentu
    BaseResponse<?> upload(MultipartFile file, TipeUpload tipeUpload, String eventId, boolean setPrimary);

    // Load file dari path
    Resource loadFileAsResource(String pathFile);

    BaseResponse<?> delete(String pathFile);
}
