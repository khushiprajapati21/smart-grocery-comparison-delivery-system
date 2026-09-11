package com.grocery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.grocery.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                	    .requestMatchers(
                	        "/",
                	        "/login",
                	        "/register",
                	        "/about",
                	        "/contact",
                	        "/customer/**",
                	        "/products/**",
                	        "/cart/**",
                	        "/orders/**",
                	        "/checkout",
                	        "/wishlist/**",
                	        "/css/**",
                	        "/js/**",
                	        "/images/**",
                	        "/icons/**",
                	        "/fonts/**",
                	        "/auth/**"
                	    ).permitAll()

                	    // API Cart
                	    .requestMatchers("/api/cart/**")
                	    .authenticated()

                	    // Admin
                	    .requestMatchers("/admin/**")
                	    .hasRole("ADMIN")

                	    // Shop Owner
                	    .requestMatchers(
                	        "/shops/**",
                	        "/inventory/**"
                	    ).hasRole("SHOP_OWNER")

                	    // Delivery
                	    .requestMatchers("/delivery/**")
                	    .hasRole("DELIVERY_AGENT")

                	    .anyRequest()
                	    .authenticated()
                	)

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

}