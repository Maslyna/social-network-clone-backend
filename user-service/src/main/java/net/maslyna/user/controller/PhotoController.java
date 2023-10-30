package net.maslyna.user.controller;

import lombok.RequiredArgsConstructor;
import net.maslyna.user.client.FileStatus;
import net.maslyna.user.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping("/edit/photo")
    @ResponseStatus(CREATED)
    public void setUserPhoto(
            @RequestHeader("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        photoService.setProfilePhoto(userId, file);
    }

    @PostMapping("/photo")
    @ResponseStatus(CREATED)
    public void addPhoto(
            @RequestHeader("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        photoService.addPhoto(userId, file);
    }

    @DeleteMapping("/photo/{photoId}")
    public ResponseEntity<FileStatus> removePhoto(
            @RequestHeader("userId") Long userId,
            @PathVariable("photoId") UUID photoId
    ) {
        return photoService.removePhoto(userId, photoId);
    }
}
