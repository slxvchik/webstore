package com.webstore.media_service.image;

import com.webstore.media_service.image.dto.ImageResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @PostMapping
    public ResponseEntity<String> createImage(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(imageService.upload(file));
    }

    @GetMapping("/{image-id}")
    public ResponseEntity<Resource> getImages(
            @PathVariable("image-id") String imageId
    ) {

        ImageResponse image = imageService.getImageResponseById(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.mimeType()))
                .contentLength(image.size())
                .body(image.content());
    }

    @DeleteMapping("/{image-id}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable("image-id") String imageId
    ) {
        imageService.deleteById(imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<String>> batchImages(
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.upload(files));
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> batchDeleteImages(
            @RequestParam("imageIds") List<String> imageIds
    ) {
        imageService.deleteByIds(imageIds);
        return ResponseEntity.ok().build();
    }

}
