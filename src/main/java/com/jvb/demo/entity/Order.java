package com.jvb.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.jvb.demo.enums.OrderStatus;

@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_date;

    @Column(name = "customer_note")
    private String customer_note;

    private Float total_price;

    private Integer isPaid; // 0: unpaid, 1:paid

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Shipment shipment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    private Integer isRefund;
    private LocalDateTime confirm_order_date;
    private LocalDateTime confirm_shipping_date;
    private LocalDateTime confirm_shipped_date;
    private LocalDateTime shipping_failed_date;
    private LocalDateTime canceled_date;
    private LocalDateTime rejected_date;

    public Order() {
    }

    public void calculateTotalPrice() {
        float totalPrice = 0;
        for (OrderDetail orderDetail : this.getOrderDetails()) {
            totalPrice += orderDetail.getPrice() * orderDetail.getQuantity();
        }
        totalPrice += this.getShipment().getShip_fee();
        this.total_price = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Float total_price) {
        this.total_price = total_price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
