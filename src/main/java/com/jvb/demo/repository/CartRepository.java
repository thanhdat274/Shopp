package com.jvb.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jvb.demo.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByAccountEmail(String email);
    
}
