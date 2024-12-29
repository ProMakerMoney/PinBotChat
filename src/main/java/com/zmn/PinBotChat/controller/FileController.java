package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.service.FileService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // Получаем ресурс файла
            Resource fileResource = fileService.getFileAsResource(fileName);

            // Определяем MIME-тип файла
            String mimeType = "application/octet-stream";
            if (fileResource.exists() && fileResource.isReadable()) {
                try {
                    mimeType = Files.probeContentType(Paths.get(fileResource.getURI()));
                } catch (IOException ex) {
                    // Логируем, если определить MIME-тип не удалось
                    System.err.println("Could not determine file type: " + ex.getMessage());
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileResource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
