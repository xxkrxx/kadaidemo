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
    private StoreProductRepository storeProductRepository; // 追加

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

    /*
     * 商品の発注を作成します。店舗に紐づく商品かどうかを確認します。
     */
    @Override
    public void createOrder(Long productId, Long storeId, int quantity) {
        // StoreProductを取得
        StoreProduct storeProduct = storeProductRepository.findByProductIdAndStoreId(productId, storeId);
        
        // 商品が存在しない、または在庫が不足している場合のエラーハンドリング
        if (storeProduct == null || storeProduct.getStock() < quantity) {
            throw new IllegalArgumentException("商品が見つからないか、在庫が不足しています。");
        }

        // 発注処理
        Order order = new Order();
        order.setProduct(storeProduct.getProduct()); // 商品をStoreProductから設定
        order.setQuantity(quantity);
        order.setTotalPrice(storeProduct.getRetailPrice() * quantity); // 合計金額を設定
        order.setStore(storeProduct.getStore()); // 正しい店舗を設定

        // 在庫を更新
        storeProduct.setStock(storeProduct.getStock() - quantity);
        orderRepository.save(order); // 注文をデータベースに保存
        storeProductRepository.save(storeProduct); // 在庫を更新して保存
    }
}


