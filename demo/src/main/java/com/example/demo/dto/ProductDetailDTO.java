package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductDetailDTO {
    private String productName;  // 商品名
    private double retailPrice;   // 小売価格（浮動小数点数）
    private int stock;            // 在庫数

    // デフォルトコンストラクタ
    public ProductDetailDTO() {
    }

    // 引数ありのコンストラクタ
    public ProductDetailDTO(String productName, double retailPrice, int stock) {
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.stock = stock;
    }
}