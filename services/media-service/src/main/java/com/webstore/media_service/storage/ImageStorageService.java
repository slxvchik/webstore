package com.webstore.media_service.storage;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public interface ImageStorageService {
    void store(InputStream inputStream, String filename) throws IOException;
    Resource load(String filename) throws IOException;
    void delete(String filename) throws IOException;
}
