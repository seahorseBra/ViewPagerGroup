package com.example.zchao.viewpagergroup;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.util.concurrent.Executors;

import util.ApiManager;

/**
 * Created by zchao on 2016/9/29.
 */

public class CApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApiManager.getInstance().inite(getApplicationContext());
        AppSetting.inite(getApplicationContext());
        initImageLoader(getApplicationContext());
    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context appContext) {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(dm.widthPixels, dm.heightPixels)
                .memoryCacheSizePercentage(20)
                .diskCache(new LimitedAgeDiskCache(StorageUtils.getCacheDirectory(appContext, true), 60 * 60 * 24 * 7))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .taskExecutorForCachedImages(Executors.newFixedThreadPool(3))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
