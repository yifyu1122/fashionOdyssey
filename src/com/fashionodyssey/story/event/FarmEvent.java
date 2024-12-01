package com.fashionodyssey.story.event;

public class FarmEvent implements StoryEvent {
    @Override
    public void trigger() {
        // 實現農場事件邏輯
    }
    
    @Override
    public boolean isCompleted() {
        return false;
    }
} 