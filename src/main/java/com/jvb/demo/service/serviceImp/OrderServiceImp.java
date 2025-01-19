package com.jvb.demo.service.serviceImp;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jvb.demo.dto.OrderDTO;
import com.jvb.demo.dto.ShipmentDTO;
import com.jvb.demo.entity.Account;
import com.jvb.demo.entity.Cart;
import com.jvb.demo.entity.CartDetail;
import com.jvb.demo.entity.Order;
import com.jvb.demo.entity.OrderDetail;
import com.jvb.demo.entity.Shipment;
import com.jvb.demo.entity.Sku;
import com.jvb.demo.enums.OrderStatus;
import com.jvb.demo.repository.AccountRepository;
import com.jvb.demo.repository.CartRepository;
import com.jvb.demo.repository.OrderRepository;
import com.jvb.demo.service.CartService;
import com.jvb.demo.service.OrderService;
import com.jvb.demo.service.ShipmentService;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private CartService cartService;

    @Override
    public OrderDTO createOrderInfor(String email) {
        OrderDTO orderDTO = new OrderDTO();
        Account account = accountRepo.findByEmail(email);
        ShipmentDTO shipmentDTO = new ShipmentDTO();
        shipmentDTO.setName(account.getName());
        shipmentDTO.setPhone(account.getPhone());
        shipmentDTO.setEmail(email);
        shipmentDTO.setAddress(account.getAddress());
        shipmentDTO.setCity_id(account.getCity_id());
        shipmentDTO.setCity_name(account.getCity_name());
        shipmentDTO.setDistrict_id(account.getDistrict_id());
        shipmentDTO.setDistrict_name(account.getDistrict_name());
        shipmentDTO.setWard_id(account.getWard_id());
        shipmentDTO.setWard_name(account.getWard_name());
        orderDTO.setShipmentDTO(shipmentDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO, String email) {
        try {
            Order order = new Order();
            List<OrderDetail> orderDetails = new ArrayList<>();
            Shipment shipment = new Shipment(orderDTO.getShipmentDTO());
            float ship_fee = shipmentService.calculateShipFee(orderDTO.getShipmentDTO().getDistrict_id(),
                    orderDTO.getShipmentDTO().getWard_id());
            Cart cart = cartRepo.findByAccountEmail(email);
            for (CartDetail cartDetail : cart.getCartDetails()) {
                OrderDetail orderDetail = new OrderDetail(cartDetail);
                orderDetail.setOrder(order);
                orderDetails.add(orderDetail);
            }
            shipment.setShip_fee(ship_fee);
            shipment.setOrder(order);
            order.setOrderDetails(orderDetails);
            order.setCreate_date(LocalDateTime.now());
            order.setAccount(accountRepo.findByEmail(email));
            order.setShipment(shipment);
            order.calculateTotalPrice();
            order.setIsPaid(0);
            order.setIsRefund(0);
            order.setOrderStatus(OrderStatus.UNPAID);
            Order result = orderRepo.save(order);
            cartService.deleteCart(email);
            return new OrderDTO(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<OrderDTO> findByEmail(String email, String status, Pageable pageable) {
        Page<Order> orders = orderRepo.getByEmail(email, status, pageable);
        Page<OrderDTO> orderDTOs = orders.map(order -> new OrderDTO(order));
        return orderDTOs;
    }

    @Override
    public Order findById(Long id) {
        Order order = null;
        try {
            order = orderRepo.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public Page<OrderDTO> findAll(String status, String fromDate, String toDate, Pageable pageable) {
        if(!isDateValid(fromDate)) fromDate = null;
        if(!isDateValid(toDate)) toDate = null;
        Page<Order> orders = orderRepo.findByOrderStatus(status,fromDate, toDate, pageable);
        Page<OrderDTO> orderDTOs = orders.map(order -> new OrderDTO(order));
        return orderDTOs;
    }

    public boolean isDateValid(String date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkOrderQuantity(Order order){
        for(OrderDetail orderDetail : order.getOrderDetails()){
            int orderQuantity = orderDetail.getQuantity();
            int productQuantity = orderDetail.getSku().getQuantity();
            if(orderQuantity > productQuantity)
                return false;
        }
        return true;
    }

    @Override
    public OrderDTO acceptOrder(Long orderID) throws Exception{
            Order order = orderRepo.findById(orderID).get();
            Map<String, String> result = shipmentService.createGHNOrder(order);
            Shipment shipment = order.getShipment();
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(result.get("expected_delivery_time"));
            LocalDateTime delivery_date = zonedDateTime.toLocalDateTime();
            shipment.setExpected_delivery_date(delivery_date.plusSeconds(1l));
            shipment.setShip_fee(Float.parseFloat(result.get("ship_fee")));
            shipment.setOrder_code(result.get("order_code"));
            order.setShipment(shipment);
            order.setOrderStatus(OrderStatus.CONFIRMED);
            order.setConfirm_order_date(LocalDateTime.now());

            // //update product quantity
            List<OrderDetail> listDetail = new ArrayList<>();
            for(OrderDetail orderDetail : order.getOrderDetails()){
                Sku sku = orderDetail.getSku();
                sku.setQuantity(sku.getQuantity() - orderDetail.getQuantity());
                orderDetail.setSku(sku);
                listDetail.add(orderDetail);
            }
            order.setOrderDetails(listDetail);
            return new OrderDTO(orderRepo.save(order));
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderID, String status) throws Exception{
        Order order = orderRepo.findById(orderID)
            .orElseThrow(() -> new EntityNotFoundException("order id "+orderID+" not found"));
        String orderStatus = order.getOrderStatus().name();
        switch(status){
            case "SHIPPING":
                if(orderStatus.equalsIgnoreCase("CONFIRMED")){
                    order.setOrderStatus(OrderStatus.SHIPPING);
                    order.setConfirm_shipping_date(LocalDateTime.now());
                    return new OrderDTO(orderRepo.save(order));
                }else 
                    return null;
            case "SHIPPED":
                if(orderStatus.equalsIgnoreCase("SHIPPING")){
                    order.setOrderStatus(OrderStatus.SHIPPED);
                    order.setConfirm_shipped_date(LocalDateTime.now());
                    return new OrderDTO(orderRepo.save(order));
                }else 
                    return null;
            case "SHIPMENT_FAILED":
                if(orderStatus.equalsIgnoreCase("SHIPPING")){
                    boolean result = shipmentService.cancelGHNOrder(order.getShipment().getOrder_code());
                    if(!result)
                        return null;
                    List<OrderDetail> listDetail = restoreProductQuantity(order);
                    order.setIsRefund(1);
                    order.setOrderDetails(listDetail);
                    order.setOrderStatus(OrderStatus.SHIPMENT_FAILED);
                    order.setShipping_failed_date(LocalDateTime.now());
                    return new OrderDTO(orderRepo.save(order));
                }else 
                    return null;
            default:
                return null;
        }
    }

    @Override
    public OrderDTO rejectOrder(Long orderID) throws Exception{
        Order order = orderRepo.findById(orderID).get();
        if(order.getShipment().getOrder_code() != null){
            boolean result = shipmentService.cancelGHNOrder(order.getShipment().getOrder_code());
            if(!result)     // failed to cancel GHN order
                return null;
        }
        //restore product quantity when order status is confirmed or shipping
        String orderStatus = order.getOrderStatus().name();
        if(orderStatus.equalsIgnoreCase("CONFIRMED") || orderStatus.equalsIgnoreCase("SHIPPING")){
            List<OrderDetail> listDetail = restoreProductQuantity(order);
            order.setOrderDetails(listDetail);
        }
        order.setIsRefund(1);
        order.setOrderStatus(OrderStatus.REJECTED);
        order.setRejected_date(LocalDateTime.now());
        return new OrderDTO(orderRepo.save(order));
    }
    
    @Override
    public OrderDTO cancelOrder(Long orderID) throws Exception{
        Order order = orderRepo.findById(orderID).get();
        String orderstatus = order.getOrderStatus().name();
        if(orderstatus.equalsIgnoreCase("CONFIRMED") || orderstatus.equalsIgnoreCase("SHIPPING")){
            boolean cancelGHNresult = shipmentService.cancelGHNOrder(order.getShipment().getOrder_code());
            if(!cancelGHNresult)
                throw new Exception("Failed to cancel GHN order");
            List<OrderDetail> listDetail = restoreProductQuantity(order);
            order.setOrderDetails(listDetail);
        }
        if(order.getIsPaid() == 1)
            order.setIsRefund(1);
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setCanceled_date(LocalDateTime.now());
        return new OrderDTO(orderRepo.save(order));
    }

   public List<OrderDetail> restoreProductQuantity(Order order){
        List<OrderDetail> listDetail = new ArrayList<>();
        for(OrderDetail orderDetail : order.getOrderDetails()){
            Sku sku = orderDetail.getSku();
            sku.setQuantity(sku.getQuantity() + orderDetail.getQuantity());
            orderDetail.setSku(sku);
            listDetail.add(orderDetail);
        }
        return listDetail;
    }

    @Override
    public Integer countByOrderStatus(OrderStatus orderStatus) {
        return orderRepo.countByOrderStatus(orderStatus);
    }

    @Override
    public List<Object[]> getRevenuaByMonth() {
        return orderRepo.getRevenuaByMonth();
    }

    @Override
    public List<Object[]> getRevenuaByDayOfWeek() {
        return orderRepo.getRevenuaByDayofWeek();
    }

    @Override
    public Double getTodayRevenua() {
        return orderRepo.getTodayRevenua();
    }

    @Override
    public Double getTodayRefund() {
        return orderRepo.getTodayRefund();
    }

    //Map<Month, Map<OrderStatus, statusCount>>
    //Object[] data = [statusCount, orderStatus, month]
    @Override
    public Map<String, Map<String,Integer>> countOrderStatusByMonth() {
        Map<String, Map<String,Integer>> result = new HashMap<>();
        List<Object[]> data = orderRepo.countOrderStatusByMonth();
        for(int i =1 ; i<=12 ;i++){
            Map<String,Integer> map = new HashMap<>();
            map.put("SHIPPED", 0);
            map.put("SHIPMENT_FAILED", 0);
            map.put("CANCELED", 0);
            map.put("REJECTED", 0);
            result.put(String.valueOf(i), map);
        }
        for(Object[] object : data){
            result.get(object[2].toString()).replace(object[1].toString(), Integer.parseInt(object[0].toString()));
        }
        return result;
    }

    // Map<DayofWeek, Map<OrderStatus, statusCount>>
    //Object[] data = [statusCount, orderStatus, dayofweek]
    @Override
    public Map<String, Map<String,Integer>> countOrderStatusByDayOfWeek() {
        Map<String, Map<String,Integer>> result = new HashMap<>();
        List<Object[]> data = orderRepo.countOrderStatusByDayOfWeek();
        for(int i = 1;i<=7;i++){
            Map<String,Integer> map = new HashMap<>();
            map.put("SHIPPED", 0);
            map.put("SHIPMENT_FAILED", 0);
            map.put("CANCELED", 0);
            map.put("REJECTED", 0);
            result.put(String.valueOf(i), map);
        }
        for(Object[] object : data){
            result.get(object[2].toString()).replace(object[1].toString(), Integer.parseInt(object[0].toString()));
        }
        return result;
    }
}
