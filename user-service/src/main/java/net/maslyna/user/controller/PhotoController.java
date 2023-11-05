package net.maslyna.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.user.client.response.FileStatus;
import net.maslyna.user.mapper.PhotoMapper;
import net.maslyna.user.model.dto.response.PhotoResponse;
import net.maslyna.user.service.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final PhotoMapper photoMapper;

    @Operation(summary = "set new user photo")
    @PostMapping("/edit/photo")
    @ResponseStatus(CREATED)
    public void setUserPhoto(
            @RequestHeader("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        photoService.setProfilePhoto(userId, file);
    }

    @Operation(summary = "add new photo on user account")
    @PostMapping("/photo")
    public ResponseEntity<UUID> addPhoto(
            @RequestHeader("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(CREATED).body(photoService.addPhoto(userId, file));
    }

    @Operation(summary = "delete a photo from user account")
    @DeleteMapping("/photo/{photoId}")
    public ResponseEntity<FileStatus> removePhoto(
            @RequestHeader("userId") Long userId,
            @PathVariable("photoId") UUID photoId
    ) {
        return photoService.removePhoto(userId, photoId);
    }

    @Operation(summary = "get user photos")
    @GetMapping("/{userId}/photo")
    public ResponseEntity<Page<PhotoResponse>> getUserPhotos(
            @PathVariable("userId") Long userId,
            @RequestHeader("userId") Long authUserId,
            @RequestHeader(name = "userId") Long authenticatedUserId,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                photoService.getUserPhoto(
                        userId,
                        authUserId,
                        PageRequest.of(pageNum, pageSize, Sort.Direction.fromString(order), sortBy)
                ).map(photoMapper::photoToPhotoResponse)
        );
    }
}
