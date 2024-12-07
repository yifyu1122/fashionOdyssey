package com.fashionodyssey.controller;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.util.ResourceManager;
import java.util.HashMap;
import java.util.Map;

public class ProcessingController {
    private ResourceManager resourceManager;
    
    public ProcessingController() {
        this.resourceManager = ResourceManager.getInstance();
        
        // 監聽資源變化事件
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_PROCESSING_PANEL")
            );
        });
        
        // 初始化合成需求
        requirements.put("white_fabric", new CraftingRequirement(
            new String[]{"harvested_cotton"}, // 原料
            new int[]{2}, // 原料數量
            "white_fabric", // 產品
            1 // 產品數量
        ));
        
        // 染料類
        requirements.put("red_dye", new CraftingRequirement(
            new String[]{"harvested_rose"},
            new int[]{1},
            "red_dye",
            1
        ));
        requirements.put("yellow_dye", new CraftingRequirement(
            new String[]{"harvested_sunflower"},
            new int[]{1},
            "yellow_dye",
            1
        ));
        requirements.put("pink_dye", new CraftingRequirement(
            new String[]{"harvested_tulip_pink"},
            new int[]{1},
            "pink_dye",
            1
        ));
        requirements.put("purple_dye", new CraftingRequirement(
            new String[]{"harvested_lavender"},
            new int[]{1},
            "purple_dye",
            1
        ));

        // 布料類
        requirements.put("white_lace", new CraftingRequirement(
            new String[]{"harvested_cotton"},
            new int[]{1},
            "white_lace",
            1
        ));

        requirements.put("red_fabric", new CraftingRequirement(
            new String[]{"white_fabric", "red_dye"},
            new int[]{1, 1},
            "red_fabric",
            1
        ));
        requirements.put("yellow_fabric", new CraftingRequirement(
            new String[]{"white_fabric", "yellow_dye"},
            new int[]{1, 1},
            "yellow_fabric",
            1
        ));
        requirements.put("pink_fabric", new CraftingRequirement(
            new String[]{"white_fabric", "pink_dye"},
            new int[]{1, 1},
            "pink_fabric",
            1
        ));
        requirements.put("purple_fabric", new CraftingRequirement(
            new String[]{"white_fabric", "purple_dye"},
            new int[]{1, 1},
            "purple_fabric",
            1
        ));

        // 蝴蝶結類
        requirements.put("white_bow", new CraftingRequirement(
            new String[]{"white_fabric"},
            new int[]{1},
            "white_bow",
            1
        ));
        requirements.put("red_bow", new CraftingRequirement(
            new String[]{"red_fabric"},
            new int[]{1},
            "red_bow",
            2
        ));
        requirements.put("yellow_bow", new CraftingRequirement(
            new String[]{"yellow_fabric"},
            new int[]{1},
            "yellow_bow",
            2
        ));
        requirements.put("pink_bow", new CraftingRequirement(
            new String[]{"pink_fabric"},
            new int[]{1},
            "pink_bow",
            2
        ));
        requirements.put("purple_bow", new CraftingRequirement(
            new String[]{"purple_fabric"},
            new int[]{1},
            "purple_bow",
            2
        ));

        // 緞帶類
        requirements.put("white_ribbon", new CraftingRequirement(
            new String[]{"white_fabric"},
            new int[]{1},
            "white_ribbon",
            1
        ));
        requirements.put("red_ribbon", new CraftingRequirement(
            new String[]{"red_fabric"},
            new int[]{1},
            "red_ribbon",
            1
        ));
        requirements.put("yellow_ribbon", new CraftingRequirement(
            new String[]{"yellow_fabric"},
            new int[]{1},
            "yellow_ribbon",
            1
        ));
        requirements.put("pink_ribbon", new CraftingRequirement(
            new String[]{"pink_fabric"},
            new int[]{1},
            "pink_ribbon",
            1
        ));
        requirements.put("purple_ribbon", new CraftingRequirement(
            new String[]{"purple_fabric"},
            new int[]{1},
            "purple_ribbon",
            1
        ));

        // 連衣裙類
        requirements.put("white_dress", new CraftingRequirement(
            new String[]{"white_fabric"},
            new int[]{2},
            "white_dress",
            1
        ));
        requirements.put("red_dress", new CraftingRequirement(
            new String[]{"red_fabric"},
            new int[]{2},
            "red_dress",
            1
        ));
        requirements.put("yellow_dress", new CraftingRequirement(
            new String[]{"yellow_fabric"},
            new int[]{2},
            "yellow_dress",
            1
        ));
        requirements.put("pink_dress", new CraftingRequirement(
            new String[]{"pink_fabric"},
            new int[]{2},
            "pink_dress",
            1
        ));
        requirements.put("purple_dress", new CraftingRequirement(
            new String[]{"purple_fabric"},
            new int[]{2},
            "purple_dress",
            1
        ));

        // 襯衫類
        requirements.put("white_shirt", new CraftingRequirement(
            new String[]{"white_fabric"},
            new int[]{1},
            "white_shirt",
            1
        ));
        requirements.put("red_shirt", new CraftingRequirement(
            new String[]{"red_fabric"},
            new int[]{1},
            "red_shirt",
            1
        ));
        requirements.put("yellow_shirt", new CraftingRequirement(
            new String[]{"yellow_fabric"},
            new int[]{1},
            "yellow_shirt",
            1
        ));
        requirements.put("pink_shirt", new CraftingRequirement(
            new String[]{"pink_fabric"},
            new int[]{1},
            "pink_shirt",
            1
        ));
        requirements.put("purple_shirt", new CraftingRequirement(
            new String[]{"purple_fabric"},
            new int[]{1},
            "purple_shirt",
            1
        ));

        // 褲子類
        requirements.put("white_pants", new CraftingRequirement(
            new String[]{"white_fabric"},
            new int[]{1},
            "white_pants",
            1
        ));
        requirements.put("red_pants", new CraftingRequirement(
            new String[]{"red_fabric"},
            new int[]{1},
            "red_pants",
            1
        ));
        requirements.put("yellow_pants", new CraftingRequirement(
            new String[]{"yellow_fabric"},
            new int[]{1},
            "yellow_pants",
            1
        ));
        requirements.put("pink_pants", new CraftingRequirement(
            new String[]{"pink_fabric"},
            new int[]{1},
            "pink_pants",
            1
        ));
        requirements.put("purple_pants", new CraftingRequirement(
            new String[]{"purple_fabric"},
            new int[]{1},
            "purple_pants",
            1
        ));

        // 蕾絲類
        requirements.put("red_lace", new CraftingRequirement(
            new String[]{"white_lace", "red_dye"},
            new int[]{1},
            "red_lace",
            1
        ));         
        requirements.put("yellow_lace", new CraftingRequirement(
            new String[]{"white_lace", "yellow_dye"},
            new int[]{1},
            "yellow_lace",
            1
        ));
        requirements.put("pink_lace", new CraftingRequirement(
            new String[]{"white_lace", "pink_dye"},
            new int[]{1},
            "pink_lace",
            1
        ));
        requirements.put("purple_lace", new CraftingRequirement(
            new String[]{"white_lace", "purple_dye"},
            new int[]{1},
            "purple_lace",
            1
        ));
    }
    
    private final Map<String, CraftingRequirement> requirements = new HashMap<>();
    
    public boolean canCraft(String product) {
        CraftingRequirement req = requirements.get(product);
        return req != null && req.canCraft(resourceManager);
    }
    
    public void craftProduct(String product) {
        CraftingRequirement req = requirements.get(product);
        if (req != null && req.canCraft(resourceManager)) {
            req.craft(resourceManager);
            resourceManager.notifyResourceChange();
        }
    }
    
    private class CraftingRequirement {
        String[] ingredients;
        int[] amounts;
        String product;
        int productAmount;
        
        CraftingRequirement(String[] ingredients, int[] amounts, String product, int productAmount) {
            this.ingredients = ingredients;
            this.amounts = amounts;
            this.product = product;
            this.productAmount = productAmount;
        }
        
        boolean canCraft(ResourceManager rm) {
            if (ingredients.length != amounts.length) {
                throw new IllegalStateException("Ingredients and amounts arrays must have the same length");
            }
            
            for (int i = 0; i < ingredients.length; i++) {
                if (rm.getResourceAmount(ingredients[i]) < amounts[i]) {
                    return false;
                }
            }
            return true;
        }
        
        void craft(ResourceManager rm) {
            for (int i = 0; i < ingredients.length; i++) {
                rm.consumeResource(ingredients[i], amounts[i]);
            }
            rm.addResource(product, productAmount);
        }
    }
} 