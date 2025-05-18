package com.example.chaiAndPosts.Service;

import com.example.chaiAndPosts.DTO.UpdatePostRequest;
import com.example.chaiAndPosts.entity.Post;

import java.util.List;

public interface BlobService {

    Post createPost(Post post);
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long id);
    Post upvoteBlob(Long id);
    Post downvoteBlob(Long id);
}
