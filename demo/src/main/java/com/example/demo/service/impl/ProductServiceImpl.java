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
        if (storeId != null) {
            return storeProductRepository.findByStoreId(storeId, pageable);
        } else {
            return storeProductRepository.findByCriteria(search, largeCategoryId, middleCategoryId, smallCategoryId, null, pageable);
        }
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
        }
    }

    @Override
    public List<StoreProduct> getAllStoreProducts() {
        return storeProductRepository.findAll();
    }

    @Override
    public List<Product> getProductsForStore(Long storeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // ユーザーのメールアドレスを取得
        
        Long currentStoreId = getCurrentUserStoreId(email);

        List<StoreProduct> storeProducts = storeProductRepository.findByStoreId(currentStoreId, Pageable.unpaged()).getContent();
        return storeProducts.stream()
                            .map(StoreProduct::getProduct)
                            .collect(Collectors.toList());
    }

    private Long getCurrentUserStoreId(String email) {
        return administratorRepository.findByEmail(email)
                                      .map(admin -> admin.getStore().getId())
                                      .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    }

    @Override
    public Page<StoreProduct> findByStoreId(Long storeId, Pageable pageable) {
        return storeProductRepository.findByStoreId(storeId, pageable);
    }
}


