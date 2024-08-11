package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductOrderController {

    @Autowired
    private ProductService productService; // 商品サービスを注入

    @Autowired
    private OrderService orderService; // 注文サービスを注入

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        // 商品サービスからすべての商品のリストを取得
        List<Product> products = productService.getAllProducts();
        // 商品リストを200 OKで返す
        return ResponseEntity.ok(products);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        // 注文サービスからすべての注文のリストを取得
        List<Order> orders = orderService.getAllOrders();
        // 注文リストを200 OKで返す
        return ResponseEntity.ok(orders);
    }
}
