package com.fashionodyssey.story;     

public class StoryManager {
    private static StoryManager instance;
    
    public static StoryManager getInstance() {
        if (instance == null) {
            instance = new StoryManager();
        }
        return instance;
    }
    
    private StoryManager() {}
} 