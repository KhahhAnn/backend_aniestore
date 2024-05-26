package com.khahhann.backend.service.impl;

import com.khahhann.backend.config.JwtProvider;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.request.ChangePasswordRequest;
import com.khahhann.backend.request.UserRequest;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
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
            String encodeCurrentPass = this.passwordEncoder.encode(changePasswordRequest.getCurrentPassword());
            System.out.println(changePasswordRequest.getCurrentPassword());
            System.out.println(encodeCurrentPass);
            if(passwordEncoder.matches(existUser.getPassword(), changePasswordRequest.getCurrentPassword())) {
                return null;
            }
                String encodePassword = this.passwordEncoder.encode(changePasswordRequest.getPassword());
                existUser.setPassword(encodePassword);
                return this.userRepository.saveAndFlush(existUser);
        }
        return null;
    }
}
