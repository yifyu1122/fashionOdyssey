package com.fashionodyssey.story.event;

public class ProcessEvent implements StoryEvent {
    @Override
    public void trigger() {
        // 實現加工事件邏輯
    }
    
    @Override
    public boolean isCompleted() {
        return false;
    }
} 