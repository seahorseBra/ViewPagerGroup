package util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javabean.PrettyGrilImage;
import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 * Created by zchao on 2016/9/29.
 */

public class ApiManager {
    private static ApiManager instance;
    private Executor mExecutor;
    private GraphService graphService;
    private Handler mMainHandler;
    private Context context;
    private CacheManager cacheManager;

    public static final String IMG_BASE_URL = "http://apis.baidu.com/showapi_open_bus/";

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    private ApiManager() {

        initeService();
    }

    public void inite(Context context) {
        this.context = context;
        cacheManager = CacheManager.inite(context);
        mExecutor = Executors.newFixedThreadPool(5);
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    private void initeService() {

        //添加公共参数
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request re = chain.request()
                        .newBuilder()
                        .addHeader("apikey", "99d5fcb6ca3f46b33431daa2b02dac04")
                        .build();

                return chain.proceed(re);
            }
        };
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IMG_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        graphService = retrofit.create(GraphService.class);
    }

    /**
     * 发送结果到主线程
     *
     * @param callBack
     * @param t
     * @param throwable
     * @param isSuccess
     * @param <T>
     */
    private <T> void postToMainLoop(final SingCallBack<T> callBack, final T t, final Throwable throwable, final boolean isSuccess) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onDateReceive(t, throwable, isSuccess);
                }
            }
        });
    }

    /**
     * 获取所有类型分类
     * @param callBack
     */
    public void getAllCategory(final SingCallBack<Object> callBack) {

    }

    /**
     * 获取某类型的具体数据
     * @param callBack  数据回调接口
     * @param typeId    图片类型
     * @param page  页码
     * @param force 是否强制刷新
     */
    public void getAllImageByType(final SingCallBack<PrettyGrilImage> callBack, final String typeId, final String page, final boolean force) {
        final String allType = "key_all_type_" + typeId + page;
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Type type = new TypeToken<CacheObj<PrettyGrilImage>>() {
                }.getType();
                CacheObj<PrettyGrilImage> obj = cacheManager.getObj(allType, type);
                if (obj != null && obj.obj != null && obj.isOutOfDate(5 * 60 * 1000)) {
                    postToMainLoop(callBack, obj.obj, null, true);
                    return;
                }
                graphService.getShownData(typeId, page).enqueue(new Callback<PrettyGrilImage>() {
                    @Override
                    public void onResponse(Call<PrettyGrilImage> call, Response<PrettyGrilImage> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cacheManager.cacheObj(allType, response.body());
                            postToMainLoop(callBack, response.body(), null, true);
                        }
                    }
                    @Override
                    public void onFailure(Call<PrettyGrilImage> call, Throwable t) {
                            postToMainLoop(callBack, null, t, false);
                    }
                });
            }
        });
    }
}
