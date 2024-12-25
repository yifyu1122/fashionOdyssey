package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ItemDescription;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;



public class InventoryPanel extends JPanel {
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private ResourceManager resourceManager;
    private boolean isLongPress = false;
    private Map<String, String> customDesignNames = new HashMap<>();
    
    // Define color scheme similar to MainFrame
    private static final Color PINK_THEME = new Color(255, 182, 193);
    private static final Color LIGHT_PINK = new Color(255, 218, 224);
    private static final Color SOFT_YELLOW = new Color(255, 245, 200);
    
    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(LIGHT_PINK);
        resourceManager = ResourceManager.getInstance();
        
        // 初始化物品描述
        ItemDescription.initializeDescriptions();
        
        // 創建網格面板
        gridPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridPanel.setBackground(LIGHT_PINK);
        
        // 只保留標題邊框
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PINK_THEME),
            "庫存",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("微軟正黑體", Font.BOLD, 16)
        ));
        
        // 4. 設置滾動面板
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // 隱藏垂直滾動條

        // 添加滑動事件監聽
        gridPanel.addMouseListener(new MouseAdapter() {
            private Timer timer;

            @Override
            public void mousePressed(MouseEvent e) {
                timer = new Timer(500, event -> {
                    isLongPress = true;
                    gridPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                });
                timer.setRepeats(false);
                timer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (timer != null) {
                    timer.stop();
                }
                if (isLongPress) {
                    gridPanel.setCursor(Cursor.getDefaultCursor());
                    isLongPress = false;
                }
            }
        });

        gridPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isLongPress) {
                    JViewport viewport = scrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(0, -e.getY());
                    gridPanel.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        

        // 註冊事件監聽
        EventManager.getInstance().addEventListener("UPDATE_RESOURCES", event -> {
            if (event.getData() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> data = (Map<String, String>) event.getData();
                String designName = data.get("designName");
                String baseItem = data.get("baseItem");
                if (designName != null && baseItem != null) {
                    // 儲存自訂名稱
                    customDesignNames.put(baseItem.toLowerCase(), designName);
                }
            }
            updateResources();
        });

        updateResources();
    }
    
    public void updateResources() {
        gridPanel.removeAll();
        ResourceManager rm = ResourceManager.getInstance();
        
        // 收集物品及其數量
        List<ItemEntry> items = new ArrayList<>();
        items.add(new ItemEntry(ItemDescription.FERTILIZER, "fertilizer"));
        items.add(new ItemEntry(ItemDescription.COTTON_SEED, "cotton_seeds"));
        items.add(new ItemEntry(ItemDescription.ROSE_SEED, "rose_seeds"));
        items.add(new ItemEntry(ItemDescription.SUNFLOWER_SEED, "sunflower_seeds"));
        items.add(new ItemEntry(ItemDescription.LAVENDER_SEED, "lavender_seeds"));
        items.add(new ItemEntry(ItemDescription.TULIP_PINK_SEED, "tulip_pink_seeds"));
        items.add(new ItemEntry(ItemDescription.COTTON, "harvested_cotton"));
        items.add(new ItemEntry(ItemDescription.ROSE, "harvested_rose"));
        items.add(new ItemEntry(ItemDescription.SUNFLOWER, "harvested_sunflower"));
        items.add(new ItemEntry(ItemDescription.LAVENDER, "harvested_lavender"));
        items.add(new ItemEntry(ItemDescription.TULIP_PINK, "harvested_tulip_pink"));
        items.add(new ItemEntry(ItemDescription.RED_DYE, "red_dye"));
        items.add(new ItemEntry(ItemDescription.YELLOW_DYE, "yellow_dye"));
        items.add(new ItemEntry(ItemDescription.PURPLE_DYE, "purple_dye"));
        items.add(new ItemEntry(ItemDescription.PINK_DYE, "pink_dye"));
        items.add(new ItemEntry(ItemDescription.WHITE_FABRIC, "white_fabric"));
        items.add(new ItemEntry(ItemDescription.RED_FABRIC, "red_fabric"));
        items.add(new ItemEntry(ItemDescription.YELLOW_FABRIC, "yellow_fabric"));
        items.add(new ItemEntry(ItemDescription.PURPLE_FABRIC, "purple_fabric"));
        items.add(new ItemEntry(ItemDescription.PINK_FABRIC, "pink_fabric"));
        items.add(new ItemEntry(ItemDescription.WHITE_LACE, "white_lace"));
        items.add(new ItemEntry(ItemDescription.RED_LACE, "red_lace"));
        items.add(new ItemEntry(ItemDescription.YELLOW_LACE, "yellow_lace"));
        items.add(new ItemEntry(ItemDescription.PURPLE_LACE, "purple_lace"));
        items.add(new ItemEntry(ItemDescription.PINK_LACE, "pink_lace"));
        items.add(new ItemEntry(ItemDescription.WHITE_BOW, "white_bow"));
        items.add(new ItemEntry(ItemDescription.RED_BOW, "red_bow"));
        items.add(new ItemEntry(ItemDescription.YELLOW_BOW, "yellow_bow"));
        items.add(new ItemEntry(ItemDescription.PURPLE_BOW, "purple_bow"));
        items.add(new ItemEntry(ItemDescription.PINK_BOW, "pink_bow"));
        items.add(new ItemEntry(ItemDescription.WHITE_RIBBON, "white_ribbon"));
        items.add(new ItemEntry(ItemDescription.RED_RIBBON, "red_ribbon"));
        items.add(new ItemEntry(ItemDescription.YELLOW_RIBBON, "yellow_ribbon"));
        items.add(new ItemEntry(ItemDescription.PURPLE_RIBBON, "purple_ribbon"));
        items.add(new ItemEntry(ItemDescription.PINK_RIBBON, "pink_ribbon"));
        items.add(new ItemEntry(ItemDescription.WHITE_DRESS, "white_dress"));
        items.add(new ItemEntry(ItemDescription.RED_DRESS, "red_dress"));
        items.add(new ItemEntry(ItemDescription.YELLOW_DRESS, "yellow_dress"));
        items.add(new ItemEntry(ItemDescription.PURPLE_DRESS, "purple_dress"));
        items.add(new ItemEntry(ItemDescription.PINK_DRESS, "pink_dress"));
        items.add(new ItemEntry(ItemDescription.WHITE_SHIRT, "white_shirt"));
        items.add(new ItemEntry(ItemDescription.RED_SHIRT, "red_shirt"));
        items.add(new ItemEntry(ItemDescription.YELLOW_SHIRT, "yellow_shirt"));
        items.add(new ItemEntry(ItemDescription.PURPLE_SHIRT, "purple_shirt"));
        items.add(new ItemEntry(ItemDescription.PINK_SHIRT, "pink_shirt"));
        items.add(new ItemEntry(ItemDescription.WHITE_PANTS, "white_pants"));
        items.add(new ItemEntry(ItemDescription.RED_PANTS, "red_pants"));
        items.add(new ItemEntry(ItemDescription.YELLOW_PANTS, "yellow_pants"));
        items.add(new ItemEntry(ItemDescription.PURPLE_PANTS, "purple_pants"));
        items.add(new ItemEntry(ItemDescription.PINK_PANTS, "pink_pants"));

        // 按數量降序排序
        items.sort((entry1, entry2) -> Integer.compare(rm.getResourceAmount(entry2.resourceKey), rm.getResourceAmount(entry1.resourceKey)));

        // 按排序順序創建按鈕
        for (ItemEntry entry : items) {
            createItemButton(gridPanel, entry.itemDescription, entry.resourceKey);
        }

        System.out.println("物品初始化完成。總按鈕數量: " + gridPanel.getComponentCount());
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private void createItemButton(JPanel parent, ItemDescription item, String resourceKey) {
        JButton button = new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80,90);
            }
        };
        
        button.setBackground(SOFT_YELLOW);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, PINK_THEME),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel countLabel = new JLabel("0");
        countLabel.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBackground(new Color(0, 0, 0, 180));
        countLabel.setOpaque(true);
        countLabel.setBounds(0, 0, 20, 20);
        
        button.setLayout(null);
        button.add(countLabel);
        
        // 設置圖標
        ImageIcon icon = resourceManager.getItemIcon(resourceKey);
        if (icon != null) {
            button.setIcon(icon);
            button.setText(""); // 隱藏文字
            button.setHorizontalTextPosition(SwingConstants.CENTER); // 設置文本位置
            button.setVerticalTextPosition(SwingConstants.BOTTOM); // 設置文本位置為底部
        } else {
            button.setText(item.getName()); // 顯示物品名稱
        }
        
        button.setToolTipText("<html>" + item.getName() + "<br>" + 
                             item.getFullDescription().replace("\n", "<br>") + "</html>");
        
        // Add click event
        button.addActionListener(e -> {
            // Create a dialog to display item information
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "物品資訊", true);
            dialog.setLayout(new BorderLayout(10, 5));
            
            // Create title panel
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            JLabel titleLabel = new JLabel(item.getName());
            titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
            titlePanel.add(titleLabel);
            
            JPanel descPanel = new JPanel(new BorderLayout(10, 10));
            descPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
            
            int amount = resourceManager.getResourceAmount(resourceKey);
            String description = item.getAcquiredDescription() + "\n\n" + item.getDescription() + "\n\n目前擁有：" + amount + " 個";
            
            JTextArea descTextArea = new JTextArea(description);
            descTextArea.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
            descTextArea.setLineWrap(true);
            descTextArea.setWrapStyleWord(true);
            descTextArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(descTextArea);
            scrollPane.setPreferredSize(new Dimension(300, 150));
            
            descPanel.add(scrollPane, BorderLayout.CENTER);
            
            JButton closeButton = new JButton("關閉");
            closeButton.addActionListener(event -> dialog.dispose());
            
            dialog.add(titlePanel, BorderLayout.NORTH);
            dialog.add(descPanel, BorderLayout.CENTER);
            dialog.add(closeButton, BorderLayout.SOUTH);
            
            dialog.setSize(350, 250);
            dialog.setLocationRelativeTo(button);
            dialog.setVisible(true);
        });
        
        // Update button state
        updateItemButton(button, countLabel, item, resourceKey);
        
        parent.add(button);
    }
    
    private void updateItemButton(JButton button, JLabel countLabel, ItemDescription item, String resourceKey) {
        int amount = resourceManager.getResourceAmount(resourceKey);
        
        countLabel.setText(String.valueOf(amount));
        countLabel.setVisible(amount > 0);
        button.setEnabled(true);  
        button.setBackground(amount > 0 ? SOFT_YELLOW : Color.LIGHT_GRAY);  
        
        if (item != null) {
            button.setText(item.getName());
        }
    }

    private static class ItemEntry {
        ItemDescription itemDescription;
        String resourceKey;

        ItemEntry(ItemDescription itemDescription, String resourceKey) {
            this.itemDescription = itemDescription;
            this.resourceKey = resourceKey;
        }
    }
} 