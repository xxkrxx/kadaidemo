package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Post;
import com.example.demo.entity.Role;
import com.example.demo.entity.Store;

public interface AdminService {
    // 管理者を登録または更新する
    void registerOrUpdateAdministrator(Administrator admin);
    
    // すべての管理者を取得する
    List<Administrator> getAllAdmins();
    
    // IDで管理者を取得する
    Administrator getAdminById(Long id);
    
    // IDで管理者を削除する
    void deleteAdminById(Long id);
    
    // すべての役職を取得する
    List<Role> getAllRoles();
    
    // すべての権限を取得する
    List<Post> getAllPosts();
    
    // IDで権限の存在確認
    boolean postExistsById(Long id);
    
    // 店舗を保存する
    Store saveStore(Store store);
    
    // メールで管理者を検索する
    Optional<Administrator> findByEmail(String email);
}
