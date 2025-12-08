package my.ddos.filestorage.controller;

import lombok.RequiredArgsConstructor;
import my.ddos.filestorage.dto.UploadPhotoResponse;
import my.ddos.filestorage.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {


    private final FileStorageService fileStorageService;


    @PostMapping("/upload/profile/photo")
    public ResponseEntity<UploadPhotoResponse> uploadProfilePhoto(@RequestParam("file") MultipartFile file,
                                                                  @RequestHeader("X-User-Id") String userId) {
        if(file.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fileStorageService.uploadFile(file, userId));
    }


    @DeleteMapping("/delete/profile/photo")
    public ResponseEntity<Void> deleteProfilePhoto(@RequestHeader("X-User-Id") String userId,
                                                   @RequestParam("photoUrl") String photoUrl){
        fileStorageService.deleteFile(userId, photoUrl);
        return ResponseEntity.noContent().build();

    }
}
