package net.maslyna.file.controller;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.model.FileType;
import net.maslyna.file.mapper.FileMapper;
import net.maslyna.file.response.FileStatus;
import net.maslyna.file.service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<String> save(
            @RequestHeader("userId") Long userId,
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaService.save(contentId, userId, type, file));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<String> getLink(
            @PathVariable("contentId") UUID contentId
    ) {
        return ResponseEntity.ok(mediaService.getLink(contentId));
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<FileStatus> delete(
            @PathVariable("contentId") UUID contentId,
            @RequestHeader("userId") Long userId
    ) {
        return ResponseEntity.ok(mediaService.remove(userId, contentId));
    }
}
