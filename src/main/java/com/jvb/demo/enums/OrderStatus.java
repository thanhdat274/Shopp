package com.jvb.demo.enums;

public enum OrderStatus {
    UNPAID,     //don hang chua thanh toan
    CANCELED,   //don hang bi huy boi khach hang
    REJECTED,   //don hang bi tu choi
    PROCESSING,    // don hang cho xac nhan
    CONFIRMED,  // don hang da dc xac nhan
    SHIPPING,   //dang giao hang    
    SHIPPED,  //giao hang thanh cong
    SHIPMENT_FAILED //giao hang that bai
}
