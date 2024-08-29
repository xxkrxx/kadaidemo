package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Store;
import com.example.demo.entity.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

    // 検索条件に基づいて StoreProduct エンティティのページネーションされたリストを取得するメソッド
    @Query("SELECT sp FROM StoreProduct sp WHERE " +
            "(:search IS NULL OR sp.product.name LIKE %:search%) AND " +
            "(:largeCategoryId IS NULL OR sp.product.largeCategory.id = :largeCategoryId) AND " +
            "(:middleCategoryId IS NULL OR sp.product.middleCategory.id = :middleCategoryId) AND " +
            "(:smallCategoryId IS NULL OR sp.product.smallCategory.id = :smallCategoryId) AND " +
            "(:storeId IS NULL OR sp.store.id = :storeId)")
    Page<StoreProduct> findByCriteria(@Param("search") String search,
                                      @Param("largeCategoryId") Long largeCategoryId,
                                      @Param("middleCategoryId") Long middleCategoryId,
                                      @Param("smallCategoryId") Long smallCategoryId,
                                      @Param("storeId") Long storeId,
                                      Pageable pageable);

    // 店舗IDで関連する全てのStoreProductをページングせずに取得するメソッド
    @Query("SELECT sp FROM StoreProduct sp WHERE sp.store = :store")
    List<StoreProduct> findByStore(@Param("store") Store store);

    // プロダクトIDと店舗IDに基づいてStoreProductを取得するメソッド
    List<StoreProduct> findByProductIdAndStoreId(Long productId, Long storeId);
    
    // 店舗IDでStoreProductのリストをページネーションして取得するメソッド
    @Query("SELECT sp FROM StoreProduct sp WHERE sp.store.id = :storeId")
    Page<StoreProduct> findByStoreId(@Param("storeId") Long storeId, Pageable pageable);

    // 店舗IDでStoreProductのリストを取得するメソッド
    List<StoreProduct> findByStoreId(Long storeId);
}