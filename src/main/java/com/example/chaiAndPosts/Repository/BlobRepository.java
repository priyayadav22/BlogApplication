package com.example.chaiAndPosts.Repository;

import com.example.chaiAndPosts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlobRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);
}
