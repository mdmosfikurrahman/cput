package za.ac.cput.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(),
                "application/octet-stream",
                input);
    }
}