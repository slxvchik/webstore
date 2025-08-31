package com.webstore.media_service.storage;

import com.webstore.media_service.exceptions.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class LocalImageStorageService implements ImageStorageService {
    @Value("${media.image.storage-path}")
    private String storagePath;

    @Override
    public void store(InputStream inputStream, String filename) throws IOException {

        Path path = Paths.get(storagePath, filename);

        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public Resource load(String filename) throws IOException {

        Path filePath = Paths.get(storagePath, filename);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filename);
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.isReadable()) {
            throw new IOException("File is not readable: " + filename);
        }

        return resource;
    }

    @Override
    public void delete(String filename) throws IOException {

        Path path = Paths.get(storagePath, filename);

        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }

        Files.delete(path);
    }

}
