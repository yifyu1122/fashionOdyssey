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
        "鬱金香", "tulip",
        "薰衣草", "lavender"
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
        resources.put("tulip_seeds", 5);
        resources.put("lavender_seeds", 5);
        
        // 初始化收穫物品
        resources.put("harvested_cotton", 0);
        resources.put("harvested_rose", 0);
        resources.put("harvested_sunflower", 0);
        resources.put("harvested_tulip", 0);
        resources.put("harvested_lavender", 0);
        
        // 初始化加工材料
        resources.put("fabric", 0);
        resources.put("red_dye", 0);
        resources.put("yellow_dye", 0);
        resources.put("purple_dye", 0);
    }
    
    public void addResource(String type, int amount) {
        resources.merge(type, amount, Integer::sum);
        notifyResourceChange();     // 通知更新
        notifyInventoryChange();    // 更新倉庫顯示
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
        // 將收穫的作物名稱轉換為對應的資源鍵值
        String resourceKey = "harvested_" + item.toLowerCase()
            .replace("收的", "")  // 移除"收穫的"前綴
            .trim();               // 移除多餘空格
        
        // 使用 addHarvestedCrop 方法來處理收穫
        String cropName = item.replace("收穫", "").trim();
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
        // 更新資源顯示
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_RESOURCES",
                money,
                getResourceAmount(currentCropType + "_seeds"),
                0,  // water 未使用
                getResourceAmount("fertilizer")
            )
        );
        
        // 確保同時更新倉庫顯示
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
    
    public boolean hasMaterials(String product) {
        // 檢查製作產品所需的材料
        return switch(product) {
            case "fabric" -> getResourceAmount("harvested_cotton") >= 2;
            case "red_dye" -> getResourceAmount("harvested_rose") >= 1;
            case "yellow_dye" -> getResourceAmount("harvested_sunflower") >= 1;
            case "purple_dye" -> getResourceAmount("harvested_lavender") >= 1;
            default -> false;
        };
    }
    
    public void craftProduct(String product) {
        // 製作產品並消耗材料
        if (hasMaterials(product)) {
            switch (product) {
                case "fabric":
                    consumeResource("harvested_cotton", 2);
                    addResource("fabric", 1);
                    showStatus("布料製作成功！");
                    break;
                case "red_dye":
                    consumeResource("harvested_rose", 1);
                    addResource("red_dye", 1);
                    showStatus("紅色染料製作成功！");
                    break;
                case "yellow_dye":
                    consumeResource("harvested_sunflower", 1);
                    addResource("yellow_dye", 1);
                    showStatus("黃色染料製作成功！");
                    break;
                case "purple_dye":
                    consumeResource("harvested_lavender", 1);
                    addResource("purple_dye", 1);
                    showStatus("紫色染料製作成功！");
                    break;
            }
            notifyResourceChange();
        }
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
            System.out.println("消耗資源: " + type + ", 消耗: " + amount + ", 剩餘: " + (current - amount));
            notifyResourceChange();
        }
    }
    
    public int getResource(String type) {
        return getResourceAmount(type);
    }
    
    public void notifyInventoryChange() {
        String[] items = {
            "肥料 (用於加速作物生長)",
            "棉花種子 (種植價格: $8)", 
            "玫瑰種子 (種植價格: $10)", 
            "向日葵種子 (種植價格: $8)", 
            "鬱金香種子 (種植價格: $25)", 
            "薰衣草種子 (種植價格: $15)",
            "棉花 (收穫量: 3-5個，用於製作布料)", 
            "玫瑰 (收穫量: 1-3個，用於製作紅色染料)", 
            "向日葵 (收穫量: 1-3個，用於製作黃色染料)", 
            "鬱金香 (收穫量: 1-3個，用於製作各色染料)", 
            "薰衣草 (收穫量: 1-3個，用於製作紫色染料)",
            "布料 (由2個棉花製成，用於製作服裝)", 
            "紅色染料 (由1個玫瑰製成，用於染色)", 
            "黃色染料 (由1個向日葵製成，用於染色)", 
            "紫色染料 (由1個薰衣草製成，用於染色)",
            "連衣裙 (需要2塊布料製作)", 
            "襯衫 (需要1塊布料製作)", 
            "褲子 (需要1塊布料製作)"
        };
        
        int[] amounts = {
            getResourceAmount("fertilizer"),
            getResourceAmount("cotton_seeds"),
            getResourceAmount("rose_seeds"),
            getResourceAmount("sunflower_seeds"),
            getResourceAmount("tulip_seeds"),
            getResourceAmount("lavender_seeds"),
            getResourceAmount("harvested_cotton"),
            getResourceAmount("harvested_rose"),
            getResourceAmount("harvested_sunflower"),
            getResourceAmount("harvested_tulip"),
            getResourceAmount("harvested_lavender"),
            getResourceAmount("fabric"),
            getResourceAmount("red_dye"),
            getResourceAmount("yellow_dye"),
            getResourceAmount("purple_dye"),
            getResourceAmount("dress"),
            getResourceAmount("shirt"),
            getResourceAmount("pants")
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