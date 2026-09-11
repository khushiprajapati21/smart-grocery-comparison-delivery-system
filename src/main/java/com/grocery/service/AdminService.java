package com.grocery.service;

import com.grocery.dto.AdminDashboardResponse;
import com.grocery.dto.RegisterRequest;
import com.grocery.entity.User;

public interface AdminService {

    AdminDashboardResponse getDashboard();


    User createShopOwner(RegisterRequest request);
}
