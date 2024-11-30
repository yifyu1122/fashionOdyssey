package com.fashionodyssey.model.design;

public class Clothing {
    private String style;
    private String color;
    private int quality;
    private int basePrice;
    
    public Clothing(String style, String color, int quality) {
        this.style = style;
        this.color = color;
        this.quality = quality;
        calculateBasePrice();
    }
    
    private void calculateBasePrice() {
        // 基礎價格計算
        basePrice = 500 + (quality * 100);
    }
    
    public int getPrice() {
        return basePrice;
    }
    
    public String getStyle() {
        return style;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getQuality() {
        return quality;
    }
} 