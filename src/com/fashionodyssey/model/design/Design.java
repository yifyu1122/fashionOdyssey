package com.fashionodyssey.model.design;

import java.util.HashMap;
import java.util.Map;

public class Design {
    private String name;
    private String baseItem;
    private Map<String, Integer> decorations;
    private Map<String, Integer> rawMaterials;
    private String designId;
    
    public Design(String name, String baseItem, Map<String, Integer> decorations, Map<String, Integer> rawMaterials, String designId) {
        this.name = name;
        this.baseItem = baseItem;
        this.decorations = new HashMap<>(decorations);
        this.rawMaterials = rawMaterials;
        this.designId = designId;
    }
    
    // Getters
    public String getName() { return name; }
    public String getBaseItem() { return baseItem; }
    public Map<String, Integer> getDecorations() { return new HashMap<>(decorations); }
    public Map<String, Integer> getRawMaterials() { return rawMaterials; }
    public String getDesignId() { return designId; }
} 