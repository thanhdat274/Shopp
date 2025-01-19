package com.jvb.demo.dto;

import com.jvb.demo.entity.Value;

public class ValueDTO {
    private long id;
    private String name;

    public ValueDTO(Value value) {
        this.id= value.getId();
        this.name = value.getName();
    }
    
    public ValueDTO() {
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
}
