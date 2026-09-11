package com.grocery.service;

import com.grocery.dto.LoginRequest;
import com.grocery.dto.LoginResponse;
import com.grocery.dto.RegisterRequest;
import com.grocery.entity.User;

public interface UserService {

    User register(RegisterRequest request);
    
    LoginResponse login(LoginRequest request);
}