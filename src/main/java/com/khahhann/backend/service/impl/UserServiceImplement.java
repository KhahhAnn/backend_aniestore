package com.khahhann.backend.service.impl;

import com.khahhann.backend.config.JwtProvider;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.request.ChangePasswordRequest;
import com.khahhann.backend.request.UserRequest;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Service
@AllArgsConstructor
@CrossOrigin("*")
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    @Override
    public Users findUserById(Long userId) throws UserException {
        Optional<Users> user = this.userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("user not found with id - " + userId);
    }

    @Override
    public Users findUserProfileByJwt(String jwt) throws UserException {
        String email = this.jwtProvider.getEmailFromToken(jwt);
        Users user = this.userRepository.findByEmail(email);
        if(user == null) {
            throw new UserException("user not found with email - " + email);
        }
        return user;
    }

    @Override
    public Users updateUser(String jwt, UserRequest userRequest) throws UserException {
        if(this.findUserProfileByJwt(jwt) != null) {
            Users existUser = this.findUserProfileByJwt(jwt);
            existUser.setLastName(userRequest.getUser().getLastName());
            existUser.setFirstName(userRequest.getUser().getFirstName());
            existUser.setEmail(userRequest.getUser().getEmail());
            existUser.setImageSrc(userRequest.getImg());
            return this.userRepository.saveAndFlush(existUser);
        }
        return null;
    }

    @Override
    public Users changePassword(String jwt, ChangePasswordRequest changePasswordRequest) throws UserException {
        if(this.findUserProfileByJwt(jwt) != null) {
            Users existUser = this.findUserProfileByJwt(jwt);
            if(passwordEncoder.matches(existUser.getPassword(), changePasswordRequest.getCurrentPassword())) {
                return null;
            }
                String encodePassword = this.passwordEncoder.encode(changePasswordRequest.getPassword());
                existUser.setPassword(encodePassword);
                return this.userRepository.saveAndFlush(existUser);
        }
        return null;
    }

    @Override
    public ApiResponse addNewUser(Users user) {
        ApiResponse apiResponse = new ApiResponse();
        if(this.userRepository.findByEmail(user.getEmail()) != null) {
            apiResponse.setMessage("Email đã được đăng ký vui lòng nhập email khác!");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.saveAndFlush(user);
        apiResponse.setMessage("Lưu tài khoản thành công!");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    @Override
    public Users updateUser(Users user) {
        Users existUser = this.userRepository.findByEmail(user.getEmail());
        if(existUser == null) {
            return null;
        }
        existUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existUser.setActive(user.isActive());
        existUser.setLastName(user.getLastName());
        existUser.setFirstName(user.getFirstName());
        existUser.setImageSrc(user.getImageSrc());
        existUser.setMobile(user.getMobile());
        return this.userRepository.saveAndFlush(existUser);
    }

    @Override
    public Users findUserEmail(String email) throws UserException {
        Users user = this.userRepository.findByEmail(email);
        if(user == null) {
            throw new UserException("user not found with email - " + email);
        }
        return user;
    }
}
