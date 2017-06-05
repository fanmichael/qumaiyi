package com.yshstudio.originalproduct.chat.net;

import com.yshstudio.originalproduct.inc.Ini;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jiang_ruicheng on 16/10/29.
 */
public class ComUnityRequest {
    private ComUnityRequest() {
    }

    private static String ACCOUNTURL = Ini.Url_ROOT;

    /*private static ComUnityRequest zkTecoRequest;

    public static ComUnityRequest getInstance() {
        if (zkTecoRequest == null) {
            zkTecoRequest = new ComUnityRequest();
        }
        return zkTecoRequest;
    }*/
    public static SheQuApi getAPI() {
        return new Retrofit.Builder().
                baseUrl(ACCOUNTURL).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(SheQuApi.class);
    }
}
