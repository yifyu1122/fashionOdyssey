package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ItemDescription;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class InventoryPanel extends JPanel {
    private JTextArea inventoryText;
    private JScrollPane scrollPane;
    private ResourceManager resourceManager;
    private JPanel resourcePanel;
    
    public InventoryPanel() {
        setLayout(new BorderLayout());
        resourceManager = ResourceManager.getInstance();
        initComponents();
        
        // Initialize resourcePanel
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add resourcePanel to the layout
        add(resourcePanel, BorderLayout.CENTER);
        
        // Listen for resource updates
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> updateDisplay());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // 創建標題
        JLabel titleLabel = new JLabel("倉庫物品", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // 創建物品網格面板
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 5, 5)); // 4列，自動行數，間距5px
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 創建物品按鈕
        createItemButton(gridPanel, ItemDescription.FERTILIZER, "fertilizer");
        
        // 種子類
        createItemButton(gridPanel, ItemDescription.COTTON_SEED, "cotton_seeds");
        createItemButton(gridPanel, ItemDescription.ROSE_SEED, "rose_seeds");
        createItemButton(gridPanel, ItemDescription.SUNFLOWER_SEED, "sunflower_seeds");
        createItemButton(gridPanel, ItemDescription.LAVENDER_SEED, "lavender_seeds");
        createItemButton(gridPanel, ItemDescription.TULIP_PINK_SEED, "tulip_pink_seeds");
        
        // 收穫物品
        createItemButton(gridPanel, ItemDescription.COTTON, "harvested_cotton");
        createItemButton(gridPanel, ItemDescription.ROSE, "harvested_rose");
        createItemButton(gridPanel, ItemDescription.SUNFLOWER, "harvested_sunflower");
        createItemButton(gridPanel, ItemDescription.LAVENDER, "harvested_lavender");
        createItemButton(gridPanel, ItemDescription.TULIP_PINK, "harvested_tulip_pink");
        
        // 加工物品
        createItemButton(gridPanel, ItemDescription.WHITE_FABRIC, "white_fabric");
        createItemButton(gridPanel, ItemDescription.RED_DYE, "red_dye");
        createItemButton(gridPanel, ItemDescription.YELLOW_DYE, "yellow_dye");
        createItemButton(gridPanel, ItemDescription.PURPLE_DYE, "purple_dye");
        createItemButton(gridPanel, ItemDescription.PINK_DYE, "pink_dye");
        
        createItemButton(gridPanel, ItemDescription.RED_FABRIC, "red_fabric");
        createItemButton(gridPanel, ItemDescription.YELLOW_FABRIC, "yellow_fabric");
        createItemButton(gridPanel, ItemDescription.PURPLE_FABRIC, "purple_fabric");
        createItemButton(gridPanel, ItemDescription.PINK_FABRIC, "pink_fabric");
        
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // 初始更新顯示
        updateDisplay();
    }
    
    private void createItemButton(JPanel parent, ItemDescription item, String resourceKey) {
        JButton button = new JButton() {
            @Override
            public Dimension getPreferredSize() {
                int size = 60;
                return new Dimension(size, size);
            }
        };
        
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel countLabel = new JLabel("0");
        countLabel.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBackground(new Color(0, 0, 0, 180));
        countLabel.setOpaque(true);
        countLabel.setBounds(0, 0, 20, 20);
        
        button.setLayout(null);
        button.add(countLabel);
        
        button.setToolTipText(item.getFullDescription());
        
        button.setText(item.getName());
        
        updateItemButton(button, countLabel, item, resourceKey);
        
        parent.add(button);
    }
    
    private void updateItemButton(JButton button, JLabel countLabel, ItemDescription item, String resourceKey) {
        int amount = resourceManager.getResourceAmount(resourceKey);
        countLabel.setText(String.valueOf(amount));
        countLabel.setVisible(amount > 0);
        
        button.setEnabled(amount > 0);
        button.setText(item.getName());
    }
    
    public void updateDisplay() {
        // 更新所有物品按鈕
        Component view = scrollPane.getViewport().getView();
        if (view instanceof JPanel) {  // Ensure the view is a JPanel
            JPanel panel = (JPanel) view;
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    JLabel countLabel = (JLabel) button.getComponent(0);
                    // 從按鈕的工具提示中獲取物品描述
                    String tooltip = button.getToolTipText();
                    // 根據描述找到對應的resourceKey並更新
                    String resourceKey = getResourceKeyFromDescription(tooltip);
                    if (resourceKey != null) {
                        updateItemButton(button, countLabel, null, resourceKey);
                    }
                }
            }
        }
    }
    
    private String getResourceKeyFromDescription(String description) {
        return switch (description) {
            // 布料類
            case "布料" -> "fabric";
            case "蕾絲" -> "lace";
            // 染料類
            case "紅色染料" -> "red_dye";
            case "黃色染料" -> "yellow_dye"; 
            case "紫色染料" -> "purple_dye";
            case "粉色染料" -> "pink_dye";
            // 染色布料類
            case "白色布料" -> "white_fabric";
            case "紅色布料" -> "red_fabric";
            case "黃色布料" -> "yellow_fabric";
            case "紫色布料" -> "purple_fabric";
            case "粉色布料" -> "pink_fabric";
            // 蝴蝶結類
            case "白色蝴蝶結" -> "white_bow";
            case "紅色蝴蝶結" -> "red_bow";
            case "黃色蝴蝶結" -> "yellow_bow";
            case "紫色蝴蝶結" -> "purple_bow";
            case "粉色蝴蝶結" -> "pink_bow";
            // 緞帶類
            case "白色緞帶" -> "white_ribbon";
            case "紅色緞帶" -> "red_ribbon";
            case "黃色緞帶" -> "yellow_ribbon";
            case "紫色緞帶" -> "purple_ribbon";
            case "粉色緞帶" -> "pink_ribbon";
            // 種子與肥料
            case "肥料" -> "fertilizer";
            case "棉花種子" -> "cotton_seeds";
            case "玫瑰種子" -> "rose_seeds";
            case "向日葵種子" -> "sunflower_seeds";
            case "薰衣草種子" -> "lavender_seeds";
            case "鬱金香(粉)種子" -> "tulip_pink_seeds";
            default -> null;
        };
    }
    
    public void updateInventory(String[] items, int[] amounts) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("倉庫物品");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        for (int i = 0; i < items.length; i++) {
            JLabel itemLabel = new JLabel(String.format("%s: %d", items[i], amounts[i]));
            contentPanel.add(itemLabel);
        }
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }
} 