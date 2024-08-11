package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Entity
@Table(name = "`order`")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference // Administratorエンティティへの参照を管理
    private Administrator admin;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonBackReference // Storeエンティティへの参照を管理
    private Store store;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    @JsonBackReference // ProductDetailエンティティへの参照を管理
    private ProductDetail productDetail;

    private int quantity; // 発注数
    private double totalPrice; // 合計金額
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
