package com.fashionodyssey.util;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.resource.CropType;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager instance;
    private Map<String, Integer> resources;
    private int money;
    private String currentCropType = "棉花";  // 添加這個成員變量
    
    // 資源類型常量
    private static final String FERTILIZER = "fertilizer";
    private static final String SEEDS_SUFFIX = "_seeds";
    private static final String HARVESTED_PREFIX = "harvested_";
    
    // 作物類型映射
    public static final Map<String, String> CROP_KEY_MAP = Map.of(
        "棉花", "cotton",
        "玫瑰", "rose",
        "向日葵", "sunflower",
        "薰衣草", "lavender",
        "鬱金香(粉)", "tulip_pink"
    );
    
    private ResourceManager() {
        resources = new HashMap<>();
        money = 1000; // 初始資金
        initializeResources();
    }
    
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }
    
    private void initializeResources() {
        // 初始化基礎資源
        resources.put("fertilizer", 5);  // 肥料
        
        // 初始化種子
        resources.put("cotton_seeds", 5);
        resources.put("rose_seeds", 5);
        resources.put("sunflower_seeds", 5);
        resources.put("tulip_pink_seeds", 5);
        resources.put("lavender_seeds", 5);
        
        // 初始化收穫物品
        resources.put("harvested_cotton", 0);
        resources.put("harvested_rose", 0);
        resources.put("harvested_sunflower", 0);
        resources.put("harvested_tulip_pink", 0);
        resources.put("harvested_lavender", 0);
        
        // 初始化加工材料
        resources.put("white_fabric", 0);
        resources.put("red_dye", 0);
        resources.put("yellow_dye", 0);
        resources.put("pink_dye", 0);
        resources.put("purple_dye", 0);

        resources.put("white_bow", 0);
        resources.put("red_bow", 0);
        resources.put("yellow_bow", 0);
        resources.put("pink_bow", 0);
        resources.put("purple_bow", 0);

        resources.put("white_dress", 0);
        resources.put("red_dress", 0);
        resources.put("yellow_dress", 0);
        resources.put("pink_dress", 0);
        resources.put("purple_dress", 0);

        resources.put("white_shirt", 0);
        resources.put("red_shirt", 0);
        resources.put("yellow_shirt", 0);
        resources.put("pink_shirt", 0);
        resources.put("purple_shirt", 0);

        resources.put("white_pants", 0);
        resources.put("red_pants", 0);
        resources.put("yellow_pants", 0);
        resources.put("pink_pants", 0);
        resources.put("purple_pants", 0);
    }
    
    public void addResource(String type, int amount) {
        System.out.println("\n===== 增加資源 =====");
        System.out.println("資源類型: " + type);
        System.out.println("增加數量: " + amount);
        int currentAmount = resources.getOrDefault(type, 0);
        resources.put(type, currentAmount + amount);
        
        // 確保觸發更新事件
        notifyResourceChange();
    }
    
    public boolean useResource(String type, int amount) {
        int current = resources.getOrDefault(type, 0);
        if (current >= amount) {
            resources.put(type, current - amount);
            notifyResourceChange();
            return true;
        }
        return false;
    }
    
    public void addMoney(int amount) {
        money += amount;
        notifyResourceChange();
    }
    
    public boolean useMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            notifyResourceChange();
            return true;
        }
        return false;
    }
    
    public int getResourceAmount(String type) {
        int amount = resources.getOrDefault(type, 0);
        System.out.println("Getting resource amount for " + type + ": " + amount);
        return amount;
    }
    
    public int getMoney() {
        return money;
    }
    
    public boolean hasEnoughResources(String cropType) {
        String seedType = cropType + "_seeds";
        return getResourceAmount(seedType) > 0;  // 檢查是否有種子
    }
    
    public void consumeResources(String cropType) {
        String seedType = cropType + "_seeds";
        if (getResourceAmount(seedType) > 0) {
            useResource(seedType, 1);  // 消耗一個種子
        }
    }
    
    public void addToInventory(String item) {
        // 移除"收穫的"前綴並清理字串
        String cropName = item.replace("收穫的", "").trim();
        
        // 使用 addHarvestedCrop 方法來處理收穫
        addHarvestedCrop(cropName);
    }
    
    public boolean canPerformAction(String action) {
        switch (action) {
            case "WATER":
                return true;  // 水是無限的
            case "FERTILIZE":
                return getResourceAmount("fertilizer") > 0;
            default:
                return false;
        }
    }
    
    public void consumeActionResource(String action) {
        switch (action) {
            case "WATER":
                // 不消耗水資源
                break;
            case "FERTILIZE":
                useResource("fertilizer", 1);
                break;
        }
    }
    
    public void notifyResourceChange() {
        System.out.println("\n===== 觸發資源更新事件 =====");
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_RESOURCES")
        );
        System.out.println("事件已觸發");
        System.out.println("========================\n");
    }
    
    // 添加購買種子的方法
    public void buySeed(String cropType) {
        String seedKey = CROP_KEY_MAP.get(cropType).toLowerCase() + "_seeds";
        CropType type = CropType.fromDisplayName(cropType);
        if (type != null && useMoney(type.getSeedCost())) {
            addResource(seedKey, 10);  // 一次購買10個種子
            System.out.println("購買種子: " + seedKey + ", 當前數量: " + getResourceAmount(seedKey));
            notifyResourceChange();
        }
    }
    
    public void addHarvestedCrop(String cropType) {
        String cropKey = CROP_KEY_MAP.get(cropType);
        if (cropKey != null) {
            String harvestedKey = HARVESTED_PREFIX + cropKey;
            int amount = cropType.equals("棉花") ? 
                (int)(Math.random() * 3) + 3 :  // 棉花 3-5個
                (int)(Math.random() * 3) + 1;   // 其他 1-3個
            
            // 直接使用 addResource 方法來確保正確更新
            addResource(harvestedKey, amount);
            showStatus("收穫了 " + amount + " 個 " + cropType);
            
            // 確保更新顯示
            notifyResourceChange();
        }
    }
    
    private void showStatus(String message) {
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_STATUS", message)
        );
    }
    
    
    public void buyFertilizer() {
        int fertilizerCost = 20;  // 肥料價格
        if (money >= fertilizerCost) {
            money -= fertilizerCost;
            addResource("fertilizer", 5);  // 一次購買5個肥料
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_STATUS", "成功購買5肥料")
            );
        } else {
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_STATUS", "金錢不足，無法購買肥料")
            );
        }
        notifyResourceChange();
    }
    
    public void consumeResource(String resourceKey, int amount) {
        System.out.println("\n===== 消耗資源 =====");
        System.out.println("資源類型: " + resourceKey);
        System.out.println("消耗數量: " + amount);
        int currentAmount = resources.getOrDefault(resourceKey, 0);
        resources.put(resourceKey, Math.max(0, currentAmount - amount));
        
        // 確保觸發更新事件
        notifyResourceChange();
    }
    
    public int getResource(String type) {
        return getResourceAmount(type);
    }
    
    public void setCurrentCropType(String cropType) {
        this.currentCropType = cropType;
        notifyResourceChange();
    }
    
    public String getCurrentCropType() {
        return currentCropType;
    }
    
    public static String getCropKey(String displayName) {
        return CROP_KEY_MAP.get(displayName);
    }
    
    public void setResourceAmount(String resourceKey, int amount) {
        System.out.println("\n===== 設置資源數量 =====");
        System.out.println("資源類型: " + resourceKey);
        System.out.println("設置數量: " + amount);
        resources.put(resourceKey, amount);
        
        // 確保觸發更新事件
        notifyResourceChange();
    }
    
    public void notifyInventoryChange() {
        System.out.println("Notifying inventory change...");
        
        // Gather inventory data
        String[] items = resources.keySet().toArray(new String[0]);
        int[] amounts = resources.values().stream().mapToInt(Integer::intValue).toArray();
        
        // Fire an event to update the inventory
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_INVENTORY", items, amounts)
        );
    }
} 