package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 商品名
    private String description; // 商品説明
    private Double price; // 商品価格
    private Integer stock; // 在庫数
    private Double costPrice; // 仕入れ原価
    private Double manufacturerSuggestedRetailPrice; // メーカー希望小売価格
    private String imageUrl; // 商品画像のURL

    @ManyToOne
    @JsonBackReference // LargeCategoryエンティティへの参照を管理
    private LargeCategory largeCategory; // 大カテゴリー

    @ManyToOne
    @JsonBackReference // MiddleCategoryエンティティへの参照を管理
    private MiddleCategory middleCategory; // 中カテゴリー

    @ManyToOne
    @JsonBackReference // SmallCategoryエンティティへの参照を管理
    private SmallCategory smallCategory; // 小カテゴリー

    @ManyToOne
    @JsonBackReference // Manufacturerエンティティへの参照を管理
    private Manufacturer manufacturer; // メーカー
}
