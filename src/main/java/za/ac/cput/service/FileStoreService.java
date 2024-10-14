package za.ac.cput.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.Attachment;
import za.ac.cput.exception.FileStoreException;

import java.io.IOException;

public interface FileStoreService {
    Attachment storeFile(String relativePath, MultipartFile file) throws FileStoreException;

    String getPublicUrl(String relativePath);

    void deleteFile(String currentImage) throws FileStoreException;

    byte[] getFileAsByteArray(String path) throws IOException;

    Resource loadAsResource(String filename);
}
