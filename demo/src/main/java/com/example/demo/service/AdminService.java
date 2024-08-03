package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Post;
import com.example.demo.entity.Role;
import com.example.demo.entity.Store;

public interface AdminService {
    void registerOrUpdateAdministrator(Administrator admin);
    List<Administrator> getAllAdmins();
    Administrator getAdminById(Long id);
    void deleteAdminById(Long id);
    List<Role> getAllRoles();
    List<Post> getAllPosts();
    boolean postExistsById(Long id);
    Store saveStore(Store store);
    Administrator findByEmail(String email);
}
