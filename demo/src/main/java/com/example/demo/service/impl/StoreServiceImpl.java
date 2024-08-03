package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Store;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    // StoreRepositoryを注入する
    @Autowired
    private StoreRepository storeRepository;

    // すべての店舗情報を取得する
    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // 製品情報を含むすべての店舗情報を取得する
    @Override
    public List<Store> getAllStoresWithProducts() {
        return storeRepository.findAllStoresWithProducts();
    }

    // 指定されたIDの店舗情報を取得する
    @Override
    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    // 新しい店舗情報を作成する
    @Override
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    // 既存の店舗情報を更新する
    @Override
    public Store updateStore(Store store) {
        return storeRepository.save(store);
    }

    // 指定されたIDの店舗情報を削除する
    @Override
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    // 指定されたIDの店舗情報が存在するかを確認する
    @Override
    public boolean existsById(Long id) {
        return storeRepository.existsById(id);
    }
}
