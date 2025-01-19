package com.jvb.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jvb.demo.entity.CartDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long>{
    @Transactional
    int deleteBySkuIdAndCartId(long skuId, long cartId);

    @Transactional
    @Modifying
    @Query(value = "delete from cart_detail where cart_id =:cartId", nativeQuery = true)
    int deleteByCartId(long cartId);

    CartDetail findByCartIdAndSkuId(long cartId, long skuId);

    @Transactional
    @Modifying
    @Query(value = "update cart_detail set quantity =:quantity where id =:cartDetail_id", nativeQuery = true)
    int updateQuantity(long cartDetail_id, int quantity);

    @Query(value = "select count(cart_detail.id) from account, cart, cart_detail "
        + "where account.email =:email and account.id = cart.account_id "
        + "and cart.id = cart_detail.cart_id", nativeQuery = true)
    int countByEmail(String email);
}
