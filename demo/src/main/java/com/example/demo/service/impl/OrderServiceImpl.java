package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.ProductDetailDTO;
import com.example.demo.dto.StoreProductOrderDTO;
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
        order.setTotalPrice(product.getCostPrice() * order.getQuantity());
        orderRepository.save(order);
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
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(username);
        return adminOpt.orElseThrow(() -> new RuntimeException("指定されたユーザー名の管理者が見つかりません。"));
    }

    @Override
    public void createOrder(Long productId, Long storeId, int quantity) {
        List<StoreProduct> storeProducts = storeProductRepository.findByProductIdAndStoreId(productId, storeId);

        if (storeProducts == null || storeProducts.isEmpty()) {
            throw new IllegalArgumentException("商品が見つかりません。");
        }

        for (StoreProduct storeProduct : storeProducts) {
            Order order = new Order();
            order.setProduct(storeProduct.getProduct());
            order.setQuantity(quantity);
            order.setTotalPrice(storeProduct.getRetailPrice() * quantity);
            order.setStore(storeProduct.getStore());
            order.setAdmin(storeProduct.getStore().getAdmin());

            storeProduct.setStock(storeProduct.getStock() + quantity);

            orderRepository.save(order);
            storeProductRepository.save(storeProduct);
        }
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Product product = order.getProduct();
            Administrator admin = order.getAdmin();

            String productName = product.getName();
            String adminName = admin.getFirstName() + " " + admin.getLastName();
            int quantity = order.getQuantity();
            double totalPrice = order.getTotalPrice();

            return new OrderDetailDTO(productName, adminName, quantity, totalPrice);
        } else {
            throw new RuntimeException("注文が見つかりません。ID: " + orderId);
        }
    }

    @Override
    public StoreProductOrderDTO getStoreProductOrderDTO(Store store) {
        // Storeに関連するStoreProductエンティティを取得してDTOにマッピング
        List<ProductDetailDTO> productDetailDTOs = storeProductRepository.findByStore(store).stream()
                .map(storeProduct -> new ProductDetailDTO(
                        storeProduct.getProduct().getName(),
                        storeProduct.getRetailPrice(),
                        storeProduct.getStock()))
                .collect(Collectors.toList());

        // Storeに関連するOrderエンティティを取得してDTOにマッピング
        List<OrderDetailDTO> orderDetailDTOs = orderRepository.findByStore(store).stream()
                .map(order -> {
                    Product product = order.getProduct();
                    Administrator admin = order.getAdmin();
                    return new OrderDetailDTO(
                            product.getName(),
                            admin.getFirstName() + " " + admin.getLastName(),
                            order.getQuantity(),
                            order.getTotalPrice());
                }).collect(Collectors.toList());

        // 最終的にStoreProductOrderDTOを作成
        StoreProductOrderDTO dto = new StoreProductOrderDTO();
        dto.setStoreName(store.getName());
        dto.setAddress(store.getAddress());
        dto.setProducts(productDetailDTOs);
        dto.setOrders(orderDetailDTOs);
        return dto;
    }
}