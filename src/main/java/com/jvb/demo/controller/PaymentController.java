package com.jvb.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jvb.demo.enums.PaymentMethod;
import com.jvb.demo.service.MomoService;
import com.jvb.demo.service.PaymentService;
import com.jvb.demo.service.VnPayService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private VnPayService vnpayService;
    @Autowired
    private MomoService momoService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String createPayment(@RequestParam("order_id") Long order_id, @RequestParam("amount") Float amount,
            @RequestParam("payment_method") String payment_method, HttpServletRequest request) {
        String payment_url = null;
        switch (payment_method) {
            case "MOMO":
                payment_url = momoService.createPayment(order_id, amount);
                break;
            case "VNPAY":
                String ipAdress;
                ipAdress = request.getHeader("X-FORWARDED-FOR");
                if (ipAdress == null) {
                    ipAdress = request.getLocalAddr();
                }
                payment_url = vnpayService.createPayment(order_id, amount, ipAdress);
                break;
        }
        return "redirect:" + payment_url;
    }

    @GetMapping("/vnpay")
    public String paymentVnpayResult(@RequestParam("vnp_Amount") Float amount,
            @RequestParam("vnp_TxnRef") String orderCode,
            @RequestParam("vnp_TransactionNo") String transactionCode,
            @RequestParam("vnp_ResponseCode") String responseCode) {
        if (responseCode.equals("00")) { // 00 success code
            paymentService.savePayment(amount, orderCode, transactionCode, PaymentMethod.VNPAY);
        } else {
            System.out.println(responseCode);
        }
        Long order_id = Long.valueOf(orderCode.substring(14)); // orderCode format: yyyyMMddHHmmss + orderId
        return "redirect:../order/"+order_id;
    }

    @RequestMapping(value = "/momo", method = { RequestMethod.GET, RequestMethod.POST })
    public String paymentMomoResult(@RequestParam("amount") Float amount, @RequestParam("orderId") String orderCode,
            @RequestParam("transId") String transactionCode, @RequestParam("message") String message,
            @RequestParam("errorCode") String errorCode) {
        if (message.equalsIgnoreCase("Success")) {
            paymentService.savePayment(amount, orderCode, transactionCode, PaymentMethod.MOMO);
        } else {
            System.out.println(message);
        }
        Long order_id = Long.valueOf(orderCode.substring(14)); // orderCode format: yyyyMMddHHmmss + orderId
        return "redirect:../order/"+order_id;
    }

}
