package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.service.FileService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final String UPLOAD_ROOT = "uploads"; // корневая директория, где храним файлы

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) {
        // filePath, например: "/api/uploads/1/videos/c5763f8b-d149-4b6a-a776-30022b969b43_temp_1735752242052"

        try {

            // 2) Формируем реальный путь: uploads/ + <relativePath>
            Path fullFilePath = Paths.get(UPLOAD_ROOT, filePath);

            System.out.println("Путь: " + fullFilePath);

            if (!Files.exists(fullFilePath) || !Files.isReadable(fullFilePath)) {
                return ResponseEntity.notFound().build();
            }

            // 3) Создаём Resource
            Resource resource = new UrlResource(fullFilePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 4) Определяем MIME-тип
            String mimeType = "application/octet-stream";
            try {
                String detectedType = Files.probeContentType(fullFilePath);
                if (detectedType != null) {
                    mimeType = detectedType;
                }
            } catch (IOException ex) {
                System.err.println("Не удалось определить MIME-тип: " + ex.getMessage());
            }

            // 5) Формируем имя файла (если хотим, можно вырезать из filePath)
            String fileName = fullFilePath.getFileName().toString();

            // 6) Возвращаем
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    // Если хотим скачивание:
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}