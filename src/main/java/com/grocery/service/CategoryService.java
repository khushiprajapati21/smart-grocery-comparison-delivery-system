package com.grocery.service;

import java.util.List;

import com.grocery.dto.CategoryRequest;
import com.grocery.dto.CategoryResponse;

public interface CategoryService {

    // Create Category
    CategoryResponse createCategory(CategoryRequest request);

    // Get Category By ID
    CategoryResponse getCategoryById(Long id);

    // Get All Categories
    List<CategoryResponse> getAllCategories();

    // Get Categories By Shop
    List<CategoryResponse> getCategoriesByShop(Long shopId);

    // Update Category
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    // Soft Delete Category
    void deleteCategory(Long id);
}
