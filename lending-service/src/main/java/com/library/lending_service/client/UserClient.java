package com.library.lending_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// The 'name' must exactly match the application name of the User Service in its YAML file
@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Object getUserById(@PathVariable("id") Long id); // Returns the JSON user data
}