package com.fashionodyssey.ui;

import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SalesPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel model;
    private JLabel salesLabel;
    private int totalSales = 0;
    
    // 定義物品價格表
    private static final Map<String, Integer> PRICES = new HashMap<>() {{
        // 布料類
        put("fabric", 50);          // 布料
        put("lace", 80);           // 蕾絲
        // 染料類
        put("red_dye", 30);        // 紅色染料
        put("yellow_dye", 30);     // 黃色染料
        put("purple_dye", 30);     // 紫色染料
        put("pink_dye", 30);       // 粉色染料
        // 染色布料類
        put("red_fabric", 100);    // 紅色布料
        put("yellow_fabric", 100); // 黃色布料
        put("purple_fabric", 100); // 紫色布料
        put("pink_fabric", 100);   // 粉色布料
        // 蝴蝶結類
        put("red_bow", 150);       // 紅色蝴蝶結
        put("yellow_bow", 150);    // 黃色蝴蝶結
        put("purple_bow", 150);    // 紫色蝴蝶結
        put("pink_bow", 150);      // 粉色蝴蝶結
        // 緞帶類
        put("red_ribbon", 120);    // 紅色緞帶
        put("yellow_ribbon", 120); // 黃色緞帶
        put("purple_ribbon", 120); // 紫色緞帶
        put("pink_ribbon", 120);   // 粉色緞帶
    }};
    
    public SalesPanel() {
        setLayout(new BorderLayout());
        
        initComponents();
        

    }
    
    private void initComponents() {
        // 商品表格
        String[] columnNames = {"商品", "數量", "單價", "總價", "操作"};
        model = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // 控制面板
        JPanel controlPanel = new JPanel();
        salesLabel = new JLabel("總銷售額: $" + totalSales);
        
        // 更新按鈕
        JButton updateButton = new JButton("更新庫存");
        updateButton.addActionListener(e -> updateInventory());
        
        controlPanel.add(updateButton);
        controlPanel.add(salesLabel);
        
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // 初始化顯示
        updateInventory();
    }
    
    private void updateInventory() {
        model.setRowCount(0); // 清空表格
        ResourceManager rm = ResourceManager.getInstance();
        
        // 遍歷所有可能的物品
        for (Map.Entry<String, Integer> entry : PRICES.entrySet()) {
            String itemKey = entry.getKey();
            int price = entry.getValue();
            int amount = rm.getResourceAmount(itemKey);
            
            if (amount > 0) {
                JButton sellButton = new JButton("賣出");
                sellButton.addActionListener(e -> sellItem(itemKey, price));
                
                model.addRow(new Object[]{
                    getChineseName(itemKey),
                    amount,
                    price,
                    amount * price,
                    sellButton
                });
            }
        }
    }
    
    private void sellItem(String itemKey, int price) {
        ResourceManager rm = ResourceManager.getInstance();
        int amount = rm.getResourceAmount(itemKey);
        
        if (amount > 0) {
            rm.consumeResource(itemKey, 1);
            int earnings = price;
            totalSales += earnings;
            salesLabel.setText("總銷售額: $" + totalSales);
            rm.addMoney(earnings);
            updateInventory();
            
            JOptionPane.showMessageDialog(this, 
                "成功賣出 1 個 " + getChineseName(itemKey) + "！\n獲得金錢: $" + earnings, 
                "銷售成功", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "沒有可以賣出的 " + getChineseName(itemKey) + "！", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private String getChineseName(String key) {
        return switch (key) {
            case "fabric" -> "布料";
            case "lace" -> "蕾絲";
            case "red_dye" -> "紅色染料";
            case "yellow_dye" -> "黃色染料";
            case "purple_dye" -> "紫色染料";
            case "pink_dye" -> "粉色染料";
            case "red_fabric" -> "紅色布料";
            case "yellow_fabric" -> "黃色布料";
            case "purple_fabric" -> "紫色布料";
            case "pink_fabric" -> "粉色布料";
            case "red_bow" -> "紅色蝴蝶結";
            case "yellow_bow" -> "黃色蝴蝶結";
            case "purple_bow" -> "紫色蝴蝶結";
            case "pink_bow" -> "粉色蝴蝶結";
            case "red_ribbon" -> "紅色緞帶";
            case "yellow_ribbon" -> "黃色緞帶";
            case "purple_ribbon" -> "紫色緞帶";
            case "pink_ribbon" -> "粉色緞帶";
            default -> key;
        };
    }
} 