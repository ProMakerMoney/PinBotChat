package com.zmn.PinBotChat.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String fileStorageLocation = "C:\\Users\\PinBot\\IdeaProjectsGIGA\\PinBotChat\\uploads"; // Укажите путь к каталогу

    public Resource getFileAsResource(String fileName) throws IOException, FileNotFoundException {
        Path filePath = Paths.get(fileStorageLocation).resolve(fileName).normalize();

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        return new UrlResource(filePath.toUri());
    }
}
