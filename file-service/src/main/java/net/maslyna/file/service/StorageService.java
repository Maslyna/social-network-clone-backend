package net.maslyna.file.service;

import com.google.cloud.storage.Blob;

import java.io.InputStream;

public interface StorageService {
    Blob upload(String filename, InputStream stream, String contentType);

    boolean delete(String filename);

    String getLink(String filename);
}
