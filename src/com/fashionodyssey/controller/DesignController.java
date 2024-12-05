package com.fashionodyssey.controller;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.design.Design;
import com.fashionodyssey.util.ResourceManager;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class DesignController {
    private static DesignController instance;
    private ResourceManager resourceManager;
    private String currentBaseItem;
    private String currentDecoration;
    private int decorationCount;
    private Map<String, Image> baseItemImages;
    
    private DesignController() {
        this.resourceManager = ResourceManager.getInstance();
        this.baseItemImages = new HashMap<>();
        setupEventListeners();
        loadBaseItemImages();  // 添加這行
    }
    
    private void loadBaseItemImages() {
        String[] colors = {"white", "red", "yellow", "pink", "purple"};
        String[] types = {"dress", "shirt", "pants"};
        
        for (String color : colors) {
            for (String type : types) {
                String key = color + "_" + type;
                File file = new File("src/main/resources/images/base/" + key + ".png");
                try {
                    if (!file.exists()) {
                        System.err.println("File not found: " + file.getAbsolutePath());
                        continue;
                    }
                    Image img = ImageIO.read(file);
                    baseItemImages.put(key, img);
                    System.out.println("Successfully loaded: " + key);
                } catch (IOException e) {
                    System.err.println("Failed to load base item image: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }

    public Image getBaseItemImage(String baseItem) {
        if (baseItem == null) return null;
        
        // 轉換中文到英文的鍵值
        String itemKey = baseItem.toLowerCase()
            .replace("連衣裙", "_dress")
            .replace("襯衫", "_shirt")
            .replace("褲子", "_pants")
            .replace("白色", "white")
            .replace("紅色", "red")
            .replace("黃色", "yellow")
            .replace("粉色", "pink")
            .replace("紫色", "purple");
            
        Image img = baseItemImages.get(itemKey);
        if (img == null) {
            System.err.println("找不到圖片: " + itemKey);
        }
        return img;
    }
    
    public static DesignController getInstance() {
        if (instance == null) {
            instance = new DesignController();
        }
        return instance;
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
                new GameEvent("SHOW_MESSAGE", "請先選���基礎服裝和裝飾品")
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