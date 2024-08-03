package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Administrator;
import com.example.demo.repository.AdministratorRepository;

@Controller
@RequestMapping("/admin")
public class ForgetPasswordController {
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // パスワード再設定リクエストフォームを表示するメソッド
    @GetMapping("/forgetPassword")
    public String showPasswordResetRequestForm() {
        return "forgetPassword"; 
    }
    
    // パスワード再設定リクエストを処理するメソッド
    @PostMapping("/forgetPassword")
    public String handlePasswordResetRequest(@RequestParam String email, 
                                             @RequestParam String new_password, 
                                             @RequestParam String confirm_password, 
                                             Model model) {
        // 新しいパスワードと確認パスワードが一致するか確認
        if (!new_password.equals(confirm_password)) {
            model.addAttribute("errorMessage", "新しいパスワードと確認パスワードが一致しません。");
            return "forgetPassword"; 
        }

        // メールアドレスに対応する管理者を検索
        Optional<Administrator> optionalAdmin = administratorRepository.findByEmail(email);
        if (!optionalAdmin.isPresent()) {
            model.addAttribute("errorMessage", "メールアドレスが見つかりません。");
            return "forgetPassword"; 
        }

        // パスワードをエンコードして保存
        Administrator administrator = optionalAdmin.get();
        administrator.setPassword(passwordEncoder.encode(new_password));
        administratorRepository.save(administrator);
        
        // パスワード再設定が完了したらサインインページにリダイレクト
        return "redirect:/admin/signin";
    }
}


