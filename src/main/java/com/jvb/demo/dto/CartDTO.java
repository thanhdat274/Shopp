package com.jvb.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.jvb.demo.entity.Cart;
import com.jvb.demo.entity.CartDetail;

public class CartDTO {
    private long id;
    private List<CartDetailDTO> cartDetailDTOs;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        cartDetailDTOs = new ArrayList<>();
        for (CartDetail cartDetail : cart.getCartDetails()) {
            cartDetailDTOs.add(new CartDetailDTO(cartDetail));
        }
    }

    public CartDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CartDetailDTO> getCartDetailDTOs() {
        return cartDetailDTOs;
    }

    public void setCartDetailDTOs(List<CartDetailDTO> cartDetailDTOs) {
        this.cartDetailDTOs = cartDetailDTOs;
    }

}
