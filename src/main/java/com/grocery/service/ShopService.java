package com.grocery.service;

import java.util.List;

import com.grocery.dto.ShopRequest;
import com.grocery.dto.ShopResponse;

public interface ShopService {

    // Create Shop
    ShopResponse createShop(ShopRequest request);

    // Get Shop By Id
    ShopResponse getShopById(Long id);

    // Get All Shops
    List<ShopResponse> getAllShops();

    // Update Shop
    ShopResponse updateShop(Long id, ShopRequest request);

    // Delete Shop
    void deleteShop(Long id);

    // Get Shops By City
    List<ShopResponse> getShopsByCity(String city);
}
