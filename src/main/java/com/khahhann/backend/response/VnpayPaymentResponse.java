package com.khahhann.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VnpayPaymentResponse implements Serializable {
    private String status;
    private String message;
    private String URL;
}
