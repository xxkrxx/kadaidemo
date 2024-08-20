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
    private StoreProductRepository storeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<StoreProduct> getAllProducts(Pageable pageable) {
        // すべてのStoreProductをページングして取得
        return storeProductRepository.findAll(pageable);
    }

    @Override
    public Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Long storeId, Pageable pageable) {
        if (storeId != null) {
            // 店舗IDが指定されている場合、その店舗の商品だけを取得
            return storeProductRepository.findByStoreId(storeId, pageable);
        } else {
            // 店舗IDが指定されていない場合、検索条件でフィルタリング
            return storeProductRepository.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, null, pageable);
        }
    }

    @Override
    public Optional<StoreProduct> getStoreProductById(Long id) {
        // IDでStoreProductを取得
        return storeProductRepository.findById(id);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        // IDでProductを取得
        return productRepository.findById(id);
    }

    @Override
    public void saveProduct(StoreProduct storeProduct) {
        // StoreProductを保存
        storeProductRepository.save(storeProduct);
    }

    @Override
    public void updateStock(Long storeProductId, int quantity) {
        Optional<StoreProduct> productOpt = storeProductRepository.findById(storeProductId);
        if (productOpt.isPresent()) {
            StoreProduct product = productOpt.get();
            int newStock = product.getStock() + quantity;
            if (newStock < 0) {
                throw new IllegalArgumentException("在庫が不足しています");
            }
            product.setStock(newStock);
            storeProductRepository.save(product);
        }
    }

    @Override
    public List<StoreProduct> getAllStoreProducts() {
        // すべてのStoreProductを取得
        return storeProductRepository.findAll();
    }

    @Override
    public List<Product> getProductsForStore(Long storeId) {
        // 店舗に関連する商品のリストを取得
        return productRepository.findByStoreId(storeId);
    }
}

