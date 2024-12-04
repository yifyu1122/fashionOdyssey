package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;


import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.resource.CropStage;
import com.fashionodyssey.model.resource.CropType;
import com.fashionodyssey.ui.dialog.ShopDialog;
import com.fashionodyssey.util.ResourceManager;

public class FarmPanel extends JPanel {
    private static final int GRID_SIZE = 2;
    private JButton[] farmSlots;
    private JComboBox<String> cropSelector;
    private JButton plantButton;
    private JButton harvestButton;
    private JButton waterButton;
    private JButton fertilizeButton;
    private JLabel harvestCountLabel;
    private JPanel resourcePanel;
    private JLabel statusLabel;
    private JLabel farmStatusLabel;
    
    public FarmPanel() {
        setLayout(new BorderLayout());
        
        initComponents();
        
        // 監聽資源更新事件
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            System.out.println("收到資源更新事件");  // 除錯訊息
            ResourceManager rm = ResourceManager.getInstance();
            updateResources(
                rm.getResourceAmount(rm.getCurrentCropType() + "_seeds"),
                0,
                rm.getResourceAmount("fertilizer")
            );
            System.out.println("資源面板已更新");  // 除錯訊息
        });
        
        // 在初始化後立即觸發資源更新
        ResourceManager rm = ResourceManager.getInstance();
        updateResources(
            rm.getResourceAmount(rm.getCurrentCropType() + "_seeds"),
            0,
            rm.getResourceAmount("fertilizer")
        );
        
        // 通知資源管理器觸發初始更新
        rm.notifyResourceChange();
        rm.notifyInventoryChange();
    }
    
    private void initComponents() {
        // 初始化農場狀態標籤
        farmStatusLabel = new JLabel("這塊地正期待著新生命的到來！");
        farmStatusLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        farmStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 創建控制面板
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();  // 用於放置按鈕的面板
        
        // 將農場狀態標籤添加到控制面板的頂部
        controlPanel.add(farmStatusLabel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // 作物選擇區
        String[] crops = {
            CropType.cotton.getDisplayName(),
            CropType.rose.getDisplayName(),
            CropType.sunflower.getDisplayName(),
            CropType.lavender.getDisplayName(),
            CropType.tulip_pink.getDisplayName()
        };
        
        // 設置大字體
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 16);
        Font labelFont = new Font("微軟正黑體", Font.PLAIN, 16);
        
        cropSelector = new JComboBox<>(crops);
        cropSelector.setFont(buttonFont);
        
        // 設置按鈕字體
        plantButton = new JButton("種植");
        waterButton = new JButton("澆水");
        fertilizeButton = new JButton("施肥");
        harvestButton = new JButton("收穫");
        
        // 設置按鈕字體
        plantButton.setFont(buttonFont);
        waterButton.setFont(buttonFont);
        fertilizeButton.setFont(buttonFont);
        harvestButton.setFont(buttonFont);
        
        // 商店按鈕
        JButton shopButton = new JButton("商店");
        shopButton.setFont(buttonFont);
        shopButton.addActionListener(e -> {
            ShopDialog shop = new ShopDialog(SwingUtilities.getWindowAncestor(this));
            shop.setVisible(true);
        });
        
        JLabel cropLabel = new JLabel("選擇作物:");
        cropLabel.setFont(labelFont);
        
        buttonPanel.add(cropLabel);
        buttonPanel.add(cropSelector);
        buttonPanel.add(shopButton);
        buttonPanel.add(plantButton);
        buttonPanel.add(waterButton);
        buttonPanel.add(fertilizeButton);
        buttonPanel.add(harvestButton);
        
        // 設置控制面板的邊距
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(controlPanel, BorderLayout.NORTH);
        
        // 農場格子
        JPanel farmGrid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5));
        farmGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        farmSlots = new JButton[GRID_SIZE * GRID_SIZE];
        
        Dimension buttonSize = new Dimension(120, 120);
        Font gridFont = new Font("微軟正黑體", Font.BOLD, 14);  // 格子字體
        
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                final int index = i * GRID_SIZE + j;
                farmSlots[index] = new JButton("一個空地");
                farmSlots[index].setFont(gridFont);
                farmSlots[index].setPreferredSize(buttonSize);
                farmSlots[index].setMinimumSize(buttonSize);
                farmSlots[index].setMaximumSize(buttonSize);
                
                // 設置點擊事件
                final int row = i;
                final int col = j;
                farmSlots[index].addActionListener(e -> {
                    System.out.println("點擊格子：" + row + "," + col);
                    EventManager.getInstance().fireEvent(
                        new GameEvent("SELECT_FARM_SLOT", row, col)
                    );
                });
                
                farmGrid.add(farmSlots[index]);
            }
        }
        
        // 創建一個新的面板來包含農場格子和狀態標籤
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(farmGrid, BorderLayout.CENTER);
        
        // 創建一個面板來放置狀態標籤，並將其靠左對齊
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(farmStatusLabel, BorderLayout.WEST);
        
        // 設置狀態標籤的邊距
        farmStatusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));  // 上, 左, 下, 右
        
        centerPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        plantButton.addActionListener(e -> {
            System.out.println("\n===== 種植按鈕點擊事件 =====");
            this.requestFocusInWindow();
            String selectedCrop = (String) cropSelector.getSelectedItem();
            System.out.println("選擇的作物: " + selectedCrop);
            CropType cropType = CropType.fromDisplayName(selectedCrop);
            System.out.println("轉換後的作物類型: " + cropType);
            
            if (cropType != null) {
                String cropKey = ResourceManager.CROP_KEY_MAP.get(selectedCrop);
                String seedKey = cropKey + "_seeds";
                ResourceManager rm = ResourceManager.getInstance();
                int seedCount = rm.getResourceAmount(seedKey);
                

                
                if (seedCount > 0) {
                    System.out.println("\n開始種植：");
                    System.out.println("觸發種植事件: PLANT_CROP, " + cropKey);
                    EventManager.getInstance().fireEvent(
                        new GameEvent("PLANT_CROP", cropKey)
                    );
                } else {
                    System.out.println("\n種植失敗：種子數量不足");
                }
            } else {
                System.out.println("\n種植失敗：作物類型轉換失敗");
            }
            System.out.println("===== 種植事件結束 =====\n");
        });
        
        harvestButton.addActionListener(e -> {
            System.out.println("\n===== 收穫按鈕點擊事件 =====");
            requestFocusInWindow();
            
            // 獲取當前選中的作物類型
            ResourceManager rm = ResourceManager.getInstance();
            String currentCrop = rm.getCurrentCropType();
            System.out.println("當前作物：" + currentCrop);
            
            // 檢查收穫前的資源數量
            String harvestKey = "harvested_" + ResourceManager.CROP_KEY_MAP.get(currentCrop).toLowerCase();
            System.out.println("收穫前數量檢查：");
            System.out.println("收穫鍵值：" + harvestKey);
            System.out.println("當前收穫數量：" + rm.getResourceAmount(harvestKey));
            
            // 觸發收穫事件
            EventManager.getInstance().fireEvent(
                new GameEvent("HARVEST_CROP", null)
            );
            
            // 檢查收穫後的資源數量
            System.out.println("收穫後數量檢查：");
            System.out.println("收穫數量：" + rm.getResourceAmount(harvestKey));
            System.out.println("===== 收穫事件結束 =====\n");
        });
        
        waterButton.addActionListener(e -> {
            requestFocusInWindow();
            EventManager.getInstance().fireEvent(
                new GameEvent("WATER_CROP", null)
            );
        });
        
        fertilizeButton.addActionListener(e -> {
            requestFocusInWindow();
            EventManager.getInstance().fireEvent(
                new GameEvent("FERTILIZE_CROP", null)
            );
        });
        
        cropSelector.addActionListener(e -> {
            String selectedCrop = (String) cropSelector.getSelectedItem();
            ResourceManager.getInstance().setCurrentCropType(selectedCrop);
        });
        
        // 修改資源面板
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resourcePanel.setPreferredSize(new Dimension(200, 0));  // 設置固定寬度
        resourcePanel.setBackground(new Color(240, 240, 240));  // 設置背景色
        
        // 確保所有標籤左對齊
        resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 將資源面板添加到左側
        JScrollPane resourceScrollPane = new JScrollPane(resourcePanel);
        resourceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resourceScrollPane.setPreferredSize(new Dimension(220, 0));
        
        add(resourceScrollPane, BorderLayout.WEST);
        
        // 註冊事件監聽器
        EventManager.getInstance().addEventListener("UPDATE_STATUS", event -> {
            String message = (String) event.getData();
            updateFarmStatus(message);
        });
    }

    public void updateHarvestCount(int count) {
        harvestCountLabel.setText("收穫數量: " + count);
    }

    public void updateSlotStatus(int row, int col, String cropType, CropStage stage) {
        int index = row * GRID_SIZE + col;
        if (index >= 0 && index < farmSlots.length) {
            String status = "一個空地";
            String message = "這塊地正期待著新生命的到來！";
            Color bgColor = null;
            
            if (cropType != null) {
                String displayName = getChineseCropName(cropType);
                
                switch (stage) {
                    case SEEDED -> {
                        status = displayName + " (種子)";
                        message = "植物寶寶需要水，快去澆水吧！";
                        bgColor = new Color(233, 236, 239);
                    }
                    case WATERED -> {
                        status = displayName + " (已澆水)";
                        message = "喝飽水的植物需要營養，快去施肥吧！";
                        bgColor = new Color(209, 231, 221);
                    }
                    case FERTILIZED -> {
                        status = displayName + " (施肥)";
                        message = "植物正在努力成長中，讓我們耐心等待～";
                        bgColor = new Color(255, 243, 205);
                    }
                    case MATURE -> {
                        status = displayName + " (已成熟)";
                        message = switch (cropType.toLowerCase()) {
                            case "cotton" -> "這裡有一團軟綿綿的白色小可愛，可以收成啦！";
                            case "rose" -> "哇！玫瑰的香氣撲鼻而來，趕快收成吧！";
                            case "sunflower" -> "向日葵朝著你露出燦爛的笑容，該收成啦！";
                            case "tulip_pink" -> "優雅的粉色鬱金香在跟你打招呼，可以收成了！";
                            case "lavender" -> "空氣中瀰漫著薰衣草的香氣，該收成囉！";
                            default -> "作物已經成熟，可以收成了！";
                        };
                        bgColor = new Color(255, 228, 181);
                    }
                }
            }
            
            farmSlots[index].setText("<html><center>" + status + "<br>" + 
                                   "<font size='2'>" + message + "</font></center></html>");
            farmSlots[index].setBackground(bgColor);
        }
    }
    
    private String getCropDescription(String cropType) {
        if (cropType == null) return "空地";
        
        for (CropType type : CropType.values()) {
            if (type.getDisplayName().equals(cropType)) {
                return type.getDescription() + 
                    "\n種子成本: $" + type.getSeedCost();
            }
        }
        return cropType;
    }
    


    public void updateResources(int seeds, int water, int fertilizer) {
        resourcePanel.removeAll();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 設置標題
        JLabel titleLabel = new JLabel("農場資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        ResourceManager rm = ResourceManager.getInstance();
        
        // 顯示肥料數量
        JLabel fertilizerLabel = new JLabel(String.format("肥料: %d", fertilizer));
        fertilizerLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        fertilizerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(fertilizerLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // 顯示種子數量
        JLabel seedTitle = new JLabel("種子數量：");
        seedTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        seedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(seedTitle);
        
        // 使用 CropType enum 來遍歷所有作物
        for (CropType crop : CropType.values()) {
            String seedCount = String.format("%s種子: %d", 
                crop.getDisplayName(), 
                rm.getResourceAmount(crop.name().toLowerCase() + "_seeds")
            );
            
            JLabel seedLabel = new JLabel(seedCount);
            seedLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            seedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(seedLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        // 顯示收穫數量
        contentPanel.add(Box.createVerticalStrut(20));
        JLabel harvestTitle = new JLabel("收穫數量：");
        harvestTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        harvestTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(harvestTitle);
        
        // 同樣使用 CropType enum 來顯示收穫數量
        for (CropType crop : CropType.values()) {
            String harvestCount = String.format("收穫的%s: %d", 
                crop.getDisplayName(), 
                rm.getResourceAmount("harvested_" + crop.name().toLowerCase())
            );
            
            JLabel harvestLabel = new JLabel(harvestCount);
            harvestLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            harvestLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(harvestLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }

    private String getChineseCropName(String cropType) {
        return switch (cropType.toLowerCase()) {
            case "cotton" -> "棉花";
            case "rose" -> "玫瑰";
            case "sunflower" -> "向日葵";
            case "lavender" -> "薰衣草";
            case "tulip_pink" -> "鬱金香(粉)";
            default -> cropType;
        };
    }

    // 更新農場狀態的方法
    private void updateFarmStatus(String message) {
        farmStatusLabel.setText(message);
    }
} 