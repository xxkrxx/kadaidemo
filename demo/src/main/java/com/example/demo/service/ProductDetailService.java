package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.ProductDetail;

public interface ProductDetailService {

    // 指定されたIDで商品詳細を取得します
    Optional<ProductDetail> getProductDetailById(Long id);

    // 商品詳細を保存または更新します
    void saveProductDetail(ProductDetail productDetail);

}
