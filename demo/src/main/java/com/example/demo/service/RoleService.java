package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Role;

public interface RoleService {
    List<Role> getAllRoles();
    void initializeRoles(); 
    Optional<Role> findByName(String name);
}
