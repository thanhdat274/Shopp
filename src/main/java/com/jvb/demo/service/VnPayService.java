package com.jvb.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface VnPayService {
    public String createPayment(Long order_id, Float vnp_Amount, String ip_address);
}
