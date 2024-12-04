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
        
        // 2. 創建網格面板，修改為固定4列，並計算合適的行數
        int columns = 4;
        int totalItems = 31;  // 目前的物品總數
        int rows = (int) Math.ceil((double) totalItems / columns);  // 計算需要的行數
        gridPanel = new JPanel(new GridLayout(rows, columns, 5, 5));  // 設置固定行列數
        
        // 設置合理的首選大小
        gridPanel.setPreferredSize(new Dimension(275, 60 * rows + (rows - 1) * 5));  // 每個按鈕60像素高，加上間距
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
    
    public void updateDisplay() {
        System.out.println("Updating display...");
        Component view = scrollPane.getViewport().getView();
        if (view instanceof JPanel) {
            JPanel panel = (JPanel) view;
            
            // 1. 收集所有按鈕並排序
            java.util.List<JButton> buttons = new java.util.ArrayList<>();
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton) {
                    buttons.add((JButton) comp);
                }
            }
            
            // 2. 根據數量排序（有數量的排前面）
            buttons.sort((b1, b2) -> {
                JLabel count1 = (JLabel) b1.getComponent(0);
                JLabel count2 = (JLabel) b2.getComponent(0);
                int amount1 = Integer.parseInt(count1.getText());
                int amount2 = Integer.parseInt(count2.getText());
                
                // 數量大的排前面
                if (amount1 > 0 && amount2 == 0) return -1;
                if (amount1 == 0 && amount2 > 0) return 1;
                return 0;  // 保持原有順序
            });
            
            // 3. 清空面板
            panel.removeAll();
            
            // 4. 重新添加排序後的按鈕
            for (JButton button : buttons) {
                JLabel countLabel = (JLabel) button.getComponent(0);
                String tooltip = button.getToolTipText();
                String resourceKey = getResourceKeyFromDescription(tooltip);
                if (resourceKey != null) {
                    updateItemButton(button, countLabel, null, resourceKey);
                }
                panel.add(button);
            }
            
            // 5. 重新布局
            panel.revalidate();
            panel.repaint();
        }
        revalidate();
        repaint();
    }
    
    private String getResourceKeyFromDescription(String description) {
        if (description == null) return null;
        
        // 先檢查是否包含完整描述，如果是，只取名稱部分
        int bracketIndex = description.indexOf(" (");
        String itemName = bracketIndex > 0 ? description.substring(0, bracketIndex) : description;
        
        return switch (itemName) {
            // Fabric types
            case "布料" -> "fabric";
            case "白色蕾絲" -> "white_lace";
            case "紅色蕾絲" -> "red_lace";
            case "黃色蕾絲" -> "yellow_lace";
            case "紫色蕾絲" -> "purple_lace";
            case "粉色蕾絲" -> "pink_lace";
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
            // Dress types
            case "白色連衣裙" -> "white_dress";
            case "紅色連衣裙" -> "red_dress";
            case "黃色連衣裙" -> "yellow_dress";
            case "紫色連衣裙" -> "purple_dress";
            case "粉色連衣裙" -> "pink_dress";
            // Shirt types
            case "白色襯衫" -> "white_shirt";
            case "紅色襯衫" -> "red_shirt";
            case "黃色襯衫" -> "yellow_shirt";
            case "粉色襯衫" -> "pink_shirt";
            case "紫色襯衫" -> "purple_shirt";
            // Pants types
            case "白色褲子" -> "white_pants";
            case "紅色褲子" -> "red_pants";
            case "黃色褲子" -> "yellow_pants";
            case "粉色褲子" -> "pink_pants";
            case "紫色褲子" -> "purple_pants";

            // Seeds and fertilizer
            case "肥料" -> "fertilizer";
            case "棉花種子" -> "cotton_seeds";
            case "玫瑰種子" -> "rose_seeds";
            case "向日葵種子" -> "sunflower_seeds";
            case "薰衣草種子" -> "lavender_seeds";
            case "鬱金香(粉)種子" -> "tulip_pink_seeds";
            // 收穫物品
            case "棉花" -> "harvested_cotton";
            case "玫瑰" -> "harvested_rose";
            case "向日葵" -> "harvested_sunflower";
            case "薰衣草" -> "harvested_lavender";
            case "鬱金香(粉)" -> "harvested_tulip_pink";
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