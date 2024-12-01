package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private FarmPanel farmPanel;
    private ProcessingPanel processingPanel;
    private DesignPanel designPanel;
    private SalesPanel salesPanel;
    private JLabel moneyLabel;
    private JLabel statusLabel;
    private InventoryPanel inventoryPanel;
    
    public MainFrame() {
        setTitle("時尚創夢家 Fashion Odyssey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // 初始化標籤，使用更大的字體
        moneyLabel = new JLabel("資金: $1000");
        moneyLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));  // 改為24號字體
        statusLabel = new JLabel("歡迎來到時尚創夢家！");
        statusLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        
        // 創建頂部面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // 添加邊距
        topPanel.add(moneyLabel, BorderLayout.EAST);
        topPanel.add(statusLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // 初始化面板
        farmPanel = new FarmPanel();
        processingPanel = new ProcessingPanel();
        designPanel = new DesignPanel();
        salesPanel = new SalesPanel();
        inventoryPanel = new InventoryPanel();
        
        // 設置標籤頁
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("農場", farmPanel);
        tabbedPane.addTab("加工", processingPanel);
        tabbedPane.addTab("設計", designPanel);
        tabbedPane.addTab("銷售", salesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        
        // 確保初始焦點在農場面板
        SwingUtilities.invokeLater(() -> {
            farmPanel.requestFocusInWindow();
        });
    }
    
    public void updateMoney(int amount) {
        moneyLabel.setText("資金: $" + amount);
    }
    
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    public FarmPanel getFarmPanel() {
        return farmPanel;
    }
    
    public void updateResources(int money, int seeds, int water, int fertilizer) {
        updateMoney(money);
        farmPanel.updateResources(seeds, water, fertilizer);
    }
    
    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }
    
    public ProcessingPanel getProcessingPanel() {
        return processingPanel;
    }
}