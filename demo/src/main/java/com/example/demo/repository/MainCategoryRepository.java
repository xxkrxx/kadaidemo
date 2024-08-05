package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MainCategory;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
}