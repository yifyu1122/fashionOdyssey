package com.fashionodyssey.util;

public enum ItemDescription {
    FERTILIZER("肥料", "神奇的肥料，讓作物長得比隔壁老王的還快！"),
    COTTON_SEED("棉花種子", "只要8元！比買一杯珍奶還便宜，還能賺錢！"),
    ROSE_SEED("玫瑰種子", "10元投資，讓你的農場充滿浪漫氣息~"),
    SUNFLOWER_SEED("向日葵種子", "8元買一個永遠朝向陽光的開朗小夥伴"),
    LAVENDER_SEED("薰衣草種子", "15元讓你的農場香到連蜜蜂都想搬家來住"),
    TULIP_PINK_SEED("鬱金香(粉)種子", "25元讓你的農場充滿甜美粉色！"),

    COTTON("棉花", "白白胖胖的雲朵掉在地上！3-5個一次收穫超划算！摘下來做衣服剛剛好～"),
    ROSE("玫瑰", "每次1-3朵，紅得像害羞的臉蛋，送情人、送老師都超加分！"),
    SUNFLOWER("向日葵", "每次1-3朵，陽光寶寶的最愛！種一片向日葵，每天都是元氣滿滿！"),
    LAVENDER("薰衣草", "每次1-3株，種一片紫色的夢，連做夢都是薰衣草香的～"),
    TULIP_PINK("鬱金香(粉)", "每次1-3株，粉嫩到讓少女心噴發！連蝴蝶看了都想談戀愛～"),
    WHITE_FABRIC("布料", "棉花的華麗變身！時尚設計師的最愛，夢想就從這開始～"),
    RED_DYE("紅色染料", "玫瑰的告白！染出來的紅色像戀愛般甜蜜"),
    YELLOW_DYE("黃色染料", "向日葵的微笑配方，穿上它連烏雲都會躲著你跑"),
    PINK_DYE("粉色染料", "用粉紅鬱金香的少女心，染出讓人融化的甜美色彩"),
    PURPLE_DYE("紫色染料", "薰衣草的浪漫配方，貴族紫讓你走到哪都是焦點"),
    RED_FABRIC("紅色布料", "用玫瑰做的，紅得像初戀的臉頰"),
    YELLOW_FABRIC("黃色布料", "向日葵的陽光色彩，穿上它連天氣都變好了"),
    PINK_FABRIC("粉色布料", "粉色鬱金香的甜美色彩，穿上它像戀愛般幸福"),
    PURPLE_FABRIC("紫色布料", "薰衣草的高貴紫，貴族都搶著要"),  
    RED_RIBBON("紅色蝴蝶結", "用玫瑰做的，紅得像初戀的臉頰"),
    YELLOW_RIBBON("黃色蝴蝶結", "向日葵的陽光色彩，穿上它連天氣都變好了"),
    PINK_RIBBON("粉色蝴蝶結", "粉色鬱金香的甜美色彩，穿上它像戀愛般幸福"),
    PURPLE_RIBBON("紫色蝴蝶結", "薰衣草的高貴紫，貴族都搶著要"),
    WHITE_DRESS("連衣裙", "兩塊布料的魔法，穿上它立刻變身時尚女神！"),
    RED_DRESS("紅色連衣裙", "由2塊紅色布料製成的時尚單品"),
    YELLOW_DRESS("黃色連衣裙", "由2塊黃色布料製成的時尚單品"),
    PINK_DRESS("粉色連衣裙", "由2塊粉色布料製成的時尚單品"),
    PURPLE_DRESS("紫色連衣裙", "由2塊紫色布料製成的時尚單品"),
    WHITE_SHIRT("襯衫", "一塊布料的時尚密碼，從辦公室到約會都能完美駕馭"),
    RED_SHIRT("紅色襯衫", "由1塊紅色布料製成的百搭單品"),
    YELLOW_SHIRT("黃色襯衫", "由1塊黃色布料製成的百搭單品"),
    PINK_SHIRT("粉色襯衫", "由1塊粉色布料製成的百搭單品"),
    PURPLE_SHIRT("紫色襯衫", "由1塊紫色布料製成的百搭單品"),
    WHITE_PANTS("褲子", "舒適與時尚的完美結合，讓你走路都帶風～"),
    RED_PANTS("紅色褲子", "由1塊紅色布料製成的實用單品"),
    YELLOW_PANTS("黃色褲子", "由1塊黃色布料製成的實用單品"),
    PINK_PANTS("粉色褲子", "由1塊粉色布料製成的實用單品"),
    PURPLE_PANTS("紫色褲子", "由1塊紫色布料製成的實用單品");
    
    private final String name;
    private final String description;
    
    ItemDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getFullDescription() {
        return name + " (" + description + ")";
    }
} 