package com.webstore.media_service.image;

import com.webstore.media_service.exceptions.ImageException;
import com.webstore.media_service.exceptions.ImageNotFoundException;
import com.webstore.media_service.exceptions.ImageValidationException;
import com.webstore.media_service.image.dto.ImageResponse;
import com.webstore.media_service.storage.ImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageStorageService imageStorageService;
    private final ImageRepository imageRepo;


    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @Override
    public String upload(MultipartFile file) {

        validateImageFile(file);

        Image image = Image.builder()
                .size(file.getSize())
                .mimeType(file.getContentType())
                .created(Instant.now())
                .build();

        Image savedImage = imageRepo.save(image);

        try {

            imageStorageService.store(file.getInputStream(), savedImage.getId());

        } catch (IOException ex) {

            log.atError().log("Could not store image");
            throw new ImageException("Failed to store image file: " + savedImage.getId(), ex);

        }

        return image.getId();
    }

    @Override
    public List<String> upload(List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new ImageException("Files array is empty");
        }

        List<String> uploadedIds = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadedIds.add(upload(file));
        }

        return uploadedIds;
    }

    @Override
    public ImageResponse getImageResponseById(String imageId) {

        try {

            Image image = getImageById(imageId);

            Resource resource = imageStorageService.load(imageId);

            return new ImageResponse(
                    image.getId(),
                    resource,
                    image.getSize(),
                    image.getMimeType()
            );

        } catch (IOException ex) {
            throw new ImageNotFoundException("Failed to find image", ex);
        }
    }

    @Override
    public void deleteById(String imageId) {

        Image image = getImageById(imageId);

        deleteImageWithFile(image);
    }

    @Override
    public void deleteByIds(List<String> imageIds) {

        List<Image> images = imageRepo.findByIdIn(imageIds);

        if ((long) images.size() != imageIds.size()) {

            List<String> missingImageIds = images.stream()
                    .filter(imageId -> !images.contains(imageId))
                    .map(Image::getId)
                    .toList();

            throw new ImageNotFoundException(String.format("Image ids does not match. Missing ids in database: %s", missingImageIds));
        }

        images.forEach(this::deleteImageWithFile);
    }

    private void deleteImageWithFile(Image image) {
        try {

            imageStorageService.delete(image.getId());

            imageRepo.delete(image);

        } catch (IOException e) {
            log.error("Failed to delete image file: {}", image.getId(), e);
            throw new ImageException("Failed to delete image file", e);
        }
    }

    private Image getImageById(String imageId) {
        return imageRepo.findById(imageId).orElseThrow(
                () -> new ImageNotFoundException("Image not found")
        );
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageValidationException("File cannot be empty");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new ImageValidationException("File must be an image");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageValidationException("File size exceeds limit");
        }
    }
}
