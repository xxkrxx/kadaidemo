package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.LargeCategory;

@Repository
public interface LargeCategoryRepository extends JpaRepository<LargeCategory, Long> {
    List<LargeCategory> findAll();
}