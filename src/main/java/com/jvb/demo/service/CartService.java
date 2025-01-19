package com.jvb.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jvb.demo.dto.CartDTO;
import com.jvb.demo.dto.CartDetailDTO;

@Service
public interface CartService {
    public void addToCart(String email, long sku_id, int quantity) throws Exception;
    public CartDTO findByEmail(String email);
    public int removeItem(long sku_id, String email)throws Exception;
    public int deleteCart(String email)throws Exception;
    public void updateCart(List<CartDetailDTO> list)throws Exception;
}
