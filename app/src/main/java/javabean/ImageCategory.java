package javabean;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mavin on 2016/10/9.
 */
public class ImageCategory {
    public static HashMap<Integer, String> typeIds = null;
    public static final List<Integer> allIds = Arrays.asList(2001,2002,2003,2004,2005,2006,2007,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014);

    private static void inite() {
        synchronized (ImageCategory.class) {
            if (typeIds == null) {
                typeIds = new LinkedHashMap<>();
            }
            typeIds.put(2001, "中国明星");
            typeIds.put(2002, "欧美明星");
            typeIds.put(2003, "中国女明星");
            typeIds.put(2004, "中国男明星");
            typeIds.put(2005, "韩国明星");
            typeIds.put(2006, "欧美女明星");
            typeIds.put(2007, "欧美男明星");

            typeIds.put(4001, "清纯");
            typeIds.put(4002, "气质");
            typeIds.put(4003, "萌女");
            typeIds.put(4004, "校花");
            typeIds.put(4005, "婚纱");
            typeIds.put(4006, "街拍");
            typeIds.put(4007, "非主流");
            typeIds.put(4008, "美腿");
            typeIds.put(4009, "性感");
            typeIds.put(4010, "车模");
            typeIds.put(4011, "男色图片");
            typeIds.put(4012, "模特美女");
            typeIds.put(4013, "美女魅惑");
            typeIds.put(4014, "日韩美女");
        }
    }

    public static HashMap<Integer, String> getAllType() {
        if (typeIds == null) {
            inite();
        }
        return typeIds;
    }

    public static String getTypeNameById(Integer typeId) {
        String name = "";
        if (typeIds == null) {
            inite();
        }
        if (typeIds.containsKey(typeId)) {
            name = typeIds.get(typeId);
        }
        return name;
    }
}
