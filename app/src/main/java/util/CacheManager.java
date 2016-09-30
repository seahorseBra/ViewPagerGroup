package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by zchao on 2016/9/29.
 */

public class CacheManager {
    private static SharedPreferences sp;
    private static Gson gson;
    private static final String SP_NAME = "date_cache";
    private static CacheManager instance;
    private CacheManager(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static CacheManager inite(Context context) {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(context);
                }
            }
        }
        return instance;

    }

    public <T> CacheObj<T> getObj(String key, Type type) {
        String string = sp.getString(key, "");
        return gson.fromJson(string, type);
    }

    public <T> void cacheObj(String key, T t) {
        CacheObj cacheObj = new CacheObj(t, System.currentTimeMillis());
        String s = gson.toJson(cacheObj);
        sp.edit().putString(key, s).apply();
    }
}
