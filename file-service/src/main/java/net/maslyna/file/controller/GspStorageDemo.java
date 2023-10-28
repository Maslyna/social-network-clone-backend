package net.maslyna.file.controller;

import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import net.maslyna.file.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@RestController
//@RequestMapping("/gcp")
//@RequiredArgsConstructor
//public class GspStorageDemo {
//    private final StorageService storageService;
//
//    @PostMapping
//    public String uploadNewFile(@RequestParam("file") MultipartFile file) {
//        try {
//            return storageService.testUpload(file).getMediaLink();
//        } catch (Exception ignored) {
//            return null;
//        }
//    }
//
//    @GetMapping("/{filename}")
//    public ResponseEntity<String> getFileLink(@PathVariable("filename") String fileName) {
//        return ResponseEntity.ok(storageService.getLink(fileName));
//    }
//}
