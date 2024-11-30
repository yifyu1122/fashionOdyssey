package com.fashionodyssey.ui;

import com.fashionodyssey.util.ItemDescription;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class InventoryPanel extends JPanel {
    private JTextArea inventoryText;
    private JScrollPane scrollPane;
    private ResourceManager resourceManager;
    
    public InventoryPanel() {
        setLayout(new BorderLayout());
        resourceManager = ResourceManager.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        // 創建標題
        JLabel titleLabel = new JLabel("倉庫物品", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // 創建物品列表區域
        inventoryText = new JTextArea();
        inventoryText.setEditable(false);
        inventoryText.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        
        scrollPane = new JScrollPane(inventoryText);
        add(scrollPane, BorderLayout.CENTER);
        
        // 初始更新顯示
        updateDisplay();
    }
    
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("目前庫存：\n\n");
        
        // 顯示肥料
        sb.append(ItemDescription.FERTILIZER.getFullDescription())
          .append(": ").append(resourceManager.getResourceAmount("fertilizer"))
          .append("\n\n");
        
        // 顯示種子
        sb.append("種子：\n");
        appendResource(sb, ItemDescription.COTTON_SEED, "cotton_seeds");
        appendResource(sb, ItemDescription.ROSE_SEED, "rose_seeds");
        appendResource(sb, ItemDescription.SUNFLOWER_SEED, "sunflower_seeds");
        appendResource(sb, ItemDescription.TULIP_SEED, "tulip_seeds");
        appendResource(sb, ItemDescription.LAVENDER_SEED, "lavender_seeds");
        sb.append("\n");
        
        // 顯示收穫物品
        sb.append("收穫物品：\n");
        appendResource(sb, ItemDescription.COTTON, "harvested_cotton");
        appendResource(sb, ItemDescription.ROSE, "harvested_rose");
        appendResource(sb, ItemDescription.SUNFLOWER, "harvested_sunflower");
        appendResource(sb, ItemDescription.TULIP, "harvested_tulip");
        appendResource(sb, ItemDescription.LAVENDER, "harvested_lavender");
        sb.append("\n");
        
        // 顯示加工物品
        sb.append("加工物品：\n");
        appendResource(sb, ItemDescription.FABRIC, "fabric");
        appendResource(sb, ItemDescription.RED_DYE, "red_dye");
        appendResource(sb, ItemDescription.YELLOW_DYE, "yellow_dye");
        appendResource(sb, ItemDescription.PURPLE_DYE, "purple_dye");
        
        inventoryText.setText(sb.toString());
    }
    
    private void appendResource(StringBuilder sb, ItemDescription item, String resourceKey) {
        int amount = resourceManager.getResourceAmount(resourceKey);
        if (amount > 0) {
            sb.append(item.getFullDescription())
              .append(": ").append(amount)
              .append("\n");
        }
    }
    
    public void updateInventory(String[] items, int[] amounts) {
        updateDisplay();  // 使用新的顯示方法
    }
} 