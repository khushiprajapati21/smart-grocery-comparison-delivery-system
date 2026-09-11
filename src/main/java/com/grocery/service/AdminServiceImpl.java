package com.grocery.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.grocery.dto.AdminDashboardResponse;
import com.grocery.dto.RegisterRequest;
import com.grocery.entity.OrderStatus;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.repository.CategoryRepository;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.ShopRepository;
import com.grocery.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final ShopRepository shopRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminServiceImpl(UserRepository userRepository, ShopRepository shopRepository,
			CategoryRepository categoryRepository, ProductRepository productRepository, OrderRepository orderRepository,
			PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.shopRepository = shopRepository;
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AdminDashboardResponse getDashboard() {

		AdminDashboardResponse response = new AdminDashboardResponse();

		// Users
		response.setTotalUsers(userRepository.count());
		response.setTotalCustomers(userRepository.countByRole(Role.CUSTOMER));
		response.setTotalShopOwners(userRepository.countByRole(Role.SHOP_OWNER));
		response.setTotalDeliveryAgents(userRepository.countByRole(Role.DELIVERY_AGENT));

		// Shops
		response.setTotalShops(shopRepository.count());
		response.setActiveShops(shopRepository.countByActiveTrue());

		// Categories
		response.setTotalCategories(categoryRepository.count());

		// Products
		response.setTotalProducts(productRepository.count());
		response.setAvailableProducts(productRepository.countByAvailableTrue());

		// Orders
		response.setTotalOrders(orderRepository.count());

		return response;
	}

	@Override
	public User createShopOwner(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// Role admin khud decide karega
		user.setRole(Role.SHOP_OWNER);

		return userRepository.save(user);
	}
}