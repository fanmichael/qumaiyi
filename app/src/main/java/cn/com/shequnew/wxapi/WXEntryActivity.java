package cn.com.shequnew.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.com.shequnew.R;
import cn.com.shequnew.tools.Constants;
import cn.com.shequnew.tools.JsonUtils;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Context mContext;
    private IWXAPI mWeixinAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        mContext = this;
        mWeixinAPI = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(WXEntryActivity.this, "获取" + baseReq, Toast.LENGTH_SHORT).show();
        Log.e("onReq:", "" + baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("onReq:", "" + baseResp);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                getResult(code);
                Toast.makeText(WXEntryActivity.this, "获取code成功" + code, Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e("onReq:", "发送取消");
                Toast.makeText(WXEntryActivity.this, "发送取消" , Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.e("onReq:", "发送拒绝");
                Toast.makeText(WXEntryActivity.this, "发送拒绝" , Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.e("onReq:", "返回");
                Toast.makeText(WXEntryActivity.this, "返回" , Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getResult(final String code) {
        new Thread() {// 开启工作线程进行网络请求
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + Constants.APP_ID
                        + "&secret="
                        + Constants.APP_KEY
                        + "&code="
                        + code
                        + "&grant_type=authorization_code";
                try {
                    JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid")
                                .toString().trim();
                        String access_token = jsonObject
                                .getString("access_token").toString().trim();
                        Log.i("getResult：", "openid = " + openid);
                        Log.i("getResult：", "access_token = " + access_token);
                        Toast.makeText(WXEntryActivity.this, "openid"+openid , Toast.LENGTH_SHORT).show();
                        getUserMesg(access_token, openid);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;

        try {
            JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
            if (null != jsonObject) {

                String nickname = jsonObject.getString("nickname");
                int sex = Integer.parseInt(jsonObject.get("sex").toString());
                String headimgurl = jsonObject.getString("headimgurl");
                Log.i("getResult：", "nickname = " + nickname);
                Toast.makeText(WXEntryActivity.this, "nickname"+nickname , Toast.LENGTH_SHORT).show();
                Log.i("getResult：", "headimgurl = " + headimgurl);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }


}
