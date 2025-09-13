package com.sinaukoding.eventbooking.service.app.impl;

import com.sinaukoding.eventbooking.entity.event.Event;
import com.sinaukoding.eventbooking.entity.event.EventImage;
import com.sinaukoding.eventbooking.model.enums.TipeUpload;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.repository.event.EventImageRepository;
import com.sinaukoding.eventbooking.repository.event.EventRepository;
import com.sinaukoding.eventbooking.service.app.FileService;
import com.sinaukoding.eventbooking.util.DateUtil;
import com.sinaukoding.eventbooking.model.app.Checks;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${app.upload-directory}")
    private String uploadDirectory;

    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;


    @Override
    public BaseResponse<?> upload(MultipartFile file, TipeUpload tipeUpload, String eventId, boolean setPrimary) {
        Checks.isTrue(file != null && !file.isEmpty(), "File tidak boleh kosong");

        String storedFilePath = storeFile(file, tipeUpload);

        if (tipeUpload == TipeUpload.IMAGE) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));

            if (setPrimary) {
                eventImageRepository.updatePrimaryStatusForEvent(eventId, false);
            }

            EventImage eventImage = new EventImage(storedFilePath, file.getOriginalFilename(), setPrimary);
            eventImage.setEvent(event);
            eventImageRepository.save(eventImage);
        }

        return BaseResponse.ok("Upload berhasil", storedFilePath);
    }


    @Override
    public Resource loadFileAsResource(String pathFile) {
        try {
            Path fileStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(pathFile).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) throw new RuntimeException("File : " + pathFile + " tidak ditemukan");
            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File : " + pathFile + " tidak ditemukan", ex);
        }
    }
    private String storeFile(MultipartFile file, TipeUpload tipeUpload) {
        String unique = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        Checks.isTrue(StringUtils.isNotBlank(originalFilename), "Filename tidak boleh kosong");
        String fileName = unique.concat("_").concat(originalFilename);

        try {
            if (originalFilename.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + originalFilename);
            }

            // gunakan DateUtil untuk bikin path yyyy/MM/dd
            String datePath = DateUtil.formatLocalDateToString(LocalDate.now());

            Path baseDir = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path targetDir = baseDir.resolve(tipeUpload.name().toLowerCase()).resolve(datePath);

            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // return path relatif
            return tipeUpload.name().toLowerCase()
                    .concat("/")
                    .concat(datePath)
                    .concat("/")
                    .concat(fileName);

        } catch (IOException ex) {
            throw new RuntimeException("Gagal upload file : " + fileName + ". Silahkan dicoba lagi", ex);
        }
    }
    @Override
    public BaseResponse<?> delete(String pathFile) {
        try {
            Path fileStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(pathFile).normalize();

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File: " + pathFile + " tidak ditemukan");
            }

            Files.delete(filePath);
            return BaseResponse.ok("File: " + pathFile + " berhasil dihapus", null);
        } catch (Exception ex) {
            throw new RuntimeException("Gagal menghapus file: " + pathFile, ex);
        }
    }


}