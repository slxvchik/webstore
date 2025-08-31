package com.webstore.media_service.image;

import com.webstore.media_service.image.dto.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public interface ImageService {
    String upload(MultipartFile file);
    List<String> upload(List<MultipartFile> file);
    ImageResponse getImageResponseById(String imageId);
    void deleteById(String imageId);

    void deleteByIds(List<String> imageIds);
}
