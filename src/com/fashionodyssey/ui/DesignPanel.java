package com.fashionodyssey.ui;

import java.awt.*;
import javax.swing.*;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.event.GameEvent;
import com.fashionodyssey.model.design.Clothing;

public class DesignPanel extends JPanel {
    private JComboBox<String> styleSelector;
    private JComboBox<String> colorSelector;
    private JSlider qualitySlider;
    private JButton designButton;
    private JButton saveButton;
    private JPanel previewPanel;
    
    public DesignPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // 設計控制面板
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        String[] styles = {"連衣裙", "上衣", "褲子"};
        String[] colors = {"紅色", "藍色", "綠色", "黃色"};
        
        styleSelector = new JComboBox<>(styles);
        colorSelector = new JComboBox<>(colors);
        qualitySlider = new JSlider(1, 10, 5);
        qualitySlider.setMajorTickSpacing(1);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setPaintLabels(true);
        
        designButton = new JButton("設計");
        saveButton = new JButton("保存");
        
        controlPanel.add(new JLabel("款式:"));
        controlPanel.add(styleSelector);
        controlPanel.add(new JLabel("顏色:"));
        controlPanel.add(colorSelector);
        controlPanel.add(new JLabel("品質:"));
        controlPanel.add(qualitySlider);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(designButton);
        buttonPanel.add(saveButton);
        controlPanel.add(buttonPanel);
        
        // 預覽面板
        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawClothingPreview(g);
            }
        };
        previewPanel.setPreferredSize(new Dimension(400, 400));
        
        // 添加事件監聽
        designButton.addActionListener(e -> {
            String style = (String) styleSelector.getSelectedItem();
            String color = (String) colorSelector.getSelectedItem();
            int quality = qualitySlider.getValue();
            
            Clothing clothing = new Clothing(style, color, quality);
            EventManager.getInstance().fireEvent(
                new GameEvent("DESIGN_CLOTHING", clothing)
            );
            
            repaint();
        });
        
        saveButton.addActionListener(e -> {
            EventManager.getInstance().fireEvent(
                new GameEvent("SAVE_DESIGN", null)
            );
        });
        
        add(controlPanel, BorderLayout.NORTH);
        add(previewPanel, BorderLayout.CENTER);
    }
    
    private void drawClothingPreview(Graphics g) {
        String style = (String) styleSelector.getSelectedItem();
        String color = (String) colorSelector.getSelectedItem();
        
        // 設置顏色
        Color drawColor = getColorFromString(color);
        g.setColor(drawColor);
        
        // 根據不同款式繪製不同形狀
        int centerX = previewPanel.getWidth() / 2;
        int centerY = previewPanel.getHeight() / 2;
        
        switch (style) {
            case "連衣裙":
                drawDress(g, centerX, centerY);
                break;
            case "上衣":
                drawShirt(g, centerX, centerY);
                break;
            case "褲子":
                drawPants(g, centerX, centerY);
                break;
        }
    }
    
    private Color getColorFromString(String colorName) {
        switch (colorName) {
            case "紅色": return Color.RED;
            case "藍色": return Color.BLUE;
            case "綠色": return Color.GREEN;
            case "黃色": return Color.YELLOW;
            default: return Color.BLACK;
        }
    }
    
    private void drawDress(Graphics g, int x, int y) {
        // 簡單的連衣裙形狀
        int[] xPoints = {x-50, x+50, x+40, x-40};
        int[] yPoints = {y-50, y-50, y+50, y+50};
        g.fillPolygon(xPoints, yPoints, 4);
    }
    
    private void drawShirt(Graphics g, int x, int y) {
        // 簡單的上衣形狀
        g.fillRect(x-40, y-30, 80, 60);
    }
    
    private void drawPants(Graphics g, int x, int y) {
        // 簡單的褲子形狀
        g.fillRect(x-40, y-20, 30, 70);
        g.fillRect(x+10, y-20, 30, 70);
    }
} 