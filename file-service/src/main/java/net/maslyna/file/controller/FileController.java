package net.maslyna.file.controller;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.model.FileType;
import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.response.FileStatus;
import net.maslyna.file.service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final MediaService mediaService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> save(
            @PathVariable("userId") Long userId,
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                mediaService.save(file, userId, contentId, type)
        );
    }

    @GetMapping("/link")
    public ResponseEntity<List<String>> getLinks(
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type
    ) {
        return ResponseEntity.ok(
                mediaService.getFiles(contentId, type)
                        .stream()
                        .map(mediaService::getLink)
                        .toList()
        );
    }

    @GetMapping
    public ResponseEntity<Map<UUID, String>> getFiles(
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type
    ) {
        return ResponseEntity.ok(
                mediaService.getFiles(contentId, type)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        FileEntity::getId,
                                        mediaService::getLink
                                )
                        )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getUserFiles(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(
                mediaService.getUserFiles(userId)
                        .stream()
                        .map(mediaService::getLink)
                        .toList()
        );
    }

    @DeleteMapping
    @ResponseStatus(OK)
    private void delete(
            @RequestParam("fileId") UUID fileId
    ) {
        mediaService.remove(fileId);
    }

}
