package com.webstore.product_service.media;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaClient {

    @Value("${application.config.media-url}")
    private String mediaUrl;
    private final RestTemplate restTemplate;

    public String uploadImage(MultipartFile multipartFile) {

        ResponseEntity<String> response = restTemplate.postForEntity(mediaUrl, multipartFile, String.class);

        if (response.getStatusCode().isError()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("Failed upload image. Status: %s, Response: %s", response.getStatusCode(), response.getBody()));
        }

        return response.getBody();
    }

    public void deleteImage(String imageId) {

        ResponseEntity<String> response = restTemplate.exchange(
                mediaUrl + "/" + imageId,
                HttpMethod.DELETE,
                null,
                String.class
        );

        if (response.getStatusCode().isError()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Failed upload image. Status: %s, Response: %s", response.getStatusCode(), response.getBody())
            );
        }
    }

    public List<String> uploadImages(List<MultipartFile> gallery) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            for (MultipartFile file : gallery) {
                body.add("files", file.getInputStream());
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<List<String>> images = restTemplate.exchange(
                    mediaUrl + "/batch",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            if (images.getStatusCode().isError()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Failed to upload images. Status: %s, Response: %s",
                            images.getStatusCode(),
                            images.getBody()
                    )
                );
            }

            return images.getBody();

        } catch (IOException ex) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Error upload images. Response body: " + ex.getMessage());
        }
    }

    public void deleteImages(List<String> galleryImageIds) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(galleryImageIds, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                mediaUrl + "/batch",
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().isError()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Failed upload image. Status: %s, Response: %s", response.getStatusCode(), response.getBody())
            );
        }
    }
}
