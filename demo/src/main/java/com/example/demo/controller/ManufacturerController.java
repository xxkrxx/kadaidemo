package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Manufacturer;
import com.example.demo.service.AdminService;
import com.example.demo.service.ManufacturerService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private AdminService adminService;

    // メーカーを一覧表示するメソッド
    @GetMapping("/list")
    public String listManufacturers(Model model) {
        // すべてのメーカー情報を取得し、モデルに追加
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());

        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 認証情報が存在し、かつその認証情報の主体がUserDetailsのインスタンスである場合
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // ログイン中のユーザーの詳細情報を取得
            Administrator admin = adminService.findByEmail(userDetails.getUsername()); // ユーザーのメールアドレスから管理者情報を取得
            
            // 管理者が存在し、かつその管理者のロールに「管理者」が含まれている場合
            boolean isAdmin = admin != null && admin.getRoles().stream()
                    .anyMatch(role -> "管理者".equals(role.getName()));
            
            // モデルにisAdmin属性を追加し、管理者かどうかの情報をビューに渡す
            model.addAttribute("isAdmin", isAdmin);
        }

        // "ManufacturerList"ビューを返す
        return "ManufacturerList";
    }


    // 新規メーカー作成フォームを表示するメソッド
    @GetMapping("/create")
    public String showCreateManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "ManufacturerForm";
    }

    // 新規メーカーを作成するメソッド
    @PostMapping("/create")
    public String createManufacturer(@Valid @ModelAttribute Manufacturer manufacturer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("message", "入力内容に誤りがあります。");
            return "ManufacturerForm";
        }
        manufacturerService.saveManufacturer(manufacturer);
        return "redirect:/manufacturers/list";
    }

    // 指定されたIDのメーカー詳細を表示するメソッド
    @GetMapping("/{id}")
    public String viewManufacturer(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer Id:" + id));
        model.addAttribute("manufacturer", manufacturer);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Administrator admin = adminService.findByEmail(userDetails.getUsername());
            model.addAttribute("isAdmin", admin != null && admin.getRoles().stream()
                    .anyMatch(role -> "管理者".equals(role.getName())));
        }

        return "ManufacturerDetails";
    }

    // 特定のメーカーを編集するためのフォームを表示するメソッド
    @GetMapping("/edit/{id}")
    public String showEditManufacturerForm(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer Id:" + id));
        model.addAttribute("manufacturer", manufacturer);
        return "ManufacturerEdit";
    }

    // メーカー情報を更新するメソッド
    @PostMapping("/{id}/edit")
    public String updateManufacturer(@PathVariable Long id, @Valid @ModelAttribute Manufacturer manufacturer, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("message", "入力内容に誤りがあります。");
            return "ManufacturerEdit";
        }
        manufacturer.setId(id);
        manufacturerService.saveManufacturer(manufacturer);
        redirectAttributes.addFlashAttribute("message", "メーカー情報が更新されました。");
        return "redirect:/manufacturers/" + id;
    }

    // 特定のメーカーを削除するメソッド
    @PostMapping("/{id}/delete")
    public String deleteManufacturer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        manufacturerService.deleteManufacturerById(id);
        redirectAttributes.addFlashAttribute("message", "メーカー情報が削除されました。");
        return "redirect:/manufacturers/list";
    }
}

