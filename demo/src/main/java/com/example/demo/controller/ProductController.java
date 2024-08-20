package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.StoreProduct;
import com.example.demo.repository.LargeCategoryRepository;
import com.example.demo.repository.MiddleCategoryRepository;
import com.example.demo.repository.SmallCategoryRepository;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private LargeCategoryRepository largeCategoryRepository;

    @Autowired
    private MiddleCategoryRepository middleCategoryRepository;

    @Autowired
    private SmallCategoryRepository smallCategoryRepository;

    // 商品一覧を表示するメソッド
    @GetMapping("/list")
    public String listProducts(Model model,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "largeCategoryId", required = false) Long largeCategoryId,
                               @RequestParam(value = "middleCategoryId", required = false) Long middleCategoryId,
                               @RequestParam(value = "smallCategoryId", required = false) Long smallCategoryId,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        // ページネーション用の Pageable オブジェクトを作成
        Pageable pageable = PageRequest.of(page, size);
        // 商品検索とフィルタリングを行い、指定ページの結果を取得
        Page<StoreProduct> productPage = productService.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, pageable);

        // モデルにデータを追加
        model.addAttribute("productPage", productPage);
        model.addAttribute("largeCategories", largeCategoryRepository.findAll());
        model.addAttribute("middleCategories", middleCategoryRepository.findAll());
        model.addAttribute("smallCategories", smallCategoryRepository.findAll());
        model.addAttribute("search", search);
        model.addAttribute("largeCategoryId", largeCategoryId);
        model.addAttribute("middleCategoryId", middleCategoryId);
        model.addAttribute("smallCategoryId", smallCategoryId);

        // 商品一覧のビューを返す
        return "productList";
    }

    // 商品詳細を表示するメソッド
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        // 商品IDで商品情報を取得
        Optional<StoreProduct> product = productService.getStoreProductById(id);
        if (product.isPresent()) {
            // 商品が見つかった場合、モデルに商品情報を追加
            model.addAttribute("product", product.get());
        } else {
            // 商品が見つからなかった場合、エラーメッセージをモデルに追加
            model.addAttribute("errorMessage", "商品が見つかりませんでした。");
        }
        // 商品詳細のビューを返す
        return "productDetails";
    }
}
