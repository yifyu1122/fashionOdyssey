package com.fashionodyssey.ui;

import com.fashionodyssey.controller.ProcessingController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ProcessingPanel extends JPanel {
    // 使用與 DesignPanel 一致的配色和字體
    private static final Color PINK_THEME = new Color(255, 182, 193);    // 淺粉色
    private static final Color LIGHT_PINK = new Color(255, 218, 224);    // 更淺的粉色
    private static final Color SOFT_YELLOW = new Color(255, 245, 200);   // 柔和的黃色
    private static final Color MINT_GREEN = new Color(200, 255, 214);    // 薄荷綠
    private static final Color TEXT_COLOR = new Color(80, 80, 80);       // 深灰色文字
    
    private static final Font CHINESE_FONT = new Font("微軟正黑體", Font.BOLD, 16);
    private static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);
    private static final Font COMBINED_FONT = new Font(
        CHINESE_FONT.getName() + ", " + EMOJI_FONT.getName(), 
        Font.BOLD, 
        16
    );
    private static final Font CONTENT_FONT = new Font("微軟正黑體", Font.PLAIN, 16);
    
    private JPanel resourcePanel;     // 資源面板
    private JLabel processMessage;    // 提示訊息
    private String selectedRecipe;    // 當前選擇的配方
    private JButton[] recipeButtons;  // 配方按鈕
    
    private final String[] pageNames = {"基本", "染料", "布料", "蝴蝶結", "緞帶", "連衣裙", "襯衫", "褲子", "蕾絲"};
    private ProcessingController controller;
    private JPanel buttonPanel; // 用於顯示配方按鈕的面板

    public ProcessingPanel() {
        controller = new ProcessingController();
        setLayout(new BorderLayout());
        setBackground(LIGHT_PINK);  // 設置背景色
        
        // 初始化 buttonPanel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(LIGHT_PINK);
        
        // 初始化 recipeButtons
        recipeButtons = new JButton[40];  // 根據您的配方數量調整
        
        // 初始化資源面板
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resourcePanel.setBackground(LIGHT_PINK);
        
        // 確保面板有最小尺寸
        resourcePanel.setMinimumSize(new Dimension(220, 100));
        resourcePanel.setPreferredSize(new Dimension(220, 500));  // 給一個預設高度
        
        // 美化頂部面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(LIGHT_PINK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(topPanel, BorderLayout.NORTH);
        
        initComponents();
        
        // 初始顯示第一個頁面
        showPage(0);
        
        // 監聽資源更新
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> updateResources());
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 0));
        
        // 創建主面板，使用 GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setBackground(LIGHT_PINK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.7; // 增加左側面板的比例
        gbc.weighty = 1.0; // 讓它們填滿整個高度
        
        // 左側第一列：配方書面板
        JPanel recipeSection = createRecipeSection();
        recipeSection.setMinimumSize(new Dimension(250, 80));  // 設置最小高度
        recipeSection.setPreferredSize(new Dimension(300, 80)); // 調整首選寬度
        mainPanel.add(recipeSection, gbc);
        
        // 左側第二列：配方清單
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(LIGHT_PINK);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "📜 配方清單",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane recipeScrollPane = new JScrollPane(buttonPanel);
        recipeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        recipeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        recipeScrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 1));  // 粉色邊框
        
        // 自定義滾動條
        JScrollBar verticalBar = recipeScrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 182, 193);  // 粉紅色滑塊
                this.trackColor = new Color(255, 240, 245);  // 淺粉色軌道
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 繪圓角矩形滑塊
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, 
                                thumbBounds.width, thumbBounds.height, 
                                10, 10);  // 圓角半徑
                
                g2.dispose();
            }
            
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 繪製圓角矩形軌道
                g2.setColor(trackColor);
                g2.fillRoundRect(trackBounds.x, trackBounds.y,
                                trackBounds.width, trackBounds.height,
                                10, 10);  // 圓角半徑
                
                g2.dispose();
            }
        });
        
        gbc.gridy = 1;
        mainPanel.add(recipeScrollPane, gbc);
        
        // 右側第一列：資源清單 (增加比例)
       
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 10, 0);
        mainPanel.add(createResourceSection(), gbc);
        
        // 右側第二列：底部控制面板 (減小比例)
        JPanel controlPanel = createControlSection();
        gbc.gridy = 1;
        mainPanel.add(controlPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createRecipeSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "📦 配方書",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 創建類別按鈕面板
        JPanel categoryPanel = createCategoryButtons();
        section.add(categoryPanel);
        
        
        return section;
    }
    
    private JPanel createResourceSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "📦 資源清單",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(resourcePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // 自定義滾動條
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 182, 193);  // 粉紅色滑塊
                this.trackColor = new Color(255, 240, 245);  // 淺粉色軌道
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 繪製圓角矩形滑塊
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, 
                                thumbBounds.width, thumbBounds.height, 
                                10, 10);  // 圓角半徑
                
                g2.dispose();
            }
            
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 繪製圓角矩形軌道
                g2.setColor(trackColor);
                g2.fillRoundRect(trackBounds.x, trackBounds.y,
                                trackBounds.width, trackBounds.height,
                                10, 10);  // 圓角半徑
                
                g2.dispose();
            }
        });
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createControlSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "🔧 加工中...",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 添加製作按鈕
        JButton craftButton = new JButton("開始製作");
        craftButton.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        craftButton.setBackground(SOFT_YELLOW);
        craftButton.setForeground(TEXT_COLOR);
        craftButton.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 滑鼠效果
        craftButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                craftButton.setBackground(MINT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                craftButton.setBackground(SOFT_YELLOW);
            }
        });
        
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
        
        section.add(craftButton);
        
        // 添加提示訊息
        processMessage = new JLabel("選擇配方開始製作！", SwingConstants.CENTER);
        processMessage.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        processMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        section.add(Box.createVerticalStrut(10));
        section.add(processMessage);
        
        return section;
    }

    private void showPage(int pageIndex) {
        try {
            System.out.println("嘗試顯示頁面: " + pageIndex);
            
            // 清除配方面板
            buttonPanel.removeAll();
            
            // 根據頁面索引更新配方
            switch (pageIndex) {
                case 0 -> { // 基本
                    addBasicRecipes(buttonPanel);
                }
                case 1 -> { // 染料
                    addDyeRecipes(buttonPanel);
                }
                case 2 -> { // 布料
                    addFabricRecipes(buttonPanel);
                }
                case 3 -> { // 蝴蝶結
                    addBowRecipes(buttonPanel);
                }
                case 4 -> { // 緞帶
                    addRibbonRecipes(buttonPanel);
                }
                case 5 -> { // 連衣裙
                    addDressRecipes(buttonPanel);
                }
                case 6 -> { // 襯衫
                    addShirtRecipes(buttonPanel);
                }
                case 7 -> { // 褲子
                    addPantsRecipes(buttonPanel);
                }
                case 8 -> { // 蕾絲
                    addLaceRecipes(buttonPanel);
                }
            }
            
            // 重新驗證和重繪配方面板
            buttonPanel.revalidate();
            buttonPanel.repaint();
            
            // 清除資源面板
            resourcePanel.removeAll();
            
            // 創建面板
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(LIGHT_PINK);
            
            ResourceManager rm = ResourceManager.getInstance();
            
            // 根據頁面索引更新資源和配方
            switch (pageIndex) {
                case 0 -> { // 基本
                    String[] items = {"棉花", "白色布料", "白色蕾絲", "白色蝴蝶結", "白色緞帶", "白色連衣裙", "白色襯衫", "白色褲子"};
                    String[] keys = {
                        "harvested_cotton", 
                        "white_fabric", 
                        "white_lace", 
                        "white_bow", 
                        "white_ribbon", 
                        "white_dress", 
                        "white_shirt", 
                        "white_pants"
                    };
                    addResourceList(contentPanel, items, keys, rm);
                    updateRecipeButtons();
                }
                case 1 -> { // 染料
                    String[] items = {"紅色染料", "黃色染料", "紫色染料", "粉色染料"};
                    String[] keys = {
                        "red_dye", 
                        "yellow_dye", 
                        "purple_dye", 
                        "pink_dye"
                    };
                    addResourceList(contentPanel, items, keys, rm);
                    updateRecipeButtons();
                }
                case 2 -> { // 布料
                    String[] items = {"白色布料", "紅色染料", "紅色布料", "黃色染料", "黃色布料", "紫色染料", "紫色布料", "粉色染料", "粉色布料"};
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
                    updateRecipeButtons();
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
                    updateRecipeButtons();
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
                        "pink_ribbon",
                        
                    };
                    addResourceList(contentPanel, items, keys, rm);
                    updateRecipeButtons();
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
                    updateRecipeButtons();
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
                    updateRecipeButtons();
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
                    updateRecipeButtons();
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
                    updateRecipeButtons();
                }
            }
            
            // 將內容面板添加到資源面板
            resourcePanel.add(contentPanel);
            resourcePanel.revalidate();
            resourcePanel.repaint();
            
        } catch (Exception e) {
            System.err.println("顯示頁面時發生錯誤: ");
            e.printStackTrace();
        }
    }

    private void addBasicRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色布料", "2個棉花 → 1個白色布料"},
            {"白色蕾絲", "1個棉花 → 1個白色蕾絲"},
            {"白色蝴蝶結", "1個白色布料 → 1個白色蝴蝶結"},
            {"白色緞帶", "1個白色布料 → 1個白色緞帶"},
            {"白色連衣裙", "2個白色布料 → 1個白色連衣裙"},
            {"白色襯衫", "1個白色布料 → 1個白色襯衫"},
            {"白色褲子", "1個白色布料 → 1個白色褲子"},
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addDyeRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"紅色染料", "1個玫瑰 → 1個紅色染料"},
            {"黃色染料", "1個向日葵 → 1個黃色染料"},
            {"粉色染料", "1個鬱金香(粉) → 1個粉色染料"},
            {"紫色染料", "1個薰衣草 → 1個紫色染料"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addFabricRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"紅色布料", "1個白色布料 + 1個紅色染料 → 1個紅色布料"},
            {"黃色布料", "1個白色布料 + 1個黃色染料 → 1個黃色布料"},
            {"粉色布料", "1個白色布料 + 1個粉色染料 → 1個粉色布料"},
            {"紫色布料", "1個白色布料 + 1個紫色染料 → 1個紫色布料"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addBowRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色蝴蝶結", "1個白色布料 → 1個白色蝴蝶結"},
            {"紅色蝴蝶結", "1個紅色布料 → 2個紅色蝴蝶結"},
            {"黃色蝴蝶結", "1個黃色布料 → 2個黃色蝴蝶結"},
            {"粉色蝴蝶結", "1個粉色布料 → 2個粉色蝴蝶結"},
            {"紫色蝴蝶結", "1個紫色布料 → 2個紫色蝴蝶結"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addRibbonRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色緞帶", "1個白色布料 → 1個白色緞帶"},
            {"紅色緞帶", "1個紅色布料 → 1個紅色緞帶"},
            {"黃色緞帶", "1個黃色布料 → 1個黃色緞帶"},
            {"粉色緞帶", "1個粉色布料 → 1個粉色緞帶"},
            {"紫色緞帶", "1個紫色布料 → 1個紫色緞帶"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addDressRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色連衣裙", "2個白色布料 → 1個白色連衣裙"},
            {"紅色連衣裙", "2個紅色布料 → 1個紅色連衣裙"},
            {"黃色連衣裙", "2個黃色布料 → 1個黃色連衣裙"},
            {"粉色連衣裙", "2個粉色布料 → 1個粉色連衣裙"},
            {"紫色連衣裙", "2個紫色布料 → 1個紫色連衣裙"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addShirtRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色襯衫", "1個白色布料 → 1個白色襯衫"},
            {"紅色襯衫", "1個紅色布料 → 1個紅色襯衫"},
            {"黃色襯衫", "1個黃色布料 → 1個黃色襯衫"},
            {"粉色襯衫", "1個粉色布料 → 1個粉色襯衫"},
            {"紫色襯衫", "1個紫色布料 → 1個紫色襯衫"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addPantsRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"白色褲子", "1個白色布料 → 1個白色褲子"},
            {"紅色褲子", "1個紅色布料 → 1個紅色褲子"},
            {"黃色褲子", "1個黃色布料 → 1個黃色褲子"},
            {"粉色褲子", "1個粉色布料 → 1個粉色褲子"},
            {"紫色褲子", "1個紫色布料 → 1個紫色褲子"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addLaceRecipes(JPanel targetPanel) {
        String[][] recipes = {
            {"紅色蕾絲", "1個白色蕾絲 + 1個紅色染料 → 1個紅色蕾絲"},
            {"黃色蕾絲", "1個白色蕾絲 + 1個黃色染料 → 1個黃色蕾絲"},
            {"紫色蕾絲", "1個白色蕾絲 + 1個紫色染料 → 1個紫色蕾絲"},
            {"粉色蕾絲", "1個白色蕾絲 + 1個粉色染料 → 1個粉色蕾絲"}
        };
        addRecipesToPanel(recipes, targetPanel);
    }

    private void addRecipesToPanel(String[][] recipes, JPanel targetPanel) {
        for (String[] recipe : recipes) {
            JButton recipeButton = new JButton();
            recipeButton.setLayout(new BorderLayout());
            
            // 使用 HTML 格式化文字，確保文字可以換行
            String formattedText = String.format("<html><div style='margin: 5px;'>%s</div></html>", recipe[1]);
            JLabel recipeLabel = new JLabel(formattedText);
            recipeLabel.setFont(CONTENT_FONT);
            recipeLabel.setForeground(TEXT_COLOR);
            
            recipeButton.add(recipeLabel, BorderLayout.CENTER);
            recipeButton.setBackground(SOFT_YELLOW);
            recipeButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, PINK_THEME),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            
            // 設置按鈕的最小和首選大小
            recipeButton.setMinimumSize(new Dimension(400, 50));
            recipeButton.setPreferredSize(new Dimension(400, 50));
            recipeButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
            recipeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // 設置按鈕狀態
            if (recipe[0].equals(selectedRecipe)) {
                recipeButton.setBackground(MINT_GREEN);
            }
            
            // 滑鼠效果
            recipeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!recipe[0].equals(selectedRecipe)) {
                        recipeButton.setBackground(MINT_GREEN);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!recipe[0].equals(selectedRecipe)) {
                        recipeButton.setBackground(SOFT_YELLOW);
                    }
                }
            });
            
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
            buttonPanel.add(Box.createVerticalStrut(5));
        }
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

        // 檢查否還能繼續製作
        if (selectedRecipe != null) {
            checkRecipeAvailability(selectedRecipe);
        }
    }

    public void updateResources() {
        resourcePanel.removeAll();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

       
        contentPanel.add(Box.createVerticalStrut(20));

       

        
        resourcePanel.add(contentPanel);
        resourcePanel.revalidate();
        resourcePanel.repaint();
    }

    private void addResourceList(JPanel panel, String[] names, String[] keys, ResourceManager rm) {
        for (int i = 0; i < names.length; i++) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            itemPanel.setBackground(LIGHT_PINK);
            
            // 添加可愛的圖標
            String icon;
            if (names[i].contains("布料")) {
                icon = "🧵";
            } else if (names[i].contains("染料")) {
                icon = "🎨";
            } else if (names[i].contains("蕾絲")) {
                icon = "✨";
            } else if (names[i].contains("蝴蝶結")) {
                icon = "🎀";
            } else if (names[i].contains("緞帶")) {
                icon = "🎗️";
            } else if (names[i].contains("連衣裙")) {
                icon = "👗";
            } else if (names[i].contains("襯衫")) {
                icon = "👔";
            } else if (names[i].contains("褲子")) {
                icon = "👖";
            } else if (names[i].contains("棉花")) {
                icon = "🌱";
            } else if (names[i].contains("玫瑰")) {
                icon = "🌹";
            } else if (names[i].contains("向日葵")) {
                icon = "🌻";
            } else if (names[i].contains("鬱金香")) {
                icon = "🌷";
            } else if (names[i].contains("薰衣草")) {
                icon = "💜";
            } else {
                icon = "🌸";
            }
            
            // 分開創建 emoji 和文字標籤
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(EMOJI_FONT);
            
            JLabel textLabel = new JLabel(String.format(" %s: %d", 
                names[i], rm.getResourceAmount(keys[i])));
            textLabel.setFont(CONTENT_FONT);
            textLabel.setForeground(new Color(199, 21, 133));
            
            itemPanel.add(iconLabel);
            itemPanel.add(textLabel);
            panel.add(itemPanel);
            panel.add(Box.createVerticalStrut(8));
        }
    }

    private void updateRecipeButtons() {
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
        
        
        // 重新驗證面板
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private JPanel createCategoryButtons() {
        JPanel categoryPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        categoryPanel.setBackground(LIGHT_PINK);
        
        for (int i = 0; i < pageNames.length; i++) {
            JButton btn = new JButton(pageNames[i]);
            btn.setFont(CHINESE_FONT);
            btn.setBackground(SOFT_YELLOW);
            btn.setForeground(TEXT_COLOR);
            
            // 添加圓角邊框
            btn.setBorder(new RoundedBorder(15, PINK_THEME));
            
            final int index = i;
            btn.addActionListener(e -> {
                // 添加調試輸出
                System.out.println("按鈕被點擊: " + pageNames[index] + ", 索引: " + index);
                showPage(index);
            });
            categoryPanel.add(btn);
        }
        
        return categoryPanel;
    }

}