package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Order;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.Store;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    /*
     指定された Order オブジェクトを保存
     商品と数量が設定されていることを確認し、在庫を更新. 
     */
    @Override
    public void saveOrder(Order order) {
        if (order.getProductDetail() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("ProductDetail and quantity must be set before saving an order.");
        }

        ProductDetail productDetail = order.getProductDetail();
        int newStock = productDetail.getStock() + order.getQuantity(); // 在庫数を加算

        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for the product.");
        }

        productDetail.setStock(newStock); // 更新後の在庫数を設定
        productDetailRepository.save(productDetail); // 在庫数をデータベースに保存

        order.setTotalPrice(productDetail.getCostPrice() * order.getQuantity()); // 合計金額を設定
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order); // 注文をデータベースに保存
    }

    /* すべての Order オブジェクトを取得 */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /* 指定された Store に関連する Order オブジェクトのリストを取得 */
    @Override
    public List<Order> getOrdersByStore(Store store) {
        return orderRepository.findByStore(store);
    }

    /* 指定された ID の Order オブジェクトを取得します。*/
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findByIdWithAdmin(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /* 指定されたユーザー名で管理者を検索します。*/
    @Override
    public Administrator findAdminByUsername(String username) {
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(username);
        return adminOpt.orElseThrow(() -> new RuntimeException("管理者が見つかりませんでした"));
    }
}
