package com.jvb.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface MomoService {
    public String createPayment(Long order_id, Float amount);
}
