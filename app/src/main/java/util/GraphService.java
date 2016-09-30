package util;

import org.json.JSONObject;

import javabean.PrettyGrilImage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zchao on 2016/9/29.
 */

public interface GraphService {

    @GET("pic/pic_search?")
    Call<PrettyGrilImage> getShownData(@Query("type") String type, @Query("page") String page);

}
