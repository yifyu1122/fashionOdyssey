package com.fashionodyssey.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DialogPanel extends JPanel {    
    // 定義配色方案
    private static final Color PINK_THEME = new Color(255, 182, 193);    // 淺粉色
    private static final Color LIGHT_PINK = new Color(255, 218, 224);    // 更淺的粉色
    private static final Color SOFT_YELLOW = new Color(255, 245, 200);   // 柔和的黃色
    private static final Color MINT_GREEN = new Color(200, 255, 214);    // 薄荷綠
    private static final Color TEXT_COLOR = new Color(80, 80, 80);       // 深灰色文字
    
    // 定義字體常量
    private static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("微軟正黑體", Font.BOLD, 18);
    private static final Font CONTENT_FONT = new Font("微軟正黑體", Font.PLAIN, 16);
    
    private JTextArea messageArea;
    private JButton closeButton;
    private JDialog dialog;
    
    public DialogPanel(JFrame parent) {
        super();
        initComponents(parent);
    }
    
    private void initComponents(JFrame parent) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(LIGHT_PINK);
        
        // 創建標題面板
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(LIGHT_PINK);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 分開創建 emoji 和文字標籤
        JLabel emojiLabel1 = new JLabel("✨");
        emojiLabel1.setFont(EMOJI_FONT);
        
        JLabel textLabel = new JLabel(" 歡迎來到時尚奧德賽 Fashion Odyssey ");
        textLabel.setFont(TITLE_FONT);
        textLabel.setForeground(TEXT_COLOR);
        
        JLabel emojiLabel2 = new JLabel("✨");
        emojiLabel2.setFont(EMOJI_FONT);
        
        titlePanel.add(emojiLabel1);
        titlePanel.add(textLabel);
        titlePanel.add(emojiLabel2);
        titlePanel.setAlignmentX(CENTER_ALIGNMENT);
        add(titlePanel);
        
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 美化消息區域
        messageArea = new JTextArea(5, 30);
        messageArea.setFont(CONTENT_FONT);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(new Color(255, 250, 245));
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 創建帶圓角的滾動面板
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, PINK_THEME),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        add(scrollPane);
        
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 美化按鈕
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(LIGHT_PINK);
        
        closeButton = new JButton();
        closeButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        // 添加左側星星
        JLabel leftStar = new JLabel("✨");
        leftStar.setFont(EMOJI_FONT);
        
        // 添加文字
        JLabel buttonText = new JLabel("開始遊戲");
        buttonText.setFont(CONTENT_FONT);
        buttonText.setForeground(TEXT_COLOR);
        
        // 添加右側星星
        JLabel rightStar = new JLabel("✨");
        rightStar.setFont(EMOJI_FONT);
        
        closeButton.add(leftStar);
        closeButton.add(buttonText);
        closeButton.add(rightStar);
        
        closeButton.setBackground(SOFT_YELLOW);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, PINK_THEME),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        closeButton.setAlignmentX(CENTER_ALIGNMENT);
        closeButton.setMaximumSize(new Dimension(200, 50));
        
        // 添加按鈕懸停效果
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(MINT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(SOFT_YELLOW);
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        add(closeButton);
        
        // 美化對話框
        dialog = new JDialog(parent, "✨ 歡迎 ✨", true);
        dialog.setContentPane(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
    }
    
    public void showDialog(String message) {
        messageArea.setText(message);
        dialog.setVisible(true);
    }
    
    public void setCloseAction(ActionListener action) {
        // 移除所有現有的 ActionListener
        for (ActionListener al : closeButton.getActionListeners()) {
            closeButton.removeActionListener(al);
        }
        // 添加新的 ActionListener
        closeButton.addActionListener(e -> {
            dialog.dispose();  // 首先關閉對話框
            action.actionPerformed(e);  // 然後執行回調
        });
    }
} 