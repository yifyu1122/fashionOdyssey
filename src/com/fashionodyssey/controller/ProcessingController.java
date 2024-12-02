package com.fashionodyssey.controller;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.util.ResourceManager;

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
    
    public boolean canCraft(String product) {
        return switch(product) {
            case "fabric" -> resourceManager.getResourceAmount("harvested_cotton") >= 2;
            case "red_dye" -> resourceManager.getResourceAmount("harvested_rose") >= 1;
            case "yellow_dye" -> resourceManager.getResourceAmount("harvested_sunflower") >= 1;
            case "pink_dye" -> resourceManager.getResourceAmount("harvested_tulip_pink") >= 1;
            case "purple_dye" -> resourceManager.getResourceAmount("harvested_lavender") >= 1;
            case "red_fabric" -> resourceManager.getResourceAmount("fabric") >= 1 && 
                               resourceManager.getResourceAmount("red_dye") >= 1;
            case "yellow_fabric" -> resourceManager.getResourceAmount("fabric") >= 1 && 
                                  resourceManager.getResourceAmount("yellow_dye") >= 1;
            case "purple_fabric" -> resourceManager.getResourceAmount("fabric") >= 1 && 
                                  resourceManager.getResourceAmount("purple_dye") >= 1;
            case "pink_fabric" -> resourceManager.getResourceAmount("fabric") >= 1 && 
                                  resourceManager.getResourceAmount("pink_dye") >= 1;
            default -> false;
        };
    }
    
    public void craftProduct(String product) {
        if (!canCraft(product)) return;
        
        switch(product) {
            case "fabric":
                resourceManager.consumeResource("harvested_cotton", 2);
                resourceManager.addResource("fabric", 1);
                break;
            case "red_dye":
                resourceManager.consumeResource("harvested_rose", 1);
                resourceManager.addResource("red_dye", 1);
                break;
            case "yellow_dye":
                resourceManager.consumeResource("harvested_sunflower", 1);
                resourceManager.addResource("yellow_dye", 1);
                break;
            case "pink_dye":
                resourceManager.consumeResource("harvested_tulip_pink", 1);
                resourceManager.addResource("pink_dye", 1);
                break;
            case "purple_dye":
                resourceManager.consumeResource("harvested_lavender", 1);
                resourceManager.addResource("purple_dye", 1);
                break;
            case "red_fabric":
                resourceManager.consumeResource("fabric", 1);
                resourceManager.consumeResource("red_dye", 1);
                resourceManager.addResource("red_fabric", 1);
                break;
            case "yellow_fabric":
                resourceManager.consumeResource("fabric", 1);
                resourceManager.consumeResource("yellow_dye", 1);
                resourceManager.addResource("yellow_fabric", 1);
                break;
            case "pink_fabric":
                resourceManager.consumeResource("fabric", 1);
                resourceManager.consumeResource("pink_dye", 1);
                resourceManager.addResource("pink_fabric", 1);
                break;
            case "purple_fabric":
                resourceManager.consumeResource("fabric", 1);
                resourceManager.consumeResource("purple_dye", 1);
                resourceManager.addResource("purple_fabric", 1);
                break;
        }
        
        resourceManager.notifyResourceChange();
    }
} 