package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void initializeRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("管理者"); // "管理者" ロールを追加
            Role userRole = new Role("一般"); // "一般" ロールを追加
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name); // RoleRepository に findByName メソッドが必要
    }
}
