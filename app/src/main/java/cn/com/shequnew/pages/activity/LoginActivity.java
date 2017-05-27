package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.imagepipeline.listener.RequestListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.chat.util.ObjectSaveUtils;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.tools.SharedPreferenceUtil;
import cn.com.shequnew.tools.UtilsUmeng;
import cn.com.shequnew.tools.ValidData;

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

//    /**
//     * san
//     */
//    private Tencent mTencent;
//    private IUiListener loginListener;
//    private IUiListener userInfoListener; //获取用户信息监听器
//    private UserInfo userInfo; //qq用户信息
//    private QQAuth mQQAuth;
    private IWXAPI api;

    public static final String SINA_APPKEY = "3287794514";//3287794514
    //注册成功之后的REDIRECT_URL
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SINA_SCOPE = "all";
//    private SsoHandler mSsoHandler;
//    private AuthInfo mAuthInfo;
//    private Oauth2AccessToken mAccessToken;


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

    private void getCode() {
        String uuid = UUID.randomUUID().toString();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = uuid;
        api.sendReq(req);
    }
    //第三方登录登录
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Constants.REQUEST_API) {
//            if (resultCode == Constants.RESULT_LOGIN) {
//                Tencent.handleResultData(data, loginListener);
//                userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
//                userInfo.getUserInfo(userInfoListener);
//                userInfoListener = new IUiListener() {
//                    @Override
//                    public void onComplete(final Object arg0) {
//                        if (arg0 == null) {
//                            return;
//                        }
//                        try {
//                            JSONObject jo = (JSONObject) arg0;
//                            int ret = jo.getInt("ret");
//                            System.out.println("json=" + String.valueOf(jo));
//                            String nickName = jo.getString("nickname");
//                            String gender = jo.getString("gender");
//                            Toast.makeText(LoginActivity.this, "你好，" + nickName,
//                                    Toast.LENGTH_LONG).show();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            // TODO: handle exception
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(UiError uiError) {
//                        Toast.makeText(LoginActivity.this, "onError2", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Toast.makeText(LoginActivity.this, "onCancel2", Toast.LENGTH_SHORT).show();
//                    }
//                };
//            }
//        }
//
//        if (resultCode == Constants.ACTIVITY_OK) {
//            // SSO 授权回调
//            // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
//            if (mSsoHandler != null) {
//                Toast.makeText(LoginActivity.this, "新浪微博登陆返回", Toast.LENGTH_LONG).show();
//                //不能少
//                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void groupLogin() {
        groupLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.weixin:
//                        loginToWeiXin();
                        UtilsUmeng.Login(LoginActivity.this,getApplicationContext(),SHARE_MEDIA.WEIXIN);
                        break;
                    case R.id.qq:
                        UtilsUmeng.Login(LoginActivity.this,getApplicationContext(),SHARE_MEDIA.QQ);
//                        qqLogin();
                        break;
                    case R.id.weibo:
                        UtilsUmeng.Login(LoginActivity.this,getApplicationContext(),SHARE_MEDIA.SINA);
//                        weibo();
                        //  Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getApplication());
//                        UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this, SINA_APPKEY, mAccessToken);
//                        long uid = Long.parseLong(mAccessToken.getUid());
//                        mUsersAPI.show(uid, mListener);
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

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
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
                case 2:

            }
        }
    }

    private void setImLogin() {
        UserLodingInFo.getInstance().setIcon(AppContext.cv.getAsString("icon")).
                setId(AppContext.cv.getAsInteger("id") + "").
                setNick(AppContext.cv.getAsString("nick")).
                setMobile(AppContext.cv.getAsString("mobile"));
        ObjectSaveUtils.saveObject(LoginActivity.this, "USERINFO", UserLodingInFo.getInstance());
        loginIM("");
    }

//    /**
//     * 获取微博登陆信息
//     */
//    private RequestListener mListener = new RequestListener() {
//        @Override
//        public void onComplete(String response) {
//            if (!TextUtils.isEmpty(response)) {
//                //LogUtil.i(TAG, response);
//                // 调用 User#parse 将JSON串解析成User对象
////                User user = User.parse(response);
////                if (user != null) {
////                    Toast.makeText(LoginActivity.this,
////                            "获取User信息成功，用户昵称：" + user.screen_name,
////                            Toast.LENGTH_LONG).show();
////                } else {
////                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
////                }
//            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//            //LogUtil.e(TAG, e.getMessage());
//            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };


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
//                AppContext.cv.put("sessionid", jsonLogin.getString("sessionid"));

                SharedPreferenceUtil.insert("mobile", jsonLogin.getString("mobile"));
                SharedPreferenceUtil.insert("password", pwd);

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
            new asyncTask().execute(1);
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
