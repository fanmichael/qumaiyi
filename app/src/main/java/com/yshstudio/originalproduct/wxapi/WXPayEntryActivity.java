package com.yshstudio.originalproduct.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.io.IOException;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.activity.AppOpenActivity;
import com.yshstudio.originalproduct.pages.activity.BuyDetailsActivity;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.tools.Constants;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

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


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String wxId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        chaxunjieguo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        setIntent(intent);
//        api.handleIntent(intent, this);
    }
    private void chaxunjieguo() {
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
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //根据这个result结果做你们需求的操作，result实际就是支付的结果json数据
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.e("jsonObject: ",""+jsonObject);
                    if(jsonObject.getString("result_code").equals("SUCCESS") && jsonObject.getString("return_code").equals("SUCCESS")){
                        if(jsonObject.getString("trade_state").equals("SUCCESS")){
                            wxId=jsonObject.getString("transaction_id");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addOrderId();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void addOrderId() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action","Orderid.orderCallback")
                .add("orderid", SharedPreferenceUtil.read("orderid", ""))
                .add("prepayid", wxId).build();
        Request request = new Request.Builder()
                .url(Ini.Url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                finish();
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {
        chaxunjieguo();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        finish();
        if (baseResp.errCode==0){
            chaxunjieguo();
            Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_LONG).show();
            Intent buyIntent = new Intent(WXPayEntryActivity.this, BuyDetailsActivity.class);
            startActivity(buyIntent);
            finish();
        }else if(baseResp.errCode==-1){
            Toast.makeText(getApplicationContext(),"支付失败",Toast.LENGTH_LONG).show();
            Intent buyIntent = new Intent(WXPayEntryActivity.this, BuyDetailsActivity.class);
            startActivity(buyIntent);
            finish();
        }else if(baseResp.errCode==-2){
            Toast.makeText(getApplicationContext(),"取消支付",Toast.LENGTH_LONG).show();
            Intent buyIntent = new Intent(WXPayEntryActivity.this, BuyDetailsActivity.class);
            startActivity(buyIntent);
            finish();
        }

    }
}
