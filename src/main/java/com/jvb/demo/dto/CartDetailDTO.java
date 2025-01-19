package com.jvb.demo.dto;

import com.jvb.demo.entity.CartDetail;

public class CartDetailDTO {
    private long id;
    private SkuDTO skuDTO;
    private ProductDTO productDTO;
    private int quantity;

    public CartDetailDTO() {
    }

    public CartDetailDTO(CartDetail cartDetail) {
        this.id = cartDetail.getId();
        this.quantity = cartDetail.getQuantity();
        this.skuDTO = new SkuDTO(cartDetail.getSku());
        this.productDTO = new ProductDTO(cartDetail.getProduct());
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

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

}
