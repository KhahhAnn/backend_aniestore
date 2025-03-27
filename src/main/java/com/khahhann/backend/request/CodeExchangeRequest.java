package com.khahhann.backend.request;

import lombok.Data;

@Data
public class CodeExchangeRequest {
    private String code;
    private String redirectUri;
}
