package net.maslyna.notification.service;

import java.io.IOException;

public interface IOService {
    String getFileAsString(String filePath) throws IOException;
}
