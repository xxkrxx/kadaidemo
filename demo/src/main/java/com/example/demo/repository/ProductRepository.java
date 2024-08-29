package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 店舗に関連する商品のリストを取得
    List<Product> findByStoreId(Long storeId);
    
    List<Product> findByNameContaining(String name);
    List<Product> findByLargeCategoryId(Long largeCategoryId);
    List<Product> findByMiddleCategoryId(Long middleCategoryId);
    List<Product> findBySmallCategoryId(Long smallCategoryId);
}