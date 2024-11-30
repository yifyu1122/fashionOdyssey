package com.fashionodyssey.story.event;

public class SalesEvent implements StoryEvent {
    @Override
    public void trigger() {
        // 實現銷售事件邏輯
    }
    
    @Override
    public boolean isCompleted() {
        return false;
    }
} 