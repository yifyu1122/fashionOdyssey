package com.fashionodyssey.model.resource;

public enum CropType {
    COTTON("棉花", 15, 8, "優質的棉花，可製作布料"),
    ROSE("玫瑰", 10, 10, "美麗的玫瑰花，可製作紅色染料"),
    SUNFLOWER("向日葵", 8, 8, "明亮的向日葵，可製作黃色染料"),
    TULIP("鬱金香", 12, 25, "優雅的鬱金香，可製作各色染料"),
    LAVENDER("薰衣草", 20, 15, "芳香的薰衣草，可製作紫色染料");
    
    private final String displayName;
    private final int growthTime;
    private final int seedCost;
    private final String description;
    
    CropType(String displayName, int growthTime, int seedCost, String description) {
        this.displayName = displayName;
        this.growthTime = growthTime;
        this.seedCost = seedCost;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getGrowthTime() {
        return growthTime;
    }
    
    public int getSeedCost() {
        return seedCost;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static CropType fromDisplayName(String displayName) {
        for (CropType type : values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        return null;
    }
} 