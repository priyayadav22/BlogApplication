package com.example.chaiAndPosts.Controller;

import com.example.chaiAndPosts.DTO.CommentRequest;
import com.example.chaiAndPosts.Service.CommentService;
import com.example.chaiAndPosts.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentRequest request){
        Comment comment = commentService.addComment(postId, request);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/post/postId")
    public ResponseEntity<?> getComments(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @RequestParam String username){
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok("Comment Deleted Successfully");
    }

    @PutMapping("/{commentId}")
    public Comment editComment(@PathVariable Long commentId, @RequestBody CommentRequest request){
        Comment updatedComment= commentService.editComment(commentId, request);
        return ResponseEntity.ok(updatedComment).getBody();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable Long commentId,@RequestParam String username) {
        Comment updatedComment = commentService.likeComment(commentId, username);
        return ResponseEntity.ok(updatedComment);
    }

}
