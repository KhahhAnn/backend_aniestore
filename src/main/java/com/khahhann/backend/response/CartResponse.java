package com.khahhann.backend.response;

import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Cart cart;
    private Page<CartItem> cartItems;
}
