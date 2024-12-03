package com.fashionodyssey.ui.dialog;

import com.fashionodyssey.model.resource.CropType;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class ShopDialog extends JDialog {
    private ResourceManager resourceManager;
    private JPanel itemsPanel;
    private JLabel moneyLabel;
    private JLabel messageLabel;  // 新增提示訊息標籤
    
    public ShopDialog(Window parent) {
        super(parent, "歡迎光臨小店鋪～", ModalityType.APPLICATION_MODAL);
        this.resourceManager = ResourceManager.getInstance();
        initComponents();
        
        // 設置對話框大小
        setSize(600, 500);  // 加大視窗尺寸
        setMinimumSize(new Dimension(500, 400));  // 設置最小尺寸
        
        // 調整位置
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // 創建商品面板
        itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 添加肥料商品
        JPanel fertilizerPanel = createItemPanel(
            "神奇肥料",
            "讓作物長得比隔壁老王家的還快！",
            20,
            resourceManager.getResourceAmount("fertilizer"),
            e -> {
                if (resourceManager.getMoney() >= 20) {
                    resourceManager.buyFertilizer();
                    showMessage("成功購買5個肥料，快去餵食小植物們吧！", new Color(46, 139, 87));
                } else {
                    showMessage("錢錢不夠啦！去賣點作物再來吧～", new Color(178, 34, 34));
                }
                updateItemCounts();
            }
        );
        itemsPanel.add(fertilizerPanel);
        
        // 添加種子商品
        for (CropType crop : CropType.values()) {
            String description = switch(crop.getDisplayName()) {
                case "棉花" -> "軟綿綿的白色精靈，織出夢想的布料";
                case "玫瑰" -> "紅色的愛情使者，染出心動的顏色";
                case "向日葵" -> "永遠追逐陽光的開朗寶貝";
                case "薰衣草" -> "紫色的香氣精靈，讓整個農場都香香的";
                case "鬱金香(粉)" -> "優雅的粉色精靈，染出甜美的顏色";
                default -> "";
            };
            
            JPanel seedPanel = createItemPanel(
                crop.getDisplayName() + "種子",
                description,
                crop.getSeedCost(),
                resourceManager.getResourceAmount(crop.name().toLowerCase() + "_seeds"),
                e -> {
                    if (resourceManager.getMoney() >= crop.getSeedCost()) {
                        resourceManager.buySeed(crop.getDisplayName());
                        showMessage(crop.getDisplayName() + "種子買好了，祝你豐收！", new Color(46, 139, 87));
                    } else {
                        showMessage("錢錢不夠買" + crop.getDisplayName() + "種子啦！", new Color(178, 34, 34));
                    }
                    updateItemCounts();
                }
            );
            itemsPanel.add(seedPanel);
        }
        
        // 添加捲軸
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // 添加金錢顯示
        JPanel topPanel = new JPanel(new BorderLayout());
        moneyLabel = new JLabel("目前資金: $" + resourceManager.getMoney());
        moneyLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        moneyLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        messageLabel = new JLabel("歡迎光臨！今天想買點什麼呢？");
        messageLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topPanel.add(moneyLabel, BorderLayout.WEST);
        topPanel.add(messageLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // 添加關閉按鈕
        JButton closeButton = new JButton("離開商店");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }
    
    private JPanel createItemPanel(String name, String description, int price, int count, java.awt.event.ActionListener buyAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createEtchedBorder()
        ));
        
        // 加大字體
        Font nameFont = new Font("微軟正黑體", Font.BOLD, 18);
        Font descFont = new Font("微軟正黑體", Font.PLAIN, 14);
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 16);
        
        // 左側：名稱和描述
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel(name + " - $" + price + " (一次購買5個)");
        nameLabel.setFont(nameFont);
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(descFont);
        descLabel.setForeground(Color.GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);
        
        // 右側：數量和購買按鈕
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel countLabel = new JLabel("現有: " + count);
        JButton buyButton = new JButton("購買");
        
        actionPanel.add(countLabel);
        actionPanel.add(buyButton);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.EAST);
        
        buyButton.addActionListener(e -> {
            if (resourceManager.getMoney() >= price) {
                showBuyAnimation(buyButton);
                showCoinAnimation(buyButton, price);
                buyAction.actionPerformed(e);
            } else {
                showShakeAnimation(buyButton);
            }
        });
        
        return panel;
    }
    
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        // 改用較柔和的顏色
        messageLabel.setForeground(new Color(46, 139, 87));  // 海綠色
        // 3秒後恢復默認訊息
        new Timer(3000, e -> {
            messageLabel.setText("歡迎光臨！今天想買點什麼呢？");
            messageLabel.setForeground(new Color(105, 105, 105));  // 深灰色
        }).start();
    }
    
    private void updateItemCounts() {
        // 更新金錢顯示
        moneyLabel.setText("目前資金: $" + resourceManager.getMoney());
        
        // 重新創建所有商品面板
        itemsPanel.removeAll();
        
        // 重新添加肥料商品
        JPanel fertilizerPanel = createItemPanel(
            "神奇肥料",
            "讓作物長得比隔壁老王家的還快！",
            20,
            resourceManager.getResourceAmount("fertilizer"),
            e -> {
                if (resourceManager.getMoney() >= 20) {
                    resourceManager.buyFertilizer();
                    showMessage("成功購買5個肥料，快去餵食小植物們吧！", new Color(46, 139, 87));
                } else {
                    showMessage("錢錢不夠啦！去賣點作物再來吧～", new Color(178, 34, 34));
                }
                updateItemCounts();
            }
        );
        itemsPanel.add(fertilizerPanel);
        
        // 重新添加所有種子商品
        for (CropType crop : CropType.values()) {
            String description = switch(crop.getDisplayName()) {
                case "棉花" -> "軟綿綿的白色精靈，織出夢想的布料";
                case "玫瑰" -> "紅色的愛情使者，染出心動的顏色";
                case "向日葵" -> "永遠追逐陽光的開朗寶貝";
                case "鬱金香(粉)" -> "優雅的粉色精靈，染出甜美的顏色";
                case "薰衣草" -> "紫色的香氣精靈，讓整個農場都香香的";
                default -> "";
            };
            
            JPanel seedPanel = createItemPanel(
                crop.getDisplayName() + "種子",
                description,
                crop.getSeedCost(),
                resourceManager.getResourceAmount(crop.name().toLowerCase() + "_seeds"),
                e -> {
                    if (resourceManager.getMoney() >= crop.getSeedCost()) {
                        resourceManager.buySeed(crop.getDisplayName());
                        showMessage(crop.getDisplayName() + "種子買好了，祝你豐收！", new Color(46, 139, 87));
                    } else {
                        showMessage("錢錢不夠買" + crop.getDisplayName() + "種子啦！", new Color(178, 34, 34));
                    }
                    updateItemCounts();
                }
            );
            itemsPanel.add(seedPanel);
        }
        
        // 重新驗證和重繪界面
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
    
    private void showBuyAnimation(JButton buyButton) {
        // 保存原始顏色大小
        Color originalColor = buyButton.getBackground();
        Dimension originalSize = buyButton.getSize();
        Font originalFont = buyButton.getFont();
        
        // 創建動畫計時器
        Timer timer = new Timer(50, null);
        final int[] frame = {0};
        
        timer.addActionListener(e -> {
            frame[0]++;
            switch (frame[0]) {
                case 1 -> {
                    // 變大和變色
                    buyButton.setBackground(new Color(144, 238, 144));  // 淺綠色
                    buyButton.setFont(originalFont.deriveFont(originalFont.getSize() + 2.0f));
                }
                case 3 -> {
                    // 閃爍效果
                    buyButton.setBackground(new Color(50, 205, 50));  // 深綠色
                }
                case 5 -> {
                    // 恢復原狀
                    buyButton.setBackground(originalColor);
                    buyButton.setFont(originalFont);
                    timer.stop();
                }
            }
        });
        timer.start();
    }
    
    private void showShakeAnimation(JButton button) {
        Point original = button.getLocation();
        Timer timer = new Timer(50, null);
        final int[] frame = {0};
        
        timer.addActionListener(e -> {
            frame[0]++;
            switch (frame[0]) {
                case 1 -> button.setLocation(original.x + 5, original.y);
                case 2 -> button.setLocation(original.x - 5, original.y);
                case 3 -> button.setLocation(original.x + 5, original.y);
                case 4 -> button.setLocation(original.x - 5, original.y);
                case 5 -> {
                    button.setLocation(original);
                    timer.stop();
                }
            }
        });
        timer.start();
    }
    
    private void showCoinAnimation(JButton buyButton, int price) {
        // 創建金幣標籤
        JLabel coinLabel = new JLabel("$");
        coinLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        coinLabel.setForeground(new Color(255, 215, 0));  // 金色
        
        // 設置金幣初始位置（從金錢標籤開始）
        Point moneyLabelLocation = moneyLabel.getLocationOnScreen();
        Point dialogLocation = getLocationOnScreen();
        Point buttonLocation = buyButton.getLocationOnScreen();
        
        // 計算相對於對話框的座標
        int startX = moneyLabelLocation.x - dialogLocation.x;
        int startY = moneyLabelLocation.y - dialogLocation.y;
        int targetX = buttonLocation.x - dialogLocation.x;
        int targetY = buttonLocation.y - dialogLocation.y;
        
        coinLabel.setBounds(startX, startY, 30, 30);
        
        // 添加到對話框的最上層
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(coinLabel, JLayeredPane.DRAG_LAYER);
        
        // 動畫參數
        final int duration = 20;  // 動畫幀數
        
        Timer timer = new Timer(30, null);
        final int[] frame = {0};
        
        timer.addActionListener(e -> {
            frame[0]++;
            if (frame[0] <= duration) {
                // 計算當前位置（使用拋物線軌跡）
                double progress = frame[0] / (double) duration;
                double x = startX + (targetX - startX) * progress;
                double y = startY + (targetY - startY) * progress 
                        - Math.sin(progress * Math.PI) * 100;  // 拋物線高度
                
                // 更新金幣位置
                coinLabel.setLocation((int) x, (int) y);
                
                // 金幣旋轉效果
                coinLabel.setFont(coinLabel.getFont().deriveFont(
                    AffineTransform.getRotateInstance(progress * Math.PI * 2)
                ));
                
                // 逐漸變小和變透明
                float alpha = (float) (1.0 - progress);
                coinLabel.setForeground(new Color(255, 215, 0, (int) (alpha * 255)));
            } else {
                // 動畫結束，移除金幣
                layeredPane.remove(coinLabel);
                layeredPane.repaint();
                timer.stop();
            }
        });
        
        timer.start();
    }
} 