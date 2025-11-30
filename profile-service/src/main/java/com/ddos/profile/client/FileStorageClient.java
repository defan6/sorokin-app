package com.ddos.profile.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class FileStorageClient {

    @Value("${file-storage.url}")
    private String fileStorageUrl;

    private final RestTemplate restTemplate;


    public String uploadFile(MultipartFile file, Long userId) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try{
            body.add("file", new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e){
            throw new RuntimeException("Error reading dile bytes", e);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("X-User-Id", String.valueOf(userId));

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(fileStorageUrl + "/files/upload/profile/photo", requestEntity, String.class);


        return response.getBody();
    }
}
