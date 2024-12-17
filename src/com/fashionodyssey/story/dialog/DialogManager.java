package com.fashionodyssey.story.dialog;

import com.fashionodyssey.ui.DialogPanel;
import javax.swing.JFrame;

public class DialogManager {
    private static DialogManager instance;
    private DialogPanel dialogPanel;
    
    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
    
    private DialogManager() {}
    
    public void initialize(JFrame mainFrame) {
        dialogPanel = new DialogPanel(mainFrame);
    }
    
    public void showWelcomeDialog() {
        String welcomeMessage = 
            "親愛的設計師，歡迎來到時尚奧德賽 Fashion Odyssey！\n\n" +
            "在這裡，你將開始一段精彩的時尚設計之旅\n" +
            "你可以種植原料、設計服裝、經營商店\n" +
            "讓我們一起開始這段奇妙的冒險吧！\n\n" +
            "想做這個模擬器是因為我國小時的夢想是當個時尚設計師\n" +
            "雖然因為時間不足+別科段考要完只能寫成這樣\n" +
            "但若對此DEMO有任何建議，請email我，bug很多很正常！";
            
        dialogPanel.setCloseAction(e -> {
            System.out.println("開始遊戲！");
            // 這裡可以觸發遊戲開始的其他邏輯
        });
        dialogPanel.showDialog(welcomeMessage);
    }
}