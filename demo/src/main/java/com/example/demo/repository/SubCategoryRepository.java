package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}