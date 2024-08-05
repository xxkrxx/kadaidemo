package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.MainCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.SubSmallCategory;
import com.example.demo.repository.MainCategoryRepository;
import com.example.demo.repository.MiddleCategoryRepository;
import com.example.demo.repository.SubSmallCategoryRepository;


@Controller
@RequestMapping("/categories")
public class MainCategoryController {
	
    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Autowired
    private MiddleCategoryRepository middleCategoryRepository;

    @Autowired
    private SubSmallCategoryRepository subSmallCategoryRepository;


	@GetMapping("/main")
    public String listMainCategories(Model model) {
        // メインカテゴリの全リストを取得し、モデルに追加
        model.addAttribute("mainCategories", mainCategoryRepository.findAll());
        return "MainCategoryList";
    }
    
    // メインカテゴリの詳細を表示するメソッド
    @GetMapping("/main/{id}")
    public String viewMainCategory(@PathVariable Long id, Model model) {
        // IDでメインカテゴリを検索し、見つからない場合は例外をスロー
        MainCategory mainCategory = mainCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid main category Id:" + id));
        // メインカテゴリとそのサブカテゴリをモデルに追加
        model.addAttribute("mainCategory", mainCategory);
        model.addAttribute("subCategories", mainCategory.getSubCategories());
        return "MainCategoryDetails"; 
    }
    
    // ミドルカテゴリの詳細を表示するメソッド
    @GetMapping("/middle/{id}") // MiddleCategoryに変更
    public String viewMiddleCategory(@PathVariable Long id, Model model) {
        // IDでミドルカテゴリを検索し、見つからない場合は例外をスロー
        MiddleCategory middleCategory = middleCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid middle category Id:" + id));
        // ミドルカテゴリとそのサブカテゴリをモデルに追加
        model.addAttribute("middleCategory", middleCategory);
        model.addAttribute("smallCategories", subSmallCategoryRepository.findByMiddleCategoryId(id));
        return "MiddleCategoryDetails"; 
    }
    
    @GetMapping("/small/{id}")
    public String viewSmallCategory(@PathVariable Long id, Model model) {
    	// IDでスモールカテゴリを検索し、見つからない場合は例外をスロー
    	SubSmallCategory subSmallCategory = subSmallCategoryRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Invalid small category Id:" + id));
    	// スモールカテゴリとその関連商品をモデルに追加
    	model.addAttribute("smallCategory", subSmallCategory);
    	return "SmallCategoryDetails";
    }

}
