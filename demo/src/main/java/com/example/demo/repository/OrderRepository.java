package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.Store;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 店舗に基づいて注文リストを取得するメソッド
    // 指定された店舗に関連するすべての注文をリストで返します
    List<Order> findByStore(Store store);

    // 注文IDに基づいて注文を取得し、関連する管理者情報も一緒に取得するクエリ
    // 注文IDを指定すると、その注文と関連する管理者情報が含まれるOptional<Order>を返します
    @Query("SELECT o FROM Order o JOIN FETCH o.admin WHERE o.id = :id")
    Optional<Order> findByIdWithAdmin(@Param("id") Long id);
}
