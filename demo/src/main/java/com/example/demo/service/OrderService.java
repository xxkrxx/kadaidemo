package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Order;
import com.example.demo.entity.Store;

public interface OrderService {
    
    /*指定された Order オブジェクトを保存します。@param order 保存する Order オブジェクト*/
    void saveOrder(Order order);

    /* すべての Order オブジェクトを取得*/
    List<Order> getAllOrders();

    /* 指定された Store に関連する Order オブジェクトのリストを取得*/
    List<Order> getOrdersByStore(Store store);

    /* 指定された ID の Order オブジェクトを取得 */
    Order getOrderById(Long id);

    /* 指定されたユーザー名で管理者を検索 */
    Administrator findAdminByUsername(String username);
    
    /* 注文を作成するメソッド */
    void createOrder(Long productId, Long storeId, int quantity);
}
