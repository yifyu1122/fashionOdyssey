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
        setSize(1024, 768);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // 創建頂部狀態欄
        JPanel statusPanel = new JPanel(new BorderLayout());
        
        // 歡迎訊息（左側）
        statusLabel = new JLabel("歡迎來到時尚創夢家！");
        statusLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        statusLabel.setForeground(Color.BLUE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        
        // 資金顯示（右側）
        moneyLabel = new JLabel("資金: $1000");
        moneyLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        moneyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        moneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(moneyLabel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.NORTH);
        
        // 創建主要標籤頁面
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        
        farmPanel = new FarmPanel();
        processingPanel = new ProcessingPanel();
        designPanel = new DesignPanel();
        salesPanel = new SalesPanel();
        inventoryPanel = new InventoryPanel();
        
        tabbedPane.addTab("農場", farmPanel);
        tabbedPane.addTab("加工", processingPanel);
        tabbedPane.addTab("設計", designPanel);
        tabbedPane.addTab("銷售", salesPanel);
        tabbedPane.addTab("倉庫", inventoryPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // 切換標籤頁時更新狀態訊息
        tabbedPane.addChangeListener(e -> {
            switch(tabbedPane.getSelectedIndex()) {
                case 0 -> statusLabel.setText("創造你的時尚花園！這片土地正期待著新生命的到來！");
                case 1 -> statusLabel.setText("歡迎來到創意工坊！讓我們把收穫變成美麗的作品吧！");
                case 2 -> statusLabel.setText("歡迎來到設計室！創造屬於你的時尚傑作！");
                case 3 -> statusLabel.setText("歡迎來到商店！讓我們的時尚夢想發光發熱！");
                case 4 -> statusLabel.setText("歡迎來到倉庫！這裡收藏著你的所有寶物！");
            }
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
}