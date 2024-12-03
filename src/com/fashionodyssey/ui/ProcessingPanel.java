package com.fashionodyssey.ui;

import com.fashionodyssey.controller.ProcessingController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class ProcessingPanel extends JPanel {
    private JPanel resourcePanel;     // 資源面板
    private JLabel processMessage;    // 提示訊息
    private String selectedRecipe;    // 當前選擇的配方
    private JButton[] recipeButtons;  // 配方按鈕
    private int currentPage = 0;
    private final String[] pageNames = {"基本", "染料", "布料", "蝴蝶結", "緞帶", "連衣裙", "襯衫", "褲子"};
    private ProcessingController controller;
    
    public ProcessingPanel() {
        controller = new ProcessingController();
        setLayout(new BorderLayout());
        initComponents();
        
        // Initialize and add the resource panel
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(resourcePanel, BorderLayout.EAST);  // Add to the appropriate position
        
        // Listen for resource updates
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> updateResources());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // 創建主面板，使用 BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 創建配方書面板
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        
        // 配方標題
        JLabel recipeTitle = new JLabel("配方書", SwingConstants.CENTER);
        recipeTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        recipeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipePanel.add(recipeTitle);
        recipePanel.add(Box.createVerticalStrut(20));
        
        // 創建一個包含配方按鈕的面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        // 添加配方按鈕
        String[][] recipes = {
            {"白色布料", "2個棉花 → 1個白色布料"},
            {"蕾絲", "1個棉花 → 1個蕾絲"},
            {"紅色染料", "1個玫瑰 → 1個紅色染料"},
            {"黃色染料", "1個向日葵 → 1個黃色染料"},
            {"粉色染料", "1個鬱金香(粉) → 1個粉色染料"},
            {"紫色染料", "1個薰衣草 → 1個紫色染料"},
            {"紅色布料", "1個白色布料 + 1個紅色染料 → 1個紅色布料"},
            {"黃色布料", "1個白色布料 + 1個黃色染料 → 1個黃色布料"},
            {"粉色布料", "1個白色布料 + 1個粉色染料 → 1個粉色布料"},
            {"紫色布料", "1個白色布料 + 1個紫色染料 → 1個紫色布料"},
            {"白色蝴蝶結", "1個白色布料 → 1個白色蝴蝶結"},
            {"紅色蝴蝶結", "1個紅色布料 → 2個紅色蝴蝶結"},
            {"黃色蝴蝶結", "1個黃色布料 → 2個黃色蝴蝶結"},
            {"粉色蝴蝶結", "1個粉色布料 → 2個粉色蝴蝶結"},
            {"紫色蝴蝶結", "1個紫色布料 → 2個紫色蝴蝶結"},
            {"白色緞帶", "1個白色布料 → 1個白色緞帶"},
            {"紅色緞帶", "1個紅色布料 → 1個紅色緞帶"},
            {"黃色緞帶", "1個黃色布料 → 1個黃色緞帶"},
            {"粉色緞帶", "1個粉色布料 → 1個粉色緞帶"},
            {"紫色緞帶", "1個紫色布料 → 1個紫色緞帶"},
            {"白色連衣裙", "2個白色布料 → 1個白色連衣裙"},
            {"紅色連衣裙", "2個紅色布料 → 1個紅色連衣裙"},
            {"黃色連衣裙", "2個黃色布料 → 1個黃色連衣裙"},
            {"粉色連衣裙", "2個粉色布料 → 1個粉色連衣裙"},
            {"紫色連衣裙", "2個紫色布料 → 1個紫色連衣裙"},
            {"白色襯衫", "1個白色布料 → 1個白色襯衫"},
            {"紅色襯衫", "1個紅色布料 → 1個紅色襯衫"},
            {"黃色襯衫", "1個黃色布料 → 1個黃色襯衫"},
            {"粉色襯衫", "1個粉色布料 → 1個粉色襯衫"},
            {"紫色襯衫", "1個紫色布料 → 1個紫色襯衫"},
            {"白色褲子", "1個白色布料 → 1個白色褲子"},
            {"紅色褲子", "1個紅色布料 → 1個紅色褲子"},
            {"黃色褲子", "1個黃色布料 → 1個黃色褲子"},
            {"粉色褲子", "1個粉色布料 → 1個粉色褲子"},
            {"紫色褲子", "1個紫色布料 → 1個紫色褲子"}
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
            
            buttonPanel.add(recipeButton);
            buttonPanel.add(Box.createVerticalStrut(10));
        }
        
        // 將按鈕面板添加到配方面板
        recipePanel.add(buttonPanel);
        
        // 創建底部控制面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        
        bottomPanel.add(craftButton);
        
        // 添加提示訊息
        processMessage = new JLabel("選擇配方開始製作！", SwingConstants.CENTER);
        processMessage.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        processMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(processMessage);
        
        // 配方面板和底部控制面板添加到主面板
        mainPanel.add(recipePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // 主面板添加到當前面板
        add(mainPanel, BorderLayout.CENTER);
        
        // 將資源面板添加到左側
        JScrollPane resourceScrollPane = new JScrollPane(resourcePanel);
        resourceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resourceScrollPane.setPreferredSize(new Dimension(220, 0));
        
        add(resourceScrollPane, BorderLayout.WEST);
        
        // 初始更新資源顯示
        updateResources();
    }
    
    private void checkRecipeAvailability(String recipe) {
        boolean canCraft = controller.canCraft(getProductId(recipe));
        
        if (canCraft) {
            processMessage.setText("材料充足，可以製作！");
            processMessage.setForeground(new Color(46, 139, 87));
        } else {
            processMessage.setText("材料不足，無法製作！");
            processMessage.setForeground(new Color(178, 34, 34));
        }
    }
    
    private void attemptCrafting(String recipe) {
        String productId = getProductId(recipe);
        if (controller.canCraft(productId)) {
            controller.craftProduct(productId);
            showSuccess(recipe + "製作成功！");
        }
    }
    
    private String getProductId(String recipeName) {
        return switch(recipeName) {
            case "白色布料" -> "white_fabric";
            case "蕾絲" -> "lace";
            case "紅色染料" -> "red_dye";
            case "黃色染料" -> "yellow_dye";
            case "粉色染料" -> "pink_dye";
            case "紫色染料" -> "purple_dye";
            case "紅色布料" -> "red_fabric";
            case "黃色布料" -> "yellow_fabric";
            case "粉色布料" -> "pink_fabric";
            case "紫色布料" -> "purple_fabric";
            case "白色蝴蝶結" -> "white_bow";
            case "紅色蝴蝶結" -> "red_bow";
            case "黃色蝴蝶結" -> "yellow_bow";
            case "粉色蝴蝶結" -> "pink_bow";
            case "紫色蝴蝶結" -> "purple_bow";
            case "白色緞帶" -> "white_ribbon";
            case "紅色緞帶" -> "red_ribbon";
            case "黃色緞帶" -> "yellow_ribbon";
            case "粉色緞帶" -> "pink_ribbon";
            case "紫色緞帶" -> "purple_ribbon";
            case "白色連衣裙" -> "white_dress";
            case "紅色連衣裙" -> "red_dress";
            case "黃色連衣裙" -> "yellow_dress";
            case "粉色連衣裙" -> "pink_dress";
            case "紫色連衣裙" -> "purple_dress";
            case "白色襯衫" -> "white_shirt";
            case "紅色襯衫" -> "red_shirt";
            case "黃色襯衫" -> "yellow_shirt";
            case "粉色襯衫" -> "pink_shirt";
            case "紫色襯衫" -> "purple_shirt";
            case "白色褲子" -> "white_pants";
            case "紅色褲子" -> "red_pants";
            case "黃色褲子" -> "yellow_pants";
            case "粉色褲子" -> "pink_pants";
            case "紫色褲子" -> "purple_pants";
            default -> "";
        };
    }
    
    private void showSuccess(String message) {
        processMessage.setText(message);
        processMessage.setForeground(new Color(46, 139, 87));
        
        // 不重置選擇狀態，只更新源顯示
        updateResources();
        
        // 檢查是否還能繼續製作
        if (selectedRecipe != null) {
            checkRecipeAvailability(selectedRecipe);
        }
    }
    
    public void updateResources() {
        if (resourcePanel == null) return;
        
        resourcePanel.removeAll();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 添加標題和下拉選單
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("加工資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // 創建類別按鈕
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 添加標題
        titleLabel = new JLabel("加工資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryPanel.add(titleLabel);
        categoryPanel.add(Box.createVerticalStrut(20));
        
        // 創建類別按鈕
        for (int i = 0; i < pageNames.length; i++) {
            JButton categoryBtn = new JButton(pageNames[i]);
            categoryBtn.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            categoryBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryBtn.setMaximumSize(new Dimension(200, 40));
            
            final int index = i;
            categoryBtn.addActionListener(e -> {
                currentPage = index;
                updateResources();
                
                // 更新按鈕外觀
                for (Component c : categoryPanel.getComponents()) {
                    if (c instanceof JButton) {
                        JButton btn = (JButton) c;
                        if (btn == categoryBtn) {
                            btn.setBackground(new Color(70, 130, 180));
                            btn.setForeground(Color.WHITE);
                        } else {
                            btn.setBackground(null);
                            btn.setForeground(Color.BLACK);
                        }
                    }
                }
            });
            
            // 設置初始選中狀態
            if (i == currentPage) {
                categoryBtn.setBackground(new Color(70, 130, 180));
                categoryBtn.setForeground(Color.WHITE);
            }
            
            categoryPanel.add(categoryBtn);
            categoryPanel.add(Box.createVerticalStrut(10));
        }
        
        contentPanel.add(categoryPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        ResourceManager rm = ResourceManager.getInstance();
        
        // 根據當前頁面顯示對應資源和配方
        switch (currentPage) {
            case 0 -> { // 基本
                String[] flowers = {"棉花", "白色布料", "蕾絲"};
                String[] keys = {"harvested_cotton", "white_fabric", "lace"};
                addResourceList(contentPanel, flowers, keys, rm);
                updateRecipeButtons(new int[]{0, 1, 2});
            }
            case 1 -> { // 染料
                String[] items = {"紅色染料", "黃色染料", "紫色染料", "粉色染料"};
                String[] keys = {"red_dye", "yellow_dye", "purple_dye", "pink_dye"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{2, 3, 4, 5});
            }
            case 2 -> { // 布料
                String[] items = {"白色布料", "紅色布料", "黃色布料", "紫色布料", "粉色布料"};
                String[] keys = {"white_fabric", "red_fabric", "yellow_fabric", "purple_fabric", "pink_fabric"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{0, 6, 7, 8, 9});
            }
            case 3 -> { // 蝴蝶結
                String[] items = {"白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "紫色蝴蝶結", "粉色蝴蝶結"};
                String[] keys = {"white_bow", "red_bow", "yellow_bow", "purple_bow", "pink_bow"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{10, 11, 12, 13, 14});
            }
            case 4 -> { // 緞帶
                String[] items = {"白色緞帶", "紅色緞帶", "黃色緞帶", "紫色緞帶", "粉色緞帶"};
                String[] keys = {"white_ribbon", "red_ribbon", "yellow_ribbon", "purple_ribbon", "pink_ribbon"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{15, 16, 17, 18, 19});
            }
            case 5 -> { // 連衣裙     
                String[] items = {"白色連衣裙", "紅色連衣裙", "黃色連衣裙", "紫色連衣裙", "粉色連衣裙"};
                String[] keys = {"white_dress", "red_dress", "yellow_dress", "purple_dress", "pink_dress"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{20, 21, 22, 23, 24});
            }
            case 6 -> { // 襯衫
                String[] items = {"白色襯衫", "紅色襯衫", "黃色襯衫", "粉色襯衫", "紫色襯衫"};
                String[] keys = {"white_shirt", "red_shirt", "yellow_shirt", "pink_shirt", "purple_shirt"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{25, 26, 27, 28, 29});
            }
            case 7 -> { // 褲子
                String[] items = {"白色褲子", "紅色褲子", "黃色褲子", "粉色褲子", "紫色褲子"};
                String[] keys = {"white_pants", "red_pants", "yellow_pants", "pink_pants", "purple_pants"};
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{30, 31, 32, 33, 34});
            }
        }
        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }
    
    private void addResourceList(JPanel panel, String[] names, String[] keys, ResourceManager rm) {
        for (int i = 0; i < names.length; i++) {
            JLabel label = new JLabel(String.format("%s: %d", 
                names[i], rm.getResourceAmount(keys[i])));
            label.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalStrut(10));
        }
    }
    
    private void updateRecipeButtons(int[] indices) {
        for (int i = 0; i < recipeButtons.length; i++) {
            recipeButtons[i].setVisible(false);
        }
        for (int index : indices) {
            recipeButtons[index].setVisible(true);
        }
    }
} 