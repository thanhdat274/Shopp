package com.jvb.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jvb.demo.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
