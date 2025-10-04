//package com.file.upload.saiful.service;
//
//
//import com.file.upload.saiful.entity.FileMetadata;
//import com.file.upload.saiful.respository.FileMetadataRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class FileService {
//
//    private final FileMetadataRepository fileMetadataRepository;
//    private final FileValidationService fileValidationService;
//
//    public FileMetadata uploadFile(MultipartFile file) throws IOException {
//        // Validate file
//        fileValidationService.validateFile(file);
//
//        // Generate unique filename
//        String originalFileName = file.getOriginalFilename();
//        String fileExtension = getFileExtension(originalFileName);
//        String storageFileName = UUID.randomUUID().toString() + "." + fileExtension;
//
//        // Create upload directory if not exists
//        Path uploadPath = Paths.get("./uploads");
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        // Store file
//        Path targetLocation = uploadPath.resolve(storageFileName);
//        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//        // Create file metadata
//        FileMetadata fileMetadata = new FileMetadata();
//        fileMetadata.setFileName(storageFileName);
//        fileMetadata.setOriginalFileName(originalFileName);
//        fileMetadata.setFileType(file.getContentType());
//        fileMetadata.setFileSize(file.getSize());
//        fileMetadata.setFilePath(targetLocation.toString());
//        fileMetadata.setFileHash(calculateFileHash(file));
//        fileMetadata.setUploadId(UUID.randomUUID().toString());
//        fileMetadata.setIsActive(true);
//
//        return fileMetadataRepository.save(fileMetadata);
//    }
//
//    public byte[] downloadFile(String uploadId) throws IOException {
//        FileMetadata fileMetadata = fileMetadataRepository.findByUploadId(uploadId)
//                .orElseThrow(() -> new RuntimeException("File not found"));
//
//        Path filePath = Paths.get(fileMetadata.getFilePath());
//        return Files.readAllBytes(filePath);
//    }
//
//    public FileMetadata getFileInfo(String uploadId) {
//        return fileMetadataRepository.findByUploadId(uploadId)
//                .orElseThrow(() -> new RuntimeException("File not found"));
//    }
//
//    public void deleteFile(String uploadId) throws IOException {
//        FileMetadata fileMetadata = fileMetadataRepository.findByUploadId(uploadId)
//                .orElseThrow(() -> new RuntimeException("File not found"));
//
//        // Delete physical file
//        Files.deleteIfExists(Paths.get(fileMetadata.getFilePath()));
//
//        // Delete database record
//        fileMetadataRepository.delete(fileMetadata);
//    }
//
//    private String getFileExtension(String fileName) {
//        if (fileName == null || !fileName.contains(".")) {
//            return "";
//        }
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
//
//    private String calculateFileHash(MultipartFile file) throws IOException {
//        // Simple hash based on filename and size (for demo)
//        // In production, use proper hashing like SHA-256
//        return file.getOriginalFilename() + "_" + file.getSize();
//    }
//}