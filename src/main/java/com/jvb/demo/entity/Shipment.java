package com.jvb.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.jvb.demo.dto.ShipmentDTO;

@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String order_code;// ma don hang tren Giao hang nhanh
    private String name;
    private String email;
    private String phone;
    private Float ship_fee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expected_delivery_date;
    @OneToOne()
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    private String address;
    private String city_name;
    private Integer city_id;
    private String district_name;
    private Integer district_id;
    private String ward_name;
    private String ward_id;

    public Shipment() {
    }

    public Shipment(ShipmentDTO shipmentDTO) {
        this.order_code = shipmentDTO.getOrder_code();
        this.name = shipmentDTO.getName();
        this.email = shipmentDTO.getEmail();
        this.phone = shipmentDTO.getPhone();
        this.ship_fee = shipmentDTO.getShip_fee();
        this.expected_delivery_date = shipmentDTO.getExpected_delivery_date();
        this.address = shipmentDTO.getAddress();
        this.city_name = shipmentDTO.getCity_name();
        this.city_id = shipmentDTO.getCity_id();
        this.district_id = shipmentDTO.getDistrict_id();
        this.district_name = shipmentDTO.getDistrict_name();
        this.ward_id = shipmentDTO.getWard_id();
        this.ward_name = shipmentDTO.getWard_name();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getShip_fee() {
        return ship_fee;
    }

    public void setShip_fee(Float ship_fee) {
        this.ship_fee = ship_fee;
    }

    public LocalDateTime getExpected_delivery_date() {
        return expected_delivery_date;
    }

    public void setExpected_delivery_date(LocalDateTime expected_delivery_date) {
        this.expected_delivery_date = expected_delivery_date;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
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

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    
}
