package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public Page<StoreProduct> getAllProducts(Pageable pageable) {
        return storeProductRepository.findAll(pageable);
    }

    @Override
    public Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Long storeId, Pageable pageable) {
        // StoreId が指定されていない場合、現在ログイン中のユーザーの店舗IDを取得
        if (storeId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            storeId = getCurrentUserStoreId(email);  // 現在ログイン中のユーザーの店舗IDを取得
        }
        
        // すべての検索条件を考慮して StoreProduct を検索
        return storeProductRepository.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, storeId, pageable);
    }

    @Override
    public Optional<StoreProduct> getStoreProductById(Long id) {
        return storeProductRepository.findById(id);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void saveProduct(StoreProduct storeProduct) {
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
        } else {
            throw new IllegalArgumentException("商品が見つかりませんでした");
        }
    }

    // 現在ログインしているユーザーの店舗IDを取得するメソッド
    private Long getCurrentUserStoreId(String email) {
        return administratorRepository.findByEmail(email)
                .map(admin -> admin.getStore().getId())
                .orElse(null);
    }

    @Override
    public List<StoreProduct> getAllStoreProducts() {
        // すべての StoreProduct を取得して返す
        return storeProductRepository.findAll();
    }

    @Override
    public List<Product> getProductsForStore(Long storeId) {
        // storeId に基づいて Product のリストを取得
        return productRepository.findByStoreId(storeId);
    }

    @Override
    public Page<StoreProduct> findByStoreId(Long storeId, Pageable pageable) {
        // storeId に基づいて StoreProduct のページネーションされたリストを取得
        return storeProductRepository.findByStoreId(storeId, pageable);
    }
}

