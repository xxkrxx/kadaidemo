package com.example.demo.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.service.CustomUserDetailsService;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final AdministratorRepository administratorRepository;

    public CustomUserDetailsServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // メールアドレスでユーザーを検索し、見つからない場合は例外をスロー
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // ユーザーの詳細情報をSpring SecurityのUserオブジェクトとして返す
        return new User(administrator.getEmail(), administrator.getPassword(), getAuthorities(administrator));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Administrator administrator) {
        // ユーザーのロールをGrantedAuthorityのコレクションに変換
        return administrator.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}

