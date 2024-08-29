package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // カスタムクエリを使用して、製品情報を含むすべての店舗を取得するメソッド
    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.storeProducts")
    List<Store> findAllStoresWithProducts();

    // IDで店舗を取得するメソッド（Optionalを使用）
    @Override
    Optional<Store> findById(Long id);
}