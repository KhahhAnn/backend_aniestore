package com.khahhann.backend.controller;

import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.ChangePasswordRequest;
import com.khahhann.backend.request.UserRequest;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:3000/")
public class UserController {
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfileHandle(@RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Users> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody UserRequest userRequest) throws  UserException{
        Users userUpdate = this.userService.updateUser( jwt, userRequest);
        return new ResponseEntity<>(userUpdate, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Users> changePassword(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest changePasswordRequest) throws  UserException{
        Users userUpdate = this.userService.changePassword( jwt, changePasswordRequest);
        return new ResponseEntity<>(userUpdate, HttpStatus.OK);
    }
}
