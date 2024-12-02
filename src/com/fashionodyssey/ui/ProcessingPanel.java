package com.fashionodyssey.ui;

import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class ProcessingPanel extends JPanel {
    private JPanel resourcePanel;     // 資源面板
    private JLabel processMessage;    // 提示訊息
    private String selectedRecipe;    // 當前選擇的配方
    private JButton[] recipeButtons;  // 添加這行來追踪所有配方按鈕
    
    public ProcessingPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // 創建左側資源面板
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resourcePanel.setPreferredSize(new Dimension(200, 0));
        resourcePanel.setBackground(new Color(240, 240, 240));
        resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 將資源面板添加到左側
        JScrollPane resourceScrollPane = new JScrollPane(resourcePanel);
        resourceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resourceScrollPane.setPreferredSize(new Dimension(220, 0));
        
        add(resourceScrollPane, BorderLayout.WEST);
        
        // 創建配方書面板
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        
        // 配方標題
        JLabel recipeTitle = new JLabel("配方書", SwingConstants.CENTER);
        recipeTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        recipeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipePanel.add(recipeTitle);
        recipePanel.add(Box.createVerticalStrut(20));
        
        // 添加配方按鈕
        String[][] recipes = {
            {"布料", "2個棉花 → 1個布料"},
            {"蕾絲", "1個棉花 → 1個蕾絲"},
            {"紅色染料", "1個玫瑰 → 1個紅色染料"},
            {"黃色染料", "1個向日葵 → 1個黃色染料"},
            {"粉色染料", "1個鬱金香(粉) → 1個粉色染料"},
            {"紫色染料", "1個薰衣草 → 1個紫色染料"},
            {"紅色布料", "1個布料 + 1個紅色染料 → 1個紅色布料"},
            {"黃色布料", "1個布料 + 1個黃色染料 → 1個黃色布料"},
            {"粉色布料", "1個布料 + 1個粉色染料 → 1個粉色布料"},
            {"紫色布料", "1個布料 + 1個紫色染料 → 1個紫色布料"},
            {"紅色蝴蝶結", "1個紅色布料 → 2個紅色蝴蝶結"},
            {"黃色蝴蝶結", "1個黃色布料 → 2個黃色蝴蝶結"},
            {"粉色蝴蝶結", "1個粉色布料 → 2個粉色蝴蝶結"},
            {"紫色蝴蝶結", "1個紫色布料 → 2個紫色蝴蝶結"},
            {"紅色緞帶", "1個紅色布料 → 1個紅色緞帶"},
            {"黃色緞帶", "1個黃色布料 → 1個黃色緞帶"},
            {"粉色緞帶", "1個粉色布料 → 1個粉色緞帶"},
            {"紫色緞帶", "1個紫色布料 → 1個紫色緞帶"}
        };
        
        recipeButtons = new JButton[recipes.length];
        
        for (int i = 0; i < recipes.length; i++) {
            String[] recipe = recipes[i];
            JButton recipeButton = new JButton(recipe[1]);
            recipeButtons[i] = recipeButton;
            
            recipeButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            recipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            recipeButton.setMaximumSize(new Dimension(400, 50));
            
            // 設置按鈕的默認外觀
            recipeButton.setBackground(null);
            recipeButton.setForeground(Color.BLACK);
            
            recipeButton.addActionListener(e -> {
                // 如果這個配方已經被選中，則取消選擇
                if (recipe[0].equals(selectedRecipe)) {
                    selectedRecipe = null;
                    recipeButton.setBackground(null);
                    recipeButton.setForeground(Color.BLACK);
                    processMessage.setText("選擇配方開始製作！");
                } else {
                    // 取消其他按鈕的選中狀態
                    for (JButton btn : recipeButtons) {
                        btn.setBackground(null);
                        btn.setForeground(Color.BLACK);
                    }
                    // 設置新選中的按鈕
                    selectedRecipe = recipe[0];
                    recipeButton.setBackground(new Color(70, 130, 180));  // 鋼藍色
                    recipeButton.setForeground(Color.WHITE);
                    checkRecipeAvailability(recipe[0]);
                }
            });
            
            recipePanel.add(recipeButton);
            recipePanel.add(Box.createVerticalStrut(10));
        }
        
        // 添加製作按鈕
        JButton craftButton = new JButton("開始製作");
        craftButton.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        craftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        craftButton.setMaximumSize(new Dimension(200, 50));
        
        craftButton.addActionListener(e -> {
            if (selectedRecipe != null) {
                attemptCrafting(selectedRecipe);
            } else {
                processMessage.setText("請先選擇要製作的配方！");
            }
        });
        
        recipePanel.add(Box.createVerticalStrut(20));
        recipePanel.add(craftButton);
        
        // 添加提示訊息
        processMessage = new JLabel("選擇配方開始製作！", SwingConstants.CENTER);
        processMessage.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        processMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        recipePanel.add(Box.createVerticalStrut(20));
        recipePanel.add(processMessage);
        
        // 添加配方書面板
        JScrollPane recipeScrollPane = new JScrollPane(recipePanel);
        recipeScrollPane.setBorder(null);
        add(recipeScrollPane, BorderLayout.CENTER);
        
        // 初始更新資源顯示
        updateResources();
    }
    
    private void checkRecipeAvailability(String recipe) {
        ResourceManager rm = ResourceManager.getInstance();
        boolean canCraft = switch (recipe) {
            case "布料" -> rm.getResourceAmount("harvested_cotton") >= 2;
            case "蕾絲" -> rm.getResourceAmount("harvested_cotton") >= 1;
            case "紅色染料" -> rm.getResourceAmount("harvested_rose") >= 1;
            case "黃色染料" -> rm.getResourceAmount("harvested_sunflower") >= 1;
            case "粉色染料" -> rm.getResourceAmount("harvested_tulip_pink") >= 1;
            case "紫色染料" -> rm.getResourceAmount("harvested_lavender") >= 1;
            case "紅色布料" -> rm.getResourceAmount("fabric") >= 1 && 
                             rm.getResourceAmount("red_dye") >= 1;
            case "黃色布料" -> rm.getResourceAmount("fabric") >= 1 && 
                             rm.getResourceAmount("yellow_dye") >= 1;
            case "粉色布料" -> rm.getResourceAmount("fabric") >= 1 && 
                             rm.getResourceAmount("pink_dye") >= 1;
            case "紫色布料" -> rm.getResourceAmount("fabric") >= 1 && 
                             rm.getResourceAmount("purple_dye") >= 1;
            case "紅色蝴蝶結" -> rm.getResourceAmount("red_fabric") >= 1;
            case "黃色蝴蝶結" -> rm.getResourceAmount("yellow_fabric") >= 1;
            case "粉色蝴蝶結" -> rm.getResourceAmount("pink_fabric") >= 1;
            case "紫色蝴蝶結" -> rm.getResourceAmount("purple_fabric") >= 1;
            case "紅色緞帶" -> rm.getResourceAmount("red_fabric") >= 1;
            case "黃色緞帶" -> rm.getResourceAmount("yellow_fabric") >= 1;
            case "粉色緞帶" -> rm.getResourceAmount("pink_fabric") >= 1;
            case "紫色緞帶" -> rm.getResourceAmount("purple_fabric") >= 1;
            default -> false;
        };
        
        if (canCraft) {
            processMessage.setText("材料充足，可以製作！");
            processMessage.setForeground(new Color(46, 139, 87));  // 綠色
        } else {
            processMessage.setText("材料不足，無法製作！");
            processMessage.setForeground(new Color(178, 34, 34));  // 紅色
        }
    }
    
    private void attemptCrafting(String recipe) {
        ResourceManager rm = ResourceManager.getInstance();
        
        switch (recipe) {
            case "布料":
                if (rm.getResourceAmount("harvested_cotton") >= 2) {
                    rm.consumeResource("harvested_cotton", 2);
                    rm.addResource("fabric", 1);
                    showSuccess("布料製作成功！");
                }
                break;
            case "紅色染料":
                if (rm.getResourceAmount("harvested_rose") >= 1) {
                    rm.consumeResource("harvested_rose", 1);
                    rm.addResource("red_dye", 1);
                    showSuccess("紅色染料製作成功！");
                }
                break;
            case "黃色染料":
                if (rm.getResourceAmount("harvested_sunflower") >= 1) {
                    rm.consumeResource("harvested_sunflower", 1);
                    rm.addResource("yellow_dye", 1);
                    showSuccess("黃色染料製作成功！");
                }
                break;
            case "粉色染料":
                if (rm.getResourceAmount("harvested_tulip_pink") >= 1) {
                    rm.consumeResource("harvested_tulip_pink", 1);
                    rm.addResource("pink_dye", 1);
                    showSuccess("粉色染料製作成功！");
                }
                break;
            case "紫色染料":
                if (rm.getResourceAmount("harvested_lavender") >= 1) {
                    rm.consumeResource("harvested_lavender", 1);
                    rm.addResource("purple_dye", 1);
                    showSuccess("紫色染料製作成功！");
                }
                break;
            case "紅色布料":
                if (rm.getResourceAmount("fabric") >= 1 && rm.getResourceAmount("red_dye") >= 1) {
                    rm.consumeResource("fabric", 1);
                    rm.consumeResource("red_dye", 1);
                    rm.addResource("red_fabric", 1);
                    showSuccess("紅色布料製作成功！");
                }
                break;
            case "黃色布料":
                if (rm.getResourceAmount("fabric") >= 1 && rm.getResourceAmount("yellow_dye") >= 1) {
                    rm.consumeResource("fabric", 1);
                    rm.consumeResource("yellow_dye", 1);
                    rm.addResource("yellow_fabric", 1);
                    showSuccess("黃色布料製作成功！");
                }
                break;
            case "粉色布料":
                if (rm.getResourceAmount("fabric") >= 1 && rm.getResourceAmount("pink_dye") >= 1) {
                    rm.consumeResource("fabric", 1);
                    rm.consumeResource("pink_dye", 1);
                    rm.addResource("pink_fabric", 1);
                    showSuccess("粉色布料製作成功！");
                }
                break;
            case "紫色布料":
                if (rm.getResourceAmount("fabric") >= 1 && rm.getResourceAmount("purple_dye") >= 1) {
                    rm.consumeResource("fabric", 1);
                    rm.consumeResource("purple_dye", 1);
                    rm.addResource("purple_fabric", 1);
                    showSuccess("紫色布料製作成功！");
                }
                break;
            case "蕾絲":
                if (rm.getResourceAmount("harvested_cotton") >= 1) {
                    rm.consumeResource("harvested_cotton", 1);
                    rm.addResource("lace", 1);
                    showSuccess("蕾絲製作成功！");
                }
                break;
            case "紅色蝴蝶結":
                if (rm.getResourceAmount("red_fabric") >= 1) {
                    rm.consumeResource("red_fabric", 1);
                    rm.addResource("red_bow", 2);
                    showSuccess("紅色蝴蝶結製作成功！");
                }
                break;
            case "黃色蝴蝶結":
                if (rm.getResourceAmount("yellow_fabric") >= 1) {
                    rm.consumeResource("yellow_fabric", 1);
                    rm.addResource("yellow_bow", 2);
                    showSuccess("黃色蝴蝶結製作成功！");
                }
                break;
            case "粉色蝴蝶結":
                if (rm.getResourceAmount("pink_fabric") >= 1) {
                    rm.consumeResource("pink_fabric", 1);
                    rm.addResource("pink_bow", 2);
                    showSuccess("粉色蝴蝶結製作成功！");
                }
                break;
            case "紫色蝴蝶結":
                if (rm.getResourceAmount("purple_fabric") >= 1) {
                    rm.consumeResource("purple_fabric", 1);
                    rm.addResource("purple_bow", 2);
                    showSuccess("紫色蝴蝶結製作成功！");
                }
                break;
            case "紅色緞帶":
                if (rm.getResourceAmount("red_fabric") >= 1) {
                    rm.consumeResource("red_fabric", 1);
                    rm.addResource("red_ribbon", 1);
                    showSuccess("紅色緞帶製作成功！");
                }
                break;
            case "黃色緞帶":
                if (rm.getResourceAmount("yellow_fabric") >= 1) {
                    rm.consumeResource("yellow_fabric", 1);
                    rm.addResource("yellow_ribbon", 1);
                    showSuccess("黃色緞帶製作成功！");
                }
                break;
            case "粉色緞帶":
                if (rm.getResourceAmount("pink_fabric") >= 1) {
                    rm.consumeResource("pink_fabric", 1);
                    rm.addResource("pink_ribbon", 1);
                    showSuccess("粉色緞帶製作成功！");
                }
                break;
            case "紫色緞帶":
                if (rm.getResourceAmount("purple_fabric") >= 1) {
                    rm.consumeResource("purple_fabric", 1);
                    rm.addResource("purple_ribbon", 1);
                    showSuccess("紫色緞帶製作成功！");
                }
                break;
        }
        
        updateResources();
    }
    
    private void showSuccess(String message) {
        processMessage.setText(message);
        processMessage.setForeground(new Color(46, 139, 87));
        
        // 不重置選擇狀態，只更新資源顯示
        updateResources();
        
        // 檢查是否還能繼續製作
        if (selectedRecipe != null) {
            checkRecipeAvailability(selectedRecipe);
        }
    }
    
    public void updateResources() {
        resourcePanel.removeAll();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 設置標題
        JLabel titleLabel = new JLabel("加工資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        ResourceManager rm = ResourceManager.getInstance();
        
        // 顯示花卉數量
        JLabel flowerTitle = new JLabel("收穫的花卉：");
        flowerTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        flowerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(flowerTitle);
        
        String[] flowers = {"棉花", "玫瑰", "向日葵", "薰衣草", "鬱金香(粉)"};
        String[] flowerKeys = {
            "harvested_cotton", "harvested_rose", 
            "harvested_sunflower", "harvested_lavender", "harvested_tulip_pink"
        };
        
        for (int i = 0; i < flowers.length; i++) {
            JLabel label = new JLabel(String.format("%s: %d", 
                flowers[i], rm.getResourceAmount(flowerKeys[i])));
            label.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        // 顯示染料數量
        contentPanel.add(Box.createVerticalStrut(20));
        JLabel dyeTitle = new JLabel("染料：");
        dyeTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        dyeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(dyeTitle);
        
        String[] dyes = {"紅色染料", "黃色染料", "紫色染料", "粉色染料"};
        String[] dyeKeys = {"red_dye", "yellow_dye", "purple_dye", "pink_dye"};
        
        for (int i = 0; i < dyes.length; i++) {
            JLabel label = new JLabel(String.format("%s: %d", 
                dyes[i], rm.getResourceAmount(dyeKeys[i])));
            label.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        // 顯示布料數量
        contentPanel.add(Box.createVerticalStrut(20));
        JLabel fabricTitle = new JLabel("布料：");
        fabricTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        fabricTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(fabricTitle);

        String[] fabrics = {"紅色布料", "黃色布料", "紫色布料", "粉色布料"};
        String[] fabricKeys = {"red_fabric", "yellow_fabric", "purple_fabric", "pink_fabric"};
        
        for (int i = 0; i < fabrics.length; i++) {
            JLabel label = new JLabel(String.format("%s: %d", 
                fabrics[i], rm.getResourceAmount(fabricKeys[i])));
            label.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
        }
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }
} 