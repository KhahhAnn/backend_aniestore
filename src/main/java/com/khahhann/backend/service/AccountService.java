package com.khahhann.backend.service;

import com.khahhann.backend.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    public ResponseEntity<?> register(Users user);

    public String createActiveCode();

    public void sendMailActiveCode(String email, String activeCode);

    public ResponseEntity<?> activeAccount(String email, String activeCode);

    public boolean isActiveAccount(String email);

}
