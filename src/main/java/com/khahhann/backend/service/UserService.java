package com.khahhann.backend.service;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    public Users findUserById(Long userId) throws UserException;
    public Users findUserProfileByJwt(String jwt) throws UserException;
}
