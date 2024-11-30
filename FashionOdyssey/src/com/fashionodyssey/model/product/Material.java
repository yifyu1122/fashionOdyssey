package com.fashionodyssey.model.product;

public abstract class Material {
    protected String name;
    protected int value;
    
    public Material(String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
} 