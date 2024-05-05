package com.khahhann.backend.service;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    public List<Address> getAllAddress(Long userId) throws UserException;

}
