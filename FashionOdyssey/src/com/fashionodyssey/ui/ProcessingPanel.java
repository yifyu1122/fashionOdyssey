package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;

import com.fashionodyssey.util.ResourceManager;

public class ProcessingPanel extends JPanel {
    private JList<String> materialList;
    private JButton processButton;
    private JLabel processMessage;  // 新增提示訊息標籤
    private Timer messageTimer;
    private JPanel resourcePanel;  // 添加資源面板
    
    public ProcessingPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // 設置字體
        Font titleFont = new Font("微軟正黑體", Font.BOLD, 20);
        Font listFont = new Font("微軟正黑體", Font.PLAIN, 16);
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 16);
        Font messageFont = new Font("微軟正黑體", Font.BOLD, 16);

        // 材料列表
        String[] materials = {
            "棉花 → 布料 (需要2個棉花)",
            "玫瑰 → 紅色染料 (需要1朵玫瑰)",
            "向日葵 → 黃色染料 (需要1朵向日葵)",
            "薰衣草 → 紫色染料 (需要1束薰衣草)",
            "布料 + 紅色染料 → 紅色布料",
            "布料 + 黃色染料 → 黃色布料",
            "布料 + 紫色染料 → 紫色布料"
        };
        materialList = new JList<>(materials);
        materialList.setFont(listFont);
        materialList.setFixedCellHeight(30);  // 設置列表項目高度
        JScrollPane scrollPane = new JScrollPane(materialList);
        
        // 控制面板
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        processButton = new JButton("開始加工");
        processButton.setFont(buttonFont);
        
        
        processMessage = new JLabel("選擇材料開始製作吧！");
        processMessage.setFont(messageFont);
        processMessage.setForeground(new Color(65, 105, 225));
        
        processButton.addActionListener(e -> {
            int selected = materialList.getSelectedIndex();
            if (selected >= 0) {
                String product = switch(selected) {
                    case 0 -> "fabric";
                    case 1 -> "red_dye";
                    case 2 -> "yellow_dye";
                    case 3 -> "purple_dye";
                    case 4 -> "red_fabric";
                    case 5 -> "yellow_fabric";
                    case 6 -> "purple_fabric";
                    default -> "";
                };
                
                if (hasMaterialsForProduct(product)) {
                    craftProduct(product);
                    showProcessMessage(getSuccessMessage(selected));
                } else {
                    showProcessMessage(getFailureMessage(selected));
                }
            }
        });
        
        controlPanel.add(processButton, BorderLayout.NORTH);
        controlPanel.add(processMessage, BorderLayout.CENTER);
        
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private boolean hasMaterialsForProduct(String product) {
        ResourceManager rm = ResourceManager.getInstance();
        return switch(product) {
            case "fabric" -> rm.getResourceAmount("harvested_cotton") >= 2;
            case "red_dye" -> rm.getResourceAmount("harvested_rose") >= 1;
            case "yellow_dye" -> rm.getResourceAmount("harvested_sunflower") >= 1;
            case "purple_dye" -> rm.getResourceAmount("harvested_lavender") >= 1;
            case "red_fabric" -> rm.getResourceAmount("fabric") >= 1 && 
                               rm.getResourceAmount("red_dye") >= 1;
            case "yellow_fabric" -> rm.getResourceAmount("fabric") >= 1 && 
                                  rm.getResourceAmount("yellow_dye") >= 1;
            case "purple_fabric" -> rm.getResourceAmount("fabric") >= 1 && 
                                  rm.getResourceAmount("purple_dye") >= 1;
            default -> false;
        };
    }
    
    private void craftProduct(String product) {
        ResourceManager rm = ResourceManager.getInstance();
        switch(product) {
            case "fabric":
                rm.consumeResource("harvested_cotton", 2);
                rm.addResource("fabric", 1);
                break;
            case "red_dye":
                rm.consumeResource("harvested_rose", 1);
                rm.addResource("red_dye", 1);
                break;
            case "yellow_dye":
                rm.consumeResource("harvested_sunflower", 1);
                rm.addResource("yellow_dye", 1);
                break;
            case "purple_dye":
                rm.consumeResource("harvested_lavender", 1);
                rm.addResource("purple_dye", 1);
                break;
            case "red_fabric":
                rm.consumeResource("fabric", 1);
                rm.consumeResource("red_dye", 1);
                rm.addResource("red_fabric", 1);
                break;
            case "yellow_fabric":
                rm.consumeResource("fabric", 1);
                rm.consumeResource("yellow_dye", 1);
                rm.addResource("yellow_fabric", 1);
                break;
            case "purple_fabric":
                rm.consumeResource("fabric", 1);
                rm.consumeResource("purple_dye", 1);
                rm.addResource("purple_fabric", 1);
                break;
        }
        rm.notifyResourceChange();
    }
    
    private String getSuccessMessage(int productType) {
        return switch(productType) {
            case 0 -> "把兩朵棉花揉在一起，變成了一塊柔軟的布料！✨";
            case 1 -> "玫瑰花瓣染出了愛情的顏色，紅得像初戀一樣～💝";
            case 2 -> "向日葵的陽光被封印在染料裡啦！☀️";
            case 3 -> "薰衣草的香氣和顏色完美融合，優雅極了！💜";
            case 4 -> "布料染上了玫瑰的紅色，浪漫又迷人！❤️";
            case 5 -> "布料染上了向日葵的金黃，充滿活力！💛";
            case 6 -> "布料染上了薰衣草的紫色，高貴優雅！💜";
            default -> "加工成功！";
        };
    }
    
    private String getFailureMessage(int productType) {
        return switch(productType) {
            case 0 -> "咦？棉花不夠呢，再去種點吧！";
            case 1 -> "需要更多玫瑰才能製作出愛情的顏色～";
            case 2 -> "向日葵不夠啦，陽光色彩還不夠濃郁！";
            case 3 -> "薰衣草太少了，香氣還不夠迷人～";
            case 4 -> "需要布料和紅色染料才能製作紅色布料哦！";
            case 5 -> "需要布料和黃色染料才能製作黃色布料哦！";
            case 6 -> "需要布料和紫色染料才能製作紫色布料哦！";
            default -> "材料不足，無法加工";
        };
    }
    
    private void showProcessMessage(String message) {
        processMessage.setText(message);
        processMessage.setHorizontalAlignment(SwingConstants.CENTER);
        processMessage.setForeground(new Color(65, 105, 225));  // 設置文字顏色
        
        // 停止之前的計時器（如果有的話）
        if (messageTimer != null && messageTimer.isRunning()) {
            messageTimer.stop();
        }
        
        // 創建新的計時器
        messageTimer = new Timer(5000, e -> {
            processMessage.setText("選擇材料開始製作吧！");
            processMessage.setForeground(new Color(65, 105, 225));
            ((Timer)e.getSource()).stop();  // 停止計時器
        });
        messageTimer.setRepeats(false);  // 確保只執行一次
        messageTimer.start();
    }
    
    public void updateResources(ResourceManager rm) {
        // 清除舊的資源顯示
        resourcePanel.removeAll();
        
        // 設置標題
        JLabel titleLabel = new JLabel("加工資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        resourcePanel.add(titleLabel);
        resourcePanel.add(Box.createVerticalStrut(20));
        
        // 顯示原料數量
        String[] materials = {
            "棉花", "玫瑰", "向日葵", "薰衣草",
            "布料", "紅色染料", "黃色染料", "紫色染料"
        };
        String[] keys = {
            "harvested_cotton", "harvested_rose", "harvested_sunflower", "harvested_lavender",
            "fabric", "red_dye", "yellow_dye", "purple_dye"
        };
        
        for (int i = 0; i < materials.length; i++) {
            JLabel label = new JLabel(String.format("%s: %d", 
                materials[i],
                rm.getResourceAmount(keys[i])
            ));
            label.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            resourcePanel.add(label);
            resourcePanel.add(Box.createVerticalStrut(5));
        }
        
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }
} 