package javabean;

import java.util.LinkedHashMap;

/**
 * Created by mavin on 2016/10/9.
 */
public class ImageCategory {
    private static LinkedHashMap<String, Integer> typeId = null;

    private void inite() {
        synchronized (ImageCategory.class) {
            if (typeId == null) {
                typeId = new LinkedHashMap<>();
            }
            typeId.put("中国明星", 2001);
            typeId.put("欧美明星", 2002);
            typeId.put("中国女明星", 2003);
            typeId.put("中国男明星", 2004);
            typeId.put("韩国明星", 2005);
            typeId.put("欧美女明星", 2006);
            typeId.put("欧美男明星", 2007);

            typeId.put("清纯", 4001);
            typeId.put("气质", 4002);
            typeId.put("萌女", 4003);
            typeId.put("校花", 4004);
            typeId.put("婚纱", 4005);
            typeId.put("街拍", 4006);
            typeId.put("非主流", 4007);
            typeId.put("美腿", 4008);
            typeId.put("性感", 4009);
            typeId.put("车模", 4010);
            typeId.put("男色图片", 4011);
            typeId.put("模特美女", 4012);
            typeId.put("美女魅惑", 4013);
            typeId.put("日韩美女", 4014);
        }
    }

    public LinkedHashMap<String, Integer> getAllType() {
        if (typeId == null) {
            inite();
        }
        return typeId;
    }

    public int getTypeIdByName(String name) {
        int id = -1;
        if (typeId == null) {
            inite();
        }

        if (typeId.containsKey(name)) {
            id = typeId.get(name);
        }
        return id;
    }
}
