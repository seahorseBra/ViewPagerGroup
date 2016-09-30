package util;

/**
 * Created by zchao on 2016/9/29.
 */

public interface SingCallBack<T> {
    void onDateReceive(T t, Throwable throwable, boolean isSuccess);
}
