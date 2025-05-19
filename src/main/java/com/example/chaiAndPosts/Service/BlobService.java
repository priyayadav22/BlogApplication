package com.example.chaiAndPosts.Service;

import com.example.chaiAndPosts.DTO.PostRequest;
import com.example.chaiAndPosts.DTO.UpdatePostRequest;
import com.example.chaiAndPosts.entity.Post;

import java.util.List;

public interface BlobService {

    Post createPost(PostRequest request);
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post updatePost(Long id, UpdatePostRequest request, String username);
    void deletePost(Long id, String username);
    Post upvoteBlob(Long id);
    Post downvoteBlob(Long id);
}
