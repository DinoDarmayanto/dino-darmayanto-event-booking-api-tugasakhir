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
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${app.upload-directory}")
    private String uploadDirectory;

    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;


    @Override
    public BaseResponse<?> upload(MultipartFile file, TipeUpload tipeUpload, String eventId, boolean setPrimary) {
        log.info("Mulai upload file {} untuk eventId {} dengan tipe {}",
                file.getOriginalFilename(), eventId, tipeUpload);
        Checks.isTrue(file != null && !file.isEmpty(), "File tidak boleh kosong");

        String storedFilePath = storeFile(file, tipeUpload);
        log.info("File disimpan di path {}", storedFilePath);

        if (tipeUpload == TipeUpload.IMAGE) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> {
                        log.warn("Event dengan id {} tidak ditemukan", eventId);
                        return new RuntimeException("Event tidak ditemukan");
                    });


            if (setPrimary) {
                log.info("Menandai file {} sebagai primary image untuk event {}", storedFilePath, eventId);
                eventImageRepository.updatePrimaryStatusForEvent(eventId, false);
            }

            EventImage eventImage = new EventImage(storedFilePath, file.getOriginalFilename(), setPrimary);
            eventImage.setEvent(event);
            eventImageRepository.save(eventImage);
            log.info("EventImage berhasil disimpan untuk event {}", eventId);
        }

        return BaseResponse.ok("Upload berhasil", storedFilePath);
    }


    @Override
    public Resource loadFileAsResource(String pathFile) {
        log.info("Memuat file sebagai resource: {}", pathFile);
        try {
            Path fileStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(pathFile).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                log.warn("File {} tidak ditemukan", pathFile);
                throw new RuntimeException("File: " + pathFile + " tidak ditemukan");
            }
            log.info("File {} berhasil dimuat sebagai resource", pathFile);
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
            log.info("Menyimpan file {} sebagai {}", originalFilename, fileName);

            Path baseDir = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path targetDir = baseDir.resolve(tipeUpload.name().toLowerCase()).resolve(datePath);

            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // return path relatif
            String relativePath = tipeUpload.name().toLowerCase()
                    .concat("/").concat(datePath)
                    .concat("/").concat(fileName);
            log.info("File berhasil disimpan dengan path relatif {}", relativePath);
            return relativePath; //

        }  catch (IOException ex) {
            log.error("Gagal upload file {}", fileName, ex);
            throw new RuntimeException("Gagal upload file: " + fileName + ". Silahkan dicoba lagi", ex);
        }
    }


    @Override
    public BaseResponse<?> delete(String pathFile) {
        log.info("Menghapus file {}", pathFile);
        try {
            Path fileStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(pathFile).normalize();

            if (!Files.exists(filePath)) {
                log.warn("File {} tidak ditemukan saat delete", pathFile);
                throw new RuntimeException("File: " + pathFile + " tidak ditemukan");
            }

            Files.delete(filePath);
            return BaseResponse.ok("File: " + pathFile + " berhasil dihapus", null);
        } catch (Exception ex) {
            log.error("Gagal menghapus file {}", pathFile, ex);
            throw new RuntimeException("Gagal menghapus file: " + pathFile, ex);
        }
    }


}