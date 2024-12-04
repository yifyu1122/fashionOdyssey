package com.fashionodyssey.ui;

import com.fashionodyssey.controller.DesignController;
import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.design.Design;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import javax.swing.*;

public class DesignPanel extends JPanel {
    private JComboBox<String> baseItemSelector;    // 基礎服裝選擇
    private JComboBox<String> decorationSelector;  // 裝飾品選擇
    private JSpinner decorationCount;              // 裝飾品數量
    private JTextField nameField;                  // 命名欄位
    private JButton designButton;
    private JPanel previewPanel;
    private ResourceManager resourceManager;
    private DesignController designController;
    
    public DesignPanel() {
        designController = DesignController.getInstance();
        resourceManager = ResourceManager.getInstance();
        initComponents();
        
        designButton.addActionListener(e -> {
            String designName = nameField.getText().trim();
            EventManager.getInstance().fireEvent(
                new GameEvent("CREATE_DESIGN", designName)
            );
        });
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel leftPanel = createLeftControlPanel();
        add(leftPanel, BorderLayout.WEST);

        previewPanel = createPreviewPanel();
        add(previewPanel, BorderLayout.CENTER);
    }

    private JPanel createLeftControlPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("設計控制台"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // 1. 基礎服裝選擇區
        JPanel baseItemSection = createBaseItemSection();
        leftPanel.add(baseItemSection);
        leftPanel.add(Box.createVerticalStrut(15));

        // 2. 裝飾品選擇區
        JPanel decorationSection = createDecorationSection();
        leftPanel.add(decorationSection);
        leftPanel.add(Box.createVerticalStrut(15));

        // 3. 材料清單區
        JPanel materialsSection = createMaterialsSection();
        leftPanel.add(materialsSection);
        leftPanel.add(Box.createVerticalStrut(15));

        // 4. 命名和製作區
        JPanel finalizeSection = createFinalizeSection();
        leftPanel.add(finalizeSection);

        return leftPanel;
    }

    private JPanel createBaseItemSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder("基礎服裝"));
        
        String[] colors = {"白色", "紅色", "黃色", "粉色", "紫色"};
        JComboBox<String> colorSelector = new JComboBox<>(colors);
        section.add(new JLabel("顏色:"));
        section.add(colorSelector);

        String[] types = {"連衣裙", "襯衫", "褲子"};
        JComboBox<String> typeSelector = new JComboBox<>(types);
        section.add(new JLabel("類型:"));
        section.add(typeSelector);

        baseItemSelector = new JComboBox<>();
        section.add(baseItemSelector);

        JButton confirmButton = new JButton("確認選擇");
        confirmButton.addActionListener(e -> {
            String color = (String) colorSelector.getSelectedItem();
            String type = (String) typeSelector.getSelectedItem();
            EventManager.getInstance().fireEvent(
                new GameEvent("SELECT_BASE_ITEM", color, type)
            );
        });
        section.add(confirmButton);

        return section;
    }

    private JPanel createDecorationSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder("裝飾品"));

        // 裝飾品選擇
        String[] decorations = {
            "白色蕾絲", "紅色蕾絲", "黃色蕾絲", "粉色蕾絲", "紫色蕾絲",
            "白色緞帶", "紅色緞帶", "黃色緞帶", "粉色緞帶", "紫色緞帶",
            "白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "粉色蝴蝶結", "紫色蝴蝶結"
        };
        
        JPanel decorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        decorPanel.add(new JLabel("選擇:"));
        decorationSelector = new JComboBox<>(decorations);
        decorPanel.add(decorationSelector);
        section.add(decorPanel);

        // 數量選擇
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("數量:"));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 5, 1);
        decorationCount = new JSpinner(spinnerModel);
        countPanel.add(decorationCount);
        section.add(countPanel);

        // 操作按鈕
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("新增");
        JButton removeButton = new JButton("移除");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        section.add(buttonPanel);

        return section;
    }

    private JPanel createMaterialsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder("所需材料"));

        // 材料清單
        JPanel materialsPanel = new JPanel();
        materialsPanel.setLayout(new BoxLayout(materialsPanel, BoxLayout.Y_AXIS));
        JLabel materialsLabel = new JLabel("尚未選擇材料");
        materialsPanel.add(materialsLabel);
        section.add(materialsPanel);

        return section;
    }

    private JPanel createFinalizeSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder("完成設計"));

        // 命名欄位
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("作品命名:"));
        nameField = new JTextField(15);
        namePanel.add(nameField);
        section.add(namePanel);

        // 製作按鈕
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        designButton = new JButton("開始製作");
        designButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        buttonPanel.add(designButton);
        section.add(buttonPanel);

        return section;
    }

    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawDesignPreview(g);
            }
        };
        panel.setPreferredSize(new Dimension(400, 400));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("預覽"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
    
    private void createDesign() {
        String baseItem = (String) baseItemSelector.getSelectedItem();
        String decoration = (String) decorationSelector.getSelectedItem();
        int count = (Integer) decorationCount.getValue();
        String designName = nameField.getText().trim();
        
        if (designName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "請為您的作品命名！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 檢查材料是否足夠
        if (!checkMaterials(baseItem, decoration, count)) {
            JOptionPane.showMessageDialog(this, 
                "材料不足，無法製作！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 消耗材料
        consumeMaterials(baseItem, decoration, count);
        
        // 創建新設計並添加到倉庫
        Design newDesign = new Design(designName, baseItem, decoration, count);
        resourceManager.addDesign(newDesign);
        
        // 通知更新
        EventManager.getInstance().fireEvent(
            new GameEvent("DESIGN_CREATED", newDesign)
        );
        
        // 重置輸入
        nameField.setText("");
        decorationCount.setValue(1);
        
        JOptionPane.showMessageDialog(this, 
            "設計完成！" + designName + "已加入倉庫", 
            "成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean checkMaterials(String baseItem, String decoration, int count) {
        return resourceManager.hasEnoughMaterials(baseItem, decoration, count);
    }
    
    private void consumeMaterials(String baseItem, String decoration, int count) {
        resourceManager.consumeMaterials(baseItem, decoration, count);
    }
    
    private void drawDesignPreview(Graphics g) {
        // 繪製預覽圖的邏輯
        if (baseItemSelector != null && decorationSelector != null) {
            String baseItem = (String) baseItemSelector.getSelectedItem();
            String decoration = (String) decorationSelector.getSelectedItem();
            int count = (Integer) decorationCount.getValue();
            
            // 繪製基礎服裝
            drawBaseItem(g, baseItem);
            
            // 繪製裝飾品
            for (int i = 0; i < count; i++) {
                drawDecoration(g, decoration, i);
            }
        }
    }
    
    private void drawBaseItem(Graphics g, String baseItem) {
        // ... 繪製基礎服裝的具體實現 ...
    }
    
    private void drawDecoration(Graphics g, String decoration, int position) {
        // ... 繪製裝飾品的具體實現 ...
    }
    
    private void updateDesignButtonState() {
        String baseItem = (String) baseItemSelector.getSelectedItem();
        String decoration = (String) decorationSelector.getSelectedItem();
        int count = (Integer) decorationCount.getValue();
        
        if(baseItem != null && decoration != null) {
            boolean hasEnoughMaterials = resourceManager.hasEnoughMaterials(
                baseItem, decoration, count);
            designButton.setEnabled(hasEnoughMaterials);
        } else {
            designButton.setEnabled(false);
        }
    }
} 