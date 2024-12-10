package com.fashionodyssey.controller;

import com.fashionodyssey.model.design.Design;
import com.fashionodyssey.util.ResourceManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesignController {
    private static DesignController instance;
    private final ResourceManager resourceManager;
    private Map<String, Integer> currentDecorations; // 當前選擇的裝飾品及其數量
    private String currentBaseItem; // 當前選擇的基礎服裝
    private String designName;
    private List<Design> availableDesigns = new ArrayList<>();

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
        if (count > 0) {
            currentDecorations.put(decoration, count); // 直接設置數量，而不是累加
            System.out.println("設置裝飾品: " + decoration + " 數量: " + count);
            System.out.println("當前所有裝飾品: " + currentDecorations);
        } else {
            currentDecorations.remove(decoration);
        }
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
    
    public void craftDesign(double totalCost) {
        System.out.println("開始製作設計");
        System.out.println("當前裝飾品列表: " + currentDecorations);
        
        if (canCraft()) {
            // 消耗基礎服裝
            if (!resourceManager.consumeResource(currentBaseItem, 1)) {
                System.out.println("基礎服裝消耗失敗: " + currentBaseItem);
                return;
            }
            
            // 消耗所有裝飾品
            for (Map.Entry<String, Integer> entry : currentDecorations.entrySet()) {
                String decorationKey = entry.getKey();
                int count = entry.getValue();
                System.out.println("嘗試消耗裝飾品: " + decorationKey + " 數量: " + count);
                if (!"無".equals(decorationKey) && count > 0) {
                    boolean success = resourceManager.consumeResource(decorationKey, count);
                    System.out.println("消耗裝飾品結果: " + success);
                    if (!success) {
                        System.out.println("裝飾品消耗失敗: " + decorationKey);
                        // 如果消耗失敗，恢復已消耗的基礎服裝
                        resourceManager.addResource(currentBaseItem, 1);
                        return;
                    }
                }
            }
            
            String designId = generateDesignId();
            Map<String, Integer> rawMaterials = new HashMap<>();
            rawMaterials.put(currentBaseItem, 1);
            rawMaterials.putAll(currentDecorations);
            
            Design newDesign = new Design(designName, currentBaseItem, currentDecorations, rawMaterials, designId);
            availableDesigns.add(newDesign);
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

    public void setDesignName(String name) {
        this.designName = name;
    }

    public String getDesignName() {
        return designName;
    }

    public List<Design> getAvailableDesigns() {
        return new ArrayList<>(availableDesigns);
    }

    public String getCurrentBaseItem() {
        return currentBaseItem;
    }

    public Map<String, Integer> getCurrentDecorations() {
        return new HashMap<>(currentDecorations);
    }
}
