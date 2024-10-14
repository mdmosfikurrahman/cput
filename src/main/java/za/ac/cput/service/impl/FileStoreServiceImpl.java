package za.ac.cput.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.Attachment;
import za.ac.cput.exception.FileStoreException;
import za.ac.cput.service.FileStoreService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStoreServiceImpl implements FileStoreService {

    @Value("${app.upload.path}")
    private String uploadPath;
    @Value("${app.upload.url}")
    private String uploadUrl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public Attachment storeFile(String relativePath, MultipartFile file) throws FileStoreException {
        try {
            file.transferTo(Path.of(uploadPath, relativePath));
            var attachment = new Attachment();
            attachment.setPath(relativePath);
            attachment.setContentType(file.getContentType());
            attachment.setBytes(file.getSize());
            attachment.setUrl(getPublicUrl(relativePath));
            return attachment;
        } catch (IOException e) {
            throw new FileStoreException();
        }
    }

    @Override
    public String getPublicUrl(String relativePath) {
        return "%s/%s".formatted(uploadUrl, relativePath);
    }

    @Override
    @Async
    public void deleteFile(String relativePath) throws FileStoreException {
        try {
            Files.deleteIfExists(Path.of(uploadPath, relativePath));
        } catch (IOException e) {
            throw new FileStoreException();
        }
    }

    @Override
    public byte[] getFileAsByteArray(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }
        return null;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = getPath(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private Path getPath(String filename) {
        return rootLocation.resolve(filename);
    }
}
