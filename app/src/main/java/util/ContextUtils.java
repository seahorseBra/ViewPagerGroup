package util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by mavin on 2016/10/9.
 */
public class ContextUtils {
    /**
     * 获取屏幕数据
     * @param context
     * @return
     */
    public static DisplayMetrics getMetrix(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        Display defaultDisplay = manager.getDefaultDisplay();
        defaultDisplay.getMetrics(metrics);
        return metrics;
    }


    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2pix(@NonNull Context context, int dpValue) {
        return dpValue * context.getResources().getDisplayMetrics().density;
    }
}
