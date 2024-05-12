package com.khahhann.backend.repository;

import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("http://localhost:3000/")
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem  ci WHERE ci.cart=:cart AND ci.product=:product AND ci.size=:size")
    CartItem isCartItemExist(@Param("cart") Cart cart,
                             @Param("product") Product product,
                             @Param("size") String size);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.id = :id")
    void deleteCartItemById(@Param("id") Long id);
}
