package com.khahhann.backend.service;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.ChangePasswordRequest;
import com.khahhann.backend.request.UserRequest;
import com.khahhann.backend.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


@Service
@CrossOrigin("*")
public interface UserService {
    Users findUserById(Long userId) throws UserException;
    Users findUserProfileByJwt(String jwt) throws UserException;
    Users updateUser(String jwt, UserRequest userRequest) throws UserException;
    Users changePassword(String jwt, ChangePasswordRequest changePasswordRequest) throws  UserException;
    ApiResponse addNewUser(Users user);
    Users updateUser(Users user);
}
