package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;

public interface ProductService {

    // ページネーションされたすべての Product オブジェクトを取得
    Page<Product> getAllProducts(Pageable pageable);

    // 検索条件に基づいてページネーションされた Product オブジェクトのリストを取得
    Page<Product> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Pageable pageable);

    // 指定された ID の Product オブジェクトを取得
    Optional<Product> getProductById(Long id);

    // 指定された Product オブジェクトを保存
    void saveProduct(Product product);

    // 指定された Product ID の在庫数量を更新
    void updateStock(Long productId, int quantity);

    // すべての Product オブジェクトを取得
    List<Product> getAllProducts();

    // 指定された ID の StoreProduct オブジェクトを取得
    Optional<StoreProduct> getStoreProductById(Long id);

    // 指定された StoreProduct オブジェクトを保存
    void saveStoreProduct(StoreProduct storeProduct);

    // すべての StoreProduct オブジェクトを取得
    List<StoreProduct> getAllStoreProducts();

    // 小カテゴリIDで商品を取得するメソッドを追加
    List<Product> findBySmallCategoryId(Long smallCategoryId);
}
