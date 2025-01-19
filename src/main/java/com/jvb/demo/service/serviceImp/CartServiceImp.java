package com.jvb.demo.service.serviceImp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvb.demo.dto.CartDTO;
import com.jvb.demo.dto.CartDetailDTO;
import com.jvb.demo.entity.Cart;
import com.jvb.demo.entity.CartDetail;
import com.jvb.demo.entity.Sku;
import com.jvb.demo.repository.AccountRepository;
import com.jvb.demo.repository.CartDetailRepository;
import com.jvb.demo.repository.CartRepository;
import com.jvb.demo.repository.SkuRepository;
import com.jvb.demo.service.CartService;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private SkuRepository skuRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CartDetailRepository cartDetailRepo;

    @Override
    public void addToCart(String email, long sku_id, int quantity) throws Exception {
        Sku sku = skuRepo.findById(sku_id).get();
        Cart cart = cartRepo.findByAccountEmail(email);
        try {
            if (cart == null) {
                cart = new Cart();
                List<CartDetail> cartDetails = new ArrayList<>();
                cartDetails.add(new CartDetail(sku, cart, quantity, sku.getProduct()));
                cart.setCartDetails(cartDetails);
                cart.setAccount(accountRepo.findByEmail(email));
                cartRepo.save(cart);
            } else {
                CartDetail cartDetail = cartDetailRepo.findByCartIdAndSkuId(cart.getId(), sku_id);
                if (cartDetail == null) {
                    System.out.println("null");
                    cartDetail = new CartDetail(sku, cart, quantity, sku.getProduct());
                } else {
                    System.out.println(cartDetail.getId());
                    cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                }
                cartDetailRepo.save(cartDetail);
            }
        } catch (Exception e) {
            throw new Exception("Exception", e);
        }

    }

    @Override
    public CartDTO findByEmail(String email) {
        try {
            Cart cart = cartRepo.findByAccountEmail(email);
            Collections.reverse(cart.getCartDetails());
            return new CartDTO(cart);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int removeItem(long sku_id, String email) throws Exception {
        try {
            long cart_id = cartRepo.findByAccountEmail(email).getId();
            return cartDetailRepo.deleteBySkuIdAndCartId(sku_id, cart_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception", e);
        }
    }

    @Override
    public int deleteCart(String email) throws Exception {
        try {
            long cart_id = cartRepo.findByAccountEmail(email).getId();
            int result = cartDetailRepo.deleteByCartId(cart_id);
            return result;
        } catch (Exception e) {
            throw new Exception("Exception", e);
        }
    }

    @Override
    public void updateCart(List<CartDetailDTO> list) throws Exception {
        try {
            for (CartDetailDTO detailDTO : list) {
                if (detailDTO.getQuantity() <= skuRepo.findById(detailDTO.getSkuDTO().getId()).get().getQuantity())
                    cartDetailRepo.updateQuantity(detailDTO.getId(), detailDTO.getQuantity());
            }
        } catch (Exception e) {
            throw new Exception("Exception", e);
        }
    }

}
