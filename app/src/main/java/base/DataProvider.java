package base;

/**
 * Created by zchao on 2016/9/30.
 */

public interface DataProvider {
    void getMoreDate(String...param);
    void refresh();
}
