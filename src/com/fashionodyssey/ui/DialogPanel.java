package com.fashionodyssey.ui;

import javax.swing.*;

public class DialogPanel extends JPanel {
    public DialogPanel() {
        super();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("對話框");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
        
        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        add(scrollPane);
        
        JButton closeButton = new JButton("關閉");
        closeButton.setAlignmentX(CENTER_ALIGNMENT);
        add(closeButton);
    }
    
} 