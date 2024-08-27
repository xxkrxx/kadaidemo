package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class StoreProductOrderDTO {
    private String storeName;
    private String address;
    private List<ProductDetailDTO> products;
    private List<OrderDetailDTO> orders;

    public StoreProductOrderDTO() {
    }

    public StoreProductOrderDTO(String storeName, String address, List<ProductDetailDTO> products,
                                 List<OrderDetailDTO> orders) {
        this.storeName = storeName;
        this.address = address;
        this.products = products;
        this.orders = orders;
    }
}