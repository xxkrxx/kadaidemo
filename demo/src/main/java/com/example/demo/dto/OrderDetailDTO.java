package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private String productName;
    private String adminName;
    private int quantity;
    private double totalPrice;

    // デフォルトコンストラクタ
    public OrderDetailDTO() {
        // デフォルトの初期化が必要な場合はここに記述
    }

    // 引数ありのコンストラクタ
    public OrderDetailDTO(String productName, String adminName, int quantity, double totalPrice) {
        this.productName = productName;
        this.adminName = adminName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}