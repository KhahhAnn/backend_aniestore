package com.khahhann.backend.controller;

import com.khahhann.backend.config.JwtProvider;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.request.LoginRequest;
import com.khahhann.backend.response.AuthResponse;
import com.khahhann.backend.service.AccountService;
import com.khahhann.backend.service.impl.CustomerUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin("**")
public class AuthController {
    private CustomerUserServiceImpl userService;
    private AccountService accountService;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody Users user) {
        ResponseEntity<?> response = this.accountService.register(user);
        return response;
    }

    @GetMapping("/active/{email}/{activeCode}")
    public ResponseEntity<?> active(@PathVariable String email, @PathVariable String activeCode) {
        ResponseEntity<?> response = this.accountService.activeAccount(email, activeCode);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandle(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token, "Login success");
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        if(!this.accountService.isActiveAccount(username)) {
            return null;
        }
        UserDetails userDetails = this.userService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
