package com.khahhann.backend.controller;

import com.khahhann.backend.model.Users;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/customer")
public class AdminCustomerController {
    private UserService userService;
    @PostMapping()
    public ApiResponse addUser(@RequestBody Users user) {
        return this.userService.addNewUser(user);
    }

    @PutMapping()
    public Users updateUser(@RequestBody Users user) {
        return this.userService.updateUser(user);
    }
}
