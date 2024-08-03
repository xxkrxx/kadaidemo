package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Store;

public interface StoreService {
    List<Store> getAllStores();
    List<Store> getAllStoresWithProducts(); 
    Optional<Store> getStoreById(Long id);
    Store createStore(Store store);
    Store updateStore(Store store);
    void deleteStore(Long id);
    boolean existsById(Long id);
}