package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Order;
import com.example.demo.entity.StoreProduct;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductOrderController {

    @Autowired
    private ProductService productService; // 商品サービスを注入

    @Autowired
    private OrderService orderService; // 注文サービスを注入

    // 商品のリストを取得するエンドポイント
    @GetMapping("/products")
    public ResponseEntity<Page<StoreProduct>> getAllProducts(Pageable pageable) {
        // 商品サービスからページネーション対応のStoreProductのリストを取得
        Page<StoreProduct> products = productService.getAllProducts(pageable);
        // 商品リストを200 OKで返す
        return ResponseEntity.ok(products);
    }

    // 注文のリストを取得するエンドポイント
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        // 注文サービスからすべての注文のリストを取得
        List<Order> orders = orderService.getAllOrders();
        // 注文リストを200 OKで返す
        return ResponseEntity.ok(orders);
    }
}
