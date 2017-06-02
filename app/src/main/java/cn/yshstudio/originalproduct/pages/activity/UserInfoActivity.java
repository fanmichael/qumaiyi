package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.chat.activity.ChatActivity;
import cn.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import cn.yshstudio.originalproduct.pages.adapter.UserDynamicAdapter;
import cn.yshstudio.originalproduct.pages.adapter.UserGoodsAdapter;
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;
import cn.yshstudio.originalproduct.pages.prompt.Loading;
import cn.yshstudio.originalproduct.tools.ListTools;
import cn.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * 个人主页
 */
public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.user_info_icon)
    SimpleDraweeView userInfoIcon;
    @BindView(R.id.user_nick)
    TextView userNick;
    @BindView(R.id.is_sigen)
    TextView isSigen;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_address)
    TextView userAddress;
    @BindView(R.id.user_info)
    TextView userInfo;
    @BindView(R.id.user_follow)
    TextView userFollow;
    @BindView(R.id.layout_group_more)
    LinearLayout layoutGroupMore;
    @BindView(R.id.user_group_more)
    TextView userGroupMore;
    @BindView(R.id.listView_dynamic_user)
    ListView listViewDynamicUser;
    @BindView(R.id.user_dynamic_more)
    TextView userDynamicMore;
    @BindView(R.id.listView_user_shop)
    ListView listViewUserShop;
    @BindView(R.id.user_shop_more)
    TextView userShopMore;
    @BindView(R.id.user_attention_image)
    ImageView userAttentionImage;
    @BindView(R.id.user_attention_text)
    TextView userAttentionText;
    @BindView(R.id.user_attention)
    LinearLayout userAttention;
    @BindView(R.id.user_take)
    LinearLayout userTake;
    @BindView(R.id.user_attention_imageone)
    ImageView userAttentionImageone;
    @BindView(R.id.user_attention_textone)
    TextView userAttentionTextone;
    @BindView(R.id.user_attention_no)
    LinearLayout userAttentionNo;
    @BindView(R.id.lan)
    LinearLayout lan;
    private Context context;
    private int uid;
    private boolean isCancal = false;
    private ContentValues cvUser = new ContentValues();//个人主页数据
    private List<ContentValues> goods = new ArrayList<>();//ta 商品
    private List<ContentValues> groups = new ArrayList<>();//他的群组
    private List<ContentValues> dynamics = new ArrayList<>();//他的动态
    private UserDynamicAdapter userDynamicAdapter;
    private UserGoodsAdapter goodsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("个人主页");
        Bundle bundle = this.getIntent().getExtras();
        uid = bundle.getInt("uid");
        initDelay();

        if (AppContext.cv.getAsInteger("id") == uid) {
            lan.setVisibility(View.GONE);
        }

        setDelayMessage(1, 100);
    }


    @OnClick(R.id.user_shop_more)
    void shopMore() {//商品加载更多
        Intent intent = new Intent(context, ShopListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "Ta的商品");
        bundle.putInt("uid", uid);
        bundle.putInt("type", 1);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @OnClick(R.id.user_dynamic_more)
    void dynamicMore() {//加载更多动态
        Intent intent = new Intent(context, ShopListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "Ta的动态");
        bundle.putInt("uid", uid);
        bundle.putInt("type", 2);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.user_group_more)
    void groupMore() {//更多群组
        Intent intent = new Intent(context, FansActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", uid);
        bundle.putString("type", "group");
        bundle.putString("name", "Ta的群组");
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.user_info)
    void userInfo() {
        //粉丝
        if (cvUser.getAsInteger("info") == 0 || cvUser.getAsInteger("info") == null) {
            return;
        } else {
            Intent intent = new Intent(context, FansActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("uid", uid);
            bundle.putString("type", "fans");
            bundle.putString("name", cvUser.getAsString("nick"));
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @OnClick(R.id.user_follow)
    void userFollow() {
        //关注数
        if (cvUser.getAsInteger("follow") == 0 || cvUser.getAsInteger("follow") == null) {
            return;
        } else {
            Intent intent = new Intent(context, FansActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("uid", uid);
            bundle.putString("type", "follow");
            bundle.putString("name", cvUser.getAsString("nick"));
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @OnClick(R.id.user_attention)
    void userSttention() {
        //关注
        new asyncTask().execute(2);

    }

    @OnClick(R.id.user_attention_no)
    void attNo() {
        //取消关注
        new asyncTask().execute(3);

    }


    private void initView() {
        Uri imageUri = Uri.parse(cvUser.getAsString("icon"));
        ValidData.load(imageUri, userInfoIcon, 60, 60);
        userNick.setText(cvUser.getAsString("nick"));
        if (cvUser.getAsInteger("sign") == 0) {
            isSigen.setVisibility(View.GONE);
        } else {
            isSigen.setVisibility(View.VISIBLE);
        }
        if (cvUser.getAsInteger("gender") == 0) {
            userSex.setText("男");
        } else if (cvUser.getAsInteger("gender") == 1) {
            userSex.setText("女");
        }
        userAddress.setText(cvUser.getAsString("location"));
        userInfo.setText("粉丝  " + cvUser.getAsInteger("info"));
        userFollow.setText("关注  " + cvUser.getAsInteger("follow"));


    }

    private void dynamicAdapter() {
        userDynamicAdapter = new UserDynamicAdapter(context, dynamics, cvUser);
        listViewDynamicUser.setAdapter(userDynamicAdapter);
        ListTools.setListViewHeightBasedOnChildren(listViewDynamicUser);
        if (dynamics.size() >= 3) {
            userDynamicMore.setVisibility(View.VISIBLE);
        } else {
            userDynamicMore.setVisibility(View.GONE);
        }
    }

    private void goodsAdapter() {
        goodsAdapter = new UserGoodsAdapter(context, goods, cvUser);
        listViewUserShop.setAdapter(goodsAdapter);
        ListTools.setListViewHeightBasedOnChildren(listViewUserShop);
        if (goods.size() >= 3) {
            userShopMore.setVisibility(View.VISIBLE);
        } else {
            userShopMore.setVisibility(View.GONE);
        }
    }

    public void userGroup() {//他的群组
        if (groups.size() > 0) {
            if (groups.size() >= 3) {
                userGroupMore.setVisibility(View.VISIBLE);
            } else {
                userGroupMore.setVisibility(View.GONE);
            }
            for (int i = 0; i < groups.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.user_group, null);
                SimpleDraweeView userIcon = (SimpleDraweeView) view.findViewById(R.id.user_group_icon);
                TextView userName = (TextView) view.findViewById(R.id.uer_group_name);
                Uri imageUri = Uri.parse(groups.get(i).getAsString("icon"));
                ValidData.load(imageUri, userIcon, 60, 60);
                userName.setText(groups.get(i).getAsString("group_name"));
                layoutGroupMore.addView(view);
            }
        } else {
            userGroupMore.setText("没有数据");
//            userGroupMore.setTextColor();
        }
    }

    /**
     * 聊天
     */
    @OnClick(R.id.user_take)
    void userMsg() {
        sendMessage();
    }

    private void sendMessage() {
        if (SharedPreferenceUtil.hasKey("id")) {
            if (SharedPreferenceUtil.read("id", "").isEmpty()) {
                return;
            }
        } else {
            if (AppContext.cv.getAsString("mobile").trim().isEmpty()) {
                return;
            }
        }
        Intent intent = new Intent(UserInfoActivity.this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, cvUser.getAsString("mobile")).putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE).putExtra("NICK", cvUser.getAsString("nick"));
        if (UserInfo.getInstance().getInfo() == null || UserInfo.getInstance().getInfo().get(cvUser.getAsString("mobile")) == null) {
            UserInfo.getInstance().addInfo(new UserInfo.User().setUid(cvUser.getAsString("mobile")).setNick(cvUser.getAsString("nick")).setIcon(cvUser.getAsString("icon")));
        } else {
            UserInfo.getInstance().getInfo().get(cvUser.getAsString("mobile")).setNick(cvUser.getAsString("nick")).setIcon(cvUser.getAsString("icon"));
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                ObjectSaveUtils.saveObject(UserInfoActivity.this, "USERICON", UserInfo.getInstance());
            }
        }.start();
        startActivity(intent);
    }

    /**
     * 延迟线程消息
     */
    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLoading = new Loading(
                                context, imageBack);
                        mLoading.setText("正在加载......");
                        mLoading.show();
                        new asyncTask().execute(1);
                        break;
                    case 2:
                        new asyncTask().execute(2);
                        break;
                    case 3:
                        new asyncTask().execute(3);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }
        };
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpUser();
                    httpFollowStatus();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpAttention();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    httpAttention();
                    bundle.putInt("what", 3);
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
                    initView();
                    userGroup();
                    dynamicAdapter();
                    goodsAdapter();
                    if (isCancal == false) {
                        userAttention.setVisibility(View.GONE);
                        userAttentionNo.setVisibility(View.VISIBLE);
                    } else {
                        userAttention.setVisibility(View.VISIBLE);
                        userAttentionNo.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    userAttention.setVisibility(View.GONE);
                    userAttentionNo.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    userAttention.setVisibility(View.VISIBLE);
                    userAttentionNo.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void httpFollowStatus() {//判断关注状态
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkFollowStatus");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "2");
            map.put("type_id", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 121) {
                    isCancal = false;
                } else if (error == 0) {
                    isCancal = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void httpAttention() {//请求关注
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.follow");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "2");
            map.put("type_id", uid + "");
            String json = HttpConnectTool.post(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void httpUser() {//个人主页数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "User.getUserInfoByTelOrOpenID");
            map.put("uid", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlUser(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlUser(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objUserInfo = new JSONObject(objData.getString("user"));//个人信息
            cvUser.put("id", objUserInfo.getInt("id"));
            cvUser.put("nick", objUserInfo.getString("nick"));
            cvUser.put("mobile", objUserInfo.getString("mobile"));
            cvUser.put("icon", objUserInfo.getString("icon"));
            cvUser.put("gender", objUserInfo.getInt("gender"));
            cvUser.put("location", objUserInfo.getString("location"));
            cvUser.put("personalized", objUserInfo.getString("personalized"));
            cvUser.put("sign", objUserInfo.getInt("sign"));//是否 签约0,1
            JSONObject follow = new JSONObject(objData.getString("follow"));//粉丝 关注
            cvUser.put("info", follow.getInt("info"));//粉丝
            cvUser.put("follow", follow.getInt("follow"));//关注
            JSONArray goodsList = new JSONArray(objData.getString("goods"));//ta 的商品
            if (goodsList.length() > 0) {
                for (int i = 0; i < goodsList.length(); i++) {
                    JSONObject jsonObj = goodsList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));//id
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));//天数
                    cv.put("good_name", jsonObj.getString("good_name"));//标题
                    cv.put("good_image", jsonObj.getString("good_image"));//图
                    cv.put("price", jsonObj.getString("price"));//价格
                    goods.add(cv);
                }

            }

            JSONArray groupsList = new JSONArray(objData.getString("group"));//ta 的群组
            if (groupsList.length() > 0) {
                for (int i = 0; i < groupsList.length(); i++) {
                    JSONObject jsonObj = groupsList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("group_name", jsonObj.getString("group_name"));
                    cv.put("id", jsonObj.getInt("id"));
                    groups.add(cv);
                }
            }
            JSONArray dynamicsList = new JSONArray(objData.getString("dynamic"));//ta 动态
            if (dynamicsList.length() > 0) {
                for (int i = 0; i < dynamicsList.length(); i++) {
                    JSONObject jsonObj = dynamicsList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("subject", jsonObj.getString("subject"));
                    if (jsonObj.has("video_img")) {
                        cv.put("video_img", jsonObj.getString("video_img"));
                    }
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("title", jsonObj.getString("title"));
                    cv.put("tags", jsonObj.getString("tags"));
                    cv.put("file_type", jsonObj.getInt("file_type"));
                    dynamics.add(cv);
                }
            }


        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
