package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MiddleCategory;

public interface MiddleCategoryRepository extends JpaRepository<MiddleCategory, Long>{
    List<MiddleCategory> findByLargeCategory_Id(Long largeCategoryId);
}