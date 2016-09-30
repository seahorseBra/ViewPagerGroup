package util;

/**
 * Created by zchao on 2016/9/29.
 */

public class CacheObj<T> {
    public T obj;
    public long cacheTime;

    public CacheObj(T t, long cacheTime) {
        this.obj = t;
        this.cacheTime = cacheTime;
    }

    public boolean isOutOfDate(long timeLengh) {
        return Math.abs(System.currentTimeMillis() - timeLengh) >= cacheTime;
    }
}
