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
    private ProductRepository productRepository; 

    @Autowired
    private StoreProductRepository storeProductRepository; 

    // 全てのProductをページング対応で取得
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // 検索条件に基づいてページング対応でProductを取得
    @Override
    public Page<Product> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Pageable pageable) {
        return productRepository.findProductsByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, pageable); // 検索条件に基づいてProductを取得
    }

    // IDでProductを取得
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Productを保存
    @Override
    public void saveProduct(Product product) {
        productRepository.save(product); 
    }

    // 指定されたIDのProductの在庫を更新
    @Override
    public void updateStock(Long productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId); 
        if (productOpt.isPresent()) { // Productが存在する場合
            Product product = productOpt.get();
            product.setStock(product.getStock() + quantity); // 在庫数を更新
            productRepository.save(product); // 更新されたProductを保存
        }
    }

    // 全てのProductを取得
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll(); 
    }

    // IDでStoreProductを取得
    @Override
    public Optional<StoreProduct> getStoreProductById(Long id) {
        return storeProductRepository.findById(id); 
    }

    // StoreProductを保存
    @Override
    public void saveStoreProduct(StoreProduct storeProduct) {
        storeProductRepository.save(storeProduct); 
    }

    // 全てのStoreProductを取得
    @Override
    public List<StoreProduct> getAllStoreProducts() {
        return storeProductRepository.findAll(); 
    }

    // 小カテゴリIDで商品を取得するメソッドを実装
    @Override
    public List<Product> findBySmallCategoryId(Long smallCategoryId) {
        return productRepository.findBySmallCategoryId(smallCategoryId);
    }
}


