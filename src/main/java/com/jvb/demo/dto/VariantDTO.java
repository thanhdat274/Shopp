package com.jvb.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.jvb.demo.entity.Value;
import com.jvb.demo.entity.Variant;

public class VariantDTO {
    private long id;
    private String name;
    private List<ValueDTO> valueDTO;

    public VariantDTO(Variant variant) {
        this.id = variant.getId();
        this.name= variant.getName();
        List<ValueDTO> valueDTO = new ArrayList<>();
        for(Value value : variant.getValues()){
            valueDTO.add(new ValueDTO(value));
        }
        this.valueDTO = valueDTO;
    }
    
    public VariantDTO() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ValueDTO> getValueDTO() {
        return valueDTO;
    }
    public void setValueDTO(List<ValueDTO> valueDTO) {
        this.valueDTO = valueDTO;
    }

}
