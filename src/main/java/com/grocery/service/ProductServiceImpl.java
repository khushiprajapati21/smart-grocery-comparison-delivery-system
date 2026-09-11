package com.grocery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grocery.dto.ProductRequest;
import com.grocery.dto.ProductResponse;
import com.grocery.entity.Category;
import com.grocery.entity.Product;
import com.grocery.entity.Shop;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CategoryRepository;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.ShopRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ShopRepository shopRepository,
                              CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Create Product
     */
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        // Verify category belongs to selected shop
        if (!category.getShop().getId().equals(shop.getId())) {
            throw new BadRequestException(
                    "Category does not belong to selected shop.");
        }

        // Duplicate product validation
        if (productRepository.existsByProductNameAndCategory(
                request.getProductName(), category)) {

            throw new DuplicateResourceException(
                    "Product already exists in this category.");
        }

        // Price validation
        if (request.getPrice() <= 0) {
            throw new BadRequestException(
                    "Price must be greater than zero.");
        }

        // Stock validation
        if (request.getStockQuantity() < 0) {
            throw new BadRequestException(
                    "Stock quantity cannot be negative.");
        }

        Product product = new Product();

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setUnit(request.getUnit());

        product.setShop(shop);
        product.setCategory(category);

        product.setAvailable(request.getStockQuantity() > 0);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    /**
     * Get Product By Id
     */
    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        return mapToResponse(product);
    }

    /**
     * Get All Products
     */
    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get Products By Shop
     */
    @Override
    public List<ProductResponse> getProductsByShop(Long shopId) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        return productRepository.findByShopAndActiveTrue(shop)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get Products By Category
     */
    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        return productRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update Product
     */
    @Override
    public ProductResponse updateProduct(Long id,
                                         ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found."));

        if (!category.getShop().getId().equals(shop.getId())) {
            throw new BadRequestException(
                    "Category does not belong to selected shop.");
        }

        if (!product.getProductName().equals(request.getProductName())
                && productRepository.existsByProductNameAndCategory(
                        request.getProductName(), category)) {

            throw new DuplicateResourceException(
                    "Product already exists in this category.");
        }

        if (request.getPrice() <= 0) {
            throw new BadRequestException(
                    "Price must be greater than zero.");
        }

        if (request.getStockQuantity() < 0) {
            throw new BadRequestException(
                    "Stock quantity cannot be negative.");
        }

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setUnit(request.getUnit());

        product.setShop(shop);
        product.setCategory(category);

        product.setAvailable(request.getStockQuantity() > 0);

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    /**
     * Soft Delete Product
     */
    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        product.setActive(false);

        productRepository.save(product);
    }

    /**
     * Entity -> DTO
     */
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setUnit(product.getUnit());

        response.setAvailable(product.getAvailable());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());

        response.setShopId(product.getShop().getId());
        response.setShopName(product.getShop().getShopName());

        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getCategoryName());

        return response;
    }

}
