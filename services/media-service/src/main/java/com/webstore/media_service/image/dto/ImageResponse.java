package com.webstore.media_service.image.dto;

import org.springframework.core.io.Resource;


public record ImageResponse(
        String id,
        Resource content,
        Long size,
        String mimeType
) {
}
