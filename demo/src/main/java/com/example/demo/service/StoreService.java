package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Store;

public interface StoreService {
    // 全ての店舗を取得するメソッド
    List<Store> getAllStores();
    
    // 製品情報を含む全ての店舗を取得するメソッド
    List<Store> getAllStoresWithProducts(); 
    
    // IDによって特定の店舗を取得するメソッド
    Optional<Store> getStoreById(Long id);
    
    // 新しい店舗を作成するメソッド
    Store createStore(Store store);
    
    // 既存の店舗情報を更新するメソッド
    Store updateStore(Store store);
    
    // IDによって特定の店舗を削除するメソッド
    void deleteStore(Long id);
    
    // IDによって特定の店舗が存在するかどうかを確認するメソッド
    boolean existsById(Long id);
}
