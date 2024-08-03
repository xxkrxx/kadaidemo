package com.example.demo.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    // AdminstratorRepositoryを使用してデータベースから管理者情報を取得
    private final AdministratorRepository administratorRepository;

    // ロギング用のLoggerインスタンスを作成
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // コンストラクタでAdminstratorRepositoryを注入
    public UserServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    // Spring SecurityのUserDetailsServiceインターフェースのメソッドを実装
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ログにデバッグ情報としてメールアドレスを出力
        logger.debug("Attempting to load user by email: {}", email);
        
        // メールアドレスで管理者を検索し、見つからない場合は例外をスロー
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 見つかった管理者の情報をデバッグ用にログ出力
        logger.debug("Found administrator: {}", administrator);
        logger.debug("Administrator password: {}", administrator.getPassword());

        // UserDetailsオブジェクトを作成し、ユーザーの権限を取得
        UserDetails userDetails = new User(administrator.getEmail(), administrator.getPassword(), getAuthorities(administrator));
        logger.debug("UserDetails created: {}", userDetails);

        // UserDetailsオブジェクトを返す
        return userDetails;
    }

    // 管理者の役割から権限のコレクションを生成するメソッド
    private Collection<? extends GrantedAuthority> getAuthorities(Administrator administrator) {
        // 管理者の役割をストリーム処理してSimpleGrantedAuthorityオブジェクトのリストに変換
        return administrator.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}

