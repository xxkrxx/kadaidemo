package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.dto.ProductDetailDTO;
import com.example.demo.dto.StoreProductOrderDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;
import com.example.demo.entity.StoreProduct;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StoreService;

@RestController
@RequestMapping("/api")
public class ProductOrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService; 

    @GetMapping("/products")
    public ResponseEntity<List<ProductCategoryDTO>> getAllProducts(Pageable pageable) {
        Page<StoreProduct> products = productService.getAllProducts(pageable);

        if (products.isEmpty()) {
            // デフォルトの商品情報を取得
            List<Product> defaultProducts = productService.getAllProducts();
            List<ProductCategoryDTO> productCategories = defaultProducts.stream()
                .map(product -> new ProductCategoryDTO(
                    product.getLargeCategory().getName(),
                    product.getMiddleCategory().getName(),
                    product.getSmallCategory().getName(),
                    product.getName(),
                    product.getDescription(),
                    0.0,
                    0.0
                ))
                .collect(Collectors.toList());
            return ResponseEntity.ok(productCategories);
        } else {
            List<ProductCategoryDTO> productCategories = products.stream()
                .map(storeProduct -> {
                    return new ProductCategoryDTO(
                        storeProduct.getProduct().getLargeCategory().getName(),
                        storeProduct.getProduct().getMiddleCategory().getName(),
                        storeProduct.getProduct().getSmallCategory().getName(),
                        storeProduct.getProduct().getName(),
                        storeProduct.getProduct().getDescription(),
                        storeProduct.getCostPrice(),
                        storeProduct.getRetailPrice()
                    );
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(productCategories);
        }
    }
    // 店舗と発注履歴の詳細を取得するエンドポイント
    @GetMapping("/orders")
    public ResponseEntity<List<StoreProductOrderDTO>> getAllStoreProductOrderDetails() {
        List<Store> stores = storeService.getAllStores();

        List<StoreProductOrderDTO> dtos = stores.stream()
            .map(store -> {
                List<Order> orders = orderService.getOrdersByStore(store);
                
                StoreProductOrderDTO dto = new StoreProductOrderDTO();
                dto.setStoreName(store.getName());
                dto.setAddress(store.getAddress());
                
                List<ProductDetailDTO> productDetails = store.getStoreProducts().stream()
                    .map(storeProduct -> new ProductDetailDTO(
                        storeProduct.getProduct().getName(),
                        storeProduct.getRetailPrice(),
                        storeProduct.getStock()))
                    .collect(Collectors.toList());
                dto.setProducts(productDetails);
                
                List<OrderDetailDTO> orderDetails = orders.stream()
                    .map(order -> {
                        String productName = (order.getProduct() != null) ? order.getProduct().getName() : "不明";
                        String adminName = (order.getAdmin() != null) 
                            ? order.getAdmin().getFirstName() + " " + order.getAdmin().getLastName() 
                            : "不明";
                        int quantity = order.getQuantity();
                        double totalPrice = order.getTotalPrice();
                        return new OrderDetailDTO(productName, adminName, quantity, totalPrice);
                    })
                    .collect(Collectors.toList());
                dto.setOrders(orderDetails);
                
                return dto;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
}