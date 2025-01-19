package com.jvb.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jvb.demo.entity.Order;
import com.jvb.demo.entity.OrderDetail;
import com.jvb.demo.enums.OrderStatus;

public class OrderDTO {
    private long id;
    private LocalDateTime create_date;
    private String customer_note;
    private float total_price;
    private List<OrderDetailDTO> orderDetailDTOs;
    private ShipmentDTO shipmentDTO;
    private OrderStatus status;
    private PaymentDTO paymentDTO;
    private Integer isPaid;
    private Integer isRefund;
    private LocalDateTime confirm_order_date;
    private LocalDateTime confirm_shipping_date;
    private LocalDateTime confirm_shipped_date;
    private LocalDateTime shipping_failed_date;
    private LocalDateTime canceled_date;
    private LocalDateTime rejected_date;

    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.customer_note = order.getCustomer_note();
        this.total_price = order.getTotal_price();
        this.create_date = order.getCreate_date();
        orderDetailDTOs = new ArrayList<>();
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetailDTOs.add(new OrderDetailDTO(orderDetail));
        }
        this.shipmentDTO = new ShipmentDTO(order.getShipment());
        this.status = order.getOrderStatus();
        this.isPaid = order.getIsPaid();
        if (order.getPayment() != null)
            this.paymentDTO = new PaymentDTO(order.getPayment());
        this.isRefund = order.getIsRefund();
        this.confirm_order_date = order.getConfirm_order_date();
        this.confirm_shipping_date = order.getConfirm_shipping_date();
        this.confirm_shipped_date = order.getConfirm_shipped_date();
        this.shipping_failed_date = order.getShipping_failed_date();
        this.canceled_date = order.getCanceled_date();
        this.rejected_date = order.getRejected_date();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public String getCustomer_note() {
        return customer_note;
    }

    public void setCustomer_note(String customer_note) {
        this.customer_note = customer_note;
    }

    public List<OrderDetailDTO> getOrderDetailDTOs() {
        return orderDetailDTOs;
    }

    public void setOrderDetailDTOs(List<OrderDetailDTO> orderDetailDTOs) {
        this.orderDetailDTOs = orderDetailDTOs;
    }

    public ShipmentDTO getShipmentDTO() {
        return shipmentDTO;
    }

    public void setShipmentDTO(ShipmentDTO shipmentDTO) {
        this.shipmentDTO = shipmentDTO;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    public void setPaymentDTO(PaymentDTO paymentDTO) {
        this.paymentDTO = paymentDTO;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public LocalDateTime getConfirm_order_date() {
        return confirm_order_date;
    }

    public void setConfirm_order_date(LocalDateTime confirm_order_date) {
        this.confirm_order_date = confirm_order_date;
    }

    public LocalDateTime getConfirm_shipping_date() {
        return confirm_shipping_date;
    }

    public void setConfirm_shipping_date(LocalDateTime confirm_shipping_date) {
        this.confirm_shipping_date = confirm_shipping_date;
    }

    public LocalDateTime getConfirm_shipped_date() {
        return confirm_shipped_date;
    }

    public void setConfirm_shipped_date(LocalDateTime confirm_shipped_date) {
        this.confirm_shipped_date = confirm_shipped_date;
    }

    public LocalDateTime getShipping_failed_date() {
        return shipping_failed_date;
    }

    public void setShipping_failed_date(LocalDateTime shipping_failed_date) {
        this.shipping_failed_date = shipping_failed_date;
    }

    public LocalDateTime getCanceled_date() {
        return canceled_date;
    }

    public void setCanceled_date(LocalDateTime canceled_date) {
        this.canceled_date = canceled_date;
    }

    public LocalDateTime getRejected_date() {
        return rejected_date;
    }

    public void setRejected_date(LocalDateTime rejected_date) {
        this.rejected_date = rejected_date;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

}
