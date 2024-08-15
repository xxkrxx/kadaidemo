package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    /*
     * 指定された Order オブジェクトを保存します。
     * 商品と数量が設定されていることを確認します。
     */
    @Override
    public void saveOrder(Order order) {
        if (order.getProduct() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("商品と数量は注文を保存する前に設定する必要があります。");
        }

        Product product = order.getProduct();

        order.setTotalPrice(product.getCostPrice() * order.getQuantity()); // 合計金額を設定
        orderRepository.save(order); // 注文をデータベースに保存
    }

    /*
     * すべての Order オブジェクトを取得します。
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /*
     * 指定された Store に関連する Order オブジェクトのリストを取得します。
     */
    @Override
    public List<Order> getOrdersByStore(Store store) {
        return orderRepository.findByStore(store);
    }

    /*
     * 指定された ID の Order オブジェクトを取得します。
     */
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findByIdWithAdmin(id)
            .orElseThrow(() -> new RuntimeException("指定されたIDの注文が見つかりません。"));
    }

    /*
     * 指定されたユーザー名で管理者を検索します。
     */
    @Override
    public Administrator findAdminByUsername(String username) {
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(username);
        return adminOpt.orElseThrow(() -> new RuntimeException("指定されたユーザー名の管理者が見つかりません。"));
    }
}


