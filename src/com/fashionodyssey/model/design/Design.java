package com.fashionodyssey.model.design;

public class Design {
    private String name;
    private String baseItem;
    private String decoration;
    private int decorationCount;
    
    public Design(String name, String baseItem, String decoration, int decorationCount) {
        this.name = name;
        this.baseItem = baseItem;
        this.decoration = decoration;
        this.decorationCount = decorationCount;
    }
    
    // Getters
    public String getName() { return name; }
    public String getBaseItem() { return baseItem; }
    public String getDecoration() { return decoration; }
    public int getDecorationCount() { return decorationCount; }
} 