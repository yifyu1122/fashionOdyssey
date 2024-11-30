package com.fashionodyssey.util;

public enum ItemDescription {
    FERTILIZER("肥料", "神奇的肥料，讓作物長得比隔壁老王的還快！"),
    COTTON_SEED("棉花種子", "只要8元！比買一杯珍奶還便宜，還能賺錢！"),
    ROSE_SEED("玫瑰種子", "10元投資，讓你的農場充滿浪漫氣息~"),
    SUNFLOWER_SEED("向日葵種子", "8元買一個永遠朝向陽光的開朗小夥伴"),
    TULIP_SEED("鬱金香種子", "雖然要25元，但荷蘭人都說值得！"),
    LAVENDER_SEED("薰衣草種子", "15元讓你的農場香到連蜜蜂都想搬家來住"),
    
    COTTON("棉花", "軟綿綿的白色金子，3-5個一次收穫超划算！"),
    ROSE("玫瑰", "每次1-3朵，不用擔心送不出去"),
    SUNFLOWER("向日葵", "1-3個笑臉向著你，心情也變好了"),
    TULIP("鬱金香", "1-3株，荷蘭人看到都流口水"),
    LAVENDER("薰衣草", "1-3束，讓你的農場變成南法小鎮"),
    
    FABRIC("布料", "兩個棉花的完美結合，時尚就從這裡開始！"),
    RED_DYE("紅色染料", "用玫瑰做的，紅得像初戀的臉頰"),
    YELLOW_DYE("黃色染料", "向日葵的陽光色彩，穿上它連天氣都變好了"),
    PURPLE_DYE("紫色染料", "薰衣草的高貴紫，貴族都搶著要");
    
    private final String name;
    private final String description;
    
    ItemDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getFullDescription() {
        return name + " (" + description + ")";
    }
} 