package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Store;
import com.example.demo.service.AdminService;
import com.example.demo.service.StoreService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;
    
    @Autowired
    private AdminService adminService;
    
    // すべての店舗を一覧表示するメソッド
    @GetMapping("/list")
    public String listStores(Model model) {
        // 認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Administrator administrator = adminService.findByEmail(email);
        
        // すべての店舗情報を取得
        List<Store> stores = storeService.getAllStores();
        Store userStore = administrator.getStore();
        
        // モデルにデータを追加
        model.addAttribute("stores", stores);
        model.addAttribute("userStore", userStore);
        
        // 店舗一覧を返す
        return "StoreList";
    }

    // 指定されたIDの店舗詳細を表示するメソッド
    @GetMapping("/{id}")
    public String viewStore(@PathVariable Long id, Model model) {
        // 指定されたIDの店舗を取得
        Store store = storeService.getStoreById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        
        // モデルに店舗データを追加
        model.addAttribute("store", store);
        
        // 店舗詳細ページを返す
        return "StoreDetails";
    }

    // 店舗作成フォームを表示するメソッド
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // 新しい店舗オブジェクトをモデルに追加
        model.addAttribute("store", new Store());
        // 店舗作成ページを返す
        return "StoreForm";
    }

    // 新しい店舗を作成するメソッド
    @PostMapping("/create")
    public String createStore(@Valid Store store, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // バリデーションエラーがある場合、作成ページに戻る
            return "StoreForm";
        }
        // 店舗を保存
        storeService.createStore(store);
        // 店舗一覧ページにリダイレクト
        return "redirect:/stores/list";
    }
    
    // 店舗編集フォームを表示するメソッド
    @GetMapping("/edit/{id}")
    public String showEditStoreForm(@PathVariable Long id, Model model) {
        // 指定されたIDの店舗を取得
        Store store = storeService.getStoreById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        // モデルに店舗データを追加
        model.addAttribute("store", store);
        // バインディング結果が含まれていない場合、新しいバインディング結果を追加
        if (!model.containsAttribute("org.springframework.validation.BindingResult.store")) {
            model.addAttribute("org.springframework.validation.BindingResult.store", new BeanPropertyBindingResult(store, "store"));
        }
        // 店舗編集ページを返す
        return "StoreEdit";
    }

    // 店舗情報を更新するメソッド
    @PostMapping("/{id}/edit")
    public String updateStore(@PathVariable Long id, @Valid @ModelAttribute Store store, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // バリデーションエラーがある場合、編集ページに戻る
            model.addAttribute("store", store);
            return "StoreEdit";
        }
        // 店舗IDをセットして更新
        store.setId(id);
        storeService.updateStore(store);
        // 更新後、店舗詳細ページにリダイレクト
        return "redirect:/stores/" + id;
    }

    // 店舗を削除するメソッド
    @PostMapping("/{id}/delete")
    public String deleteStore(@PathVariable Long id) {
        // 指定されたIDの店舗を削除
        storeService.deleteStore(id);
        // 店舗一覧ページにリダイレクト
        return "redirect:/stores/list";
    }
}
