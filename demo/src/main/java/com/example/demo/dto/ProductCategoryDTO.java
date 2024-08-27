package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // コンストラクタを自動生成
public class ProductCategoryDTO {
    private String largeCategoryName; // 大カテゴリ名
    private String middleCategoryName; // 中カテゴリ名
    private String smallCategoryName; // 小カテゴリ名
    private String productName; // 商品名
    private String description; // 商品の説明
    private Double costPrice; // 仕入れ原価
    private Double manufacturerSuggestedRetailPrice; // メーカ希望小売価格   
}