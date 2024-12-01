package com.fashionodyssey.model.resource;

public abstract class Crop {
    private final CropType type;
    
    protected Crop(CropType type) {
        this.type = type;
    }
    
    public CropType getType() {
        return type;
    }
} 