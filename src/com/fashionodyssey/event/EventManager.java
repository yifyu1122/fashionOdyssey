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
        System.out.println("\n===== 事件管理器：添加監聽器 =====");
        System.out.println("事件類型: " + eventType);
        
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        
        System.out.println("當前監聽器數量: " + listeners.get(eventType).size());
        System.out.println("==============================\n");
    }
    
    public void removeEventListener(String eventType, GameEventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }
    
    public void fireEvent(GameEvent event) {
        System.out.println("\n===== 事件管理器：觸發事件 =====");
        System.out.println("事件類型: " + event.getEventType());
        
        if (listeners.containsKey(event.getEventType())) {
            System.out.println("找到監聽器數量: " + listeners.get(event.getEventType()).size());
            for (GameEventListener listener : listeners.get(event.getEventType())) {
                System.out.println("正在通知監聽器");
                listener.onEvent(event);
            }
        } else {
            System.out.println("沒有找到相關事件的監聽器");
        }
        System.out.println("============================\n");
    }
    
    public void dispatchEvent(String eventType, Object... args) {
        GameEvent event = new GameEvent(eventType, args);
        fireEvent(event);
    }
} 