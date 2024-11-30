package com.fashionodyssey.model.resource;

public class Crop {
    private String name;
    private int growthTime;    // 生長時間（分鐘）
    private int value;         // 收穫後的價值

    public Crop(String name, int growthTime, int value) {
        this.name = name;
        this.growthTime = growthTime;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public int getValue() {
        return value;
    }
} 