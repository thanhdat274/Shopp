package com.jvb.demo.dto;

import com.jvb.demo.entity.VariantValue;

public class VariantValueDTO {
    private long id;
    private String variant_name;
    private String variant_value;
    private long variant_id;
    private long value_id;

    

    public VariantValueDTO(VariantValue variantValue) {
        this.id = variantValue.getId();
        this.variant_name = variantValue.getVariant().getName();
        this.variant_value = variantValue.getValue().getName();
        this.variant_id = variantValue.getVariant().getId();
        this.value_id = variantValue.getValue().getId();
    }
    public VariantValueDTO(VariantValueDTO variantValueDTO) {
        this.id = variantValueDTO.getId();

    }
    public VariantValueDTO() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getVariant_name() {
        return variant_name;
    }
    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }
    public String getVariant_value() {
        return variant_value;
    }
    public void setVariant_value(String variant_value) {
        this.variant_value = variant_value;
    }
    public long getVariant_id() {
        return variant_id;
    }
    public void setVariant_id(long variant_id) {
        this.variant_id = variant_id;
    }
    public long getValue_id() {
        return value_id;
    }
    public void setValue_id(long value_id) {
        this.value_id = value_id;
    }

}
