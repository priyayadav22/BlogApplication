package com.example.chaiAndPosts.Service;

import com.example.chaiAndPosts.DTO.CommentRequest;
import com.example.chaiAndPosts.Exception.AppException;
import com.example.chaiAndPosts.Exception.NoChangeDetectedException;
import com.example.chaiAndPosts.Repository.BlobRepository;
import com.example.chaiAndPosts.Repository.CommentRepository;
import com.example.chaiAndPosts.Repository.UserRepository;
import com.example.chaiAndPosts.entity.Comment;
import com.example.chaiAndPosts.entity.Post;
import com.example.chaiAndPosts.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlobRepository blobRepository;

    public Comment addComment(Long postId, CommentRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Post post = blobRepository.findById(postId).orElseThrow(()->new RuntimeException(("Post Not Found")));

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        return  commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void deleteComment(Long commentId, String username){
        Comment comment= commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException("NotFound", "Comment Not Found"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new AppException("Unauthorized", "You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public Comment editComment(Long commentId, CommentRequest request){
        Comment comment= commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException("NotFound", "Comment Not Found"));

        boolean updated= false;

        if (request.getText() != null && !request.getText().isBlank() &&
                !request.getText().equals(comment.getText())) {
            comment.setText(request.getText());
            updated = true;
        }

        if (updated) {
            return commentRepository.save(comment);
        } else {
                throw new NoChangeDetectedException("No changes detected As Comment is already up to date.");
        }
    }

    public Comment likeComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException("NotFound", "Comment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("NotFound", "User not found"));

        if (comment.getLikedBy().contains(user)) {
            throw new AppException("Conflict", "User has already liked this comment");
        }

        comment.getLikedBy().add(user);
        comment.setLikes(comment.getLikes() + 1);

        return commentRepository.save(comment);
    }


}
