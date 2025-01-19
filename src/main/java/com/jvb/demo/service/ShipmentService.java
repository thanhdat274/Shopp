package com.jvb.demo.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.jvb.demo.entity.Order;

@Service
public interface ShipmentService {
    public float calculateShipFee(int to_district_id, String to_ward_code);
    public Map<String, String> createGHNOrder(Order order) throws Exception;
    public boolean cancelGHNOrder(String orderCode) throws Exception;
}
