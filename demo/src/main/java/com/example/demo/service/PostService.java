package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Post;

public interface PostService {
    // すべての権限を取得する
    List<Post> getAllPosts();
    
    // IDで権限を取得する
    Post getPostById(Long id);
    
    // 権限を作成する
    Post createPost(Post post);
    
    // 権限を更新する
    Post updatePost(Post post);
    
    // 権限を削除する
    void deletePost(Long id);
    
    // IDで権限の存在確認
    boolean PostExistsById(Long id);
}
