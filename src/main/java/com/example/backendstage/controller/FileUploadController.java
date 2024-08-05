package com.example.backendstage.controller;

import com.example.backendstage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "/api/v1/files/uploads/" + fileName;
            response.put("message", "File uploaded successfully");
            response.put("fileName", fileUrl);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "File upload failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<?> getFile(@PathVariable String fileName) {
        try {
            Path file = fileStorageService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file))
                    .body(Files.readAllBytes(file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }
}
