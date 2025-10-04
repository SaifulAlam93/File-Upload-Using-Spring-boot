//package com.file.upload.saiful.service;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Service
//public class FileValidationService {
//
//    @Value("${file.storage.max-file-size:52428800}")
//    private long maxFileSize;
//
//    @Value("${file.storage.allowed-content-types}")
//    private List<String> allowedContentTypes;
//
//    public void validateFile(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            throw new RuntimeException("File is empty");
//        }
//
//        if (file.getSize() > maxFileSize) {
//            throw new RuntimeException("File size exceeds limit");
//        }
//
//        if (!allowedContentTypes.contains(file.getContentType())) {
//            throw new RuntimeException("File type not allowed");
//        }
//
//        String fileName = file.getOriginalFilename();
//        if (fileName != null && fileName.contains("..")) {
//            throw new RuntimeException("Invalid file name");
//        }
//    }
//}