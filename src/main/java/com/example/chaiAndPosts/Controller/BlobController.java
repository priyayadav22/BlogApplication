package com.example.chaiAndPosts.Controller;

import com.example.chaiAndPosts.DTO.PostRequest;
import com.example.chaiAndPosts.DTO.UpdatePostRequest;
import com.example.chaiAndPosts.Service.BlobService;
import com.example.chaiAndPosts.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class BlobController {

    @Autowired
    private BlobService blobService;

    @PostMapping
    public Post createBlog(@RequestBody PostRequest request){
        return blobService.createPost(request);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return blobService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return blobService.getPostById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request,@RequestParam String username) {
       Post updated=  blobService.updatePost(id, request, username);
       return ResponseEntity.ok(updated).getBody();
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id,@RequestParam String username) {
        blobService.deletePost(id, username);
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<Post> upvotePost(@PathVariable Long id){
        return ResponseEntity.ok(blobService.upvoteBlob(id));
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<Post> downvotePost(@PathVariable Long id){
        return ResponseEntity.ok(blobService.downvoteBlob(id));
    }


}
