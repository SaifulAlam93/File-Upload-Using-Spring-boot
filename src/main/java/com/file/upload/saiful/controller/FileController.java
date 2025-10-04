//package com.file.upload.saiful.controller;
//
//
//
//import com.file.upload.saiful.entity.FileMetadata;
//import com.file.upload.saiful.service.FileService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/files")
//@RequiredArgsConstructor
//@CrossOrigin("*")
//public class FileController {
//
//    private final FileService fileService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            FileMetadata fileMetadata = fileService.uploadFile(file);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "File uploaded successfully");
//            response.put("uploadId", fileMetadata.getUploadId());
//            response.put("fileName", fileMetadata.getOriginalFileName());
//            response.put("fileSize", fileMetadata.getFileSize());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Upload failed: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    @GetMapping("/download/{uploadId}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable String uploadId) {
//        try {
//            FileMetadata fileMetadata = fileService.getFileInfo(uploadId);
//            byte[] fileContent = fileService.downloadFile(uploadId);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(fileMetadata.getFileType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + fileMetadata.getOriginalFileName() + "\"")
//                    .body(fileContent);
//
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/info/{uploadId}")
//    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable String uploadId) {
//        try {
//            FileMetadata fileMetadata = fileService.getFileInfo(uploadId);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("file", fileMetadata);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "File not found");
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{uploadId}")
//    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String uploadId) {
//        try {
//            fileService.deleteFile(uploadId);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "File deleted successfully");
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Delete failed: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//}