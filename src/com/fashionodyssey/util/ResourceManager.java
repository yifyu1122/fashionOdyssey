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
    }
    
    public void addResource(String type, int amount) {
        resources.merge(type, amount, Integer::sum);
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
        return resources.getOrDefault(type, 0);
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
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_RESOURCES")
        );
        notifyInventoryChange();
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
            notifyInventoryChange();
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
                new GameEvent("UPDATE_STATUS", "成功購買5個肥料")
            );
        } else {
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_STATUS", "金錢不足，無法購買肥料")
            );
        }
        notifyResourceChange();
    }
    
    public void consumeResource(String type, int amount) {
        int current = getResourceAmount(type);
        if (current >= amount) {
            resources.put(type, current - amount);
            notifyResourceChange();
            notifyInventoryChange();
        } else {
            System.out.println("Not enough resources: " + type);
        }
    }
    
    public int getResource(String type) {
        return getResourceAmount(type);
    }
    
    public void notifyInventoryChange() {
        String[] items = {
            "肥料",
            "棉花種子", 
            "玫瑰種子", 
            "向日葵種子", 
            "鬱金香(粉)種子", 
            "薰衣草種子",
            "棉花", 
            "玫瑰", 
            "向日葵", 
            "鬱金香(粉)", 
            "薰衣草",
            "白色布料", 
            "紅色染料", 
            "黃色染料", 
            "紫色染料",
            "粉色染料",
            "白色緞帶", 
            "紅色緞帶", 
            "黃色緞帶", 
            "紫色緞帶",
            "粉色緞帶",
            "白色蝴蝶結", 
            "紅色蝴蝶結", 
            "黃色蝴蝶結", 
            "紫色蝴蝶結",
            "粉色蝴蝶結",
            "白色連衣裙",
            "紅色連衣裙",
            "黃色連衣裙",
            "紫色連衣裙",
            "粉色連衣裙",
            "白色褲子",
            "紅色褲子",
            "黃色褲子",
            "紫色褲子",
            "粉色褲子",
            "白色襯衫",
            "紅色襯衫",
            "黃色襯衫",
            "紫色襯衫",
            "粉色襯衫"
        };
        
        int[] amounts = {
            getResourceAmount("fertilizer"),
            getResourceAmount("cotton_seeds"),
            getResourceAmount("rose_seeds"),
            getResourceAmount("sunflower_seeds"),
            getResourceAmount("tulip_pink_seeds"),
            getResourceAmount("lavender_seeds"),
            getResourceAmount("harvested_cotton"),
            getResourceAmount("harvested_rose"),
            getResourceAmount("harvested_sunflower"),
            getResourceAmount("harvested_tulip_pink"),
            getResourceAmount("harvested_lavender"),
            getResourceAmount("white_fabric"),
            getResourceAmount("red_dye"),
            getResourceAmount("yellow_dye"),
            getResourceAmount("purple_dye"),
            getResourceAmount("pink_dye"),
            getResourceAmount("white_ribbon"),
            getResourceAmount("red_ribbon"),
            getResourceAmount("yellow_ribbon"),
            getResourceAmount("purple_ribbon"),
            getResourceAmount("pink_ribbon"),
            getResourceAmount("white_bow"),
            getResourceAmount("red_bow"),
            getResourceAmount("yellow_bow"),
            getResourceAmount("purple_bow"),
            getResourceAmount("pink_bow"),
            getResourceAmount("white_dress"),
            getResourceAmount("red_dress"),
            getResourceAmount("yellow_dress"),
            getResourceAmount("purple_dress"),
            getResourceAmount("pink_dress"),
            getResourceAmount("white_pants"),
            getResourceAmount("red_pants"),
            getResourceAmount("yellow_pants"),
            getResourceAmount("purple_pants"),
            getResourceAmount("pink_pants"),
            getResourceAmount("white_shirt"),
            getResourceAmount("red_shirt"),
            getResourceAmount("yellow_shirt"),
            getResourceAmount("purple_shirt"),
            getResourceAmount("pink_shirt")
        };
        
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_INVENTORY", items, amounts)
        );
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
} 