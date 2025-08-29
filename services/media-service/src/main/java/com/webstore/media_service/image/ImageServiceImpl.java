package com.webstore.media_service.image;

import com.webstore.media_service.exceptions.ImageException;
import com.webstore.media_service.image.dto.ImageResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${media.image.storage-path}")
    private String storagePath;

    private final ImageRepository imageRepo;

    @Autowired
    ImageServiceImpl(ImageRepository imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Override
    @Transactional
    public String upload(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new ImageException("File is empty");
            }

            String fileType = file.getContentType();

            if (fileType != null && !file.getContentType().startsWith("image/")) {
                throw new ImageException("File is not an image");
            }

            Image image = Image.builder()
                    .size(file.getSize())
                    .mimeType(fileType)
                    .created(Instant.now())
                    .build();

            Image savedImage = imageRepo.save(image);

            Path path = Paths.get(storagePath, savedImage.getId());

            InputStream inputStream = file.getInputStream();

            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            return savedImage.getId();

        } catch (IOException ex) {

            throw new ImageException("Failed to upload image", ex);

        }
    }

    @Override
    public ImageResponse findById(String imageId) {

        try {

            Image image = imageRepo.findById(imageId).orElseThrow(
                    () -> new ImageException("Image not found")
            );

            Path path = Paths.get(storagePath, imageId);

            if (!Files.exists(path)) {
                throw new ImageException("Image file doesn't exist");
            }

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ImageException("Image file could not be read");
            }

            return new ImageResponse(
                    image.getId(),
                    resource,
                    image.getSize(),
                    image.getMimeType()
            );

        } catch (Exception ex) {
            throw new ImageException("Failed to find image", ex);
        }
    }

    @Override
    public void deleteById(String imageId) {
        try {

            Image image = imageRepo.findById(imageId).orElseThrow(
                    () -> new ImageException("Image not found")
            );

            Path path = Paths.get(storagePath, imageId);

            if (!Files.exists(path)) {
                throw new ImageException("Image file doesn't exist");
            }

            imageRepo.delete(image);

            Files.delete(path);

        } catch (Exception ex) {
            throw new ImageException("Failed to delete image", ex);
        }
    }
}
