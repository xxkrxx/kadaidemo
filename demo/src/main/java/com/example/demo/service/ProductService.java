package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;

public interface ProductService {

    Page<StoreProduct> getAllProducts(Pageable pageable);

    Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Pageable pageable);

    Optional<StoreProduct> getStoreProductById(Long id);

    Optional<Product> getProductById(Long id);

    void saveProduct(StoreProduct storeProduct);

    void updateStock(Long storeProductId, int quantity);

    // メソッドの戻り値型をList<StoreProduct>に変更
    List<StoreProduct> getAllStoreProducts();
}

