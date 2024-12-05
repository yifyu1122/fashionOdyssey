package com.fashionodyssey.ui;

import com.fashionodyssey.controller.DesignController;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class DesignPanel extends JPanel {
    private JComboBox<String> baseItemSelector;    // 基礎服裝選擇
    private JComboBox<String> decorationSelector; // 裝飾品選擇
    private JSpinner decorationCount;             // 裝飾品數量
    private JTextField nameField;                 // 命名欄位
    private JButton designButton;                 // 開始製作按鈕
    private ResourceManager resourceManager;      // 資源管理
    private DesignController designController;    // 控制器
    private JLabel materialsLabel;                // 材料清單標籤

    public DesignPanel() {
        designController = DesignController.getInstance();
        resourceManager = ResourceManager.getInstance();
        initComponents();
        addListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Initialize baseItemSelector here
        baseItemSelector = new JComboBox<>(new String[]{
            "白色連衣裙", "紅色連衣裙", "黃色連衣裙", "粉色連衣裙", "紫色連衣裙", 
            "白色襯衫", "紅色襯衫", "黃色襯衫", "粉色襯衫", "紫色襯衫",
            "白色褲子", "紅色褲子", "黃色褲子", "粉色褲子", "紫色褲子"
        });

        // Initialize decorationSelector here
        decorationSelector = new JComboBox<>(new String[]{
            "白色蕾絲", "紅色蕾絲", "黃色蕾絲", "粉色蕾絲", "紫色蕾絲",
            "白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "粉色蝴蝶結", "紫色蝴蝶結",
            "白色緞帶", "紅色緞帶", "黃色緞帶", "粉色緞帶", "紫色緞帶"
        });

        // Add baseItemSelector and decorationSelector to the panel
        JPanel leftPanel = createControlPanel();
        add(leftPanel, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("設計控制台"));
        controlPanel.setPreferredSize(new Dimension(300, 0));

        // 基礎服裝
        controlPanel.add(createBaseItemSection());
        controlPanel.add(Box.createVerticalStrut(15));

        // 裝飾品
        controlPanel.add(createDecorationSection());
        controlPanel.add(Box.createVerticalStrut(15));

        // 材料清單
        controlPanel.add(createMaterialsSection());
        controlPanel.add(Box.createVerticalStrut(15));

        // 完成區域
        controlPanel.add(createFinalizeSection());
        return controlPanel;
    }

    private JPanel createBaseItemSection() {
        JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
        section.setBorder(BorderFactory.createTitledBorder("基礎服裝"));

        String[] baseItems = {
            "白色連衣裙", "紅色連衣裙", "黃色連衣裙", "粉色連衣裙", "紫色連衣裙", 
            "白色襯衫", "紅色襯衫", "黃色襯衫", "粉色襯衫", "紫色襯衫",
            "白色褲子", "紅色褲子", "黃色褲子", "粉色褲子", "紫色褲子"
        };
        baseItemSelector = new JComboBox<>(baseItems);
        section.add(new JLabel("基礎服裝:"));
        section.add(baseItemSelector);

        return section;
    }

    private JPanel createDecorationSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("裝飾品"));

        // 裝飾品選擇面板
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        String[] decorations = {
            "白色蕾絲", "紅色蕾絲", "黃色蕾絲", "粉色蕾絲", "紫色蕾絲",
            "白色蝴蝶結", "紅色蝴蝶結", "黃色蝴蝶結", "粉色蝴蝶結", "紫色蝴蝶結",
            "白色緞帶", "紅色緞帶", "黃色緞帶", "粉色緞帶", "紫色緞帶"
        };
        JList<String> decorationList = new JList<>(decorations);
        decorationList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(decorationList);
        scrollPane.setPreferredSize(new Dimension(150, 100));
        
        decorationCount = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        
        // 建立按鈕面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("添加");
        JButton clearAllButton = new JButton("清除全部");
        
        // 添加按鈕事件
        addButton.addActionListener(e -> {
            for (String selectedDecoration : decorationList.getSelectedValuesList()) {
                int count = (Integer) decorationCount.getValue();
                designController.addDecoration(mapToKey(selectedDecoration), count);
            }
            updateDecorationList();
            updateMaterialsList();
        });
        clearAllButton.addActionListener(e -> {
            designController.clearDecorations();
            updateDecorationList();
            updateMaterialsList();
        });

        // 添加組件到選擇面板
        selectionPanel.add(new JLabel("裝飾品:"));
        selectionPanel.add(scrollPane);
        selectionPanel.add(new JLabel("數量:"));
        selectionPanel.add(decorationCount);
        
        // 添加按鈕面板
        selectionPanel.add(buttonPanel);

        // 已選裝飾品列表
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        section.add(selectionPanel, BorderLayout.NORTH);
        section.add(listPanel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createMaterialsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("材料清單"));

        JLabel materialsLabel = new JLabel("<html><span style='color:gray'>選擇基礎服裝和裝飾品後顯示</span></html>");
        section.add(materialsLabel, BorderLayout.CENTER);

        // Store the label for later updates
        this.materialsLabel = materialsLabel;

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
        decorationSelector.addActionListener(e -> updatePreviewAndMaterials());
        decorationCount.addChangeListener(e -> updatePreviewAndMaterials());
        designButton.addActionListener(e -> createDesign());
    }

    private void updatePreviewAndMaterials() {
        // 確保不會重置裝飾品選擇器
        String selectedDecoration = (String) decorationSelector.getSelectedItem();
        String selectedBase = (String) baseItemSelector.getSelectedItem();
        int count = (Integer) decorationCount.getValue();

        // 設置當前基礎服裝和裝飾品
        designController.setBaseItem(mapToKey(selectedBase));
        designController.addDecoration(mapToKey(selectedDecoration), count);

        // 更按鈕狀態
        boolean hasEnoughMaterials = designController.canCraft();
        designButton.setEnabled(hasEnoughMaterials);

        // 更新材料清單
        updateMaterialsList();

        // 恢復裝飾品選擇
        decorationSelector.setSelectedItem(selectedDecoration);
    }

    private void updateMaterialsList() {
        Map<String, Integer> materials = designController.getRequiredMaterials();
        
        StringBuilder builder = new StringBuilder("<html>");
        materials.forEach((name, required) -> {
            int available = resourceManager.getResourceAmount(name);
            String color = available < required ? "red" : "black";
            String displayName = getDisplayName(name);
            builder.append(String.format(
                "<div><span style='color:%s'>%s (需要: %d, 擁有: %d)</span></div>",
                color, displayName, required, available));
        });
        builder.append("</html>");

        // Update the materials label
        materialsLabel.setText(builder.toString());
        materialsLabel.revalidate();
        materialsLabel.repaint();
    }

    private void createDesign() {
        String baseItem = (String) baseItemSelector.getSelectedItem();
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請為您的設計命名！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (designController.canCraft()) {
            designController.craftDesign();
            JOptionPane.showMessageDialog(this, "設計成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            decorationCount.setValue(1);
            updateMaterialsList();
        } else {
            JOptionPane.showMessageDialog(this, "材料不足！", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
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
        // 將基礎服裝名稱轉換為資源ID
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

    private void updateDecorationList() {
        // Assuming you have a component to display the selected decorations
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        Map<String, Integer> decorations = designController.getSelectedDecorations();
        for (Map.Entry<String, Integer> entry : decorations.entrySet()) {
            String decorationName = getDisplayName(entry.getKey());
            int count = entry.getValue();
            JLabel label = new JLabel(decorationName + " x " + count);
            listPanel.add(label);
        }
        
    }
}
