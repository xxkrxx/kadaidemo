package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.LargeCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.SmallCategory;

public interface CategoryService {
    List<LargeCategory> getAllLargeCategories();
    List<MiddleCategory> getAllMiddleCategories();
    List<SmallCategory> getAllSmallCategories();
}