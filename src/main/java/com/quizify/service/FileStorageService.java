package com.quizify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}
