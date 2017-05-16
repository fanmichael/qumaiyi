package cn.com.shequnew.pages.http;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 16823 on 2016/9/9.
 */
public interface APIService {

//    @GET
//    Observable<ResponseBody> getConnect(@Url String url);

    @GET
    Observable<String> getConnect(@Url String url);

    @FormUrlEncoded
    @POST
    Observable<String> validLeshouPsw(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Observable<String> getSalewellPrinterIs(@Url String url, @FieldMap Map<String, String> map);
}
