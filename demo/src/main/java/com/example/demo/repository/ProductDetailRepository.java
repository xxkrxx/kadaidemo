package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    // smallCategoryId に基づいて ProductDetail のリストを取得するメソッド
    List<ProductDetail> findBySmallCategoryId(Long smallCategoryId);
}
