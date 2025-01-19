package com.jvb.demo.dto;

import java.time.LocalDateTime;

import com.jvb.demo.entity.Shipment;

public class ShipmentDTO {
    private long id;
    private String order_code;
    private String name;
    private String email;
    private String phone;
    private float ship_fee;
    private LocalDateTime expected_delivery_date;
    private String address;
    private String city_name;
    private int city_id;
    private String district_name;
    private int district_id;
    private String ward_name;
    private String ward_id;
    
    public ShipmentDTO() {
    }
    public ShipmentDTO(Shipment shipment) {
        this.id = shipment.getId();
        this.order_code = shipment.getOrder_code();
        this.name = shipment.getName();
        this.email = shipment.getEmail();
        this.phone = shipment.getPhone();
        this.ship_fee = shipment.getShip_fee();
        this.expected_delivery_date = shipment.getExpected_delivery_date();
        this.address = shipment.getAddress();
        this.city_name = shipment.getCity_name();
        this.city_id = shipment.getCity_id();
        this.district_name = shipment.getDistrict_name();
        this.district_id = shipment.getDistrict_id();
        this.ward_name = shipment.getWard_name();
        this.ward_id = shipment.getWard_id();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getShip_fee() {
        return ship_fee;
    }

    public void setShip_fee(float ship_fee) {
        this.ship_fee = ship_fee;
    }

    public LocalDateTime getExpected_delivery_date() {
        return expected_delivery_date;
    }

    public void setExpected_delivery_date(LocalDateTime expected_delivery_date) {
        this.expected_delivery_date = expected_delivery_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }
    public String getOrder_code() {
        return order_code;
    }
    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    
}
