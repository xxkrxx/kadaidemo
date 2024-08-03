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

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // リクエストの認可設定
            .authorizeHttpRequests(authz -> authz
                // 特定のパスに対するアクセスを許可
                .requestMatchers("/admin/signin", "/css/**", "/images/**", "/admin/forgetPassword", "/contact").permitAll()
                // 認証が必要なパスを設定
                .requestMatchers("/admin/edit/**", "/admin/list", "/admin/create").authenticated()
                // その他のすべてのリクエストも認証が必要
                .anyRequest().authenticated()
            )
            // フォームログインの設定
            .formLogin(form -> form
                .loginPage("/admin/signin") // ログインページの指定
                .defaultSuccessUrl("/admin/top", true) // ログイン成功後のリダイレクト先
                .failureUrl("/admin/signin?error") // ログイン失敗後のリダイレクト先
                .permitAll() // ログインページはすべてのユーザーに許可
            )
            // ログアウトの設定
            .logout(logout -> logout
                .permitAll() // ログアウトページもすべてのユーザーに許可
            );
        return http.build(); // HttpSecurity設定を構築して返す
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return auth.build();
    }
}

