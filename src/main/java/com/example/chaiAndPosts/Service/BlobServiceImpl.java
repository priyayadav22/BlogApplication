package com.example.chaiAndPosts.Service;

import com.example.chaiAndPosts.DTO.PostRequest;
import com.example.chaiAndPosts.DTO.UpdatePostRequest;
import com.example.chaiAndPosts.Exception.AppException;
import com.example.chaiAndPosts.Exception.DuplicateTitleException;
import com.example.chaiAndPosts.Exception.NoChangeDetectedException;
import com.example.chaiAndPosts.Repository.BlobRepository;
import com.example.chaiAndPosts.Repository.UserRepository;
import com.example.chaiAndPosts.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.chaiAndPosts.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlobServiceImpl implements BlobService{

    @Autowired
    private BlobRepository blobRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Post createPost(PostRequest postRequest){
        User user = userRepository.findByUsername(postRequest.getUsername())
                .orElseThrow(() -> new AppException("NotFound", "User not found"));

        Optional<Post> existingPost = blobRepository.findByTitle(postRequest.getTitle());
        if(existingPost.isPresent()){
            throw new DuplicateTitleException("A post with this title already exists");
        }

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setUser(user);  // set creator user
        post.setCreatedAt(LocalDateTime.now());
        post.setLastUpdated(LocalDateTime.now());
        return blobRepository.save(post);
    }


    @Override
    public List<Post> getAllPosts(){
        return blobRepository.findAll();
    }

    @Override
    public Post getPostById(Long id){
        return  blobRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public void deletePost(Long id, String username){
        Post post = blobRepository.findById(id).orElseThrow(() -> new RuntimeException("No Blog Found!"));
        if (!post.getUser().getUsername().equals(username)) {
            throw new AppException("Unauthorized", "You are not authorized to update this post");
        }
        blobRepository.deleteById(id);
    }

    @Override
    public Post updatePost(Long id, UpdatePostRequest request, String username){
        Post post = blobRepository.findById(id).orElseThrow(() -> new RuntimeException("No Blog Found!"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new AppException("Unauthorized", "You are not authorized to update this post");
        }

        boolean updated = false;
        if(request.getTitle()!= null && !request.getTitle().isBlank()  && !request.getTitle().equals(post.getTitle()))
        {
            post.setTitle(request.getTitle());
            updated= true;
        }

        if(request.getContent()!=null && !request.getContent().isBlank()  && !request.getContent().equals(post.getContent())){
            post.setContent(request.getContent());
            updated= true;
        }

        if (!updated) {
            throw new NoChangeDetectedException("No changes detected As Post is already up to date.");
        }
        post.setLastUpdated(LocalDateTime.now());
        return blobRepository.save(post);
    }

    @Override
    public Post upvoteBlob(Long id){
        Post post = blobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not  found with id :" + id));
        post.setUpvotes(post.getUpvotes()+1);
        return blobRepository.save(post);
    }

    @Override
    public Post downvoteBlob(Long id){
        Post post = blobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not  found with id :" + id));
        post.setDownvotes(post.getDownvotes()+1);
        return blobRepository.save(post);
    }

}
