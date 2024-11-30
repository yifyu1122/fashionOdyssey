package com.fashionodyssey.util;  

public class GameState {
    private static GameState instance;
    private boolean isGameStarted;
    private String currentModule; // 當前所在模組
    private boolean isTutorialCompleted;
    
    private GameState() {
        isGameStarted = false;
        currentModule = "main";
        isTutorialCompleted = false;
    }
    
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    
    public void startGame() {
        isGameStarted = true;
    }
    
    public void setCurrentModule(String module) {
        this.currentModule = module;
    }
    
    public void completeTutorial() {
        this.isTutorialCompleted = true;
    }
    
    // Getters
    public boolean isGameStarted() {
        return isGameStarted;
    }
    
    public String getCurrentModule() {
        return currentModule;
    }
    
    public boolean isTutorialCompleted() {
        return isTutorialCompleted;
    }
} 