package com.fashionodyssey.model.cost;

import java.util.HashMap;
import java.util.Map;

public class ItemCost {
    private static final Map<String, Double> BASE_COSTS = new HashMap<>() {{
        // 基礎材料成本
        put("harvested_cotton", 1.2);     // 棉花
        put("harvested_rose", 2.5);       // 玫瑰
        put("harvested_sunflower", 2.4);  // 向日葵
        put("harvested_lavender", 2.75);   // 薰衣草
        put("harvested_tulip_pink", 3.25); // 粉色鬱金香
        
        // 染料成本 (基於花卉成本)
        put("red_dye", 2.5);      // 紅色染料 (玫瑰)
        put("yellow_dye", 2.4);   // 黃色染料 (向日葵)
        put("purple_dye", 2.75);  // 紫色染料 (薰衣草)
        put("pink_dye", 3.25);    // 粉色染料 (粉色鬱金香)
        
        // 基礎布料成本
        put("white_fabric", 2.4);  // 白色布料 (2個棉花)
        put("white_lace", 1.2);     // 白色蕾絲 (1個棉花)
    }};
    
    private static final Map<String, Map<String, Double>> RECIPE_COSTS = new HashMap<>() {{
        // 染色布料配方
        put("red_fabric", Map.of("white_fabric", 1.0, "red_dye", 1.0));
        put("yellow_fabric", Map.of("white_fabric", 1.0, "yellow_dye", 1.0));
        put("purple_fabric", Map.of("white_fabric", 1.0, "purple_dye", 1.0));
        put("pink_fabric", Map.of("white_fabric", 1.0, "pink_dye", 1.0));
        
        // 染色蕾絲配方
        put("red_lace", Map.of("white_lace", 1.0, "red_dye", 1.0));
        put("yellow_lace", Map.of("white_lace", 1.0, "yellow_dye", 1.0));
        put("purple_lace", Map.of("white_lace", 1.0, "purple_dye", 1.0));
        put("pink_lace", Map.of("white_lace", 1.0, "pink_dye", 1.0));

        // 連衣裙配方
        put("white_dress", Map.of("white_fabric", 2.0));  // 白色連衣裙需要2塊白色布料
        put("red_dress", Map.of("red_fabric", 2.0));      // 紅色連衣裙需要2塊紅色布料
        put("yellow_dress", Map.of("yellow_fabric", 2.0)); // 黃色連衣裙需要2塊黃色布料
        put("purple_dress", Map.of("purple_fabric", 2.0)); // 紫色連衣裙需要2塊紫色布料
        put("pink_dress", Map.of("pink_fabric", 2.0));     // 粉色連衣裙需要2塊粉色布料

        // 襯衫配方
        put("white_shirt", Map.of("white_fabric", 1.0));  // 白色襯衫需要1塊白色布料
        put("red_shirt", Map.of("red_fabric", 1.0));      // 紅色襯衫需要1塊紅色布料
        put("yellow_shirt", Map.of("yellow_fabric", 1.0)); // 黃色襯衫需要1塊黃色布料
        put("purple_shirt", Map.of("purple_fabric", 1.0)); // 紫色襯衫需要1塊紫色布料
        put("pink_shirt", Map.of("pink_fabric", 1.0));     // 粉色襯衫需要1塊粉色布料 

        // 褲子配方
        put("white_pants", Map.of("white_fabric", 1.0));  // 白色褲子需要1塊白色布料
        put("red_pants", Map.of("red_fabric", 1.0));      // 紅色褲子需要1塊紅色布料
        put("yellow_pants", Map.of("yellow_fabric", 1.0)); // 黃色褲子需要1塊黃色布料
        put("purple_pants", Map.of("purple_fabric", 1.0)); // 紫色褲子需要1塊紫色布料
        put("pink_pants", Map.of("pink_fabric", 1.0));     // 粉色褲子需要1塊粉色布料 

        // 蝴蝶結配方
        put("white_bow", Map.of("white_fabric", 0.5));  // 2個白色蝴蝶結需要1塊白色布料
        put("red_bow", Map.of("red_fabric", 0.5));      // 2個紅色蝴蝶結需要1塊紅色布料
        put("yellow_bow", Map.of("yellow_fabric", 0.5)); // 2個黃色蝴蝶結需要1塊黃色布料
        put("purple_bow", Map.of("purple_fabric", 0.5)); // 2個紫色蝴蝶結需要1塊紫色布料
        put("pink_bow", Map.of("pink_fabric", 0.5)); 
        
        // 緞帶配方
        put("white_ribbon", Map.of("white_fabric", 1.0));  // 白色緞帶需要1塊白色布料
        put("red_ribbon", Map.of("red_fabric", 1.0));      // 紅色緞帶需要1塊紅色布料
        put("yellow_ribbon", Map.of("yellow_fabric", 1.0)); // 黃色緞帶需要1塊黃色布料
        put("purple_ribbon", Map.of("purple_fabric", 1.0)); // 紫色緞帶需要1塊紫色布料
        put("pink_ribbon", Map.of("pink_fabric", 1.0));     // 粉色緞帶需要1塊粉色布料
    }};

    public static double calculateCost(String itemId) {
        // 如果是基礎物品，直接返回成本
        if (BASE_COSTS.containsKey(itemId)) {
            return BASE_COSTS.get(itemId);
        }
        
        // 如果是合成物品，計算原材料總成本
        if (RECIPE_COSTS.containsKey(itemId)) {
            Map<String, Double> recipe = RECIPE_COSTS.get(itemId);
            double totalCost = 0.0;
            for (Map.Entry<String, Double> ingredient : recipe.entrySet()) {
                totalCost += calculateCost(ingredient.getKey()) * ingredient.getValue();
            }
            return totalCost;
        }
        
        return 0.0; // 未知物品
    }
} 