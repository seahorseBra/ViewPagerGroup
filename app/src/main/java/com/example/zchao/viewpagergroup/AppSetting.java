package com.example.zchao.viewpagergroup;

import android.content.Context;

/**
 * 应用设置
 * Created by zchao on 2016/9/29.
 */

public class AppSetting {
    private static AppSetting instance;
    private Context context;

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

    public static final void inite(Context context) {
        AppSetting instance = getInstance();


    }
}
