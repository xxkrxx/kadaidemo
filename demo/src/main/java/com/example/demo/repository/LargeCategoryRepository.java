package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LargeCategory;

public interface LargeCategoryRepository extends JpaRepository<LargeCategory, Long> {
}
