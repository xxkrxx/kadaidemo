package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

    // 検索条件に基づいて StoreProduct エンティティのページネーションされたリストを取得するメソッド
    @Query("SELECT sp FROM StoreProduct sp WHERE " +
            "(:search IS NULL OR sp.product.name LIKE %:search%) AND " + // 商品名で検索（部分一致）
            "(:largeCategoryId IS NULL OR sp.product.largeCategory.id = :largeCategoryId) AND " + // 大カテゴリで検索
            "(:middleCategoryId IS NULL OR sp.product.middleCategory.id = :middleCategoryId) AND " + // 中カテゴリで検索
            "(:smallCategoryId IS NULL OR sp.product.smallCategory.id = :smallCategoryId) AND " + // 小カテゴリで検索
            "(:storeId IS NULL OR sp.store.id = :storeId)") // 店舗IDで検索
    Page<StoreProduct> findByCriteria(@Param("search") String search,
                                      @Param("largeCategoryId") Long largeCategoryId,
                                      @Param("middleCategoryId") Long middleCategoryId,
                                      @Param("smallCategoryId") Long smallCategoryId,
                                      @Param("storeId") Long storeId,
                                      Pageable pageable);

    // 店舗IDでStoreProductをページングして取得するメソッド
    Page<StoreProduct> findByStoreId(Long storeId, Pageable pageable);

    // 店舗IDで関連する全てのStoreProductを取得するメソッド
    @Query("SELECT sp FROM StoreProduct sp WHERE sp.store.id = :storeId")
    List<StoreProduct> findProductsByStoreId(@Param("storeId") Long storeId);
    
    StoreProduct findByProductIdAndStoreId(Long productId, Long storeId);
}
