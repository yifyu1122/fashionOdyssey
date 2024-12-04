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
    }
    
    private class CraftingRequirement {
        String[] ingredients;
        int[] amounts;
        String product;
        int productAmount;
        
        boolean canCraft(ResourceManager rm) {
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
} 