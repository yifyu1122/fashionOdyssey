package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ItemDescription;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class InventoryPanel extends JPanel {
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private ResourceManager resourceManager;
    
    public InventoryPanel() {
        setLayout(new BorderLayout());
        resourceManager = ResourceManager.getInstance();
        
        // 2. 創建網格面板
        gridPanel = new JPanel(new GridLayout(0, 4, 5, 5));  // 設置4列，行數自動調整
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 只保留標題邊框
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "庫存",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("微軟正黑體", Font.BOLD, 16)
        ));
        
        // 4. 設置滾動面板
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // 7. 註冊事件監聽
        EventManager.getInstance().addEventListener("RESOURCE_CHANGED", event -> {
            System.out.println("Resource amount changed, updating display...");
            updateResources();
        });
        
        // 初始化顯示
        updateResources();
    }
    
    public void updateResources() {
        gridPanel.removeAll();
        ResourceManager rm = ResourceManager.getInstance();
        
        // 創建物品按鈕列表
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

        // 其他物品
        createItemButton(gridPanel, ItemDescription.WHITE_FABRIC, "white_fabric");
        createItemButton(gridPanel, ItemDescription.RED_FABRIC, "red_fabric");
        createItemButton(gridPanel, ItemDescription.YELLOW_FABRIC, "yellow_fabric");
        createItemButton(gridPanel, ItemDescription.PURPLE_FABRIC, "purple_fabric");
        createItemButton(gridPanel, ItemDescription.PINK_FABRIC, "pink_fabric");

        createItemButton(gridPanel, ItemDescription.WHITE_LACE, "white_lace");
        createItemButton(gridPanel, ItemDescription.RED_LACE, "red_lace");
        createItemButton(gridPanel, ItemDescription.YELLOW_LACE, "yellow_lace");
        createItemButton(gridPanel, ItemDescription.PURPLE_LACE, "purple_lace");
        createItemButton(gridPanel, ItemDescription.PINK_LACE, "pink_lace");

        createItemButton(gridPanel, ItemDescription.WHITE_BOW, "white_bow");
        createItemButton(gridPanel, ItemDescription.RED_BOW, "red_bow");
        createItemButton(gridPanel, ItemDescription.YELLOW_BOW, "yellow_bow");
        createItemButton(gridPanel, ItemDescription.PURPLE_BOW, "purple_bow");
        createItemButton(gridPanel, ItemDescription.PINK_BOW, "pink_bow");

        createItemButton(gridPanel, ItemDescription.WHITE_RIBBON, "white_ribbon");
        createItemButton(gridPanel, ItemDescription.RED_RIBBON, "red_ribbon");
        createItemButton(gridPanel, ItemDescription.YELLOW_RIBBON, "yellow_ribbon");
        createItemButton(gridPanel, ItemDescription.PURPLE_RIBBON, "purple_ribbon");
        createItemButton(gridPanel, ItemDescription.PINK_RIBBON, "pink_ribbon");

        createItemButton(gridPanel, ItemDescription.WHITE_DRESS, "white_dress");
        createItemButton(gridPanel, ItemDescription.RED_DRESS, "red_dress");
        createItemButton(gridPanel, ItemDescription.YELLOW_DRESS, "yellow_dress");
        createItemButton(gridPanel, ItemDescription.PURPLE_DRESS, "purple_dress");
        createItemButton(gridPanel, ItemDescription.PINK_DRESS, "pink_dress");

        createItemButton(gridPanel, ItemDescription.WHITE_SHIRT, "white_shirt");
        createItemButton(gridPanel, ItemDescription.RED_SHIRT, "red_shirt");
        createItemButton(gridPanel, ItemDescription.YELLOW_SHIRT, "yellow_shirt");
        createItemButton(gridPanel, ItemDescription.PURPLE_SHIRT, "purple_shirt");
        createItemButton(gridPanel, ItemDescription.PINK_SHIRT, "pink_shirt");

        createItemButton(gridPanel, ItemDescription.WHITE_PANTS, "white_pants");
        createItemButton(gridPanel, ItemDescription.RED_PANTS, "red_pants");
        createItemButton(gridPanel, ItemDescription.YELLOW_PANTS, "yellow_pants");
        createItemButton(gridPanel, ItemDescription.PURPLE_PANTS, "purple_pants");
        createItemButton(gridPanel, ItemDescription.PINK_PANTS, "pink_pants");  
        
        System.out.println("Items initialized. Total buttons: " + gridPanel.getComponentCount());
        gridPanel.revalidate();
        gridPanel.repaint();
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
        
        // 添加點擊事件
        button.addActionListener(e -> {
            // 創建一個對話框來顯示物品描述
            JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "物品資訊", true);
            dialog.setLayout(new BorderLayout(10, 10));
            
            // 創建標題面板
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel titleLabel = new JLabel(item.getName());
            titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
            titlePanel.add(titleLabel);
            
            // 創建描述面板
            JPanel descPanel = new JPanel(new BorderLayout(10, 10));
            descPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            // 獲取物品數量
            int amount = resourceManager.getResourceAmount(resourceKey);
            
            // 創建描述文本
            String description = "<html>" + 
                item.getFullDescription().replace("(", "<br>(") + 
                "<br><br>目前擁有：" + amount + " 個" +
                "</html>";
            
            JLabel descLabel = new JLabel(description);
            descLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            descPanel.add(descLabel, BorderLayout.CENTER);
            
            // 添加關閉按鈕
            JButton closeButton = new JButton("關閉");
            closeButton.addActionListener(event -> dialog.dispose());
            
            // 將所有元件添加到對話框
            dialog.add(titlePanel, BorderLayout.NORTH);
            dialog.add(descPanel, BorderLayout.CENTER);
            dialog.add(closeButton, BorderLayout.SOUTH);
            
            // 設置對話框大小和位置
            dialog.setSize(300, 200);
            dialog.setLocationRelativeTo(button);
            dialog.setVisible(true);
        });
        
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
} 