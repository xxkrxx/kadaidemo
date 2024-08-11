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
import com.example.demo.entity.ProductDetail;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductDetailService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/new")
    public String newOrderForm(@RequestParam("productDetailId") Long productDetailId, Model model, Principal principal) {
        Optional<ProductDetail> productDetailOpt = productDetailService.getProductDetailById(productDetailId);
        if (!productDetailOpt.isPresent()) {
            return "error/404";
        }

        ProductDetail productDetail = productDetailOpt.get();

        // 新しい注文オブジェクトを作成し、商品詳細を設定
        Order order = new Order();
        order.setProductDetail(productDetail);

        model.addAttribute("order", order);

        // ログイン中の管理者のユーザー名を取得
        if (principal != null) {
            String username = principal.getName();
            Administrator admin = orderService.findAdminByUsername(username);
            if (admin != null) {
                model.addAttribute("adminFullName", admin.getFirstName() + " " + admin.getLastName());
                model.addAttribute("storeName", admin.getStore().getName());
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
    public String createOrder(@ModelAttribute Order order, Principal principal, @RequestParam("productDetailId") Long productDetailId) {
        // ログイン中の管理者のユーザー名を取得し、管理者情報を取得
        String username = principal.getName();
        Administrator admin = orderService.findAdminByUsername(username);
        if (admin != null) {
            // 指定された商品詳細IDで商品情報を取得
            Optional<ProductDetail> productDetailOpt = productDetailService.getProductDetailById(productDetailId);
            if (productDetailOpt.isPresent()) {
                ProductDetail productDetail = productDetailOpt.get();
                // 注文オブジェクトに商品詳細、管理者、店舗情報をセット
                order.setProductDetail(productDetail);
                order.setAdmin(admin);
                order.setStore(admin.getStore());
                // 注文を保存し、在庫を更新
                orderService.saveOrder(order);
                productDetailService.updateStock(productDetailId, order.getQuantity());
                return "redirect:/orders/history"; // 注文履歴ページにリダイレクト
            } else {
                return "error/404"; // 商品詳細が見つからない場合はエラーページを表示
            }
        } else {
            return "error/500"; // 管理者が見つからない場合はエラーページを表示
        }
    }

    // 注文履歴を表示するメソッド
    @GetMapping("/history")
    public String listOrders(Model model, Principal principal) {
        // ログイン中の管理者のユーザー名を取得し、管理者情報を取得
        String username = principal.getName();
        Administrator admin = orderService.findAdminByUsername(username);
        if (admin != null) {
            // 管理者の店舗に関連する注文のリストを取得
            List<Order> orders = orderService.getOrdersByStore(admin.getStore());
            model.addAttribute("orders", orders);
            model.addAttribute("adminFullName", admin.getFirstName() + " " + admin.getLastName());
            model.addAttribute("storeName", admin.getStore().getName());
            return "OrderHistory"; // 注文履歴のビューを返す
        } else {
            return "error/500"; // 管理者が見つからない場合はエラーページを表示
        }
    }
}

