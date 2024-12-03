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
            case "white_fabric" -> resourceManager.getResourceAmount("harvested_cotton") >= 2;
            case "lace" -> resourceManager.getResourceAmount("harvested_cotton") >= 1;
            case "red_dye" -> resourceManager.getResourceAmount("harvested_rose") >= 1;
            case "yellow_dye" -> resourceManager.getResourceAmount("harvested_sunflower") >= 1;
            case "pink_dye" -> resourceManager.getResourceAmount("harvested_tulip_pink") >= 1;
            case "purple_dye" -> resourceManager.getResourceAmount("harvested_lavender") >= 1;
            case "red_fabric" -> resourceManager.getResourceAmount("white_fabric") >= 1 && 
                               resourceManager.getResourceAmount("red_dye") >= 1;
            case "yellow_fabric" -> resourceManager.getResourceAmount("white_fabric") >= 1 && 
                                  resourceManager.getResourceAmount("yellow_dye") >= 1;
            case "purple_fabric" -> resourceManager.getResourceAmount("white_fabric") >= 1 && 
                                  resourceManager.getResourceAmount("purple_dye") >= 1;
            case "pink_fabric" -> resourceManager.getResourceAmount("white_fabric") >= 1 && 
                                resourceManager.getResourceAmount("pink_dye") >= 1;
            case "white_bow" -> resourceManager.getResourceAmount("white_fabric") >= 1;
            case "red_bow" -> resourceManager.getResourceAmount("red_fabric") >= 1;
            case "yellow_bow" -> resourceManager.getResourceAmount("yellow_fabric") >= 1;
            case "purple_bow" -> resourceManager.getResourceAmount("purple_fabric") >= 1;
            case "pink_bow" -> resourceManager.getResourceAmount("pink_fabric") >= 1;
            case "white_ribbon" -> resourceManager.getResourceAmount("white_fabric") >= 1;
            case "red_ribbon" -> resourceManager.getResourceAmount("red_fabric") >= 1;
            case "yellow_ribbon" -> resourceManager.getResourceAmount("yellow_fabric") >= 1;
            case "purple_ribbon" -> resourceManager.getResourceAmount("purple_fabric") >= 1;
            case "pink_ribbon" -> resourceManager.getResourceAmount("pink_fabric") >= 1;
            case "white_dress" -> resourceManager.getResourceAmount("white_fabric") >= 2;
            case "red_dress" -> resourceManager.getResourceAmount("red_fabric") >= 2;
            case "yellow_dress" -> resourceManager.getResourceAmount("yellow_fabric") >= 2;
            case "purple_dress" -> resourceManager.getResourceAmount("purple_fabric") >= 2;
            case "pink_dress" -> resourceManager.getResourceAmount("pink_fabric") >= 2;
            case "white_shirt" -> resourceManager.getResourceAmount("white_fabric") >= 1;
            case "red_shirt" -> resourceManager.getResourceAmount("red_fabric") >= 1;
            case "yellow_shirt" -> resourceManager.getResourceAmount("yellow_fabric") >= 1;
            case "purple_shirt" -> resourceManager.getResourceAmount("purple_fabric") >= 1;
            case "pink_shirt" -> resourceManager.getResourceAmount("pink_fabric") >= 1;
            case "white_pants" -> resourceManager.getResourceAmount("white_fabric") >= 1;
            case "red_pants" -> resourceManager.getResourceAmount("red_fabric") >= 1;
            case "yellow_pants" -> resourceManager.getResourceAmount("yellow_fabric") >= 1;
            case "purple_pants" -> resourceManager.getResourceAmount("purple_fabric") >= 1;
            case "pink_pants" -> resourceManager.getResourceAmount("pink_fabric") >= 1;
            default -> false;
        };
    }
    
    public void craftProduct(String product) {
        if (!canCraft(product)) return;
        
        switch(product) {
            case "white_fabric":
                resourceManager.consumeResource("harvested_cotton", 2);
                resourceManager.addResource("white_fabric", 1);
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
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.consumeResource("red_dye", 1);
                resourceManager.addResource("red_fabric", 1);
                break;
            case "yellow_fabric":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.consumeResource("yellow_dye", 1);
                resourceManager.addResource("yellow_fabric", 1);
                break;
            case "pink_fabric":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.consumeResource("pink_dye", 1);
                resourceManager.addResource("pink_fabric", 1);
                break;
            case "purple_fabric":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.consumeResource("purple_dye", 1);
                resourceManager.addResource("purple_fabric", 1);
                break;
            case "lace":
                resourceManager.consumeResource("harvested_cotton", 1);
                resourceManager.addResource("lace", 1);
                break;
            case "white_bow":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.addResource("white_bow", 1);
                break;
            case "red_bow":
                resourceManager.consumeResource("red_fabric", 1);
                resourceManager.addResource("red_bow", 1);
                break;
            case "yellow_bow":
                resourceManager.consumeResource("yellow_fabric", 1);
                resourceManager.addResource("yellow_bow", 1);
                break;
            case "purple_bow":
                resourceManager.consumeResource("purple_fabric", 1);
                resourceManager.addResource("purple_bow", 1);
                break;
            case "pink_bow":
                resourceManager.consumeResource("pink_fabric", 1);
                resourceManager.addResource("pink_bow", 1);
                break;
            case "white_ribbon":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.addResource("white_ribbon", 1);
                break;
            case "red_ribbon":
                resourceManager.consumeResource("red_fabric", 1);
                resourceManager.addResource("red_ribbon", 1);
                break;
            case "yellow_ribbon":
                resourceManager.consumeResource("yellow_fabric", 1);
                resourceManager.addResource("yellow_ribbon", 1);
                break;
            case "purple_ribbon":
                resourceManager.consumeResource("purple_fabric", 1);
                resourceManager.addResource("purple_ribbon", 1);
                break;
            case "pink_ribbon":
                resourceManager.consumeResource("pink_fabric", 1);
                resourceManager.addResource("pink_ribbon", 1);
                break;
            case "white_dress":
                resourceManager.consumeResource("white_fabric", 2);
                resourceManager.addResource("white_dress", 1);
                break;
            case "red_dress":
                resourceManager.consumeResource("red_fabric", 2);
                resourceManager.addResource("red_dress", 1);
                break;
            case "yellow_dress":
                resourceManager.consumeResource("yellow_fabric", 2);
                resourceManager.addResource("yellow_dress", 1);
                break;
            case "purple_dress":
                resourceManager.consumeResource("purple_fabric", 2);
                resourceManager.addResource("purple_dress", 1);
                break;
            case "pink_dress":
                resourceManager.consumeResource("pink_fabric", 2);
                resourceManager.addResource("pink_dress", 1);
                break;
            case "white_shirt":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.addResource("white_shirt", 1);
                break;
            case "red_shirt":
                resourceManager.consumeResource("red_fabric", 1);
                resourceManager.addResource("red_shirt", 1);
                break;
            case "yellow_shirt":
                resourceManager.consumeResource("yellow_fabric", 1);
                resourceManager.addResource("yellow_shirt", 1);
                break;
            case "purple_shirt":
                resourceManager.consumeResource("purple_fabric", 1);
                resourceManager.addResource("purple_shirt", 1);
                break;
            case "pink_shirt":
                resourceManager.consumeResource("pink_fabric", 1);
                resourceManager.addResource("pink_shirt", 1);
                break;
            case "white_pants":
                resourceManager.consumeResource("white_fabric", 1);
                resourceManager.addResource("white_pants", 1);
                break;
            case "red_pants":
                resourceManager.consumeResource("red_fabric", 1);
                resourceManager.addResource("red_pants", 1);
                break;
            case "yellow_pants":
                resourceManager.consumeResource("yellow_fabric", 1);
                resourceManager.addResource("yellow_pants", 1);
                break;
            case "purple_pants":
                resourceManager.consumeResource("purple_fabric", 1);
                resourceManager.addResource("purple_pants", 1);
                break;
            case "pink_pants":
                resourceManager.consumeResource("pink_fabric", 1);
                resourceManager.addResource("pink_pants", 1);
                break;
        }
        
        resourceManager.notifyResourceChange();
    }
} 