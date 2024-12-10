package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;  // 用於顯示內容的面板
    private JPanel buttonPanel;   // 用於放置按鈕的面板
    private FarmPanel farmPanel;           // 農場面板
    private ProcessingPanel processingPanel;// 加工面板
    private DesignPanel designPanel;       // 設計面板
    private SalesPanel salesPanel;         // 銷售面板
    private JLabel statusLabel;            // 狀態顯示標籤
    private InventoryPanel inventoryPanel; // 庫存面板
    private JLabel moneyLabel;             // 資金顯示標籤
    
    public MainFrame() {
        setTitle("時尚創夢家 Fashion Odyssey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // 初始化標籤，使用更大的字體
        statusLabel = new JLabel("歡迎來到夢想農場！今天也要好好照顧作物喔～");
        statusLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        
        // 初始化資金標籤
        moneyLabel = new JLabel(String.format("資金: $%.2f", ResourceManager.getInstance().getMoney()));
        moneyLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        
        // 創建頂部面板，包含狀態標籤和資金標籤
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(moneyLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // 初始化面板
        farmPanel = new FarmPanel();
        processingPanel = new ProcessingPanel();
        designPanel = new DesignPanel();
        salesPanel = new SalesPanel();
        inventoryPanel = new InventoryPanel();
        
        // 創建一個包含內容面板和按鈕面板的中央容器
        JPanel centerContainer = new JPanel(new BorderLayout());
        
        // 創建內容面板
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(farmPanel, "農場");
        contentPanel.add(processingPanel, "加工");
        contentPanel.add(designPanel, "設計");
        contentPanel.add(salesPanel, "銷售");
        
        // 創建底部按鈕面板，使用 GridLayout 確保按鈕等寬
        buttonPanel = new JPanel(new GridLayout(1, 4, 1, 0));  // 1行4列，水平���距1像素
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(11, 0, 10, 0));  // 頂部加一個像素的邊距
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 20);
        
        String[] buttonNames = {"農場", "加工", "設計", "銷售"};
        for (String name : buttonNames) {
            buttonPanel.add(createNavButton(name, buttonFont));
        }
        
        // 將內容面板和按鈕面板添加到中央容器
        centerContainer.add(contentPanel, BorderLayout.CENTER);
        centerContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        // 添加到主框架
        add(centerContainer, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        
        // 顯示初始面板
        showPanel("農場");
        
        // 註冊資金更新事件
        EventManager.getInstance().addEventListener("UPDATE_MONEY", event -> {
            double amount = (Double) event.getData();
            updateMoney(amount);
        });
    }
    
    private JButton createNavButton(String name, Font font) {
        JButton button = new JButton(name);
        button.setFont(font);
        button.setFocusPainted(false);  // 移除焦點邊框
        button.setBorderPainted(true);   // 顯示邊框
        button.setBackground(new Color(245, 245, 245));  // 設置淺灰色背景
        button.setForeground(new Color(50, 50, 50));    // 設置深灰色文字
        
        // 設置邊框為淺灰色
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)),  // 右邊框
            BorderFactory.createEmptyBorder(10, 0, 10, 0)  // 內邊距
        ));
        
        // 添加滑鼠事件
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(235, 235, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
            }
        });
        
        button.addActionListener(e -> showPanel(name));
        return button;
    }
    
    private void showPanel(String name) {
        // 根據不同位置顯示不同的歡迎語
        String welcomeMessage = switch (name) {
            case "農場" -> "歡迎來到夢想農場！今天也要好好照顧作物喔～";
            case "加工" -> "織布機已經準備就緒，讓我們開始創造奇蹟吧！";
            case "設計" -> "靈感來襲！讓我們一起創造最閃耀的時尚���品～";
            case "銷售" -> "展示櫥窗整理好了！準備好要驚艷全世界了嗎？";
            default -> "歡迎來到時尚創夢家！";
        };
        
        statusLabel.setText(welcomeMessage);
        
        // 切換面板
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    public void updateMoney(double amount) {
        moneyLabel.setText(String.format("資金: $%.2f", amount));
    }
    
    public FarmPanel getFarmPanel() {
        return farmPanel;
    }
    
    public void updateResources(int seeds, int water, int fertilizer) {
        farmPanel.updateResources(seeds, water, fertilizer);
    }
    
    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }
    
    public ProcessingPanel getProcessingPanel() {
        return processingPanel;
    }
}