package com.fashionodyssey.story.dialog;

public class DialogManager {
    private static DialogManager instance;
    
    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
    
    private DialogManager() {}
} 