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
    private final String[] pageNames = {"基本", "染料", "布料", "蝴蝶結", "緞帶", "連衣裙", "襯衫", "褲子", "蕾絲"};
    private ProcessingController controller;
    private JLabel moneyLabel;
    private JPanel buttonPanel; // 用於顯示配方按鈕的面板

    public ProcessingPanel() {
        controller = new ProcessingController();
        setLayout(new BorderLayout());
        
        // 初始化資源面板
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        initComponents();
        
        // 監聽資源更新
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> updateResources());
        
        // 添加資金更新監聽
        EventManager.getInstance().addEventListener("UPDATE_MONEY", event -> {
            int amount = (Integer) event.getData();
            moneyLabel.setText("資金: $" + amount);
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 0));  // 添加水平間距20像素
        
        // 初始化 recipeButtons 數組為最大可能的大小
        recipeButtons = new JButton[40];  // 設置一個足夠大的初始大小
        
        // 創建主面板，添加邊距
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // 添加內邊距
        
        // 創建配方書面板
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        recipePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));  // 左側添加10像素間距
        
        // 配方標題
        JLabel recipeTitle = new JLabel("配方書", SwingConstants.CENTER);
        recipeTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        recipeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipePanel.add(recipeTitle);
        recipePanel.add(Box.createVerticalStrut(20));
        
        // 創建按鈕面板
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
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
                String productId = getProductId(selectedRecipe);
                if (controller.canCraft(productId)) {
                    controller.craftProduct(productId);
                    showSuccess(selectedRecipe + "製作成功！");
                } else {
                    processMessage.setText("材料不足，無法製作！");
                }
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
        resourceScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));  // 添加內邊距
        
        add(resourceScrollPane, BorderLayout.WEST);
        
        // 初始更新資源顯示
        updateResources();
        
        // 顯示初始頁面
        showPage(currentPage);
    }

    private void showPage(int pageIndex) {
        currentPage = pageIndex;
        
        // 清除所有現有按鈕
        buttonPanel.removeAll();
        
        // 添加類別按鈕
        JPanel categoryButtons = new JPanel();
        categoryButtons.setLayout(new GridLayout(3, 4, 10, 10));  // 改為 3x4 網格
        categoryButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 設置面板大小
        categoryButtons.setMinimumSize(new Dimension(500, 150));
        categoryButtons.setPreferredSize(new Dimension(500, 150));
        categoryButtons.setMaximumSize(new Dimension(Short.MAX_VALUE, 150));
        
        // 添加所有類別按鈕
        for (int i = 0; i < pageNames.length; i++) {
            JButton btn = new JButton(pageNames[i]);
            btn.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            final int index = i;
            
            // 設置當前頁面按鈕的顏色
            if (i == currentPage) {
                btn.setBackground(new Color(70, 130, 180));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(null);
                btn.setForeground(Color.BLACK);
            }
            
            btn.addActionListener(e -> showPage(index));
            categoryButtons.add(btn);
        }
        
        // 創建一個容器來包裝類別按鈕面板
        JPanel categoryContainer = new JPanel();
        categoryContainer.setLayout(new BoxLayout(categoryContainer, BoxLayout.X_AXIS));
        categoryContainer.add(categoryButtons);
        categoryContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        buttonPanel.add(categoryContainer);
        buttonPanel.add(Box.createVerticalStrut(20));
        
        // 添加配方按鈕
        JPanel recipesPanel = new JPanel();
        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        recipesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        switch (pageIndex) {
            case 0 -> addBasicRecipes();
            case 1 -> addDyeRecipes();
            case 2 -> addFabricRecipes();
            case 3 -> addBowRecipes();
            case 4 -> addRibbonRecipes();
            case 5 -> addDressRecipes();
            case 6 -> addShirtRecipes();
            case 7 -> addPantsRecipes();
            case 8 -> addLaceRecipes();
        }
        
        buttonPanel.add(recipesPanel);
        
        // 更新資源顯示
        updateResources();
        
        // 重新驗證面板
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addBasicRecipes() {
        String[][] recipes = {
            {"白色布料", "2個棉花 → 1個白色布料"},
            {"白色蕾絲", "1個棉花 → 1個白色蕾絲"}
        };
        addRecipesToPanel(recipes);
    }

    private void addDyeRecipes() {
        String[][] recipes = {
            {"紅色染料", "1個玫瑰 → 1個紅色染料"},
            {"黃色染料", "1個向日葵 → 1個黃色染料"},
            {"粉色染料", "1個鬱金香(粉) → 1個粉色染料"},
            {"紫色染料", "1個薰衣草 → 1個紫色染料"}
        };
        addRecipesToPanel(recipes);
    }

    private void addFabricRecipes() {
        String[][] recipes = {
            {"紅色布料", "1個白色布料 + 1個紅色染料 → 1個紅色布料"},
            {"黃色布料", "1個白色布料 + 1個黃色染料 → 1個黃色布料"},
            {"粉色布料", "1個白色布料 + 1個粉色染料 → 1個粉色布料"},
            {"紫色布料", "1個白色布料 + 1個紫色染料 → 1個紫色布料"}
        };
        addRecipesToPanel(recipes);
    }

    private void addBowRecipes() {
        String[][] recipes = {
            {"白色蝴蝶結", "1個白色布料 → 1個白色蝴蝶結"},
            {"紅色蝴蝶結", "1個紅色布料 → 2個紅色蝴蝶結"},
            {"黃色蝴蝶結", "1個黃色布料 → 2個黃色蝴蝶結"},
            {"粉色蝴蝶結", "1個粉色布料 → 2個粉色蝴蝶結"},
            {"紫色蝴蝶結", "1個紫色布料 → 2個紫色蝴蝶結"}
        };
        addRecipesToPanel(recipes);
    }

    private void addRibbonRecipes() {
        String[][] recipes = {
            {"白色緞帶", "1個白色布料 → 1個白色緞帶"},
            {"紅色緞帶", "1個紅色布料 → 1個紅色緞帶"},
            {"黃色緞帶", "1個黃色布料 → 1個黃色緞帶"},
            {"粉色緞帶", "1個粉色布料 → 1個粉色緞帶"},
            {"紫色緞帶", "1個紫色布料 → 1個紫色緞帶"}
        };
        addRecipesToPanel(recipes);
    }

    private void addDressRecipes() {
        String[][] recipes = {
            {"白色連衣裙", "2個白色布料 → 1個白色連衣裙"},
            {"紅色連衣裙", "2個紅色布料 → 1個紅色連衣裙"},
            {"黃色連衣裙", "2個黃色��料 → 1個黃色連衣裙"},
            {"粉色連衣裙", "2個粉色布料 → 1個粉色連衣裙"},
            {"紫色連衣裙", "2個紫色布料 → 1個紫色連衣裙"}
        };
        addRecipesToPanel(recipes);
    }

    private void addShirtRecipes() {
        String[][] recipes = {
            {"白色襯衫", "1個白色布料 → 1個白色襯衫"},
            {"紅色襯衫", "1個紅色布料 → 1個紅色襯衫"},
            {"黃色襯衫", "1個黃色布料 → 1個黃色襯衫"},
            {"粉色襯衫", "1個粉色布料 → 1個粉色襯衫"},
            {"紫色襯衫", "1個紫色布料 → 1個紫色襯衫"}
        };
        addRecipesToPanel(recipes);
    }

    private void addPantsRecipes() {
        String[][] recipes = {
            {"白色褲子", "1個白色布料 → 1個白色褲子"},
            {"紅色褲子", "1個紅色布料 → 1個紅色褲子"},
            {"黃色褲子", "1個黃色布料 → 1個黃色褲子"},
            {"粉色褲子", "1個粉色布料 → 1個粉色褲子"},
            {"紫色褲子", "1個紫色布料 → 1個紫色褲子"}
        };
        addRecipesToPanel(recipes);
    }

    private void addLaceRecipes() {
        String[][] recipes = {
            {"紅色蕾絲", "1個白色蕾絲 + 1個紅色染料 → 1個紅色蕾絲"},
            {"黃色蕾絲", "1個白色蕾絲 + 1個黃色染料 → 1個黃色蕾絲"},
            {"紫色蕾絲", "1個白色蕾絲 + 1個紫色染料 → 1個紫色蕾絲"},
            {"粉色蕾絲", "1個白色蕾絲 + 1個粉色染料 → 1個粉色蕾絲"}
        };
        addRecipesToPanel(recipes);
    }

    private void addRecipesToPanel(String[][] recipes) {
        for (String[] recipe : recipes) {
            JButton recipeButton = new JButton(recipe[1]);
            recipeButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
            recipeButton.setPreferredSize(new Dimension(380, 50));
            recipeButton.setMaximumSize(new Dimension(380, 50));
            recipeButton.setMinimumSize(new Dimension(380, 50));
            recipeButton.setHorizontalAlignment(SwingConstants.LEFT);
            recipeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // 設置按鈕狀態
            if (recipe[0].equals(selectedRecipe)) {
                recipeButton.setBackground(new Color(70, 130, 180));
                recipeButton.setForeground(Color.WHITE);
            }
            
            recipeButton.addActionListener(e -> {
                if (recipe[0].equals(selectedRecipe)) {
                    selectedRecipe = null;
                    recipeButton.setBackground(null);
                    recipeButton.setForeground(Color.BLACK);
                    processMessage.setText("選擇配方開始製作！");
                } else {
                    // 取消其他按鈕的選擇狀態
                    for (Component c : buttonPanel.getComponents()) {
                        if (c instanceof JButton) {
                            c.setBackground(null);
                            c.setForeground(Color.BLACK);
                        }
                    }
                    selectedRecipe = recipe[0];
                    recipeButton.setBackground(new Color(70, 130, 180));
                    recipeButton.setForeground(Color.WHITE);
                    checkRecipeAvailability(recipe[0]);
                }
            });
            
            buttonPanel.add(recipeButton);
            buttonPanel.add(Box.createVerticalStrut(5));  // 添加間距
        }
    }

    private void checkRecipeAvailability(String recipe) {
        boolean canCraft = controller.canCraft(getProductId(recipe));

        if (canCraft) {
            processMessage.setText("材料充足，可以製作！");
            processMessage.setForeground(new Color(46, 139, 87));
        } else {
            processMessage.setText("材料不足，無��製作！");
            processMessage.setForeground(new Color(178, 34, 34));
        }
    }


    private String getProductId(String recipeName) {
        return switch(recipeName) {
            case "白色布料" -> "white_fabric";
            case "白色蕾絲" -> "white_lace";
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
            case "紅色蕾絲" -> "red_lace";
            case "黃色蕾絲" -> "yellow_lace";
            case "紫色蕾絲" -> "purple_lace";
            case "粉色蕾絲" -> "pink_lace";
            case "棉花" -> "harvested_cotton";
            case "玫瑰" -> "harvested_rose";
            case "向日葵" -> "harvested_sunflower";
            case "鬱金香(粉)" -> "harvested_tulip_pink";
            case "薰衣草" -> "harvested_lavender";
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
        resourcePanel.removeAll();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 添加標題
        JLabel titleLabel = new JLabel("加工資源");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        ResourceManager rm = ResourceManager.getInstance();

        // 根據當前頁面顯示對應資源和配方
        switch (currentPage) {
            case 0 -> { // 基本
                String[] flowers = {"棉花", "白色布料", "白色蕾絲"};
                String[] keys = {"harvested_cotton", "white_fabric", "white_lace"};
                addResourceList(contentPanel, flowers, keys, rm);
                updateRecipeButtons(new int[]{0, 1});
            }
            case 1 -> { // 染料
                String[] items = {"玫瑰", "向日葵", "鬱金香(粉)", "薰衣草", "紅色染料", "黃色染料", "紫色染料", "粉色染料"};
                String[] keys = {
                    "harvested_rose",      
                    "harvested_sunflower", 
                    "harvested_tulip_pink",
                    "harvested_lavender",  
                    "red_dye", 
                    "yellow_dye", 
                    "purple_dye", 
                    "pink_dye"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{2, 3, 4, 5});
            }
            case 2 -> { // 布料
                String[] items = {"白色布料","紅色染料", "紅色布料", "黃色染料", "黃色布料", "紫色染料", "紫色布料", "粉色染料", "粉色布料"};
                String[] keys = {
                    "white_fabric", 
                    "red_dye", 
                    "red_fabric", 
                    "yellow_dye", 
                    "yellow_fabric", 
                    "purple_dye", 
                    "purple_fabric", 
                    "pink_dye", 
                    "pink_fabric"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{0, 6, 7, 8, 9});
            }
            case 3 -> { // 蝴蝶結
                String[] items = {"白色布料", "白色蝴蝶結", "紅色布料", "紅色蝴蝶結", "黃色布料", "黃色蝴蝶結", "紫色布料", "紫色蝴蝶結", "粉色布料", "粉色蝴蝶結"};
                String[] keys = {
                    "white_fabric", 
                    "white_bow", 
                    "red_fabric", 
                    "red_bow", 
                    "yellow_fabric", 
                    "yellow_bow", 
                    "purple_fabric", 
                    "purple_bow", 
                    "pink_fabric", 
                    "pink_bow"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{10, 11, 12, 13, 14});
            }
            case 4 -> { // 緞帶
                String[] items = {"白色布料", "白色緞帶", "紅色布料", "紅色緞帶", "黃色布料", "黃色緞帶", "紫色布料", "紫色緞帶", "粉色布料", "粉色緞帶"};
                String[] keys = {
                    "white_fabric", 
                    "white_ribbon", 
                    "red_fabric", 
                    "red_ribbon", 
                    "yellow_fabric", 
                    "yellow_ribbon", 
                    "purple_fabric", 
                    "purple_ribbon", 
                    "pink_fabric", 
                    "pink_ribbon"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{15, 16, 17, 18, 19});
            }
            case 5 -> { // 連衣裙     
                String[] items = {"白色布料", "白色連衣裙", "紅色布料", "紅色連衣裙", "黃色布料", "黃色連衣裙", "紫色布料", "紫色連衣裙", "粉色布料", "粉色連衣裙"};
                String[] keys = {
                    "white_fabric", 
                    "white_dress", 
                    "red_fabric", 
                    "red_dress", 
                    "yellow_fabric", 
                    "yellow_dress", 
                    "purple_fabric", 
                    "purple_dress", 
                    "pink_fabric", 
                    "pink_dress"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{20, 21, 22, 23, 24});
            }
            case 6 -> { // 襯衫
                String[] items = {"白色布料", "白色襯衫", "紅色布料", "紅色襯衫", "黃色布料", "黃色襯衫", "紫色布料", "紫色襯衫", "粉色布料", "粉色襯衫"};
                String[] keys = {
                    "white_fabric", 
                    "white_shirt", 
                    "red_fabric", 
                    "red_shirt", 
                    "yellow_fabric", 
                    "yellow_shirt", 
                    "purple_fabric", 
                    "purple_shirt", 
                    "pink_fabric", 
                    "pink_shirt"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{25, 26, 27, 28, 29});
            }
            case 7 -> { // 褲子
                String[] items = {"白色布料", "白色褲子", "紅色布料", "紅色褲子", "黃色布料", "黃色褲子", "紫色布料", "紫色褲子", "粉色布料", "粉色褲子"};
                String[] keys = {
                    "white_fabric", 
                    "white_pants", 
                    "red_fabric", 
                    "red_pants", 
                    "yellow_fabric", 
                    "yellow_pants", 
                    "purple_fabric", 
                    "purple_pants", 
                    "pink_fabric", 
                    "pink_pants"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{30, 31, 32, 33, 34});
            }
            case 8 -> { // 蕾絲
                String[] items = {"白色蕾絲", "紅色染料", "紅色蕾絲", "黃色染料", "黃色蕾絲", "紫色染料", "紫色蕾絲", "粉色染料", "粉色蕾絲"};
                String[] keys = {
                    "white_lace", 
                    "red_dye", 
                    "red_lace", 
                    "yellow_dye", 
                    "yellow_lace", 
                    "purple_dye", 
                    "purple_lace", 
                    "pink_dye", 
                    "pink_lace"
                };
                addResourceList(contentPanel, items, keys, rm);
                updateRecipeButtons(new int[]{35, 36, 37, 38});
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
        // 確保所有按鈕都被初始化
        for (int i = 0; i < recipeButtons.length; i++) {
            if (recipeButtons[i] == null) {
                recipeButtons[i] = new JButton();
                recipeButtons[i].setFont(new Font("微軟正黑體", Font.PLAIN, 16));
                recipeButtons[i].setPreferredSize(new Dimension(400, 50));
                recipeButtons[i].setMaximumSize(new Dimension(400, 50));
                recipeButtons[i].setMinimumSize(new Dimension(400, 50));
                recipeButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
                buttonPanel.add(recipeButtons[i]);
            }
            recipeButtons[i].setVisible(false);
        }
        
        // 顯示定的按鈕
        for (int index : indices) {
            if (index < recipeButtons.length) {
                recipeButtons[index].setVisible(true);
            }
        }
        
        // 重新驗證面板
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
}