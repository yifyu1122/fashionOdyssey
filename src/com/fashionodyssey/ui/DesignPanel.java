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
    private JComboBox<String> baseItemSelector;    // 基礎服裝選擇
    private JComboBox<String>[] decorationSelectors; // 裝飾品選擇器
    private JSpinner[] decorationCounts;             // 裝飾品數量
    private JTextField nameField;                    // 命名欄位
    private JButton designButton;                    // 開始製作按鈕
    private ResourceManager resourceManager;         // 資源管理
    private DesignController designController;       // 控制器
    private JLabel materialsLabel;                   // 材料清單標籤

    public DesignPanel() {
        designController = DesignController.getInstance();
        resourceManager = ResourceManager.getInstance();
        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        
        baseItemSelector = new JComboBox<>(new String[]{
            "無", "白色連衣裙", "紅色連衣裙", "黃色連衣裙", "粉色連衣裙", "紫色連衣裙", 
            "白色襯衫", "紅色襯衫", "黃色襯衫", "粉色襯衫", "紫色襯衫",
            "白色褲子", "紅色褲子", "黃色褲子", "粉色褲子", "紫色褲子"
        });

          
        decorationSelectors = new JComboBox[4];  
        decorationCounts = new JSpinner[4];     
        String[] decorations = {
            "無", "白色蕾絲", "紅色蕾絲", "黃色蕾絲", "粉色蕾絲", "紫色蕾絲",
            "白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "粉色蝴蝶結", "紫色蝴蝶結",
            "白色緞帶", "紅色緞帶", "黃色緞帶", "粉色緞帶", "紫色緞帶"
        };

        JPanel decorationPanel = new JPanel();
        decorationPanel.setLayout(new GridLayout(4, 2, 5, 5)); // 4 rows, 2 columns

        for (int i = 0; i < 4; i++) {
            decorationSelectors[i] = new JComboBox<>(decorations);
            decorationCounts[i] = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1)); // Start at 0
            decorationPanel.add(decorationSelectors[i]);
            decorationPanel.add(decorationCounts[i]);
        }

        // 設計控制台
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("設計控制台"));
        controlPanel.setPreferredSize(new Dimension(300, 0));

        controlPanel.add(createBaseItemSection());
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(decorationPanel);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(createMaterialsSection());
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(createFinalizeSection());

        add(controlPanel, BorderLayout.CENTER);

        // 修改：初始化後立即更新材料列表和按鈕狀態
        SwingUtilities.invokeLater(this::updateMaterialsList);
    }

    private JPanel createBaseItemSection() {
        JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
        section.setBorder(BorderFactory.createTitledBorder("基礎服裝"));
        section.add(new JLabel("基礎服裝:"));
        section.add(baseItemSelector);
        return section;
    }

    private JPanel createMaterialsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("材料清單"));

        materialsLabel = new JLabel("<html><span style='color:gray'>選擇基礎服裝和裝飾品後顯示</span></html>");
        section.add(materialsLabel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createFinalizeSection() {
        JPanel section = new JPanel();
        section.setLayout(new FlowLayout(FlowLayout.CENTER));

        nameField = new JTextField(15);
        section.add(new JLabel("作品名稱:"));
        section.add(nameField);

        designButton = new JButton("開始製作");
        designButton.setEnabled(false); // 初始不可用
        section.add(designButton);

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
            
            JOptionPane.showMessageDialog(this, "設計成功！已新增至您的設計列表中，請前往銷售頁面刷新查看。", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        // 將資源ID轉換為顯示名稱
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
