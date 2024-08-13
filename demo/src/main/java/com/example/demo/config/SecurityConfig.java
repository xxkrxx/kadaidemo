package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import com.example.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    // コンストラクタで UserService を注入
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // SecurityFilterChain の設定を行う Bean メソッド
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // 特定の URL に対しては認証なしでアクセスを許可
                .requestMatchers("/admin/signin", "/signin.css","/admin/forgetPassword", "/api/orders", "/api/products").permitAll()
                // 上記以外の URL には認証を要求
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // カスタムログインページの設定
                .loginPage("/admin/signin")
                // ログイン成功後の遷移先 URL
                .defaultSuccessUrl("/admin/top", true)
                // ログイン失敗時の遷移先 URL
                .failureUrl("/admin/signin?error")
                .permitAll()
            )
            .logout(logout -> logout
                    // ログアウト URL の設定
                    .logoutUrl("/logout")
                    // ログアウト後の遷移先 URL
                    .logoutSuccessUrl("/admin/signin")
                    .permitAll()
            );
        return http.build();
    }

    // パスワードのエンコーディングに使用する Bean メソッド
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Thymeleaf の Spring Security ダイアレクトを設定する Bean メソッド
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    // AuthenticationManager を設定する Bean メソッド
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return auth.build();
    }
}

