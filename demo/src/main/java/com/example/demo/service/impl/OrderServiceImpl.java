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

        // 商品情報を取得
        Product product = order.getProduct();

        // 合計金額を計算して設定
        order.setTotalPrice(product.getCostPrice() * order.getQuantity());

        // 注文をデータベースに保存
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStore(Store store) {
        // 指定された店舗に紐づく注文を取得して返す
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

        // メールアドレスで管理者を検索
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(username);

        // 管理者が見つからない場合は例外をスロー
        Administrator admin = adminOpt.orElseThrow(() -> 
            new RuntimeException("指定されたユーザー名の管理者が見つかりません。"));

        System.out.println("Found admin: " + admin);
        return admin;
    }

    @Override
    public void createOrder(Long productId, Long storeId, int quantity) {
        // 商品と店舗に対応するStoreProductを取得
        StoreProduct storeProduct = storeProductRepository.findByProductIdAndStoreId(productId, storeId);
        
        if (storeProduct == null) {
            throw new IllegalArgumentException("商品が見つかりません。");
        }

        // 新しい注文を作成
        Order order = new Order();
        order.setProduct(storeProduct.getProduct());
        order.setQuantity(quantity);
        order.setTotalPrice(storeProduct.getRetailPrice() * quantity);
        order.setStore(storeProduct.getStore());
        order.setAdmin(storeProduct.getStore().getAdmin());

        // 在庫を増やす
        storeProduct.setStock(storeProduct.getStock() + quantity);

        // 注文と更新されたStoreProductをデータベースに保存
        orderRepository.save(order);
        storeProductRepository.save(storeProduct);
    }
}

