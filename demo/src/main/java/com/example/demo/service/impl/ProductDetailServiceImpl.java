package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.service.ProductDetailService;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public Optional<ProductDetail> getProductDetailById(Long id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public void saveProductDetail(ProductDetail productDetail) {
        productDetailRepository.save(productDetail);
    }

    @Override
    public void updateStock(Long productDetailId, int quantity) {
        Optional<ProductDetail> productDetailOpt = productDetailRepository.findById(productDetailId);
        if (productDetailOpt.isPresent()) {
            ProductDetail productDetail = productDetailOpt.get();
            int newStock = productDetail.getStock() + quantity;
            if (newStock < 0) {
                throw new IllegalArgumentException("Insufficient stock for the product detail.");
            }
            productDetail.setStock(newStock);
            productDetailRepository.save(productDetail);
        } else {
            throw new IllegalArgumentException("Product detail not found.");
        }
    }
}
