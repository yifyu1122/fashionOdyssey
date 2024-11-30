package com.fashionodyssey.story.event;

public interface StoryEvent {
    void trigger();
    boolean isCompleted();
} 