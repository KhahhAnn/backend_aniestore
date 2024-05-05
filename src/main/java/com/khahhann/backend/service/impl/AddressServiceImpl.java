package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.AddressRepository;
import com.khahhann.backend.service.AddressService;
import com.khahhann.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Address> getAllAddress(Long userId)  throws UserException {
        Users user = this.userService.findUserById(userId);
        if(user == null) {
            throw new UserException("User not found!");
        }
        return user.getAddressList();
    }
}
