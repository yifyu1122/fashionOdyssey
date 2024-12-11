package com.fashionodyssey.ui;

import com.fashionodyssey.controller.DesignController;
import com.fashionodyssey.model.cost.ItemCost;
import com.fashionodyssey.model.design.Design;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SalesPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel model;
    private JLabel salesLabel;
    private double totalSales = 0.0;
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);
    
    public SalesPanel() {
        setLayout(new BorderLayout(10, 10)); // 增加間距
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加邊距
        setBackground(Color.WHITE);
        initComponents();
        updateInventory();
    }
    
    private void initComponents() {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 修改列名，移除利潤欄位
        String[] columnNames = {"商品", "數量", "成本", "原材料", "銷售價格", "操作"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // 只允許編輯銷售價格
            }
        };
        
        // 設置表格
        productTable = new JTable(model);
        
        // 設置表格的選擇模式
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 為表格添加滑鼠監聽器
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = productTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / productTable.getRowHeight();
                
                if (row < productTable.getRowCount() && row >= 0 && 
                    column < productTable.getColumnCount() && column >= 0) {
                    if (column == 5) { // 操作列
                        handleSellButtonClick(row);
                    }
                }
            }
        });
        
        // 設置表格樣式
        productTable.setRowHeight(30);
        productTable.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(PRIMARY_COLOR);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setSelectionBackground(BACKGROUND_COLOR);
        productTable.setSelectionForeground(Color.BLACK);
        productTable.setShowGrid(true);
        productTable.setGridColor(BORDER_COLOR);
        
        // 設置列寬
        productTable.getColumnModel().getColumn(0).setPreferredWidth(150); // 商品名稱
        productTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // 數量
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 成本
        productTable.getColumnModel().getColumn(3).setPreferredWidth(200); // 原材料
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // 銷售價格
        productTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // 操作
        
        // 設置銷售價格欄位的編輯器
        productTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                try {
                    String value = (String) getCellEditorValue();
                    if (!value.isEmpty()) {
                        Double.parseDouble(value);
                    }
                    return super.stopCellEditing();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "請輸入有效的數字！");
                    return false;
                }
            }
        });
        
        // 設置銷售價格���位的渲染器
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // 創建帶有陰影的滾動面板
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 10, 0),
            BorderFactory.createLineBorder(BORDER_COLOR, 1)
        ));
        
        // 控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        // 銷售額標籤
        salesLabel = new JLabel("總銷售額: $" + totalSales);
        salesLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        salesLabel.setForeground(PRIMARY_COLOR);
        
        // 更新按鈕
        JButton updateButton = createStyledButton("更新庫存");
        updateButton.addActionListener(e -> updateInventory());
        
        controlPanel.add(updateButton);
        controlPanel.add(salesLabel);
        
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BACKGROUND_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    // ButtonRenderer 類
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("微軟正黑體", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setBackground(PRIMARY_COLOR);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFocusPainted(false);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(BACKGROUND_COLOR);
            } else {
                setBackground(PRIMARY_COLOR);
            }
            setText("賣出");
            return this;
        }
    }
    
    // ButtonEditor 類
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("微軟正黑體", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(PRIMARY_COLOR);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            
            // 重要：設置點擊次數為1
            setClickCountToStart(1);
            
            // 添加按鈕點擊監聽器
            button.addActionListener(e -> {
                fireEditingStopped();
                isPushed = true;
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
            label = "賣出";
            button.setText(label);
            isPushed = false;
            
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // 獲取當前行的數據
                int row = productTable.getSelectedRow();
                String priceText = (String) productTable.getValueAt(row, 4);
                
                if (priceText == null || priceText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(SalesPanel.this, 
                        "請先輸入銷售價格！", 
                        "提示", 
                        JOptionPane.WARNING_MESSAGE);
                    return label;
                }
                
                try {
                    double salePrice = Double.parseDouble(priceText);
                    double cost = Double.parseDouble(productTable.getValueAt(row, 2).toString().replace("$", ""));
                    
                    // 執行銷售操作
                    String itemName = (String) productTable.getValueAt(row, 0);
                    sellItem(itemName, salePrice);
                    
                    System.out.println("執行銷售: " + itemName + ", 價格: " + salePrice);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SalesPanel.this, 
                        "請輸入有效的銷售價格！", 
                        "錯誤", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    private void updateInventory() {
        model.setRowCount(0);
        ResourceManager rm = ResourceManager.getInstance();
        DesignController designController = DesignController.getInstance();
        List<Design> designs = designController.getAvailableDesigns();
        
        for (Design design : designs) {
            String designId = design.getDesignId();
            String designName = design.getName();
            double cost = calculateDesignCost(design);
            int amount = rm.getResourceAmount(designId);
            String rawMaterials = formatRawMaterials(design.getRawMaterials());
            
            if (amount > 0) {
                model.addRow(new Object[]{
                    designName != null && !designName.isEmpty() ? designName : designId,
                    amount,
                    String.format("$%.2f", cost),
                    rawMaterials,
                    "", // 銷售價格
                    "賣出" // 按鈕文字
                });
            }
        }
        
        // 確保表格刷新
        productTable.repaint();
    }
    
    private String formatRawMaterials(Map<String, Integer> rawMaterials) {
        StringBuilder builder = new StringBuilder();
        rawMaterials.forEach((material, quantity) -> {
            if (quantity > 0 && !"無".equals(material)) {
                String displayName = getDisplayName(material);
                builder.append(displayName).append(" x").append(quantity).append(", ");
            }
        });
        return builder.length() > 0 ? builder.substring(0, builder.length() - 2) : "";
    }
    
    private String getDisplayName(String key) {
        // Convert resource ID to display name
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
    
    private double calculateDesignCost(Design design) {
        double baseCost = ItemCost.calculateCost(design.getBaseItem());
        double decorationsCost = design.getDecorations().entrySet().stream()
            .mapToDouble(entry -> ItemCost.calculateCost(entry.getKey()) * entry.getValue())
            .sum();
        return baseCost + decorationsCost;
    }
    
    private void sellItem(String itemName, double salePrice) {
        System.out.println("開始銷售商品: " + itemName);
        ResourceManager rm = ResourceManager.getInstance();
        DesignController designController = DesignController.getInstance();
        
        // 根據商品名稱找到對應的設計ID
        String designId = null;
        for (Design design : designController.getAvailableDesigns()) {
            if (itemName.equals(design.getName()) || itemName.equals(design.getDesignId())) {
                designId = design.getDesignId();
                System.out.println("找到對應設計ID: " + designId);
                break;
            }
        }
        
        if (designId == null) {
            System.out.println("錯誤：找不到商品ID");
            JOptionPane.showMessageDialog(this, 
                "找不到商品: " + itemName, 
                "錯誤", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int amount = rm.getResourceAmount(designId);
        System.out.println("當前庫存數量: " + amount);
        
        if (amount > 0) {
            rm.consumeResource(designId, 1);
            totalSales += salePrice;
            salesLabel.setText(String.format("總銷售額: $%.2f", totalSales));
            
            // 先更新金錢，再觸發事件
            rm.addMoney(salePrice);
            
            // 更新庫存
            updateInventory();
            
            JOptionPane.showMessageDialog(this, 
                String.format("成功賣出 1 個 %s！\n獲得金錢: $%.2f", 
                    itemName, salePrice), 
                "銷售成功", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("錯誤：庫存不足");
            JOptionPane.showMessageDialog(this, 
                "沒有可以賣出的 " + itemName + "！", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // 添加新方法處理賣出按鈕點擊
    private void handleSellButtonClick(int row) {
        String priceText = (String) productTable.getValueAt(row, 4);
        if (priceText == null || priceText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "請先輸入銷售價格！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double salePrice = Double.parseDouble(priceText);
            double cost = Double.parseDouble(productTable.getValueAt(row, 2)
                .toString().replace("$", ""));
            String itemName = (String) productTable.getValueAt(row, 0);
            
            System.out.println("嘗試銷售��品：" + itemName);
            System.out.println("銷售價格：" + salePrice);
            System.out.println("成本：" + cost);
            
            sellItem(itemName, salePrice);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "請輸入有效的銷售價格！", 
                "錯誤", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 