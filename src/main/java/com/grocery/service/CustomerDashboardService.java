package com.grocery.service;

import com.grocery.dto.CustomerDashboardResponse;

public interface CustomerDashboardService {

    CustomerDashboardResponse getDashboard(Long customerId);

}
