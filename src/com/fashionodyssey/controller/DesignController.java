package com.fashionodyssey.controller;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.design.Design;
import com.fashionodyssey.util.ResourceManager;

public class DesignController {
    private static DesignController instance;
    private ResourceManager resourceManager;
    private String currentBaseItem;
    private String currentDecoration;
    private int decorationCount;
    
    private DesignController() {
        this.resourceManager = ResourceManager.getInstance();
        initializeResources();
        setupEventListeners();
    }
    
    public static DesignController getInstance() {
        if (instance == null) {
            instance = new DesignController();
        }
        return instance;
    }
    
    private void initializeResources() {
        // 初始化基礎服裝資源
        resourceManager.addResource("white_dress", 5);
        resourceManager.addResource("red_dress", 5);
        resourceManager.addResource("yellow_dress", 5);
        resourceManager.addResource("pink_dress", 5);
        resourceManager.addResource("purple_dress", 5);
        
        // 初始化裝飾品資源
        resourceManager.addResource("white_lace", 10);
        resourceManager.addResource("red_lace", 10);
        resourceManager.addResource("yellow_lace", 10);
        resourceManager.addResource("pink_lace", 10);
        resourceManager.addResource("purple_lace", 10);
        
        resourceManager.addResource("white_ribbon", 10);
        resourceManager.addResource("red_ribbon", 10);
        resourceManager.addResource("yellow_ribbon", 10);
        resourceManager.addResource("pink_ribbon", 10);
        resourceManager.addResource("purple_ribbon", 10);
        
        resourceManager.addResource("white_bow", 10);
        resourceManager.addResource("red_bow", 10);
        resourceManager.addResource("yellow_bow", 10);
        resourceManager.addResource("pink_bow", 10);
        resourceManager.addResource("purple_bow", 10);
    }
    
    private void setupEventListeners() {
        EventManager eventManager = EventManager.getInstance();
        
        eventManager.addEventListener("SELECT_BASE_ITEM", event -> {
            String color = (String) event.getArgs()[0];
            String type = (String) event.getArgs()[1];
            selectBaseItem(color, type);
        });
        
        eventManager.addEventListener("SELECT_DECORATION", event -> {
            String decoration = (String) event.getArgs()[0];
            int count = (Integer) event.getArgs()[1];
            selectDecoration(decoration, count);
        });
        
        eventManager.addEventListener("CREATE_DESIGN", event -> {
            String designName = (String) event.getArgs()[0];
            createDesign(designName);
        });
    }
    
    public void selectBaseItem(String color, String type) {
        String itemKey = color.toLowerCase() + "_" + 
            type.toLowerCase().replace("連衣裙", "dress")
                            .replace("襯衫", "shirt")
                            .replace("褲子", "pants");
        
        if (resourceManager.hasResource(itemKey)) {
            currentBaseItem = itemKey;
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_BASE_ITEM", itemKey)
            );
        } else {
            EventManager.getInstance().fireEvent(
                new GameEvent("SHOW_MESSAGE", "庫存不足")
            );
        }
    }
    
    public void selectDecoration(String decoration, int count) {
        String decorationKey = decoration.toLowerCase()
            .replace("蕾絲", "lace")
            .replace("緞帶", "ribbon")
            .replace("蝴蝶結", "bow");
            
        if (resourceManager.hasResource(decorationKey, count)) {
            currentDecoration = decorationKey;
            decorationCount = count;
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_DECORATION", decorationKey, count)
            );
        } else {
            EventManager.getInstance().fireEvent(
                new GameEvent("SHOW_MESSAGE", "裝飾品數量不足")
            );
        }
    }
    
    public void createDesign(String designName) {
        if (currentBaseItem == null || currentDecoration == null) {
            EventManager.getInstance().fireEvent(
                new GameEvent("SHOW_MESSAGE", "請先選擇基礎服裝和裝飾品")
            );
            return;
        }
        
        if (designName == null || designName.trim().isEmpty()) {
            EventManager.getInstance().fireEvent(
                new GameEvent("SHOW_MESSAGE", "請為設計命名")
            );
            return;
        }
        
        // 檢查材料是否足夠
        if (!resourceManager.hasResource(currentBaseItem) || 
            !resourceManager.hasResource(currentDecoration, decorationCount)) {
            EventManager.getInstance().fireEvent(
                new GameEvent("SHOW_MESSAGE", "材料不足")
            );
            return;
        }
        
        // 消耗材料
        resourceManager.consumeResource(currentBaseItem, 1);
        resourceManager.consumeResource(currentDecoration, decorationCount);
        
        // 創建設計
        Design newDesign = new Design(designName, currentBaseItem, currentDecoration, decorationCount);
        resourceManager.addDesign(newDesign);
        
        // 觸發事件
        EventManager.getInstance().fireEvent(
            new GameEvent("DESIGN_CREATED", newDesign)
        );
        
        // 重置當前選擇
        currentBaseItem = null;
        currentDecoration = null;
        decorationCount = 0;
        
        // 更新資源顯示
        resourceManager.notifyResourceChange();
    }
    
    public boolean hasEnoughMaterials(String baseItem, String decoration, int count) {
        return resourceManager.hasResource(baseItem) && 
               resourceManager.hasResource(decoration, count);
    }
    
    public int getResourceAmount(String resourceKey) {
        return resourceManager.getResourceAmount(resourceKey);
    }
} 