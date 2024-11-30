package com.fashionodyssey.model;

import com.fashionodyssey.model.resource.CropStage;

public class Grid {
    private String cropType;
    private CropStage stage;
    private boolean isOccupied;
    
    public Grid() {
        this.isOccupied = false;
        this.stage = CropStage.EMPTY;
    }
    
    public void plant(String cropType) {
        this.cropType = cropType;
        this.stage = CropStage.SEEDED;
        this.isOccupied = true;
    }
    
    public void water() {
        if (stage == CropStage.SEEDED) {
            stage = CropStage.WATERED;
        }
    }
    
    public void fertilize() {
        if (stage == CropStage.WATERED) {
            stage = CropStage.FERTILIZED;
        }
    }
    
    public void mature() {
        if (stage == CropStage.FERTILIZED) {
            stage = CropStage.MATURE;
        }
    }
    
    public void clear() {
        this.cropType = null;
        this.stage = CropStage.EMPTY;
        this.isOccupied = false;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public String getCropType() {
        return cropType;
    }
    
    public CropStage getCropStage() {
        return stage;
    }
} 