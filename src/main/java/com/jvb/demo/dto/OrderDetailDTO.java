package com.jvb.demo.dto;

import com.jvb.demo.entity.OrderDetail;

public class OrderDetailDTO {
    private long id;
    private SkuDTO skuDTO;
    private ProductDTO productDTO;
    private int quantity;
    private float price;
    
    public OrderDetailDTO() {
    }

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.skuDTO = new SkuDTO(orderDetail.getSku());
        this.productDTO = new ProductDTO(orderDetail.getProduct());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SkuDTO getSkuDTO() {
        return skuDTO;
    }

    public void setSkuDTO(SkuDTO skuDTO) {
        this.skuDTO = skuDTO;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    
}
