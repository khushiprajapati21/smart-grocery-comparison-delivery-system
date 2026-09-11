package com.grocery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grocery.dto.CategoryRequest;
import com.grocery.dto.CategoryResponse;
import com.grocery.entity.Category;
import com.grocery.entity.Shop;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CategoryRepository;
import com.grocery.repository.ShopRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ShopRepository shopRepository) {

        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
    }

    /**
     * Create Category
     */
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        if (categoryRepository.existsByCategoryNameAndShop(
                request.getCategoryName(), shop)) {

            throw new DuplicateResourceException(
                    "Category already exists in this shop.");
        }

        Category category = new Category();

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setShop(shop);
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    /**
     * Get Category By Id
     */
    @Override
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        return mapToResponse(category);
    }

    /**
     * Get All Categories
     */
    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get Categories By Shop
     */
    @Override
    public List<CategoryResponse> getCategoriesByShop(Long shopId) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        return categoryRepository.findByShopAndActiveTrue(shop)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update Category
     */
    @Override
    public CategoryResponse updateCategory(Long id,
                                           CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        if (!category.getCategoryName().equals(request.getCategoryName())
                && categoryRepository.existsByCategoryNameAndShop(
                        request.getCategoryName(), shop)) {

            throw new DuplicateResourceException(
                    "Category already exists in this shop.");
        }

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setShop(shop);

        Category updatedCategory = categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }

    /**
     * Soft Delete Category
     */
    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        category.setActive(false);

        categoryRepository.save(category);
    }

    /**
     * Convert Entity to DTO
     */
    private CategoryResponse mapToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());

        response.setShopId(category.getShop().getId());
        response.setShopName(category.getShop().getShopName());

        response.setActive(category.isActive());
        response.setCreatedAt(category.getCreatedAt());

        return response;
    }

}
