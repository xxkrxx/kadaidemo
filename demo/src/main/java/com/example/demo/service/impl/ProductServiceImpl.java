package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.StoreProduct;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreProductRepository;
import com.example.demo.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j // ログ機能を有効にする
public class ProductServiceImpl implements ProductService {

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public Page<StoreProduct> getAllProducts(Pageable pageable) {
        log.debug("Fetching all store products with pagination.");
        return storeProductRepository.findAll(pageable);
    }

    @Override
    public Page<StoreProduct> findByCriteria(String search, Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, Long storeId, Pageable pageable) {
        if (storeId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            storeId = getCurrentUserStoreId(email);
            log.debug("Current user store ID: {}", storeId);
        }
        log.debug("Searching store products by criteria: search={}, largeCategoryId={}, middleCategoryId={}, smallCategoryId={}, storeId={}", 
                  search, largeCategoryId, middleCategoryId, smallCategoryId, storeId);
        return storeProductRepository.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, storeId, pageable);
    }

    @Override
    public Optional<StoreProduct> getStoreProductById(Long id) {
        log.debug("Fetching store product by ID: {}", id);
        return storeProductRepository.findById(id);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        log.debug("Fetching product by ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
    public void saveProduct(StoreProduct storeProduct) {
        log.debug("Saving store product: {}", storeProduct);
        storeProductRepository.save(storeProduct);
    }

    @Override
    public void updateStock(Long storeProductId, int quantity) {
        log.debug("Updating stock for store product ID: {}, quantity: {}", storeProductId, quantity);
        Optional<StoreProduct> productOpt = storeProductRepository.findById(storeProductId);
        if (productOpt.isPresent()) {
            StoreProduct product = productOpt.get();
            int newStock = product.getStock() + quantity;
            if (newStock < 0) {
                log.error("Insufficient stock for store product ID: {}", storeProductId);
                throw new IllegalArgumentException("在庫が不足しています");
            }
            product.setStock(newStock);
            storeProductRepository.save(product);
            log.debug("Updated stock for store product ID: {}", storeProductId);
        } else {
            log.error("Store product not found for ID: {}", storeProductId);
            throw new IllegalArgumentException("商品が見つかりませんでした");
        }
    }

    private Long getCurrentUserStoreId(String email) {
        log.debug("Fetching store ID for current user with email: {}", email);
        return administratorRepository.findByEmail(email)
                .map(admin -> admin.getStore().getId())
                .orElse(null);
    }

    @Override
    public List<StoreProduct> getAllStoreProducts() {
        log.debug("Fetching all store products.");
        return storeProductRepository.findAll();
    }

    @Override
    public List<StoreProduct> getProductsForStore(Long storeId) {
        log.debug("Fetching products for store ID: {}", storeId);
        return storeProductRepository.findByStoreId(storeId);
    }

    @Override
    public Page<StoreProduct> findByStoreId(Long storeId, Pageable pageable) {
        log.debug("Fetching store products with pagination for store ID: {}", storeId);
        return storeProductRepository.findByStoreId(storeId, pageable);
    }

    @Override
    public List<Product> getAllProducts() {
        log.debug("Fetching all products.");
        List<Product> products = productRepository.findAll();
        log.debug("Products: {}", products); // デバッグ用
        return products;
    }
    
    public List<ProductCategoryDTO> getProductCategories(Long storeId) {
        log.debug("Fetching product categories for store ID: {}", storeId);
        List<StoreProduct> storeProducts = storeProductRepository.findByStoreId(storeId);
        if (storeProducts.isEmpty()) {
            log.warn("No store products found for store ID: {}", storeId);
            return List.of(); // 空のリストを返す
        }
        
        return storeProducts.stream().map(storeProduct -> {
            Product product = storeProduct.getProduct();
            return new ProductCategoryDTO(
                product.getLargeCategory().getName(),
                product.getMiddleCategory().getName(),
                product.getSmallCategory().getName(),
                product.getName(),
                product.getDescription(),
                storeProduct.getCostPrice(),  
                storeProduct.getRetailPrice() 
            );
        }).collect(Collectors.toList());
    }
}