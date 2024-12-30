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
    private static final Color PINK_THEME = new Color(255, 182, 193);
    private static final Color LIGHT_PINK = new Color(255, 218, 224);
    private static final Color SOFT_YELLOW = new Color(255, 245, 200);
    private static final Color MINT_GREEN = new Color(200, 255, 214);
    private static final Color TEXT_COLOR = new Color(80, 80, 80);

    private JButton[] farmSlots;
    private JComboBox<String> cropSelector;
    private JButton plantButton;
    private JButton harvestButton;
    private JButton waterButton;
    private JButton fertilizeButton;
    private JLabel harvestCountLabel;
    private JPanel resourcePanel;
    private JLabel farmStatusLabel;
    
    public FarmPanel() {
        setLayout(new BorderLayout());
        setBackground(LIGHT_PINK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        initComponents();
        
        // 監聽資源更新事件
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            ResourceManager rm = ResourceManager.getInstance();
            updateResources(
                rm.getResourceAmount(rm.getCurrentCropType() + "_seeds"),
                0,
                rm.getResourceAmount("fertilizer")
            );
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
        
        // Inside the FarmPanel constructor, add event listeners
        EventManager.getInstance().addEventListener("HARVEST_CROP", event -> {
            
        });
        
        EventManager.getInstance().addEventListener("BUY_FERTILIZER", event -> {
            // Implement the logic to buy fertilizer
            buyFertilizer();
        });
    }
    
    private void initComponents() {
        // 修改控制面板樣式
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBackground(LIGHT_PINK);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_THEME, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 初始化農場狀態標籤
        farmStatusLabel = new JLabel("這塊地正期待著新生命的到來！");
        farmStatusLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        farmStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        farmStatusLabel.setBackground(LIGHT_PINK);
        farmStatusLabel.setOpaque(true);
        
        // 創建控制面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_PINK);
        
        // 作物選擇區
        String[] crops = {
            CropType.cotton.getDisplayName(),
            CropType.rose.getDisplayName(),
            CropType.sunflower.getDisplayName(),
            CropType.lavender.getDisplayName(),
            CropType.tulip_pink.getDisplayName()
        };
        
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 16);
        Font labelFont = new Font("微軟正黑體", Font.PLAIN, 16);
        
        cropSelector = new JComboBox<>(crops);
        cropSelector.setFont(buttonFont);
        
        // 設置按鈕字體和樣式
        plantButton = createStyledButton("種植", buttonFont);
        waterButton = createStyledButton("澆水", buttonFont);
        fertilizeButton = createStyledButton("施肥", buttonFont);
        harvestButton = createStyledButton("收穫", buttonFont);
        
        JButton shopButton = createStyledButton("商店", buttonFont);
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
        
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        
        // 農場格子
        JPanel farmGrid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5));
        farmGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        farmGrid.setBackground(LIGHT_PINK);
        farmSlots = new JButton[GRID_SIZE * GRID_SIZE];
        
        Dimension buttonSize = new Dimension(120, 120);
        Font gridFont = new Font("微軟正黑體", Font.BOLD, 18);
        
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                final int index = i * GRID_SIZE + j;
                String status = "一個空地";
                String message = "這塊地正期待著新生命的到來！";
                farmSlots[index] = new JButton("<html><center><font size='4'>" + status + "</font><br>" + 
                                               "<font size='3'>" + message + "</font></center></html>");
                farmSlots[index].setFont(gridFont);
                farmSlots[index].setPreferredSize(buttonSize);
                farmSlots[index].setMinimumSize(buttonSize);
                farmSlots[index].setMaximumSize(buttonSize);
                farmSlots[index].setBackground(new Color(210, 180, 140));
                farmSlots[index].setBorder(BorderFactory.createLineBorder(PINK_THEME, 1));
                
                final int row = i;
                final int col = j;
                farmSlots[index].addActionListener(e -> {
                    EventManager.getInstance().fireEvent(
                        new GameEvent("SELECT_FARM_SLOT", row, col)
                    );
                });
                
                farmGrid.add(farmSlots[index]);
            }
        }
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(farmGrid, BorderLayout.CENTER);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(LIGHT_PINK);
        statusPanel.add(farmStatusLabel, BorderLayout.WEST);
        
        farmStatusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        centerPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        plantButton.addActionListener(e -> {
            String selectedCrop = (String) cropSelector.getSelectedItem();
            CropType cropType = CropType.fromDisplayName(selectedCrop);
            
            if (cropType != null) {
                String cropKey = ResourceManager.CROP_KEY_MAP.get(selectedCrop);
                String seedKey = cropKey + "_seeds";
                ResourceManager rm = ResourceManager.getInstance();
                int seedCount = rm.getResourceAmount(seedKey);
                
                if (seedCount > 0) {
                    EventManager.getInstance().fireEvent(
                        new GameEvent("PLANT_CROP", cropKey)
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "種子數量不足", "錯誤", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "作物類型轉換失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        harvestButton.addActionListener(e -> {
            ResourceManager rm = ResourceManager.getInstance();
            String currentCrop = rm.getCurrentCropType();
            
            String harvestKey = "harvested_" + ResourceManager.CROP_KEY_MAP.get(currentCrop).toLowerCase();
            
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
        resourcePanel.setPreferredSize(new Dimension(200, 0));
        resourcePanel.setBackground(Color.WHITE);
        
        resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane resourceScrollPane = new JScrollPane(resourcePanel);
        resourceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resourceScrollPane.setPreferredSize(new Dimension(220, 0));
        
        add(resourceScrollPane, BorderLayout.WEST);
        
        EventManager.getInstance().addEventListener("UPDATE_STATUS", event -> {
            String message = (String) event.getData();
            updateFarmStatus(message);
        });
    }
    
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(SOFT_YELLOW);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(MINT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SOFT_YELLOW);
            }
        });
        
        return button;
    }
    
    public void updateHarvestCount(int count) {
        harvestCountLabel.setText("收穫數量: " + count);
    }

    public void updateSlotStatus(int row, int col, String cropType, CropStage stage) {
        int index = row * GRID_SIZE + col;
        if (index >= 0 && index < farmSlots.length) {
            String status = "一個空地";
            String message = "這塊地正期待著新生命的到來！";
            Color bgColor = new Color(210, 180, 140);
            
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
                        message = "植物正在努力成長讓我們耐心等待～";
                        bgColor = new Color(255, 243, 205);
                    }
                    case MATURE -> {
                        status = displayName + " (已成熟)";
                        message = switch (cropType.toLowerCase()) {
                            case "cotton" -> "這裡有一團綿綿的白色小可愛，可以收成啦！";
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
            
            farmSlots[index].setText("<html><center><font size='4'>" + status + "</font><br>" + 
                                   "<font size='3'>" + message + "</font></center></html>");
            farmSlots[index].setBackground(bgColor);
        }
    }
    
    private String getChineseCropName(String cropType) {
        switch (cropType.toLowerCase()) {
            case "cotton":
                return "棉花";
            case "rose":
                return "玫瑰";
            case "sunflower":
                return "向日葵";
            case "tulip_pink":
                return "粉色鬱金香";
            case "lavender":
                return "薰衣草";
            default:
                return cropType; // Return the original if no match is found
        }
    }
    
    public void updateResources(int seeds, int water, int fertilizer) {
        resourcePanel.removeAll();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        JLabel seedTitle = new JLabel("種子數量：");
        seedTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        seedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(seedTitle);
        
        ResourceManager rm = ResourceManager.getInstance();
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
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }

    private void updateFarmStatus(String message) {
        farmStatusLabel.setText(message);
    }


    private void buyFertilizer() {
        // Logic to buy fertilizer
        JOptionPane.showMessageDialog(this, "已購買肥料！", "購買成功", JOptionPane.INFORMATION_MESSAGE);
    }
} 