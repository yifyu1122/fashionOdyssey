package com.fashionodyssey.ui;

import com.fashionodyssey.event.EventManager;
import com.fashionodyssey.util.ResourceManager;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class TaskPanel extends JPanel {
    private JPanel taskPanel; // Panel to hold tasks in a rectangular layout
    private Set<String> completedTasks; // Set to track completed tasks
    private int rewardAmount = 50; // Amount of money rewarded for completing a task

    public TaskPanel() {
        // Register event listener for task completion first
        EventManager.getInstance().addEventListener("TASK_COMPLETE", event -> {
            String completedTask = (String) event.getData();
            System.out.println("收到任務完成事件: " + completedTask); // 調試輸出
            completeTask(completedTask);
        });

        setLayout(new BorderLayout());
        setBackground(new Color(255, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize completed tasks set
        completedTasks = new HashSet<>();

        // Create a panel for tasks with a GridLayout
        taskPanel = new JPanel(new GridLayout(0, 2)); // 2 columns: task and button
        taskPanel.setBackground(new Color(255, 240, 245));
        taskPanel.setBorder(BorderFactory.createTitledBorder("任務列表"));

        // Load tasks
        loadTasks();

        // Create a scroll pane for the task panel
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTasks() {
        // Clear existing tasks
        taskPanel.removeAll(); // Clear the task panel

        // Add specific tasks
        String[] tasks = {
            "收穫一個棉花", // Harvest a cotton
            "收穫一個薰衣草", // Harvest a lavender
            "購買肥料" // Buy fertilizer
        };

        for (String task : tasks) {
            JLabel taskLabel = new JLabel(task);
            taskLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            taskPanel.add(taskLabel); // Add task labels to the task panel

            // Create a button for claiming rewards
            JButton claimRewardButton = new JButton("領取獎勵");
            claimRewardButton.setEnabled(false); // Initially disabled
            claimRewardButton.addActionListener(e -> claimReward(task, claimRewardButton));
            taskPanel.add(claimRewardButton); // Add button next to the task
        }

        taskPanel.revalidate(); // Refresh the task panel
        taskPanel.repaint(); // Repaint the task panel
    }

    private void claimReward(String task, JButton button) {
        if (!completedTasks.contains(task)) {
            completedTasks.add(task); // Mark the task as completed
            button.setEnabled(false); // Disable the button after claiming
            ResourceManager.getInstance().addMoney(rewardAmount); // Reward the user
            JOptionPane.showMessageDialog(this, "你獲得了 $" + rewardAmount + "！", "獎勵", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "此任務已完成！", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Update the completeTask method to handle different tasks
    private void completeTask(String task) {
        if (!completedTasks.contains(task)) {
            // Find the corresponding task label and button
            Component[] components = taskPanel.getComponents();
            for (int i = 0; i < components.length; i += 2) { // Increment by 2 since we have label-button pairs
                if (components[i] instanceof JLabel) {
                    JLabel label = (JLabel) components[i];
                    if (label.getText().equals(task)) {
                        // Enable the corresponding button
                        if (components[i + 1] instanceof JButton) {
                            JButton button = (JButton) components[i + 1];
                            button.setEnabled(true);
                        }
                        break;
                    }
                }
            }
        }
    }
}
