package com.fashionodyssey.controller;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.Grid;
import com.fashionodyssey.model.resource.CropStage;
import com.fashionodyssey.util.ResourceManager;

public class FarmController {
    private static final int GRID_SIZE = 2;
    private ResourceManager resourceManager;
    private Grid[][] farmGrid;
    private int currentRow = 0;
    private int currentCol = 0;
    private int harvestCount = 0;

    public FarmController() {
        this.resourceManager = ResourceManager.getInstance();
        this.farmGrid = new Grid[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                farmGrid[i][j] = new Grid();
                EventManager.getInstance().fireEvent(
                    new GameEvent("UPDATE_FARM_SLOT", 
                        i * GRID_SIZE + j,
                        null,
                        CropStage.EMPTY
                    )
                );
            }
        }
        initializeResources();
    }

    private void initializeResources() {
        resourceManager.addResource("money", 1000);
        resourceManager.addResource("seeds", 10);
        resourceManager.addResource("water", 20);
        resourceManager.addResource("fertilizer", 5);
    }

    public void plantCrop(String cropType) {
        Grid currentGrid = farmGrid[currentRow][currentCol];
        if (!currentGrid.isOccupied()) {
            String seedType = cropType + "_seeds";
            if (resourceManager.getResourceAmount(seedType) > 0) {
                resourceManager.consumeResource(seedType, 1);
                currentGrid.plant(cropType);
                EventManager.getInstance().fireEvent(
                    new GameEvent("UPDATE_FARM_SLOT", 
                        currentRow * GRID_SIZE + currentCol,
                        cropType,
                        CropStage.SEEDED
                    )
                );
                showStatus("種植 " + cropType + "，現在處於種子階段");
            } else {
                showStatus(cropType + "的種子不足，無法種植");
            }
        } else {
            showStatus("這個格子已經種植作物了");
        }
    }

    private void showStatus(String message) {
        String funMessage = switch (message) {
            case "這個格子已經種植作物了" -> 
                "這裡已經住著一位小植物了，別擠啦！";
            case "水資源不足" -> 
                "水都不夠了，植物要渴死啦！快去澆水！";
            case "肥料不足" -> 
                "沒有肥料，植物寶寶們會長不高的！快去施肥！";
            case "作物還未成熟，無法收穫" -> 
                "還不夠成熟呢，先澆水施肥吧~";
            case "這個格子沒有作物可以收穫" -> 
                "這裡空空如也，種點什麼吧！";
            case "選擇了一個空地" -> 
                "這塊地正期待著新生命的到來！";
            case "作物不在種子階段，無法澆水" ->
                "現在不需要澆水啦，試試施肥？";
            case "作物尚未澆水，無法施肥" ->
                "別急著施肥，先給它澆點水吧！";
            case "澆水完成" ->
                "植物喝到水，精神百倍！現在可以施肥了～";
            case "施肥完成，作物已成熟" ->
                "施肥成功！作物長大成熟，可以收成啦！";
            case "選擇了種植 棉花 的格子" ->
                "這裡有一團軟綿綿的白色小可愛~";
            case "選擇了種植 玫瑰 的格子" ->
                "哇！玫瑰的香氣撲鼻而來~";
            case "選擇了種植 向日葵 的格子" ->
                "向日葵朝著你露出燦爛的笑容！";
            case "選擇了種植 鬱金香 的格子" ->
                "優雅的鬱金香在跟你打招呼呢！";
            case "選擇了種植 薰衣草 的格子" ->
                "空氣中瀰漫著薰衣草的香氣~";
            default -> {
                if (message.startsWith("種植")) {
                    yield message + " 加油長大吧！";
                } else if (message.startsWith("成功收穫")) {
                    yield "豐收啦！" + message.substring(2);
                } else {
                    yield message;
                }
            }
        };
        
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_STATUS", funMessage)
        );
        System.out.println(funMessage);
    }

    public void waterCrop() {
        Grid currentGrid = farmGrid[currentRow][currentCol];
        if (currentGrid.isOccupied()) {
            if (currentGrid.getCropStage() == CropStage.SEEDED) {
                if (resourceManager.canPerformAction("WATER")) {
                    currentGrid.water();
                    resourceManager.consumeActionResource("WATER");
                    EventManager.getInstance().fireEvent(
                        new GameEvent("UPDATE_FARM_SLOT", 
                            currentRow * GRID_SIZE + currentCol,
                            currentGrid.getCropType(),
                            CropStage.WATERED
                        )
                    );
                    showStatus("澆水完成");
                } else {
                    showStatus("水資源不足");
                }
            } else {
                showStatus("作物不在種子階段，無法澆水");
            }
        } else {
            showStatus("這個格子沒有作物");
        }
    }

    public void fertilizeCrop() {
        Grid currentGrid = farmGrid[currentRow][currentCol];
        if (currentGrid.isOccupied()) {
            if (currentGrid.getCropStage() == CropStage.WATERED) {
                if (resourceManager.canPerformAction("FERTILIZE")) {
                    currentGrid.fertilize();
                    resourceManager.consumeActionResource("FERTILIZE");
                    currentGrid.mature();
                    EventManager.getInstance().fireEvent(
                        new GameEvent("UPDATE_FARM_SLOT", 
                            currentRow * GRID_SIZE + currentCol,
                            currentGrid.getCropType(),
                            CropStage.MATURE
                        )
                    );
                    showStatus("施肥完成，作物已成熟");
                } else {
                    showStatus("肥料不足");
                }
            } else {
                showStatus("作物尚未澆水，無法施肥");
            }
        } else {
            showStatus("這個格子沒有作物");
        }
    }

    public void harvestCrop() {
        Grid currentGrid = farmGrid[currentRow][currentCol];
        if (currentGrid.isOccupied()) {
            if (currentGrid.getCropStage() == CropStage.MATURE) {
                String cropType = currentGrid.getCropType();
                resourceManager.addHarvestedCrop(cropType);
                currentGrid.clear();
                harvestCount++;
                EventManager.getInstance().fireEvent(
                    new GameEvent("UPDATE_FARM_SLOT", 
                        currentRow * GRID_SIZE + currentCol,
                        null,
                        CropStage.EMPTY
                    )
                );
                EventManager.getInstance().fireEvent(
                    new GameEvent("UPDATE_HARVEST_COUNT", harvestCount)
                );
                resourceManager.notifyInventoryChange();
            } else {
                showStatus("作物還未成熟，無法收穫");
            }
        } else {
            showStatus("這個格子沒有作物可以收穫");
        }
    }

    public void setCurrentPosition(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            this.currentRow = row;
            this.currentCol = col;
            Grid currentGrid = farmGrid[row][col];
            if (currentGrid.isOccupied()) {
                showStatus("選擇了種植 " + currentGrid.getCropType() + " 的格子");
            } else {
                showStatus("選擇了一個空地");
            }
        } else {
            showStatus("選擇的位置超出範圍");
        }
    }

    public int getHarvestCount() {
        return harvestCount;
    }
}