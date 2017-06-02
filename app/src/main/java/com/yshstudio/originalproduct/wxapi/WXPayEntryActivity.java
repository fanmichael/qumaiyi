package com.yshstudio.originalproduct.wxapi;

import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.io.IOException;

import cn.yshstudio.originalproduct.inc.Ini;
import cn.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 版 权：方直科技
 * 作 者：陈景坤
 * 创 建 日 期：2017/5/24
 * 描 述：
 */


public class WXPayEntryActivity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("aa", "sssssssssssssssssssssssssssssssssssssssssssssssssssssss");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("out_trade_no", SharedPreferenceUtil.read("orderid", "")).build();
        Request request = new Request.Builder()
                .url(Ini.RequestPayCallBack_Weixin)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aa", "sssssssssssssssssssssssssssssssssss");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("aa", "sssssssssssssssssssssssssssssssssssssssssssssssssssssss");
            }
        });
    }
}
