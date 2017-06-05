package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.pages.prompt.Loading;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import com.yshstudio.originalproduct.tools.UtilsUmeng;
import com.yshstudio.originalproduct.tools.ValidData;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_phone_delete)
    TextView loginPhoneDelete;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_delete)
    TextView loginDelete;
    @BindView(R.id.login_paw)
    TextView loginPaw;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.weixin)
    RadioButton weixin;
    @BindView(R.id.qq)
    RadioButton qq;
    @BindView(R.id.weibo)
    RadioButton weibo;
    @BindView(R.id.group_login)
    RadioGroup groupLogin;

    private Context context;
    private String phone = "";
    private String pwd = "";
    private String msg = "";
    private int tag;
    private boolean is = true;

    private IWXAPI api;
    private String typeLogin;
    public static final String SINA_APPKEY = "3287794514";//3287794514
    //注册成功之后的REDIRECT_URL
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SINA_SCOPE = "all";
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        init();
        setLogin();
        groupLogin();
    }


    //第三方登录登录
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void groupLogin() {
        groupLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.weixin:
                        is = false;
                        typeLogin = "weixin";
                        SharedPreferenceUtil.insert("type", "weixin");
                        UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.WEIXIN, mHandler);
                        weixin.setChecked(false);
                        break;
                    case R.id.qq:
                        is = false;
                        typeLogin = "qq";
                        SharedPreferenceUtil.insert("type", "qq");
                        UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.QQ, mHandler);
                        qq.setChecked(false);
                        break;
                    case R.id.weibo:
                        is = false;
                        typeLogin = "sina";
                        SharedPreferenceUtil.insert("type", "sina");
                        UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.SINA, mHandler);
                        weibo.setChecked(false);
                        break;
                }
            }
        });


    }


    @OnClick(R.id.login)
    void login() {
        phone = loginPhone.getText().toString().trim();
        pwd = loginPassword.getText().toString().trim();
        boolean is = true;
        String msg = "";
        if (phone.equals("") || pwd.equals("")) {
            msg = "输入不能为空";
            is = false;
        }
        if (ValidData.validMobile(phone) == false) {
            msg = "请输入正确的手机号";
            is = false;
        }
        if (is) {
            mLoading = new Loading(context, login);
            mLoading.setText("正在加载......");
            mLoading.show();
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.login_phone_delete)
    void phoneClear() {
        loginPhone.setText("");
    }

    @OnClick(R.id.login_delete)
    void pwdClear() {
        loginPassword.setText("");
    }

    @OnClick(R.id.login_register)
    void regs() {
        Intent intent = new Intent(context, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "register");
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @OnClick(R.id.login_paw)
    void pwdUpdate() {
        Intent intent = new Intent(context, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "paw");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void init() {
        loginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0 && loginPhoneDelete.getVisibility() != View.VISIBLE) {
                    loginPhoneDelete.setVisibility(View.VISIBLE);
                } else if (text.length() <= 0 && loginPhoneDelete.getVisibility() == View.VISIBLE) {
                    loginPhoneDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0 && loginDelete.getVisibility() != View.VISIBLE) {
                    loginDelete.setVisibility(View.VISIBLE);
                } else if (text.length() <= 0 && loginDelete.getVisibility() == View.VISIBLE) {
                    loginDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Ini.SDK_PAY_FLAG3:
                        mLoading = new Loading(context, login);
                        mLoading.setText("正在加载......");
                        mLoading.show();
                        new asyncTask().execute(2);
                        break;
                }
            }
        };
    }


    /**
     * 请求登录
     */
    private void SanhttpLogin() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.oAuthLogin");
            hashMap.put("openid", SharedPreferenceUtil.read("id", ""));
            hashMap.put("name", SharedPreferenceUtil.read("nick", ""));
            hashMap.put("avatar", SharedPreferenceUtil.read("icon", ""));
            hashMap.put("oauthtype", SharedPreferenceUtil.read("type", ""));
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                xmlJsonData(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlJsonData(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            tag = obj.getInt("error");
            if (tag == 100) {
                msg = obj.getString("desc");
            } else {
                JSONObject jsonLogin = new JSONObject(obj.getString("data"));
                AppContext.cv.put("id", jsonLogin.getInt("id"));//标记
                AppContext.cv.put("mobile", jsonLogin.getString("mobile"));//手机号
                AppContext.cv.put("password", "");//md5加密密码
                AppContext.cv.put("nick", jsonLogin.getString("nick"));//昵称
                AppContext.cv.put("icon", jsonLogin.getString("icon"));//头像
                // AppContext.cv.put("gender", jsonLogin.getInt("gender"));//性别
                //AppContext.cv.put("location", "");//地址
                AppContext.cv.put("personalized", "");//个性签名
                AppContext.cv.put("sign", "");//是否签约
                AppContext.cv.put("merchant", jsonLogin.getString("merchant"));//卖家识别'0'否'1'是
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpLogin();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    SanhttpLogin();
                    bundle.putInt("what", 1);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                    if (tag == 100) {
                        Toast.makeText(context, "用户名或密码输入错误", Toast.LENGTH_SHORT).show();
                    } else {
                        setImLogin();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        destroyActitity();
                    }
                    break;
            }
        }
    }

    private void setImLogin() {
        UserLodingInFo.getInstance().setIcon(AppContext.cv.getAsString("icon")).
                setId(AppContext.cv.getAsInteger("id") + "").
                setNick(AppContext.cv.getAsString("nick")).
                setMobile(AppContext.cv.getAsString("mobile").equals("") ? SharedPreferenceUtil.read("id", "") : AppContext.cv.getAsString("mobile"));
        ObjectSaveUtils.saveObject(LoginActivity.this, "USERINFO", UserLodingInFo.getInstance());
        loginIM("");
    }

    /**
     * 请求登录
     */
    private void httpLogin() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.login");
            hashMap.put("mobile", phone);
            hashMap.put("password", pwd);
            String json = HttpConnectTool.post(hashMap);
            xmlJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlJson(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            tag = obj.getInt("error");
            if (tag == 100) {
                msg = obj.getString("desc");
            } else {
                JSONObject jsonLogin = new JSONObject(obj.getString("data"));
                AppContext.cv.put("id", jsonLogin.getInt("id"));//标记
                AppContext.cv.put("mobile", jsonLogin.getString("mobile"));//手机号
                AppContext.cv.put("password", jsonLogin.getString("password"));//md5加密密码
                AppContext.cv.put("nick", jsonLogin.getString("nick"));//昵称
                AppContext.cv.put("icon", jsonLogin.getString("icon"));//头像
                AppContext.cv.put("gender", jsonLogin.getInt("gender"));//性别
                AppContext.cv.put("location", jsonLogin.getString("location"));//地址
                AppContext.cv.put("personalized", jsonLogin.getString("personalized"));//个性签名
                AppContext.cv.put("sign", jsonLogin.getInt("sign"));//是否签约
                AppContext.cv.put("merchant", jsonLogin.getString("merchant"));//卖家识别'0'否'1'是
                if (is) {
                    SharedPreferenceUtil.insert("mobile", jsonLogin.getString("mobile"));
                    SharedPreferenceUtil.insert("password", pwd);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setLogin() {
        if (SharedPreferenceUtil.hasKey("mobile") && SharedPreferenceUtil.hasKey("password")) {
            phone = SharedPreferenceUtil.read("mobile", "");
            pwd = SharedPreferenceUtil.read("password", "");
            is = true;
            new asyncTask().execute(1);
        }

        if (SharedPreferenceUtil.hasKey("id") && SharedPreferenceUtil.hasKey("type")) {
            if (SharedPreferenceUtil.read("type", "").equals("sina")) {
                UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.SINA, mHandler);
            } else if (SharedPreferenceUtil.read("type", "").equals("qq")) {
                UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.QQ, mHandler);
            } else if (SharedPreferenceUtil.read("type", "").equals("weixin")) {
                UtilsUmeng.Login(LoginActivity.this, getApplicationContext(), SHARE_MEDIA.WEIXIN, mHandler);
            }
        }
    }

    private void loginIM(final String username) {

        if (EMClient.getInstance().isLoggedInBefore()) {
            /*EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login(username);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });*/
        } else {
            login(username);
        }
    }

    private void login(String username) {
        EMClient.getInstance().login(UserLodingInFo.getInstance().getMobile(), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


}
