package my.ddos.filestorage.service;

import my.ddos.filestorage.dto.UploadPhotoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    UploadPhotoResponse uploadFile(MultipartFile file, String userId);

    void deleteFile(String userId, String photoUrl);
}
