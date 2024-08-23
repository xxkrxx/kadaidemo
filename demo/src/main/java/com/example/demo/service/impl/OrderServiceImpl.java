package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;
import com.example.demo.entity.StoreProduct;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.StoreProductRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Override
    public void saveOrder(Order order) {
        if (order.getProduct() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("商品と数量は注文を保存する前に設定する必要があります。");
        }

        Product product = order.getProduct();
        order.setTotalPrice(product.getCostPrice() * order.getQuantity()); // 合計金額を設定
        orderRepository.save(order); // 注文をデータベースに保存
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStore(Store store) {
        return orderRepository.findByStore(store);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findByIdWithAdmin(id)
            .orElseThrow(() -> new RuntimeException("指定されたIDの注文が見つかりません。"));
    }

    @Override
    public Administrator findAdminByUsername(String username) {
        System.out.println("Searching for admin with username: " + username);
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(username);
        Administrator admin = adminOpt.orElseThrow(() -> new RuntimeException("指定されたユーザー名の管理者が見つかりません。"));
        System.out.println("Found admin: " + admin);
        return admin;
    }

    @Override
    public void createOrder(Long productId, Long storeId, int quantity) {
        // StoreProductを取得
        StoreProduct storeProduct = storeProductRepository.findByProductIdAndStoreId(productId, storeId);
        
        if (storeProduct == null || storeProduct.getStock() < quantity) {
            throw new IllegalArgumentException("商品が見つからないか、在庫が不足しています。");
        }

        // 発注処理
        Order order = new Order();
        order.setProduct(storeProduct.getProduct());
        order.setQuantity(quantity);
        order.setTotalPrice(storeProduct.getRetailPrice() * quantity);
        order.setStore(storeProduct.getStore());
        order.setAdmin(storeProduct.getStore().getAdmin()); // 管理者情報を設定

        // 在庫を更新
        storeProduct.setStock(storeProduct.getStock() - quantity);
        orderRepository.save(order);
        storeProductRepository.save(storeProduct);
    }

}

