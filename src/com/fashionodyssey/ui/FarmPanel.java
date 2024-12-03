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
    private static final int GRID_SIZE = 1;
    private JButton[] farmSlots;
    private JComboBox<String> cropSelector;
    private JButton plantButton;
    private JButton harvestButton;
    private JButton waterButton;
    private JButton fertilizeButton;
    private JLabel harvestCountLabel;
    private JPanel resourcePanel;
    private JLabel statusLabel;
    
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
        rm.notifyInventoryChange();
    }
    
    private void initComponents() {
        // 作物選擇區
        JPanel controlPanel = new JPanel();
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
        JButton shopButton = new JButton("商");
        shopButton.setFont(buttonFont);
        shopButton.addActionListener(e -> {
            ShopDialog shop = new ShopDialog(SwingUtilities.getWindowAncestor(this));
            shop.setVisible(true);
        });
        
        JLabel cropLabel = new JLabel("選擇作物:");
        cropLabel.setFont(labelFont);
        
        controlPanel.add(cropLabel);
        controlPanel.add(cropSelector);
        controlPanel.add(shopButton);
        controlPanel.add(plantButton);
        controlPanel.add(waterButton);
        controlPanel.add(fertilizeButton);
        controlPanel.add(harvestButton);
        
        // 設置控制面板的邊距
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(controlPanel, BorderLayout.NORTH);
        
        // 農場格子
        JPanel farmGrid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5));
        farmGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        farmSlots = new JButton[GRID_SIZE * GRID_SIZE];
        
        Dimension buttonSize = new Dimension(120, 120);
        Font gridFont = new Font("微軟正黑體", Font.BOLD, 14);  // 格子字體
        
        for (int slotIndex = 0; slotIndex < GRID_SIZE * GRID_SIZE; slotIndex++) {
            final int index = slotIndex;
            farmSlots[index] = new JButton("一個空地");
            farmSlots[index].setFont(gridFont);
            farmSlots[index].setToolTipText("點擊選擇此格子");
            farmSlots[index].setPreferredSize(buttonSize);
            farmSlots[index].setMinimumSize(buttonSize);
            farmSlots[index].setMaximumSize(buttonSize);
            farmSlots[index].setOpaque(true);
            farmSlots[index].setBorderPainted(true);
            farmSlots[index].setContentAreaFilled(true);
            farmSlots[index].addActionListener(e -> {
                System.out.println("===== 土地格子點擊事件 =====");
                this.requestFocusInWindow();
                int row = index / GRID_SIZE;
                int col = index % GRID_SIZE;
                System.out.println("點擊的格子索引: " + index);
                System.out.println("轉換後座標: row=" + row + ", col=" + col);
                
                EventManager.getInstance().fireEvent(
                    new GameEvent("SELECT_FARM_SLOT", row, col)
                );
            });
            farmGrid.add(farmSlots[index]);
        }
        
        JScrollPane scrollPane = new JScrollPane(farmGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        
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
                
                System.out.println("\n資源檢查：");
                System.out.println("作物鍵值: " + cropKey);
                System.out.println("種子鍵值: " + seedKey);
                System.out.println("當前種子數量: " + seedCount);
                
                System.out.println("\n所有種子數量：");
                System.out.println("cotton_seeds: " + rm.getResourceAmount("cotton_seeds"));
                System.out.println("rose_seeds: " + rm.getResourceAmount("rose_seeds"));
                System.out.println("sunflower_seeds: " + rm.getResourceAmount("sunflower_seeds"));
                System.out.println("tulip_pink_seeds: " + rm.getResourceAmount("tulip_pink_seeds"));
                System.out.println("lavender_seeds: " + rm.getResourceAmount("lavender_seeds"));
                
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
        
        // 在控制面板下方添加狀態標籤
        statusLabel = new JLabel("歡迎來到農場！");
        statusLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(statusLabel, BorderLayout.SOUTH);
        
        // 註冊事件監聽器
        EventManager.getInstance().addEventListener("UPDATE_STATUS", event -> {
            String message = (String) event.getData();
            statusLabel.setText(message);
        });
    }

    public void updateHarvestCount(int count) {
        harvestCountLabel.setText("收穫數量: " + count);
    }

    public void updateSlotStatus(int index, String cropType, CropStage stage) {
        if (index >= 0 && index < farmSlots.length) {
            String status = "一個空地";  // Default status
            String tooltip = "點擊選擇此格子";
            Color bgColor = null;  // Default background color
            
            if (cropType != null) {  // If there's a crop, show its status
                String displayName = getChineseCropName(cropType);
                
                switch (stage) {
                    case SEEDED:
                        status = displayName + " (種子)";
                        tooltip = "需要水";
                        bgColor = new Color(233, 236, 239);  // Light gray
                        break;
                    case WATERED:
                        status = displayName + " (已澆水)";
                        tooltip = "需施肥";
                        bgColor = new Color(209, 231, 221);  // Light green
                        break;
                    case FERTILIZED:
                        status = displayName + " (已施肥)";
                        tooltip = "等待成熟";
                        bgColor = new Color(255, 243, 205);  // Light yellow
                        break;
                    case MATURE:
                        status = displayName + " (已成熟)";
                        tooltip = "可以收割";
                        bgColor = new Color(255, 228, 181);  // Light orange
                        break;
                }
                tooltip += "\n" + getCropDescription(displayName);
            }
            
            farmSlots[index].setText("<html><center>" + status + "</center></html>");
            farmSlots[index].setToolTipText(tooltip);
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
} 