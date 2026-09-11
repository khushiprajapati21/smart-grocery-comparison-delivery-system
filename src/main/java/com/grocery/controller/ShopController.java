package com.grocery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.ShopRequest;
import com.grocery.dto.ShopResponse;
import com.grocery.service.ShopService;

@RestController
@RequestMapping("/shops")
public class ShopController {

	private final ShopService shopService;

	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}

	/**
	 * Create Shop
	 */
	@PostMapping
	public ResponseEntity<ShopResponse> createShop(@RequestBody ShopRequest request) {

		ShopResponse response = shopService.createShop(request);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Get Shop By Id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ShopResponse> getShopById(@PathVariable Long id) {

		return ResponseEntity.ok(shopService.getShopById(id));
	}

	/**
	 * Get All Shops
	 */
	@GetMapping
	public ResponseEntity<List<ShopResponse>> getAllShops() {

		return ResponseEntity.ok(shopService.getAllShops());
	}

	/**
	 * Update Shop
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ShopResponse> updateShop(@PathVariable Long id, @RequestBody ShopRequest request) {

		return ResponseEntity.ok(shopService.updateShop(id, request));
	}

	/**
	 * Soft Delete Shop
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteShop(@PathVariable Long id) {

		shopService.deleteShop(id);

		return ResponseEntity.ok("Shop deleted successfully.");
	}

	/**
	 * Get Shops By City
	 */
	@GetMapping("/city/{city}")
	public ResponseEntity<List<ShopResponse>> getShopsByCity(@PathVariable String city) {

		return ResponseEntity.ok(shopService.getShopsByCity(city));
	}

}
