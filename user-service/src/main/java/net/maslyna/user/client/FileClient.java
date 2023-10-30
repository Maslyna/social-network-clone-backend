package net.maslyna.user.client;

import net.maslyna.common.model.FileType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient("file-service")
public interface FileClient {

    @PostMapping("/api/v1/file")
    ResponseEntity<String> save(
            @RequestHeader("userId") Long userId,
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type,
            @RequestParam("file") MultipartFile file
    );

    @DeleteMapping("/api/v1/file/{contentId}")
    ResponseEntity<FileStatus> delete(
            @PathVariable("contentId") UUID contentId,
            @RequestHeader("userId") Long userId
    );

    @GetMapping("/api/v1/file/{contentId}")
    ResponseEntity<String> getLink(
            @PathVariable("contentId") UUID contentId
    );
}
