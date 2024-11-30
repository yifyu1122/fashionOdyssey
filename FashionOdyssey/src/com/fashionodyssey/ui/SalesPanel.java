package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;

public class SalesPanel extends JPanel {
    private JTable productTable;
    private JButton sellButton;
    private JLabel salesLabel;
    
    public SalesPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // 商品表格
        String[] columnNames = {"商品", "類型", "顏色", "價格"};
        Object[][] data = {
            {"連衣裙", "春季", "紅色", "1000"},
            {"上衣", "夏季", "藍色", "500"}
        };
        
        productTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // 控制面板
        JPanel controlPanel = new JPanel();
        sellButton = new JButton("銷售");
        salesLabel = new JLabel("總銷售額: $0");
        
        controlPanel.add(sellButton);
        controlPanel.add(salesLabel);
        
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
} 