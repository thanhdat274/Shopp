package com.jvb.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.jvb.demo.entity.Sku;
import com.jvb.demo.entity.VariantValue;

public class SkuDTO {
    private long id;
    private long product_id;
    private String sku;
    private Integer quantity;
    private Float price;
    private List<VariantValueDTO> variantValueDTO;
    private ProductDTO productDTO;

    public SkuDTO(Sku sku) {
        this.id = sku.getId();
        this.product_id = sku.getProduct().getId();
        this.sku = sku.getSku();
        List<VariantValueDTO> variantValueDTO = new ArrayList<>();
        if( sku.getVariant_values() != null)
            for (VariantValue variantValue : sku.getVariant_values()) {
                variantValueDTO.add(new VariantValueDTO(variantValue));
            }
        this.variantValueDTO = variantValueDTO;
        this.quantity = sku.getQuantity();
        this.price = sku.getPrice();
    }

    public SkuDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<VariantValueDTO> getVariantValueDTO() {
        return variantValueDTO;
    }

    public void setVariantValueDTO(List<VariantValueDTO> variantValueDTO) {
        this.variantValueDTO = variantValueDTO;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

}
