package com.khahhann.backend.service.impl;

import com.khahhann.backend.config.JwtProvider;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
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
}
