package com.jvb.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jvb.demo.dto.OrderDTO;
import com.jvb.demo.entity.Order;
import com.jvb.demo.enums.OrderStatus;

@Service
public interface OrderService {
    public OrderDTO createOrderInfor(String email);
    public OrderDTO saveOrder(OrderDTO orderDTO, String email);
    public Order findById(Long id);
    public Page<OrderDTO> findByEmail(String email, String status, Pageable pageable);
    public Page<OrderDTO> findAll(String status,String fromDate, String toDate, Pageable pageable);
    public boolean checkOrderQuantity(Order order);
    public OrderDTO acceptOrder(Long orderID) throws Exception;
    public OrderDTO updateOrderStatus(Long orderID, String status) throws Exception;
    public OrderDTO rejectOrder(Long orderID) throws Exception;
    public OrderDTO cancelOrder(Long orderID) throws Exception;
    public List<Object[]> getRevenuaByMonth();
    public List<Object[]> getRevenuaByDayOfWeek();
    public Map<String, Map<String,Integer>> countOrderStatusByMonth();
    public Map<String, Map<String,Integer>> countOrderStatusByDayOfWeek();
    public Integer countByOrderStatus(OrderStatus orderStatus);
    public Double getTodayRevenua();
    public Double getTodayRefund();
}
