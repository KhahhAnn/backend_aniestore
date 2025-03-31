package com.khahhann.backend.controller;

import com.khahhann.backend.config.JwtProvider;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.ChangePasswordRequest;
import com.khahhann.backend.request.UserRequest;
import com.khahhann.backend.response.AuthResponse;
import com.khahhann.backend.service.AccountService;
import com.khahhann.backend.service.UserService;
import com.khahhann.backend.service.impl.CustomerUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:3000/")
public class UserController {
    private UserService userService;
    private AccountService accountService;
    private CustomerUserServiceImpl customerUserService;
    private JwtProvider jwtProvider;


    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfileHandle(@RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/profile-oauth")
    public ResponseEntity<Users> getUserProfileOauthHandle(@RequestParam String email) throws UserException {
        Users user = this.userService.findUserEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Users> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody UserRequest userRequest) throws  UserException{
        Users userUpdate = this.userService.updateUser( jwt, userRequest);
        return new ResponseEntity<>(userUpdate, HttpStatus.OK);
    }

    @GetMapping("/oauth-get-token")
    public ResponseEntity<AuthResponse> oauthGetToken(@RequestParam String email) throws UserException {
        Users user = this.userService.findUserEmail(email);
        Authentication authentication = oauth(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtProvider.generateToken(authentication);
        List<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        AuthResponse authResponse = new AuthResponse(token, "Login success", permissions);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Users> changePassword(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest changePasswordRequest) throws  UserException{
        Users userUpdate = this.userService.changePassword( jwt, changePasswordRequest);
        return new ResponseEntity<>(userUpdate, HttpStatus.OK);
    }

    private Authentication oauth(String username, String password) {
        if(!this.accountService.isActiveAccount(username)) {
            return null;
        }
        UserDetails userDetails = this.customerUserService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
