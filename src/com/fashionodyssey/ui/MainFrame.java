package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.story.dialog.DialogManager;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    // 定義配色方案
    private static final Color PINK_THEME = new Color(255, 182, 193);    // 淺粉色
    private static final Color LIGHT_PINK = new Color(255, 218, 224);    // 更淺的粉色
    private static final Color SOFT_YELLOW = new Color(255, 245, 200);   // 柔和的黃色
    private static final Color MINT_GREEN = new Color(200, 255, 214);    // 薄荷綠
    private static final Color TEXT_COLOR = new Color(80, 80, 80);       // 深灰色文字
    
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
        setTitle("✨ 時尚創夢家 Fashion Odyssey ✨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // 設置整體背景色
        getContentPane().setBackground(LIGHT_PINK);
        
        // 初始化標籤，使用更大的字體
        statusLabel = new JLabel("● 靈感來襲！讓我們一起創造最閃耀的時尚品～");
        statusLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_THEME, 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 初始化資金標籤
        moneyLabel = new JLabel(String.format("資金: $%.2f", ResourceManager.getInstance().getMoney()));
        moneyLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        moneyLabel.setForeground(TEXT_COLOR);
        
        // 創建頂部面板，包含狀態標籤和資金標籤
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(15, 0));
        topPanel.setBackground(LIGHT_PINK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(moneyLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // 初始化面板
        farmPanel = new FarmPanel();
        processingPanel = new ProcessingPanel();
        designPanel = new DesignPanel();
        salesPanel = new SalesPanel();
        inventoryPanel = new InventoryPanel();
        inventoryPanel.setPreferredSize(new Dimension(300, 600)); // Set a shorter height for the inventory panel
        
        // Create a task button
        // JButton taskButton = new JButton("查看任務");
        // taskButton.setFont(new Font("微軟正黑體", Font.BOLD, 16)); // Set a bold font
        // taskButton.setBackground(SOFT_YELLOW); // Set background color
        // taskButton.setForeground(TEXT_COLOR); // Set text color
        // taskButton.setFocusPainted(false); // Remove focus border
        // taskButton.setBorder(BorderFactory.createCompoundBorder(
        //     new RoundedBorder(15, PINK_THEME), // Rounded border
        //     BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding
        // ));
        
        // // Add mouse hover effects
        // taskButton.addMouseListener(new java.awt.event.MouseAdapter() {
            
        //     public void mouseEntered(java.awt.event.MouseEvent evt) {
        //         taskButton.setBackground(MINT_GREEN); // Change background on hover
        //     }
        //     public void mouseExited(java.awt.event.MouseEvent evt) {
        //         taskButton.setBackground(SOFT_YELLOW); // Revert background color
        //     }
        // });
        
        // // Add action listener for the button
        // taskButton.addActionListener(e -> {
        //     // Create and display TaskPanel
        //     JFrame taskFrame = new JFrame("任務面板");
        //     taskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //     taskFrame.add(new TaskPanel()); // Add the TaskPanel instance
        //     taskFrame.setSize(400, 300); // Set window size
        //     taskFrame.setLocationRelativeTo(null); // Center the window
        //     taskFrame.setVisible(true); // Show the window
        // });
        
        // Create a panel to hold the InventoryPanel and the task button
        JPanel inventoryContainer = new JPanel();
        inventoryContainer.setLayout(new BorderLayout());
        inventoryContainer.add(inventoryPanel, BorderLayout.CENTER);
        // inventoryContainer.add(taskButton, BorderLayout.SOUTH); // Add task button below the inventory panel
        
        // 創建一個包含內容面板和按鈕面板的中央容器
        JPanel centerContainer = new JPanel(new BorderLayout());
        
        // 創建內容面板
        contentPanel = new JPanel(new CardLayout());
        
        contentPanel.add(farmPanel, "農場");
        contentPanel.add(processingPanel, "加工");
        contentPanel.add(designPanel, "設計");
        contentPanel.add(salesPanel, "銷售");
        
        // 創建底部按鈕面板，使用 GridLayout 確保按鈕等寬
        buttonPanel = new JPanel(new GridLayout(1, 4, 1, 0));  // 1行4列，水平距1像素
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel.setBackground(LIGHT_PINK);
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
        add(inventoryContainer, BorderLayout.EAST);
        
        // 自定義圓形按鈕類
        JButton roundButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.PINK); // 設置按鈕顏色
                g.fillOval(0, 0, getWidth(), getHeight()); // 繪製圓形
                super.paintComponent(g);
            }
        };
        roundButton.setPreferredSize(new Dimension(60, 60)); // 設置按鈕大小
        roundButton.setContentAreaFilled(false); // 去除背景
        roundButton.setBorderPainted(false); // 去除邊框
        roundButton.setFocusPainted(false); // 去除焦點邊框
        roundButton.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        
        // 添加按鈕的行為
        roundButton.addActionListener(e -> {
            // 在這裡添加查看訂單的邏輯
            JOptionPane.showMessageDialog(this, "這裡是訂單面板的邏輯");
        });

        
        // 顯示初始面板
        showPanel("農場");
        
        // 註冊資金更新事件
        EventManager.getInstance().addEventListener("UPDATE_MONEY", event -> {
            double amount = (Double) event.getData();
            updateMoney(amount);
        });
        
        // 初始化並顯示歡迎對話框
        DialogManager.getInstance().initialize(this);
        DialogManager.getInstance().showWelcomeDialog();
    }
    
    private JButton createNavButton(String name, Font font) {
        // 使用更基本的符號
        String symbol = switch (name) {
            case "農場" -> "◆";
            case "加工" -> "◇"; 
            case "設計" -> "△";  
            case "銷售" -> "★";  
            default -> "★";
        };
        
        JButton button = new JButton(symbol + " " + name);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBackground(SOFT_YELLOW);
        button.setForeground(TEXT_COLOR);
        
        // 圓角邊框
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 滑鼠效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(MINT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SOFT_YELLOW);
            }
        });
        
        button.addActionListener(e -> showPanel(name));
        return button;
    }
    
    private void showPanel(String name) {
        // 根據不同位置顯示不同的歡迎語
        String welcomeMessage = switch (name) {
            case "農場" -> "★ 歡迎來到夢想農場！今天也要好好照顧作物喔～ ★";
            case "加工" -> "◇ 織布機已經準備就緒，讓我們開始創造奇蹟吧！";
            case "設計" -> "● 靈感來襲！讓我們一起創造最閃耀的時尚品～";
            case "銷售" -> "☆ 展示櫥窗整理好了！準備好要驚艷全世界了嗎？";
            default -> "★ 歡迎來到時尚創夢家！";
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