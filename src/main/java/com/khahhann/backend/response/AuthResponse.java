package com.khahhann.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private List<String> permission;
}
