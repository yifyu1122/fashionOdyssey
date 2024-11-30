package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;
import java.util.Map;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.resource.CropStage;
import com.fashionodyssey.model.resource.CropType;
import com.fashionodyssey.ui.dialog.ShopDialog;
import com.fashionodyssey.util.ResourceManager;
import com.fashionodyssey.util.ItemDescription;

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
    
    public FarmPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // 作物選擇區
        JPanel controlPanel = new JPanel();
        String[] crops = {
            CropType.COTTON.getDisplayName(),
            CropType.ROSE.getDisplayName(),
            CropType.SUNFLOWER.getDisplayName(),
            CropType.TULIP.getDisplayName(),
            CropType.LAVENDER.getDisplayName()
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
        harvestCountLabel = new JLabel("收穫數量: 0");
        
        // 設置按鈕字體
        plantButton.setFont(buttonFont);
        waterButton.setFont(buttonFont);
        fertilizeButton.setFont(buttonFont);
        harvestButton.setFont(buttonFont);
        harvestCountLabel.setFont(labelFont);
        
        // 商店按鈕
        JButton shopButton = new JButton("商店");
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
        controlPanel.add(harvestCountLabel);
        
        // 設置控制面板的邊距
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(controlPanel, BorderLayout.NORTH);
        
        // 農場格子
        JPanel farmGrid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5));
        farmGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        farmSlots = new JButton[GRID_SIZE * GRID_SIZE];
        
        Dimension buttonSize = new Dimension(120, 120);
        Font gridFont = new Font("微軟正黑體", Font.BOLD, 14);  // 格子字體
        
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            final int index = i;
            farmSlots[i] = new JButton("空地");
            farmSlots[i].setFont(gridFont);
            farmSlots[i].setToolTipText("點擊選擇此格子");
            farmSlots[i].setPreferredSize(buttonSize);
            farmSlots[i].setMinimumSize(buttonSize);
            farmSlots[i].setMaximumSize(buttonSize);
            farmSlots[i].setOpaque(true);
            farmSlots[i].setBorderPainted(true);
            farmSlots[i].setContentAreaFilled(true);
            farmSlots[i].addActionListener(e -> {
                int row = index / GRID_SIZE;
                int col = index % GRID_SIZE;
                EventManager.getInstance().fireEvent(
                    new GameEvent("SELECT_FARM_SLOT", row, col)
                );
            });
            farmGrid.add(farmSlots[i]);
        }
        
        JScrollPane scrollPane = new JScrollPane(farmGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        
        plantButton.addActionListener(e -> {
            EventManager.getInstance().fireEvent(
                new GameEvent("PLANT_CROP", cropSelector.getSelectedItem())
            );
        });
        
        harvestButton.addActionListener(e -> {
            EventManager.getInstance().fireEvent(
                new GameEvent("HARVEST_CROP", null)
            );
        });
        
        waterButton.addActionListener(e -> {
            EventManager.getInstance().fireEvent(
                new GameEvent("WATER_CROP", null)
            );
        });
        
        fertilizeButton.addActionListener(e -> {
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
    }

    public void updateHarvestCount(int count) {
        harvestCountLabel.setText("收穫數量: " + count);
    }

    public void updateSlotStatus(int index, String cropType, CropStage stage) {
        if (index >= 0 && index < farmSlots.length) {
            String status = "空地";
            String tooltip = "點擊選擇此格子";
            
            if (cropType != null) {
                switch (stage) {
                    case SEEDED:
                        status = cropType + " (種子)";
                        tooltip = "需要澆水";
                        break;
                    case WATERED:
                        status = cropType + " (已澆水)";
                        tooltip = "需要施肥";
                        break;
                    case FERTILIZED:
                        status = cropType + " (已施肥)";
                        tooltip = "等待成熟";
                        break;
                    case MATURE:
                        status = cropType + " (已成熟)";
                        tooltip = "可以收割";
                        break;
                }
                tooltip += "\n" + getCropDescription(cropType);
            }
            
            farmSlots[index].setText("<html>" + status + "</html>");
            farmSlots[index].setToolTipText(tooltip);
            
            if (cropType != null) {
                switch (stage) {
                    case SEEDED:
                        farmSlots[index].setBackground(new Color(255, 255, 200));
                        break;
                    case WATERED:
                        farmSlots[index].setBackground(new Color(200, 255, 200));
                        break;
                    case FERTILIZED:
                        farmSlots[index].setBackground(new Color(200, 255, 255));
                        break;
                    case MATURE:
                        farmSlots[index].setBackground(new Color(255, 200, 200));
                        break;
                    default:
                        farmSlots[index].setBackground(null);
                }
            } else {
                farmSlots[index].setBackground(null);
            }
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
    
    private void updateSeedCount(String cropType) {
        EventManager.getInstance().fireEvent(
            new GameEvent("UPDATE_SEED_COUNT", cropType)
        );
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
        
        String[] cropTypes = {"棉花", "玫瑰", "向日葵", "鬱金香", "薰衣草"};
        String[] seedKeys = {"cotton_seeds", "rose_seeds", "sunflower_seeds", "tulip_seeds", "lavender_seeds"};
        
        for (int i = 0; i < cropTypes.length; i++) {
            JLabel seedLabel = new JLabel(String.format("%s種子: %d", 
                cropTypes[i], rm.getResourceAmount(seedKeys[i])));
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
        
        String[] harvestKeys = {
            "harvested_cotton", "harvested_rose", "harvested_sunflower", 
            "harvested_tulip", "harvested_lavender"
        };
        
        for (int i = 0; i < cropTypes.length; i++) {
            JLabel harvestLabel = new JLabel(String.format("收穫的%s: %d", 
                cropTypes[i], rm.getResourceAmount(harvestKeys[i])));
            harvestLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            harvestLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(harvestLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }
} 