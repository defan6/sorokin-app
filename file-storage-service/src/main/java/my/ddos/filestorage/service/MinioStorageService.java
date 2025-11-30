package my.ddos.filestorage.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import my.ddos.filestorage.dto.UploadPhotoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Override
    public UploadPhotoResponse uploadFile(MultipartFile file, String userId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String objectName = generateFileName(userId, originalFilename);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return new UploadPhotoResponse(String.format("%s/%s/%s", minioEndpoint, bucketName, objectName));
        } catch (Exception e){
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }


    private String generateFileName(String userId, String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if(dotIndex > 0){
            fileExtension = originalFilename.substring(dotIndex);
        }
        return String.format("%s_%s_%s", userId, uuid, fileExtension);
    }
}
