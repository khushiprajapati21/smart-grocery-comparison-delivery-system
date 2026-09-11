package com.grocery.service;

import com.grocery.dto.ShopOwnerDashboardResponse;

public interface ShopOwnerService {

    ShopOwnerDashboardResponse getDashboard(Long ownerId);

}
