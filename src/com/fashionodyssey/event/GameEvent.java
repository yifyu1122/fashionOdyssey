package com.fashionodyssey.event;

public class GameEvent {
    private String eventType;
    private Object[] args;
    
    public GameEvent(String eventType, Object... args) {
        this.eventType = eventType;
        this.args = args != null ? args : new Object[0];
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
    
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        if (args != null && args.length > 0) {
            return (T) args[0];
        }
        return null;
    }
} 