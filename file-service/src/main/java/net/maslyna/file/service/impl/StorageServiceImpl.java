package net.maslyna.file.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.file.exception.GlobalFileServiceException;
import net.maslyna.file.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final Storage storage;
    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Override
    public Blob upload(String filename, InputStream stream, String contentType) {
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.create(filename, stream, contentType);
        log.info("file was saved, filename = '{}'", blob.getName());
        return blob;
    }

    @Override
    public boolean delete(String filename) {
        return storage.delete(BlobId.of(bucketName, filename));
    }

    @Override
    public String getLink(String filename) {
        Blob blob = storage.get(bucketName).get(filename);
        if (blob != null) {
            return blob.getMediaLink();
        }
        return null;
    }
}
