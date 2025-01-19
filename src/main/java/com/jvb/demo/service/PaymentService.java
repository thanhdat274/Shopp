package com.jvb.demo.service;

import org.springframework.stereotype.Service;

import com.jvb.demo.entity.Payment;
import com.jvb.demo.enums.PaymentMethod;

@Service
public interface PaymentService {
    public Payment savePayment(Float amount, String orderCode, String transactionCode, PaymentMethod moethod);
}
