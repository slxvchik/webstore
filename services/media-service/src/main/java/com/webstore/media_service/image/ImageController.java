package com.webstore.media_service.image;

import com.webstore.media_service.image.dto.ImageResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        ImageResponse image = imageService.findById(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.mimeType()))
                .contentLength(image.size())
                .body(image.content());
    }

}
