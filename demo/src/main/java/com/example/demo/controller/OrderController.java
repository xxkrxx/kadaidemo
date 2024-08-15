package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Administrator;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/new")
    public String newOrderForm(@RequestParam("productId") Long productId, Model model, Principal principal) {
        Optional<Product> productOpt = productService.getProductById(productId);

        if (productOpt.isEmpty()) {
            return "error/404"; // 商品が見つからない場合はエラーページ
        }

        Product product = productOpt.get();
        Order order = new Order();
        order.setProduct(product);

        model.addAttribute("order", order);

        if (principal != null) {
            String username = principal.getName();
            Administrator admin = orderService.findAdminByUsername(username);
            if (admin != null) {
                model.addAttribute("adminFullName", admin.getFirstName() + " " + admin.getLastName());
                model.addAttribute("storeName", admin.getStore() != null ? admin.getStore().getName() : "未設定");
            } else {
                model.addAttribute("adminFullName", "未設定");
                model.addAttribute("storeName", "未設定");
            }
        } else {
            model.addAttribute("adminFullName", "未設定");
            model.addAttribute("storeName", "未設定");
        }

        return "OrderForm"; // 注文フォームのビューを返す
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order, Principal principal, @RequestParam("productId") Long productId) {
        String username = principal.getName();
        Administrator admin = orderService.findAdminByUsername(username);

        if (admin == null) {
            return "error/500"; // 管理者が見つからない場合はエラーページ
        }

        Optional<Product> productOpt = productService.getProductById(productId);

        if (productOpt.isEmpty()) {
            return "error/404"; // 商品が見つからない場合はエラーページ
        }

        Product product = productOpt.get();
        order.setProduct(product);
        order.setAdmin(admin);
        order.setStore(admin.getStore());

        orderService.saveOrder(order);
        productService.updateStock(productId, order.getQuantity());

        return "redirect:/orders/history"; // 注文履歴ページにリダイレクト
    }

    @GetMapping("/history")
    public String listOrders(Model model, Principal principal) {
        String username = principal.getName();
        Administrator admin = orderService.findAdminByUsername(username);

        if (admin == null) {
            return "error/500"; // 管理者が見つからない場合はエラーページ
        }

        List<Order> orders = orderService.getOrdersByStore(admin.getStore());
        model.addAttribute("orders", orders);
        model.addAttribute("adminFullName", admin.getFirstName() + " " + admin.getLastName());
        model.addAttribute("storeName", admin.getStore() != null ? admin.getStore().getName() : "未設定");

        return "OrderHistory"; // 注文履歴のビューを返す
    }
}

