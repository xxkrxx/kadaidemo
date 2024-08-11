package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findBySmallCategoryId(Long smallCategoryId);

    /* 検索条件に基づいて Product エンティティのページネーションされたリストを取得するメソッド */
    @Query("SELECT p FROM Product p WHERE " +
           "(:search IS NULL OR p.name LIKE %:search% OR p.manufacturer.name LIKE %:search%) AND " +
           "(:largeCategoryId IS NULL OR p.largeCategory.id = :largeCategoryId) AND " +
           "(:middleCategoryId IS NULL OR p.middleCategory.id = :middleCategoryId) AND " +
           "(:smallCategoryId IS NULL OR p.smallCategory.id = :smallCategoryId)")
    Page<Product> findProductsByCriteria(@Param("search") String search,
                                         @Param("largeCategoryId") Long largeCategoryId,
                                         @Param("middleCategoryId") Long middleCategoryId,
                                         @Param("smallCategoryId") Long smallCategoryId,
                                         Pageable pageable);
}
