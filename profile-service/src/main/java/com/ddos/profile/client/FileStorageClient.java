package com.ddos.profile.client;

import com.ddos.profile.dto.UploadFileResponse;
import com.ddos.profile.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class FileStorageClient {

    private final RestClient restClient;

    public FileStorageClient(@Value("${file-storage.url}") String fileStorageUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(fileStorageUrl)
                .build();
    }

    public UploadFileResponse uploadFile(MultipartFile file, Long userId) {
        log.info("Uploading profile photo for user ID: {}", userId);

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            body.add("file", fileResource);

            return restClient
                    .post()
                    .uri("/api/files/upload/profile/photo")
                    .header("X-User-Id", userId.toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)  // <-- ВАЖНО: MultipartBody = MultiValueMap<String, Object>
                    .retrieve()
                    .body(UploadFileResponse.class);

        } catch (IOException e) {
            throw new FileUploadException("Error reading file bytes", e);
        }
    }


    public void deleteProfilePhoto(Long userId, String photoUrl) {
        log.info("Deleting profile photo for user ID: {}", userId);
        try {
            restClient.delete()
                    .uri("/api/files/delete/profile/photo?photoUrl=" + photoUrl)
                    .header("X-User-Id", String.valueOf(userId))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully deleted profile photo for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error deleting profile photo for user ID: {}", userId, e);
            throw new RuntimeException("Error during file deletion", e); // или кастомное исключение
        }
    }
}
