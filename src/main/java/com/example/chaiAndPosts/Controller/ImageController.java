package com.example.chaiAndPosts.Controller;

import com.example.chaiAndPosts.Repository.CommentRepository;
import com.example.chaiAndPosts.Repository.BlobRepository;
import com.example.chaiAndPosts.Service.ImageService;
import com.example.chaiAndPosts.entity.Comment;
import com.example.chaiAndPosts.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BlobRepository blobRepository;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commentId,
            @RequestParam String username) {

        try {
            // Check only one of postId or commentId is present
            if ((postId == null && commentId == null) || (postId != null && commentId != null)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Exactly one of postId or commentId must be provided"));
            }

            // Validate ownership for post
            if (postId != null) {
                Post post = blobRepository.findById(postId)
                        .orElse(null);
                if (post == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Post not found"));
                }
                if (!post.getUser().getUsername().equals(username)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "You are not the owner of this post"));
                }
            }

            // Validate ownership for comment
            if (commentId != null) {
                Comment comment = commentRepository.findById(commentId)
                        .orElse(null);
                if (comment == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Comment not found"));
                }
                if (!comment.getUser().getUsername().equals(username)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "You are not the owner of this comment"));
                }
            }

            // Prepare upload folder
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Sanitize filename and add uuid_username_timestamp
            String originalName = file.getOriginalFilename();
            String baseName = "file";
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                int dotIndex = originalName.lastIndexOf(".");
                baseName = originalName.substring(0, dotIndex).trim().replaceAll("\\s+", "_");
                extension = originalName.substring(dotIndex);
            } else if (originalName != null) {
                baseName = originalName.trim().replaceAll("\\s+", "_");
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String uuid = UUID.randomUUID().toString();

            String filename = baseName + "_" + username + "_" + timestamp + "_" + uuid + extension;

            // Save the file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String publicUrl = "/images/" + filename;
            return ResponseEntity.ok(Map.of("url", publicUrl));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not upload file: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{imageName}")
    public ResponseEntity<String> deleteImage(@PathVariable String imageName) {
        boolean deleted = imageService.deleteImage(imageName);
        if (deleted) {
            return ResponseEntity.ok("Image deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found.");
        }
    }
}
