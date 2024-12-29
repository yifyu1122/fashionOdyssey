package com.fashionodyssey.main;

import com.fashionodyssey.controller.FarmController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.model.resource.CropStage;
import com.fashionodyssey.ui.MainFrame;
import com.fashionodyssey.util.ResourceManager;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameMain {
    public static void main(String[] args) {
        FarmController farmController = new FarmController();
        MainFrame mainFrame = new MainFrame();
        ResourceManager resourceManager = ResourceManager.getInstance();
        
        EventManager.getInstance().addEventListener("HARVEST_CROP", event -> {
            farmController.harvestCrop();
        });
        
        EventManager.getInstance().addEventListener("UPDATE_FARM_SLOT", event -> {
            int index = (Integer) event.getArgs()[0];
            String cropType = (String) event.getArgs()[1];
            CropStage stage = (CropStage) event.getArgs()[2];
            
            int row = index / 2;  // GRID_SIZE = 2
            int col = index % 2;
            
            mainFrame.getFarmPanel().updateSlotStatus(row, col, cropType, stage);
        });
        
        EventManager.getInstance().addEventListener("SELECT_FARM_SLOT", event -> {
            int row = (Integer) event.getArgs()[0];
            int col = (Integer) event.getArgs()[1];
            farmController.setCurrentPosition(row, col);
        });
        
    
        
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            Object[] resourceArgs = event.getArgs();
            if (resourceArgs != null && resourceArgs.length >= 4) {
                int seeds = (Integer) resourceArgs[1];
                int water = (Integer) resourceArgs[2];
                int fertilizer = (Integer) resourceArgs[3];
                mainFrame.updateResources( seeds, water, fertilizer);
            }
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
        
        EventManager.getInstance().addEventListener("BUY_FERTILIZER", event -> {
            resourceManager.buyFertilizer();
        });
        
        EventManager.getInstance().addEventListener("UPDATE_INVENTORY", event -> {
            mainFrame.getInventoryPanel().updateResources();
        });
 
        // 播放音樂
        playMusic("c:/Users/user/Desktop/FashionOdyssey/src/main/resources/music/Sunshine and Stitch.wav");

        mainFrame.setVisible(true);
    }

    private static void playMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                System.out.println("Music file found: " + filePath);
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Can't find file: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}