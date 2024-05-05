package com.khahhann.backend.controller;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.service.AddressService;
import com.khahhann.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAllAddress(@RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        List<Address> addresses =  this.addressService.getAllAddress(user.getId());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
}
