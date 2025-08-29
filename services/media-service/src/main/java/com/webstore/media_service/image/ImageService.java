package com.webstore.media_service.image;

import com.webstore.media_service.image.dto.ImageResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface ImageService {
    String upload(MultipartFile file);
    ImageResponse findById(String imageId);
    void deleteById(String imageId);
}
