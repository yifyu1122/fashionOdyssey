package com.fashionodyssey.event;

import java.util.*;

public class EventManager {
    private static EventManager instance;
    private Map<String, List<GameEventListener>> listeners;
    
    private EventManager() {
        listeners = new HashMap<>();
    }
    
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
    
    public void addEventListener(String eventType, GameEventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }
    
    public void removeEventListener(String eventType, GameEventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }
    
    public void fireEvent(GameEvent event) {
        if (listeners.containsKey(event.getEventType())) {
            for (GameEventListener listener : listeners.get(event.getEventType())) {
                listener.onEvent(event);
            }
        }
    }
    
    public void dispatchEvent(String eventType, Object... args) {
        GameEvent event = new GameEvent(eventType, args);
        fireEvent(event);
    }
} 