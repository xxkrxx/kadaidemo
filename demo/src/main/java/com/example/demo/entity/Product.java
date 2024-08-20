package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // エンティティが永続化される前に作成日時を設定
        this.updatedAt = LocalDateTime.now(); // エンティティが永続化される前に更新日時を設定
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(); // エンティティが更新される前に更新日時を設定
    }
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference // Orderエンティティとの双方向リレーションを管理するために使用
    private List<Order> orders; // この商品詳細に関連する注文のリスト

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
    @JoinColumn(name = "store_id") // 店舗情報を持つための関連
    private Store store; // 店舗情報
}

