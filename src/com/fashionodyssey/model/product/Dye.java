package com.fashionodyssey.model.product;

public class Dye extends Material {
    private String color;
    
    public Dye(String color) {
        super("Dye", 50);
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
} 