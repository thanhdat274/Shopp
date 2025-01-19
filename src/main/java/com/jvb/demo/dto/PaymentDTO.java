package com.jvb.demo.dto;

import java.time.LocalDateTime;

import com.jvb.demo.entity.Payment;
import com.jvb.demo.enums.PaymentMethod;

public class PaymentDTO {
    private Long id;
    private LocalDateTime create_date;
    private String transaction_code;
    private String order_code;
    private Float amount;
    private PaymentMethod paymentMethod;

    public PaymentDTO() {
    }

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.create_date = payment.getCreate_date();
        this.transaction_code = payment.getTransaction_code();
        this.order_code = payment.getOrder_code();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
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

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
