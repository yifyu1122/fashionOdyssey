package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ItemDescription;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class InventoryPanel extends JPanel {
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private ResourceManager resourceManager;
    
    public InventoryPanel() {
        // 1. 基本設置
        setLayout(new BorderLayout());
        resourceManager = ResourceManager.getInstance();
        
        // 2. 創建網格面板
        gridPanel = new JPanel(new GridLayout(0, 4, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 添加測試邊框
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "庫存",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("微軟正黑體", Font.BOLD, 16)
            )
        ));
        
        // 3. 創建物品按鈕
        initializeItems();
        
        // 4. 設置滾動面板
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // 5. 添加到主面板
        add(scrollPane, BorderLayout.CENTER);
        
        // 6. 添加大小變化監聽器用於調試
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("InventoryPanel size: " + getSize());
                System.out.println("GridPanel size: " + gridPanel.getSize());
            }
        });
        
        // 7. 註冊事件監聽
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            System.out.println("Received UPDATE_RESOURCES event");
            updateDisplay();
        });
    }
    
    private void initializeItems() {
        System.out.println("Initializing items...");
        
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
        
        System.out.println("Items initialized. Total buttons: " + gridPanel.getComponentCount());
    }
    
    private void createItemButton(JPanel parent, ItemDescription item, String resourceKey) {
        JButton button = new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 60);
            }
            
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        
        // 設置按鈕外觀
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // 創建數量標籤
        JLabel countLabel = new JLabel("0");
        countLabel.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBackground(new Color(0, 0, 0, 180));
        countLabel.setOpaque(true);
        countLabel.setBounds(0, 0, 20, 20);
        
        button.setLayout(null);
        button.add(countLabel);
        
        // 設置工具提示和名稱
        button.setToolTipText(item.getFullDescription());
        button.setText(item.getName());
        
        // 更新按鈕狀態
        updateItemButton(button, countLabel, item, resourceKey);
        
        parent.add(button);
        System.out.println("Created button for: " + item.getName());
    }
    
    private void updateItemButton(JButton button, JLabel countLabel, ItemDescription item, String resourceKey) {
        int amount = resourceManager.getResourceAmount(resourceKey);
        System.out.println("Updating " + resourceKey + ": " + amount);
        
        countLabel.setText(String.valueOf(amount));
        countLabel.setVisible(amount > 0);
        button.setEnabled(amount > 0);
        
        if (item != null) {
            button.setText(item.getName());
        }
    }
    
    public void updateDisplay() {
        System.out.println("Updating display...");
        Component view = scrollPane.getViewport().getView();
        if (view instanceof JPanel) {
            JPanel panel = (JPanel) view;
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    JLabel countLabel = (JLabel) button.getComponent(0);
                    String tooltip = button.getToolTipText();
                    String resourceKey = getResourceKeyFromDescription(tooltip);
                    if (resourceKey != null) {
                        updateItemButton(button, countLabel, null, resourceKey);
                    }
                }
            }
        }
        revalidate();
        repaint();
    }
    
    private String getResourceKeyFromDescription(String description) {
        return switch (description) {
            // Fabric types
            case "布料" -> "fabric";
            case "蕾絲" -> "lace";
            // Dye types
            case "紅色染料" -> "red_dye";
            case "黃色染料" -> "yellow_dye"; 
            case "紫色染料" -> "purple_dye";
            case "粉色染料" -> "pink_dye";
            // Dyed fabric types
            case "白色布料" -> "white_fabric";
            case "紅色布料" -> "red_fabric";
            case "黃色布料" -> "yellow_fabric";
            case "紫色布料" -> "purple_fabric";
            case "粉色布料" -> "pink_fabric";
            // Bow types
            case "白色蝴蝶結" -> "white_bow";
            case "紅色蝴蝶結" -> "red_bow";
            case "黃色蝴蝶結" -> "yellow_bow";
            case "紫色蝴蝶結" -> "purple_bow";
            case "粉色蝴蝶結" -> "pink_bow";
            // Ribbon types
            case "白色緞帶" -> "white_ribbon";
            case "紅色緞帶" -> "red_ribbon";
            case "黃色緞帶" -> "yellow_ribbon";
            case "紫色緞帶" -> "purple_ribbon";
            case "粉色緞帶" -> "pink_ribbon";
            // Seeds and fertilizer
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
        for (int i = 0; i < items.length; i++) {
            String resourceKey = getResourceKeyFromDescription(items[i]);
            if (resourceKey != null) {
                resourceManager.setResourceAmount(resourceKey, amounts[i]);
            }
        }
        updateDisplay();
    }
} 