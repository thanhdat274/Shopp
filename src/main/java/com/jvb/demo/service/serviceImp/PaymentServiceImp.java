package com.jvb.demo.service.serviceImp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvb.demo.entity.Order;
import com.jvb.demo.entity.Payment;
import com.jvb.demo.enums.OrderStatus;
import com.jvb.demo.enums.PaymentMethod;
import com.jvb.demo.repository.OrderRepository;
import com.jvb.demo.repository.PaymentRepository;
import com.jvb.demo.service.PaymentService;

@Service
public class PaymentServiceImp implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Override
    public Payment savePayment(Float amount, String orderCode, String transactionCode, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setCreate_date(LocalDateTime.now());
        payment.setTransaction_code(transactionCode);
        payment.setOrder_code(orderCode);
        switch(method.toString()){
            case "VNPAY":
                payment.setAmount(amount/100);
                payment.setPaymentMethod(PaymentMethod.VNPAY);
                break;
            case "MOMO":
                payment.setAmount(amount);
                payment.setPaymentMethod(PaymentMethod.MOMO);
                break;
        }
        try {
            Long order_id = Long.valueOf(orderCode.substring(14)); // orderCode format: yyyyMMddHHmmss + orderId
            Order order = orderRepo.findById(order_id).get();
            order.setOrderStatus(OrderStatus.PROCESSING);
            order.setIsPaid(1);
            payment.setOrder(order);
            return paymentRepo.save(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
