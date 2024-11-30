package com.fashionodyssey.story.cutscene;

public class CutsceneManager {
    private static CutsceneManager instance;
    
    public static CutsceneManager getInstance() {
        if (instance == null) {
            instance = new CutsceneManager();
        }
        return instance;
    }
    
    private CutsceneManager() {}
} 