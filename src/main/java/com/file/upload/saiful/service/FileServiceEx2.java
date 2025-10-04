package com.file.upload.saiful.service;


import com.file.upload.saiful.config.FileNotFoundException;
import com.file.upload.saiful.config.FileStorageException;
import com.file.upload.saiful.entity.FileEntityEx2;
import com.file.upload.saiful.respository.FileRepositoryEx2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceEx2 {

    private final FileRepositoryEx2 fileRepositoryEx2;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileServiceEx2(FileRepositoryEx2 fileRepositoryEx2) {
        this.fileRepositoryEx2 = fileRepositoryEx2;
    }

    public FileEntityEx2 saveFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Use UUID to avoid collisions
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            FileEntityEx2 fileEntityEx2 = FileEntityEx2.builder()
                    .fileName(filename)
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .createdAt(LocalDateTime.now())
                    .build();

            return fileRepositoryEx2.save(fileEntityEx2);

        } catch (IOException e) {
            throw new FileStorageException("Could not store file. Error: " + e.getMessage(), e);
        }
    }

    public byte[] downloadFile(Long id) {
        FileEntityEx2 fileEntityEx2 = fileRepositoryEx2.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + id));
        try {
            return Files.readAllBytes(Paths.get(fileEntityEx2.getFilePath()));
        } catch (IOException e) {
            throw new FileStorageException("Error reading file. Error: " + e.getMessage(), e);
        }
    }

    public List<FileEntityEx2> getAllFiles() {
        return fileRepositoryEx2.findAll();
    }
}
