package com.fashionodyssey.ui;

import com.fashionodyssey.controller.DesignController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.cost.ItemCost;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class DesignPanel extends JPanel {
    // 使用 MainFrame 相同的配色方案
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
    
    private JComboBox<String> baseItemSelector;    // 基礎服裝選擇
    private JComboBox<String>[] decorationSelectors; // 裝飾品選擇器
    private JSpinner[] decorationCounts;             // 裝飾品數量
    private JTextField nameField;                    // 命名欄位
    private JButton designButton;                    // 開始製作按鈕
    private ResourceManager resourceManager;         // 資源管理
    private DesignController designController;       // 控制器
    private JLabel materialsLabel;                   // 材料清單標籤
    private static final String[] decorations = {
        "無", "白色蕾絲", "紅色蕾絲", "黃色蕾絲", "粉色蕾絲", "紫色蕾絲",
        "白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "粉色蝴蝶結", "紫色蝴蝶結",
        "白色緞帶", "紅色緞帶", "黃色緞帶", "粉色緞帶", "紫色緞帶"
    };

    public DesignPanel() {
        designController = DesignController.getInstance();
        resourceManager = ResourceManager.getInstance();
        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(LIGHT_PINK);

        // 初始化基礎服裝選擇器
        baseItemSelector = new JComboBox<>(new String[]{"連衣裙", "襯衫", "褲子"});
        
        // 初始化裝飾品選擇器和數量選擇器
        decorationSelectors = new JComboBox[4];  
        decorationCounts = new JSpinner[4];     
        
        // 初始化裝飾品面板
        JPanel decorationPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        decorationPanel.setBackground(LIGHT_PINK);
        decorationPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new RoundedBorder(10, PINK_THEME),
                "✨ 裝飾品選擇",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        baseItemSelector = new JComboBox<>(new String[]{
            "無", "白色連衣裙", "紅色連衣裙", "黃色連衣裙", "粉色連衣裙", "紫色連衣裙", 
            "白色襯衫", "紅色襯衫", "黃色襯衫", "粉色襯衫", "紫色襯衫",
            "白色褲子", "紅色褲子", "黃色褲子", "粉色褲子", "紫色褲子"
        });

    
        

        // 美化下拉選單和數量選擇器
        for (int i = 0; i < 4; i++) {
            decorationSelectors[i] = new JComboBox<>(decorations);
            decorationSelectors[i].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            decorationSelectors[i].setBackground(SOFT_YELLOW);
            
            decorationCounts[i] = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
            decorationCounts[i].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            JComponent editor = decorationCounts[i].getEditor();
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setBackground(SOFT_YELLOW);
            
            decorationPanel.add(decorationSelectors[i]);
            decorationPanel.add(decorationCounts[i]);
        }

        // 修改控制面板的布局為 GridBagLayout
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        controlPanel.setBackground(LIGHT_PINK);

        // 設置 GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // 基礎服裝區域 (40%)
        gbc.weightx = 0.4;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 5);
        controlPanel.add(createBaseItemSection(), gbc);

        // 裝飾品選擇區域 (60%)
        gbc.weightx = 0.6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 10, 0);
        controlPanel.add(decorationPanel, gbc);

        // 底部面板
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(LIGHT_PINK);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.4;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        bottomPanel.add(createMaterialsSection(), gbc);

        gbc.weightx = 0.6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        bottomPanel.add(createFinalizeSection(), gbc);

        // 將底部面板添加到控制面板
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 0, 0);
        controlPanel.add(bottomPanel, gbc);

        // 將控制面板添加到主面板
        add(controlPanel, BorderLayout.CENTER);

        // 初始化後立即更新材料列表
        SwingUtilities.invokeLater(this::updateMaterialsList);
    }

    private JPanel createBaseItemSection() {
        JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "👗 基礎服裝",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        baseItemSelector.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        baseItemSelector.setBackground(SOFT_YELLOW);
        section.add(baseItemSelector);
        
        return section;
    }

    private JPanel createMaterialsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "📋 材料清單",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        materialsLabel = new JLabel("<html><span style='color:gray'>選擇基礎服裝和裝飾品後顯示</span></html>");
        materialsLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        section.add(materialsLabel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createFinalizeSection() {
        // 改用 BoxLayout 來更好地控制垂直排列
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_PINK);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_THEME, 2, true),
                "🎨 完成設計",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                COMBINED_FONT,
                TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // 創建名稱輸入區���面板
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setBackground(LIGHT_PINK);
        
        JLabel nameLabel = new JLabel("作品名稱:");
        nameLabel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        
        nameField = new JTextField(15);
        nameField.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        nameField.setBackground(SOFT_YELLOW);
        
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        // 創建按鈕面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(LIGHT_PINK);
        
        designButton = new JButton("✨ 開始製作");
        designButton.setFont(COMBINED_FONT);
        designButton.setBackground(SOFT_YELLOW);
        designButton.setForeground(TEXT_COLOR);
        designButton.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, PINK_THEME),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        designButton.setPreferredSize(new Dimension(180, 35));
        
        buttonPanel.add(designButton);

        // 添加組件到主面板，並在名稱區域和按鈕之間添加間距
        section.add(namePanel);
        section.add(Box.createVerticalStrut(15));  // 添加 15 像素的垂直間距
        section.add(buttonPanel);

        // 添加按鈕懸停效果
        designButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (designButton.isEnabled()) {
                    designButton.setBackground(MINT_GREEN);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (designButton.isEnabled()) {
                    designButton.setBackground(SOFT_YELLOW);
                }
            }
        });

        return section;
    }

    private void addListeners() {
        baseItemSelector.addActionListener(e -> updatePreviewAndMaterials());
        for (int i = 0; i < 4; i++) {
            decorationSelectors[i].addActionListener(e -> updateMaterialsList());
            decorationCounts[i].addChangeListener(e -> updateMaterialsList());
        }
        designButton.addActionListener(e -> createDesign());
        
        // 新增：監聽名稱欄位的變更
        nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateMaterialsList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateMaterialsList(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateMaterialsList(); }
        });
    }

    private void updateMaterialsList() {
        designController.clearDecorations();
        for (int i = 0; i < decorationSelectors.length; i++) {
            String selectedDecoration = (String) decorationSelectors[i].getSelectedItem();
            int count = (Integer) decorationCounts[i].getValue();
            if (!"無".equals(selectedDecoration) && count > 0) {
                String decorationKey = mapToKey(selectedDecoration);
                designController.addDecoration(decorationKey, count);
                System.out.println("添加裝飾品: " + decorationKey + " 數量: " + count);
            }
        }
        
        Map<String, Integer> materials = designController.getRequiredMaterials();
        
        // Use an array to hold the total cost
        final double[] totalCost = {0.0};
        StringBuilder builder = new StringBuilder("<html>");
        
        // Add materials list
        builder.append("<div><b>需要材料：</b></div>");
        materials.forEach((name, required) -> {
            int available = resourceManager.getResourceAmount(name);
            String color = available < required ? "red" : "black";
            String displayName = getDisplayName(name);
            double itemCost = ItemCost.calculateCost(name) * required;
            totalCost[0] += itemCost;
            builder.append(String.format(
                "<div><span style='color:%s'>%s (需要: %d, 擁有: %d) - 成本: %.2f金幣</span></div>",
                color, displayName, required, available, itemCost));
        });
        
        // Add total cost
        builder.append(String.format("<div style='margin-top:10px'><b>總成本：%.2f金幣</b></div>", totalCost[0]));
        builder.append("</html>");

        // Update the materials label
        materialsLabel.setText(builder.toString());
        materialsLabel.revalidate();
        materialsLabel.repaint();

        // Enable or disable the design button based on conditions
        designButton.setEnabled(!nameField.getText().trim().isEmpty() && designController.canCraft());
    }

    private void createDesign() {
        if (designController.canCraft()) {
            double totalCost = calculateTotalCost();
            System.out.println("開始製作設計，總成本: " + totalCost);
            
            // Set the design name in the controller
            designController.setDesignName(nameField.getText().trim());
            
            designController.craftDesign(totalCost);
            
            EventManager.getInstance().fireEvent(
                new GameEvent("UPDATE_RESOURCES", Map.of(
                    "designName", nameField.getText(),
                    "baseItem", (String) baseItemSelector.getSelectedItem(),
                    "totalCost", totalCost
                ))
            );
            
            JOptionPane.showMessageDialog(this, "設計成功！已新增至您的設計列表中，請前往銷售面刷新查看。", "提示", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            for (JSpinner spinner : decorationCounts) {
                spinner.setValue(0);
            }
            updateMaterialsList();
        } else {
            JOptionPane.showMessageDialog(this, "材料不足！", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateTotalCost() {
        double baseCost = ItemCost.calculateCost(designController.getCurrentBaseItem());
        double decorationCost = designController.getCurrentDecorations().entrySet().stream()
            .mapToDouble(entry -> ItemCost.calculateCost(entry.getKey()) * entry.getValue())
            .sum();
        return baseCost + decorationCost;
    }

    private String getDisplayName(String key) {
        // 將資源ID轉換為顯示名���
        return key.replace("white_", "白色")
                 .replace("red_", "紅色")
                 .replace("yellow_", "黃色")
                 .replace("pink_", "粉色")
                 .replace("purple_", "紫色")
                 .replace("dress", "連衣裙")
                 .replace("shirt", "襯衫")
                 .replace("pants", "褲子")
                 .replace("lace", "蕾絲")
                 .replace("ribbon", "緞帶")
                 .replace("bow", "蝴蝶結");
    }

    private String mapToKey(String item) {
        // 將服裝名稱轉換為資源ID
        return item.toLowerCase()
                   .replace("白色", "white_")
                   .replace("紅色", "red_")
                   .replace("黃色", "yellow_")
                   .replace("粉色", "pink_")
                   .replace("紫色", "purple_")
                   .replace("連衣裙", "dress")
                   .replace("襯衫", "shirt")
                   .replace("褲子", "pants")
                   .replace("蕾絲", "lace")
                   .replace("緞帶", "ribbon")
                   .replace("蝴蝶結", "bow");
    }

    private void updatePreviewAndMaterials() {
        String selectedBase = (String) baseItemSelector.getSelectedItem();
        designController.setBaseItem(mapToKey(selectedBase));
        updateMaterialsList();
    }
}
