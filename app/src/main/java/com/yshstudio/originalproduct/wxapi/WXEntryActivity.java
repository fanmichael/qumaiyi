package com.yshstudio.originalproduct.wxapi;


import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.weixin.callback.WXCallbackActivity;

import static junit.runner.Version.id;

//import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
         if (baseResp.errCode==0){
             Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_LONG).show();
         }else if(baseResp.errCode==-1){
             Toast.makeText(getApplicationContext(),"支付失败",Toast.LENGTH_LONG).show();
         }else if(baseResp.errCode==-2){
             Toast.makeText(getApplicationContext(),"取消支付",Toast.LENGTH_LONG).show();
         }
    }
}
