package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主キーID、データベースで一意に識別される

    @Column(name = "small_category_id")
    private Long smallCategoryId; // 小カテゴリーID、外部キーとして使用


    @Column(name = "product_name")
    private String productName; // 商品名

    @Column(name = "description")
    private String description; // 商品説明

    @Column(name = "image")
    private String image; // 商品画像のURL

    @Column(name = "cost_price")
    private Double costPrice; // 仕入れ原価

    @Column(name = "retail_price")
    private Double retailPrice; // 小売価格

    @Column(name = "stock")
    private int stock; // 在庫数を管理するフィールドを追加

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    @JsonManagedReference // Orderエンティティとの双方向リレーションを管理するために使用
    private List<Order> orders; // この商品詳細に関連する注文のリスト

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // エンティティが永続化される前に作成日時を設定
        this.updatedAt = LocalDateTime.now(); // エンティティが永続化される前に更新日時を設定
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(); // エンティティが更新される前に更新日時を設定
    }

    // 在庫数のゲッターとセッターを追加
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}




