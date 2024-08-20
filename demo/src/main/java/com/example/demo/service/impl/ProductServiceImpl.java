package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private StoreProductRepository storeProductRepository; // StoreProductリポジトリのインジェクション

    @Autowired
    private ProductRepository productRepository; // Productリポジトリのインジェクション

    // ページネーションされたStoreProductのリストを取得
    @Override
    public Page<StoreProduct> getAllProducts(Pageable pageable) {
        return storeProductRepository.findAll(pageable);
    }

    // 検索条件に基づいてページネーションされたStoreProductのリストを取得
    @Override
    public Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Pageable pageable) {
        return storeProductRepository.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, pageable);
    }

    // 指定されたIDのStoreProductを取得
    @Override
    public Optional<StoreProduct> getStoreProductById(Long id) {
        return storeProductRepository.findById(id);
    }

    // 指定されたIDのProductを取得
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // StoreProductを保存
    @Override
    public void saveProduct(StoreProduct storeProduct) {
        storeProductRepository.save(storeProduct);
    }

    // 指定されたStoreProductのIDの在庫数量を更新
    @Override
    public void updateStock(Long storeProductId, int quantity) {
        Optional<StoreProduct> productOpt = storeProductRepository.findById(storeProductId);
        if (productOpt.isPresent()) {
            StoreProduct product = productOpt.get();
            product.setStock(product.getStock() + quantity); // 在庫数量を更新
            storeProductRepository.save(product); // 更新されたStoreProductを保存
        }
    }

    // すべてのStoreProductを取得
    @Override
    public List<StoreProduct> getAllStoreProducts() {
        return storeProductRepository.findAll();
    }
}

