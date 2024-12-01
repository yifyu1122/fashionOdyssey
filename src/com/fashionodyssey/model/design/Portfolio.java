package com.fashionodyssey.model.design;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private List<Clothing> designs;
    
    public Portfolio() {
        designs = new ArrayList<>();
    }
    
    public void addDesign(Clothing clothing) {
        designs.add(clothing);
    }
    
    public List<Clothing> getDesigns() {
        return new ArrayList<>(designs);
    }
} 