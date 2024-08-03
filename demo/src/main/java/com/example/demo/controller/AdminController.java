package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Post;
import com.example.demo.service.AdminService;
import com.example.demo.service.PostService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StoreService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PostService postService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    // サインインページを表示
    @GetMapping("/signin")
    public String showSignInPage() {
        logger.debug("サインインページに移動します");
        return "signin";
    }

    // 管理者の登録処理
    @PostMapping("/register")
    public String registerAdministrator(@Valid Administrator admin, BindingResult result, Model model) {
        return processAdminForm(admin, result, model, "AdminEdit");
    }

    // 管理者トップページを表示
    @GetMapping("/top")
    public String showTop() {
        logger.debug("管理者トップページに移動します");
        return "AdminTop";
    }

    // 管理者の一覧を表示
    @GetMapping("/list")
    public String listAdmins(Model model) {
        logger.debug("すべての管理者のリストを取得しています");
        model.addAttribute("admins", adminService.getAllAdmins());
        addCurrentAdminDetailsToModel(model);
        return "AdminList";
    }

    // 管理者の詳細を表示
    @GetMapping("/details/{id}")
    public String adminDetails(@PathVariable Long id, Model model) {
        logger.debug("管理者の詳細を取得しています。ID: {}", id);
        Administrator admin = adminService.getAdminById(id);
        model.addAttribute("admin", admin);
        addCurrentAdminDetailsToModel(model);
        return "AdminDetails";
    }

    // 管理者の編集ページを表示
    @GetMapping("/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model) {
        logger.debug("ID: {} の管理者の編集ページに移動します", id);
        Administrator admin = adminService.getAdminById(id);
        model.addAttribute("admin", admin);
        model.addAttribute("allStores", storeService.getAllStores());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("allposts", postService.getAllPosts());
        return "AdminEdit";
    }

    // 管理者の更新処理
    @PostMapping("/update")
    public String updateAdmin(@Valid Administrator admin, BindingResult result, Model model) {
        return processAdminForm(admin, result, model, "AdminEdit");
    }

    // 管理者の削除処理
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        logger.debug("ID: {} の管理者の削除を試みています", id);
        adminService.deleteAdminById(id);
        logger.debug("ID: {} の管理者が正常に削除されました", id);
        return "redirect:/AdminList";
    }

    // 管理者作成ページを表示
    @GetMapping("/create")
    public String showCreateAdminForm(Model model) {
        logger.debug("管理者作成ページに移動します");
        model.addAttribute("admin", new Administrator());
        model.addAttribute("allStores", storeService.getAllStores());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("allposts", postService.getAllPosts());
        return "AdminCreate";
    }

    // 管理者の作成処理
    @PostMapping("/create")
    public String createAdmin(@Valid Administrator admin, BindingResult result, Model model) {
        return processAdminForm(admin, result, model, "AdminCreate");
    }

    // 管理者のフォーム処理を行う
    private String processAdminForm(Administrator admin, BindingResult result, Model model, String viewName) {
        logger.debug("管理者の処理を試みています: {}", admin);
        if (result.hasErrors()) {
            logger.debug("バリデーションエラーが見つかりました: {}", result.getAllErrors());
            prepareAdminFormModel(model, admin);
            return viewName;
        }

        // 店舗IDの検証
        if (admin.getStore() == null || admin.getStore().getId() == null || !storeService.existsById(admin.getStore().getId())) {
            logger.debug("無効な店舗ID: {}", admin.getStore().getId());
            result.rejectValue("store.id", "error.admin", "無効な店舗IDです");
            prepareAdminFormModel(model, admin);
            return viewName;
        }

        // 権限IDの検証
        if (admin.getPosts() != null) {
            for (Post post : admin.getPosts()) {
                if (post.getId() == null || !postService.PostExistsById(post.getId())) {
                    logger.debug("無効な権限ID: {}", post.getId());
                    result.rejectValue("posts", "error.admin", "無効な権限IDです");
                    prepareAdminFormModel(model, admin);
                    return viewName;
                }
            }
        }

        // パスワード処理
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            if (admin.getPassword().length() > 64) {
                result.rejectValue("password", "error.admin", "パスワードは64文字以内で入力してください。");
                prepareAdminFormModel(model, admin);
                return viewName;
            }
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        } else {
            Administrator existingAdmin = adminService.getAdminById(admin.getId());
            admin.setPassword(existingAdmin.getPassword());
        }

        adminService.registerOrUpdateAdministrator(admin);
        logger.debug("管理者が正常に処理されました: {}", admin);
        return "redirect:/admin/details/" + admin.getId();
    }

    // 現在の管理者の詳細をモデルに追加
    private void addCurrentAdminDetailsToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Administrator currentAdmin = adminService.findByEmail(userDetails.getUsername());
            boolean isAdmin = currentAdmin != null && currentAdmin.getPosts().stream()
                .anyMatch(post -> "管理者".equals(post.getName()));
            model.addAttribute("isAdmin", isAdmin);
        }
    }

    // 管理者フォームのモデル準備
    private void prepareAdminFormModel(Model model, Administrator admin) {
        model.addAttribute("admin", admin);
        model.addAttribute("allStores", storeService.getAllStores());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("allposts", postService.getAllPosts());
    }
}

