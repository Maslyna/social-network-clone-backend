package net.maslyna.user.client;

import net.maslyna.common.model.FileType;
import net.maslyna.user.client.response.FileStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "file-service", url = "${eureka.client.file-service-url:''}")
public interface FileClient {

    @PostMapping(path = "/api/v1/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> save(
            @RequestHeader("userId") Long userId,
            @RequestParam("contentId") UUID contentId,
            @RequestParam("fileType") FileType type,
            @RequestPart("file") MultipartFile file
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
