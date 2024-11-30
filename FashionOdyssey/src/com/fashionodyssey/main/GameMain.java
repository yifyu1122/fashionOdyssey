package com.fashionodyssey.main;

import com.fashionodyssey.controller.FarmController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.model.resource.CropStage;
import com.fashionodyssey.ui.MainFrame;
import com.fashionodyssey.util.ResourceManager;

public class GameMain {
    public static void main(String[] args) {
        FarmController farmController = new FarmController();
        MainFrame mainFrame = new MainFrame();
        ResourceManager resourceManager = ResourceManager.getInstance();
        
        EventManager.getInstance().addEventListener("PLANT_CROP", event -> {
            farmController.plantCrop((String)event.getData());
        });
        
        EventManager.getInstance().addEventListener("HARVEST_CROP", event -> {
            farmController.harvestCrop();
        });
        
        EventManager.getInstance().addEventListener("UPDATE_FARM_SLOT", event -> {
            int index = (Integer) event.getArgs()[0];
            String cropType = (String) event.getArgs()[1];
            CropStage stage = (CropStage) event.getArgs()[2];
            mainFrame.getFarmPanel().updateSlotStatus(index, cropType, stage);
        });
        
        EventManager.getInstance().addEventListener("SELECT_FARM_SLOT", event -> {
            int row = (Integer) event.getArgs()[0];
            int col = (Integer) event.getArgs()[1];
            farmController.setCurrentPosition(row, col);
        });
        
        EventManager.getInstance().addEventListener("UPDATE_STATUS", event -> {
            String message = (String) event.getData();
            mainFrame.updateStatus(message);
        });
        
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            int money = (Integer) event.getArgs()[0];
            int seeds = (Integer) event.getArgs()[1];
            int water = (Integer) event.getArgs()[2];
            int fertilizer = (Integer) event.getArgs()[3];
            mainFrame.updateResources(money, seeds, water, fertilizer);
        });
        
        EventManager.getInstance().addEventListener("WATER_CROP", event -> {
            farmController.waterCrop();
        });
        
        EventManager.getInstance().addEventListener("FERTILIZE_CROP", event -> {
            farmController.fertilizeCrop();
        });
        
        EventManager.getInstance().addEventListener("BUY_SEED", event -> {
            String cropType = (String) event.getData();
            resourceManager.buySeed(cropType);
        });
        
        EventManager.getInstance().addEventListener("UPDATE_INVENTORY", event -> {
            String[] items = (String[]) event.getArgs()[0];
            int[] amounts = (int[]) event.getArgs()[1];
            mainFrame.getInventoryPanel().updateInventory(items, amounts);
        });
        
        EventManager.getInstance().addEventListener("BUY_FERTILIZER", event -> {
            resourceManager.buyFertilizer();
        });
        
        mainFrame.setVisible(true);
    }
} 