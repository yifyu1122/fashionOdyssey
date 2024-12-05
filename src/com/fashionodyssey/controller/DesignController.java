package com.fashionodyssey.controller;

import com.fashionodyssey.util.ResourceManager;
import java.util.HashMap;
import java.util.Map;

public class DesignController {
    private static DesignController instance;
    private final ResourceManager resourceManager;
    private Map<String, Integer> currentDecorations; // 當前選擇的裝飾品及其數量
    private String currentBaseItem; // 當前選擇的基礎服裝
    private Map<String, Integer> selectedDecorations = new HashMap<>();

    private DesignController() {
        resourceManager = ResourceManager.getInstance();
        currentDecorations = new HashMap<>();
    }

    public static DesignController getInstance() {
        if (instance == null) {
            instance = new DesignController();
        }
        return instance;
    }

    public void setBaseItem(String baseItem) {
        currentBaseItem = baseItem;
        clearDecorations(); // 更換基礎服裝時清空裝飾品
    }

    public void addDecoration(String decoration, int count) {
        selectedDecorations.put(decoration, selectedDecorations.getOrDefault(decoration, 0) + count);
    }

    public void updateDecoration(String decoration, int count) {
        // 直接設置指定裝飾品的數量
        if (count <= 0) {
            currentDecorations.remove(decoration);
        } else {
            currentDecorations.put(decoration, count);
        }
    }

    public void removeDecoration(String decoration) {
        currentDecorations.remove(decoration);
    }

    public void clearDecorations() {
        currentDecorations.clear();
        selectedDecorations.clear();
    }

    public boolean canCraft() {
        if (currentBaseItem == null || !resourceManager.hasResource(currentBaseItem, 1)) {
            return false;
        }
        
        for (Map.Entry<String, Integer> entry : currentDecorations.entrySet()) {
            if (!resourceManager.hasResource(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    public void craftDesign() {
        if (canCraft()) {
            // 消耗基礎服裝
            resourceManager.consumeResource(currentBaseItem, 1);
            
            // 消耗所有裝飾品
            for (Map.Entry<String, Integer> entry : currentDecorations.entrySet()) {
                resourceManager.consumeResource(entry.getKey(), entry.getValue());
            }
            
            // 生成設計品
            String designId = generateDesignId();
            resourceManager.addResource(designId, 1);
            resourceManager.notifyResourceChange();
        }
    }

    private String generateDesignId() {
        StringBuilder id = new StringBuilder("design_" + currentBaseItem);
        // 按照裝飾品名稱排序，確保相同組合產生相同ID
        currentDecorations.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> 
                id.append("_").append(entry.getKey())
                  .append("_").append(entry.getValue())
            );
        return id.toString();
    }

    public Map<String, Integer> getRequiredMaterials() {
        Map<String, Integer> materials = new HashMap<>();
        if (currentBaseItem != null) {
            materials.put(currentBaseItem, 1);
        }
        materials.putAll(currentDecorations);
        return materials;
    }

    public Map<String, Integer> getSelectedDecorations() {
        return new HashMap<>(selectedDecorations);
    }
}
