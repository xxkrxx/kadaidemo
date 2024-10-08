package com.example.demo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Post;
import com.example.demo.entity.Role;
import com.example.demo.entity.Store;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdministratorRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public void registerOrUpdateAdministrator(Administrator admin) {
        logger.debug("Starting registerOrUpdateAdministrator with admin: {}", admin);

        // 新しい管理者のパスワードをエンコード
        if (admin.getId() == null && admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            logger.debug("Encoding password for new admin: {}", admin.getEmail());
            String encodedPassword = passwordEncoder.encode(admin.getPassword());
            logger.debug("Encoded password: {}", encodedPassword);
            admin.setPassword(encodedPassword);
        }

        // 既存の管理者のパスワードをエンコード（もし未エンコードの場合）
        if (admin.getId() != null && admin.getPassword() != null && !admin.getPassword().startsWith("$2a$")) {
            logger.debug("Encoding existing password for admin: {}", admin.getEmail());
            String encodedPassword = passwordEncoder.encode(admin.getPassword());
            logger.debug("Encoded password: {}", encodedPassword);
            admin.setPassword(encodedPassword);
        }

        // 新しいストアの保存
        if (admin.getStore() != null && admin.getStore().getId() == null) {
            logger.debug("Saving new store: {}", admin.getStore());
            Store savedStore = storeRepository.save(admin.getStore());
            admin.setStore(savedStore);
        }

        // ロールが設定されていない場合、デフォルトロールを設定
        if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
            logger.debug("Setting default role for admin: {}", admin.getEmail());
            Optional<Role> optionalDefaultRole = roleRepository.findByName("管理者"); // デフォルトロール名を指定
            Role defaultRole = optionalDefaultRole.orElseThrow(() -> new RuntimeException("デフォルトロールが見つかりません"));
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole);
            admin.setRoles(roles);
        }

        adminRepository.save(admin);
        logger.debug("Administrator saved: {}", admin);
    }

    @Override
    public List<Administrator> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Administrator getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public boolean postExistsById(Long id) {
        return postRepository.existsById(id);
    }

    @Override
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Administrator findByEmail(String email) {
        logger.debug("Fetching administrator with email: {}", email);
        return adminRepository.findByEmail(email).orElse(null);
    }
}



