package com.esm.epam.mapper;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.esm.epam.util.ParameterAttribute.IMAGES_FOLDER_PATH;
import static com.esm.epam.util.ParameterAttribute.SLASH;

public abstract class AbstractMapper {
    public void uploadImage(MultipartFile file, String name, String fileName, String folderName) throws IOException {
        String uploadDir = IMAGES_FOLDER_PATH + folderName + SLASH + name;
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = file.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
