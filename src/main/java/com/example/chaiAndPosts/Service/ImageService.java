package com.example.chaiAndPosts.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final Path fileStorageLocation;

    public ImageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public boolean deleteImage(String fileName) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            File file = targetLocation.toFile();
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger in production
        }
        return false;
    }
}
