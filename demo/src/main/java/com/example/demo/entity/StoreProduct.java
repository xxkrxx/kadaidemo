package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "store_products")
@Data
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id") // store_id 列でStoreエンティティと結合
    @ToString.Exclude
    private Store store; // Storeエンティティとの多対一のリレーション

    @ManyToOne
    @JoinColumn(name = "product_id") // product_id 列でProductエンティティと結合
    private Product product; // Productエンティティとの多対一のリレーション
    
    @ManyToOne
    @JoinColumn(name = "store_product_id")
    private StoreProduct storeProduct; // StoreProductエンティティとの関連

    @Column(name = "retail_price", nullable = false)
    private int retailPrice; // 小売価格

    @Column(name = "stock", nullable = false)
    private int stock; // 在庫数

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 更新日時

    // デフォルトコンストラクター
    public StoreProduct() {
    }

    // ProductからStoreProductを作成するためのコンストラクター
    public StoreProduct(Product product, int retailPrice, int stock, Store store) {
        this.product = product;
        this.retailPrice = retailPrice;
        this.stock = stock;
        this.store = store;
    }

    @PrePersist
    protected void onCreate() {
        // レコード作成前に呼ばれるメソッド
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        // レコード更新前に呼ばれるメソッド
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        // カスタムtoStringメソッド
        return "StoreProduct{id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", retailPrice=" + retailPrice +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + '}';
    }
}

