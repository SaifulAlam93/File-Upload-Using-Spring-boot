package com.file.upload.saiful.controller;


import com.file.upload.saiful.entity.FileEntityEx2;
import com.file.upload.saiful.service.FileServiceEx2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/filesEx2")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class FileControllerEx2 {

    private final FileServiceEx2 fileServiceEx2;

    public FileControllerEx2(FileServiceEx2 fileServiceEx2) {
        this.fileServiceEx2 = fileServiceEx2;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileEntityEx2> uploadFile(@RequestParam("file") MultipartFile file) {
        FileEntityEx2 saved = fileServiceEx2.saveFile(file);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] data = fileServiceEx2.downloadFile(id);
        FileEntityEx2 file = fileServiceEx2.getAllFiles().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(data);
    }

    @GetMapping
    public ResponseEntity<List<FileEntityEx2>> listFiles() {
        return ResponseEntity.ok(fileServiceEx2.getAllFiles());
    }
}
