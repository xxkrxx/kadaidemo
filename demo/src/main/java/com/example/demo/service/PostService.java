package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Post;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post createPost(Post Post);
    Post updatePost(Post Post);
    void deletePost(Long id);
    boolean PostExistsById(Long id);
}