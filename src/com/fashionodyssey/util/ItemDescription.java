package com.fashionodyssey.util;

public enum ItemDescription {
    FERTILIZER("肥料"),
    COTTON_SEED("棉花種子"),
    ROSE_SEED("玫瑰種子"),
    SUNFLOWER_SEED("向日葵種子"),
    LAVENDER_SEED("薰衣草種子"),
    TULIP_PINK_SEED("鬱金香(粉)種子"),

    COTTON("棉花"),
    ROSE("玫瑰"),
    SUNFLOWER("向日葵"),
    LAVENDER("薰衣草"),
    TULIP_PINK("鬱金香(粉)"),
    WHITE_FABRIC("白色布料"),
    RED_DYE("紅色染料"),
    YELLOW_DYE("黃色染料"),
    PINK_DYE("粉色染料"),
    PURPLE_DYE("紫色染料"),
    RED_FABRIC("紅色布料"),
    YELLOW_FABRIC("黃色布料"),
    PINK_FABRIC("粉色布料"),
    PURPLE_FABRIC("紫色布料"),
    WHITE_BOW("白色蝴蝶結"),
    RED_BOW("紅色蝴蝶結"),
    YELLOW_BOW("黃色蝴蝶結"),
    PINK_BOW("粉色蝴蝶結"),
    PURPLE_BOW("紫色蝴蝶結"),
    WHITE_RIBBON("白色緞帶"),
    RED_RIBBON("紅色緞帶"),
    YELLOW_RIBBON("黃色緞帶"),
    PINK_RIBBON("粉色緞帶"),
    PURPLE_RIBBON("紫色緞帶"),
    WHITE_LACE("白色蕾絲"),
    RED_LACE("紅色蕾絲"),
    YELLOW_LACE("黃色蕾絲"),
    PINK_LACE("粉色蕾絲"),
    PURPLE_LACE("紫色蕾絲"),
    WHITE_DRESS("白色連衣裙"),
    RED_DRESS("紅色連衣裙"),
    YELLOW_DRESS("黃色連衣裙"),
    PINK_DRESS("粉色連衣裙"),
    PURPLE_DRESS("紫色連衣裙"),
    WHITE_SHIRT("白色襯衫"),
    RED_SHIRT("紅色襯衫"),
    YELLOW_SHIRT("黃色襯衫"),
    PINK_SHIRT("粉色襯衫"),
    PURPLE_SHIRT("紫色襯衫"),
    WHITE_PANTS("白色褲子"),
    RED_PANTS("紅色褲子"),
    YELLOW_PANTS("黃色褲子"),
    PINK_PANTS("粉色褲子"),
    PURPLE_PANTS("紫色褲子");
    
    private final String name;
    private String description;
    private String acquiredDescription;
    
    ItemDescription(String name) {
        this.name = name;
    }

    public static void initializeDescriptions() {
        for (ItemDescription item : values()) {
            item.description = getDefaultDescription(item);
            item.acquiredDescription = item.getAcquiredDescription();
        }
    }

    private static String getDefaultDescription(ItemDescription item) {
        return switch (item) {
            case FERTILIZER -> "神奇的肥料，讓作物長得比隔壁老王的還快！";
            case COTTON_SEED -> "便宜又實惠的致富良方！種下一顆種子，收穫滿滿的希望和夢想～";
            case ROSE_SEED -> "愛情與浪漫的種子，每一朵玫瑰都是一個等待綻放的愛情故事～";
            case SUNFLOWER_SEED -> "種下一顆陽光，收穫整片燦爛！向日葵的樂觀精神，讓你的農場充滿正能量！";
            case LAVENDER_SEED -> "紫色夢幻的芳香使者，讓你的農場成為普羅旺斯的浪漫縮影～";
            case TULIP_PINK_SEED -> "粉嫩夢幻的少女心，每一朵鬱金香都是一個綻放的甜蜜童話！";
            case COTTON -> "白白胖胖的雲朵掉在地上！軟綿綿的白色精靈，織出夢想的布料摘下來做衣服剛剛好～";
            case ROSE -> "紅色的愛情使者，染出心動的顏色，紅得像害羞的臉蛋，送情人、送老師都超加分！";
            case SUNFLOWER -> "永遠追逐陽光的開朗寶貝，陽光寶寶的最愛！種一片向日葵，每天都是元氣滿滿！";
            case LAVENDER -> "紫色的香氣精靈，讓整個農場都香香的，種一片紫色的夢，連做夢都是薰衣草香的～";
            case TULIP_PINK -> "優雅的粉色精靈，染出甜美的顏色，粉嫩到讓少女心噴發！連蝴蝶看了都想談戀愛～";
            case WHITE_FABRIC -> "純淨如雪的布料，是時尚夢想的起點";
            case RED_DYE -> "來自玫瑰花瓣的魔法染料，能染出熱情似火的色彩";
            case YELLOW_DYE -> "捕捉陽光的染料，散發著向日葵般的暖意";
            case PINK_DYE -> "粉色花瓣萃取的精華，能染出最甜美的粉色";
            case PURPLE_DYE -> "神秘的薰衣草染料，帶來高貴優雅的氣息";
            case RED_FABRIC -> "如晚霞般燦爛的紅布，觸感柔軟似絲綢";
            case YELLOW_FABRIC -> "金燦燦的布料，彷彿編織了一片陽光";
            case PINK_FABRIC -> "柔和的粉色布料，散發著春天的氣息";
            case PURPLE_FABRIC -> "深邃優雅的紫色布料，展現非凡品味";
            case WHITE_BOW -> "純白蝴蝶結，像雪花般輕盈飄逸";
            case RED_BOW -> "熱情似火的蝴蝶結，為造型增添活力";
            case YELLOW_BOW -> "充滿朝氣的蝴蝶結，點亮每個造型";
            case PINK_BOW -> "甜美可愛的蝴蝶結，少女心噴發";
            case PURPLE_BOW -> "典雅的紫色蝴蝶結，完美襯托高貴氣質";
            case WHITE_RIBBON -> "純白緞帶，為造型增添清新優雅";
            case RED_RIBBON -> "如火焰般躍動的緞帶，展現獨特魅力";
            case YELLOW_RIBBON -> "金黃色緞帶，綻放耀眼光芒";
            case PINK_RIBBON -> "浪漫粉色緞帶，編織甜蜜夢想";
            case PURPLE_RIBBON -> "神秘優雅的紫色緞帶，展現獨特品味";
            case WHITE_LACE -> "精緻的白色蕾絲，彷彿雲朵般輕盈";
            case RED_LACE -> "熱情奔放的紅色蕾絲，散發迷人魅力";
            case YELLOW_LACE -> "明亮活潑的黃色蕾絲，為設計增添趣味";
            case PINK_LACE -> "夢幻般的粉色蕾絲，營造浪漫氛圍";
            case PURPLE_LACE -> "高貴典雅的紫色蕾絲，展現精緻工藝";
            case WHITE_DRESS -> "純淨優雅的白色連衣裙，彷彿童話中的公主，穿上它讓你成為最耀眼的新娘～";
            case RED_DRESS -> "熱情似火的紅色連衣裙，散發著迷人魅力，穿上它讓你成為派對女王！";
            case YELLOW_DRESS -> "明亮活潑的黃色連衣裙，像向日葵一樣燦爛，穿上它讓你成為夏日精靈～";
            case PINK_DRESS -> "甜美浪漫的粉色連衣裙，彷彿櫻花般夢幻，穿上它讓你成為戀愛系女孩！";
            case PURPLE_DRESS -> "高貴典雅的紫色連衣裙，散發著神秘魅力，穿上它讓你成為優雅名媛～";
            case WHITE_SHIRT -> "簡約優雅的白色襯衫，從辦公室到約會都能完美駕馭，時尚必備單品！";
            case RED_SHIRT -> "熱情奔放的紅色襯衫，穿上它讓你成為全場的焦點，展現獨特魅力～";
            case YELLOW_SHIRT -> "充滿活力的黃色襯衫，像陽光一樣溫暖，為造型增添亮眼色彩！";
            case PINK_SHIRT -> "甜美可愛的粉色襯衫，散發著浪漫氣息，穿上它讓你少女感滿分！";
            case PURPLE_SHIRT -> "高貴優雅的紫色襯衫，展現獨特的時尚品味，連貴族看了都讚嘆～";
            case WHITE_PANTS -> "簡約時尚的白色長褲，百搭又實用，穿上它讓你的腿更顯修長！";
            case RED_PANTS -> "熱情奔放的紅色長褲，穿上它讓你成為街頭最耀眼的風景～";
            case YELLOW_PANTS -> "充滿活力的黃色長褲，像陽光一樣溫暖，走到哪裡都是快樂的焦點！";
            case PINK_PANTS -> "甜美可愛的粉色長褲，散發著浪漫氣息，穿上它讓你少女感爆發！";
            case PURPLE_PANTS -> "高貴優雅的紫色長褲，展現獨特的時尚品味，連貴族看了都讚嘆～";
        };
    }

    public String getAcquiredDescription() {
        return switch (this) {
            case FERTILIZER -> "獲取方法：商店購買，20元/5個";
            case COTTON_SEED -> "獲取方法：商店購買，8元/10個";
            case ROSE_SEED -> "獲取方法：商店購買，10元/10個";
            case SUNFLOWER_SEED -> "獲取方法：商店購買，8元/10個";
            case LAVENDER_SEED -> "獲取方法：商店購買，15元/10個";
            case TULIP_PINK_SEED -> "獲取方法：商店購買，25元/10個";
            case COTTON -> "獲取方法：於農場種植棉花種子取得，一次收穫可獲得3-5個";
            case ROSE -> "獲取方法：於農場種植玫瑰種子取得，一次收穫可獲得1-3朵";
            case SUNFLOWER -> "獲取方法：於農場種植向日葵種子取得，一次收穫可獲得1-3朵";
            case LAVENDER -> "獲取方法：於農場種植薰衣草種子取得，一次收穫可獲得1-3株";
            case TULIP_PINK -> "獲取方法：於農場種植鬱金香(粉)種子取得，一次收穫可獲得1-3株";
            case WHITE_FABRIC -> "獲取方法：於加工場合成取得，兩朵棉花可合成1個白色布料";
            case RED_DYE -> "獲取方法：於加工場合成取得，將玫瑰搗碎後可取得1個紅色染料";
            case YELLOW_DYE -> "獲取方法：於加工場合成取得，將向日葵搗碎後可取得1個黃色染料";
            case PINK_DYE -> "獲取方法：於加工場合成取得，將鬱金香(粉)搗碎後可取得1個粉色染料";
            case PURPLE_DYE -> "獲取方法：於加工場合成取得，將薰衣草搗碎後可取得1個紫色染料";
            case RED_FABRIC -> "獲取方法：於加工場合成取得，1個白色布料+1個紅色染料即可製作";
            case YELLOW_FABRIC -> "獲取方法：於加工場合成取得，1個白色布料+1個黃色染料即可製作";
            case PINK_FABRIC -> "獲取方法：於加工場合成取得，1個白色布料+1個粉色染料即可製作";
            case PURPLE_FABRIC -> "獲取方法：於加工場合成取得，1個白色布料+1個紫色染料即可製作";
            case WHITE_BOW -> "獲取方法：於加工場合成取得，1個白色布料即可製作";
            case RED_BOW -> "獲取方法：於加工場合成取得，1個紅色布料即可製作";
            case YELLOW_BOW -> "獲取方法：於加工場合成取得，1個黃色布料即可製作";
            case PINK_BOW -> "獲取方法：於加工場合成取得，1個粉色布料即可製作";
            case PURPLE_BOW -> "獲取方法：於加工場合成取得，1個紫色布料即可製作";
            case WHITE_RIBBON -> "獲取方法：於加工場合成取得，1個白色布料即可製作";
            case RED_RIBBON -> "獲取方法：於加工場合成取得，1個紅色布料即可製作";
            case YELLOW_RIBBON -> "獲取方法：於加工場合成取得，1個黃色布料即可製作";
            case PINK_RIBBON -> "獲取方法：於加工場合成取得，1個粉色布料即可製作";
            case PURPLE_RIBBON -> "獲取方法：於加工場合成取得，1個紫色布料即可製作";
            case WHITE_LACE -> "獲取方法：於加工場合成取得，1朵棉花即可製作";
            case RED_LACE -> "獲取方法：於加工場合成取得，1個白色蕾絲+1個紅色染料=1個紅色蕾絲";
            case YELLOW_LACE -> "獲取方法：於加工場合成取得，1個白色蕾絲+1個黃色染料=1個黃色蕾絲";
            case PINK_LACE -> "獲取方法：於加工場合成取得，1個白色蕾絲+1個粉色染料即可製作";
            case PURPLE_LACE -> "獲取方法：於加工場合成取得，1個白色蕾絲+1個紫色染料即可製作";
            case WHITE_DRESS -> "獲取方法：於加工場合成取得，2塊白色布料即可製作";
            case RED_DRESS -> "獲取方法：於加工場合成取得，2塊紅色布料即可製作";
            case YELLOW_DRESS -> "獲取方法：於加工場合成取得，2塊黃色布料即可製作";
            case PINK_DRESS -> "獲取方法：於加工場合成取得，2塊粉色布料即可製作";
            case PURPLE_DRESS -> "獲取方法：於加工場合成取得，2塊紫色布料即可製作";
            case WHITE_SHIRT -> "獲取方法：於加工場合成取得，1塊白色布料即可製作";
            case RED_SHIRT -> "獲取方法：於加工場合成取得，1塊紅色布料即可製作";
            case YELLOW_SHIRT -> "獲取方法：於加工場合成取得，1塊黃色布料即可製作";
            case PINK_SHIRT -> "獲取方法：於加工場合成取得，1塊粉色布料即可製作";
            case PURPLE_SHIRT -> "獲取方法：於加工場合成取得，1塊紫色布料即可製作";
            case WHITE_PANTS -> "獲取方法：於加工場合成取得，1塊白色布料即可製作";
            case RED_PANTS -> "獲取方法：於加工場合成取得，1塊紅色布料即可製作";
            case YELLOW_PANTS -> "獲取方法：於加工場合成取得，1塊黃色布料即可製作";
            case PINK_PANTS -> "獲取方法：於加工場合成取得，1塊粉色布料即可製作";
            case PURPLE_PANTS -> "獲取方法：於加工場合成取得，1塊紫色布料即可製作";
        };
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getFullDescription() {
        return "----------------------------------------------\n" +
               acquiredDescription + "\n" + 
               description;
    }
} 