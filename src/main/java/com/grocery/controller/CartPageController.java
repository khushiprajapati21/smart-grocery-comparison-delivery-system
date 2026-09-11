package com.grocery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartPageController {

    @GetMapping("/cart")
    public String cartPage() {

        return "customer/cart";
    }
    
    @GetMapping("/checkout")
    public String checkoutPage() {
        return "customer/checkout";
    }
    
    @GetMapping("/orders/success")
    public String orderSuccessPage() {
        return "customer/order-success";
    }
    
    @GetMapping("/orders")
    public String ordersPage() {
        return "customer/orders";
    }
}