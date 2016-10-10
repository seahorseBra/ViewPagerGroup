package com.example.zchao.viewpagergroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.text.TextUtils;

/**
 * 应用设置
 * Created by zchao on 2016/9/29.
 */

public class AppSetting {
    private static AppSetting instance;
    private static final String APP_SETTING = "app_setting_sp_key";
    private Context context;
    private SharedPreferences SP;

    private AppSetting() {
    }

    public static final AppSetting getInstance() {
        if (instance == null) {
            synchronized (AppSetting.class) {
                if (instance == null) {
                    instance = new AppSetting();
                }
            }
        }
        return instance;
    }

    public void inite(Context context) {
        SP = context.getSharedPreferences(APP_SETTING, Context.MODE_PRIVATE);
    }

    public int getInt(String key) {
        if (TextUtils.isEmpty(key)) {
            return -1;
        }
        return SP.getInt(key, -1);
    }

    public void setInt(String key, Integer value) {
        SP.edit().putInt(key, value).commit();
    }
}
