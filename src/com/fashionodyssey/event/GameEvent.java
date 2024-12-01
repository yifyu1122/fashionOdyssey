package com.fashionodyssey.event;

public class GameEvent {
    private String eventType;
    private Object[] args;
    
    public GameEvent(String eventType, Object... args) {
        this.eventType = eventType;
        this.args = args;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getArg(int index) {
        if (index >= 0 && index < args.length) {
            return (T) args[index];
        }
        return null;
    }
    
    public Object getData() {
        return args.length > 0 ? args[0] : null;
    }
} 