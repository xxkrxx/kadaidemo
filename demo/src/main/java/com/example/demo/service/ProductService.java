package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;

public interface ProductService {

    // すべてのStoreProductをページングして取得
    Page<StoreProduct> getAllProducts(Pageable pageable);

    // 検索条件でフィルタリングしたStoreProductをページングして取得
    Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Long storeId, Pageable pageable);

    // IDでStoreProductを取得
    Optional<StoreProduct> getStoreProductById(Long id);

    // IDでProductを取得
    Optional<Product> getProductById(Long id);

    // StoreProductを保存
    void saveProduct(StoreProduct storeProduct);

    // 在庫数量を更新
    void updateStock(Long storeProductId, int quantity);

    // すべてのStoreProductを取得
    List<StoreProduct> getAllStoreProducts();

    // 店舗に関連する商品のリストを取得
    List<Product> getProductsForStore(Long storeId);
}

