package com.jvb.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.jvb.demo.dto.CartDTO;
import com.jvb.demo.dto.CartDetailDTO;
import com.jvb.demo.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String getCartPage(Principal principal, Model model) {
        CartDTO cartDTO = cartService.findByEmail(principal.getName());
        model.addAttribute("cartDTO", cartDTO);
        return "cart";
    }

    @PostMapping
    public RedirectView addToCart(@Param("sku_id") long sku_id, @Param("quantity") int quantity, Principal principal,
            RedirectAttributes redirect) {
        if(quantity <= 0)
            redirect.addAttribute("error", "Số lượng sản phẩm không hợp lệ");
        else {
            String email = principal.getName();
            String message = "success";
            try {
                cartService.addToCart(email, sku_id, quantity);
            } catch (Exception e) {
                message = "failed";
            }
            redirect.addAttribute("add", message);
        }    
        return new RedirectView("/cart");
    }

    @PostMapping("/update")
    public RedirectView updateCart(@ModelAttribute CartDTO cartDTO, RedirectAttributes redirect) {
        try {
            for(CartDetailDTO cartDetailDTO : cartDTO.getCartDetailDTOs()){
                if(cartDetailDTO.getQuantity() <= 0)
                    throw new Exception("Số lượng sản phẩm không hợp lệ");
            }
            cartService.updateCart(cartDTO.getCartDetailDTOs());
            redirect.addAttribute("update", "success");
        } catch (Exception e) {
            redirect.addAttribute("update", "failed");
        }
        return new RedirectView("/cart");
    }

    @PostMapping("/remove/item")
    public RedirectView removeCartItem(@Param("sku_id") long sku_id, Principal principal, RedirectAttributes redirect) {
        String message = "success";
        try {
            cartService.removeItem(sku_id, principal.getName());
        } catch (Exception e) {
            message = "failed";
        }
        redirect.addAttribute("remove", message);
        return new RedirectView("/cart");
    }

    @PostMapping("/remove")
    public RedirectView removeCart(Principal principal, RedirectAttributes redirect) {
        String message = "success";
        try {
            cartService.deleteCart(principal.getName());
        } catch (Exception e) {
            message = "failed";
        }
        redirect.addAttribute("remove", message);
        return new RedirectView("/cart");
    }
}
