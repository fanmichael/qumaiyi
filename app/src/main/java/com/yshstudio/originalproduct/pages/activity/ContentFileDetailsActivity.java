package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.activity.ChatActivity;
import com.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.adapter.CommentAdapter;
import com.yshstudio.originalproduct.pages.adapter.ConGoodsAdapter;
import com.yshstudio.originalproduct.pages.adapter.ContentFileDetailsAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.pages.prompt.Loading;
import com.yshstudio.originalproduct.tools.ImageToools;
import com.yshstudio.originalproduct.tools.ListTools;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import com.yshstudio.originalproduct.tools.UtilsUmeng;
import com.yshstudio.originalproduct.tools.ValidData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 详情
 * ,ContentFileDetailsAdapter.setOnClickLoction
 */
public class ContentFileDetailsActivity extends BaseActivity implements CommentAdapter.setOnClickLoction {

    @BindView(R.id.image_back_coll)
    ImageView imageBackColl;
    @BindView(R.id.top_title_coll)
    TextView topTitleColl;
    @BindView(R.id.collect)
    ImageView collect;
    @BindView(R.id.collect_re)
    ImageView collectRe;
    @BindView(R.id.share_coll)
    ImageView shareColl;
    @BindView(R.id.coll_con_no)
    RadioButton collConNo;
    @BindView(R.id.coll_con)
    RadioButton collCon;
    @BindView(R.id.chat)
    RadioButton chat;
    @BindView(R.id.faith)
    RadioButton faith;
    @BindView(R.id.dis)
    RadioButton dis;
    @BindView(R.id.radio_pages_file)
    RadioGroup radioPagesFile;
    @BindView(R.id.lan)
    LinearLayout lan;
    @BindView(R.id.file_details_sim_title)
    ImageView fileDetailsSimTitle;
    @BindView(R.id.file_details_text_content)
    TextView fileDetailsTextContent;
    @BindView(R.id.file_title)
    TextView fileTitle;
    @BindView(R.id.file_details_price)
    TextView fileDetailsPrice;
    @BindView(R.id.file_user_info_icon)
    SimpleDraweeView fileUserInfoIcon;
    @BindView(R.id.file_details_nick)
    TextView fileDetailsNick;
    @BindView(R.id.file_details_tags)
    TextView fileDetailsTags;
    @BindView(R.id.file_details_attention)
    LinearLayout fileDetailsAttention;
    @BindView(R.id.file_details_attention_no)
    LinearLayout fileDetailsAttentionNo;
    @BindView(R.id.file_details_images)
    LinearLayout fileDetailsImages;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.file_name)
    TextView fileName;
    @BindView(R.id.file_details_goods)
    ListView fileDetailsGoods;
    @BindView(R.id.file_details_comment)
    ListView fileDetailsComment;
    @BindView(R.id.content_cal)
    ImageView contentCal;
    @BindView(R.id.contect_text)
    EditText contectText;
    @BindView(R.id.content_sumbit)
    Button contentSumbit;
    @BindView(R.id.content_lin)
    LinearLayout contentLin;
    @BindView(R.id.file_shop_spek)
    TextView fileShopSpek;
    @BindView(R.id.file_details_spek)
    TextView fileDetailsSpek;
    @BindView(R.id.file_details_info_from)
    LinearLayout fileDetailsInfoFrom;
    @BindView(R.id.expendlistS)
    ExpandableListView expendlistS;
//    @BindView(R.id.expendlist)
//    ExpandableListView expendlist;

    private Context context;
    public int id;
    public int uid;
    private int typecomm;
    private int typeatt;
    private int coid;
    private int parentnum;
    private String contentDetails = "";

    private boolean isSart = false;//是否出售
    private boolean isCancal = false;//是否关注
    private boolean isSoll;//是否收藏
    private ContentValues values = new ContentValues();
    private List<ContentValues> imgs = new ArrayList<>();
    private List<ContentValues> list = new ArrayList<>();
    private List<List<ContentValues>> lists = new ArrayList<>();
    private List<ContentValues> shopList = new ArrayList<>();
    private CommentAdapter commentAdapter;/////////////////////
    private ConGoodsAdapter conGoodsAdapter;
    private Handler handlers;
    private int h;

    /**
     * 双层list
     */
    private ContentFileDetailsAdapter contentFileDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_file_details);
        ButterKnife.bind(this);
        context = this;
        topTitleColl.setText("详情");
        collect.setVisibility(View.GONE);
        collectRe.setVisibility(View.GONE);
        initDelay();
        groupClike();
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        uid = bundle.getInt("uid");
        if (String.valueOf(uid).equals(SharedPreferenceUtil.read("id",""))) {
            radioPagesFile.setVisibility(View.GONE);
            fileDetailsAttention.setVisibility(View.GONE);
            fileDetailsAttentionNo.setVisibility(View.GONE);
            collect.setVisibility(View.GONE);
            collectRe.setVisibility(View.GONE);
            collCon.setVisibility(View.GONE);
            collConNo.setVisibility(View.GONE);
        }
        setDelayMessage(1, 100);

        //对于的低端手机可能会有如下问题，
        // 从开发者app调到qq或者微信的授权界面，
        // 后台把开发者app杀死了，这样，授权成功没有回调了，可以用如下方式避免
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        });


    }

    @OnClick(R.id.image_back_coll)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.file_details_info_from)
    void infrom() {
        Intent intent = new Intent(context, InfromActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("rid", "" + id);
        bundle.putString("type", "2");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.file_user_info_icon)
    void userInfo() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", uid);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @OnClick(R.id.share_coll)
    void share() {
        //分享
        if (Build.VERSION.SDK_INT >= 26) {
            ImageToools.requestCameraPermission(context);
        }
        UtilsUmeng.share(ContentFileDetailsActivity.this, Ini.ShareCommunity_Url + id, values.getAsString("content"));
    }

    /**
     * 关注
     */
    @OnClick(R.id.file_details_attention)
    void attention() {
        typeatt = 4;
        fileDetailsAttention.setVisibility(View.GONE);
        fileDetailsAttentionNo.setVisibility(View.VISIBLE);
        setDelayMessage(3, 100);
    }

    /**
     * 取消关注
     */
    @OnClick(R.id.file_details_attention_no)
    void attno() {
        typeatt = 5;
        fileDetailsAttention.setVisibility(View.VISIBLE);
        fileDetailsAttentionNo.setVisibility(View.GONE);
        setDelayMessage(3, 100);
    }


    @OnClick(R.id.content_cal)
    void cal() {
        right();
        contectText.setText("");
    }


    /**
     * 回复-评论提交
     */
    @OnClick(R.id.content_sumbit)
    void sumbit() {
        right();
        contentDetails = contectText.getText().toString().trim();
        if (contentDetails.equals("")) {
            Toast.makeText(context, "请输入评语", Toast.LENGTH_SHORT).show();
        } else {
            new asyncTask().execute(6);
        }
    }


    /**
     * 进入动画
     */
    private void left() {//jin
        contentLin.setVisibility(View.VISIBLE);
        radioPagesFile.setVisibility(View.GONE);
        contentLin.setAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    /**
     * 进入动画
     */
    private void right() {//chu
        contentLin.setVisibility(View.GONE);
        radioPagesFile.setVisibility(View.VISIBLE);
        contentLin.setAnimation(AnimationUtils.makeOutAnimation(this, false));
    }



    private void initData() {
        try {
            if(h<500){
                ImageLoader.getInstance().displayImage(values.getAsString("subject"), fileDetailsSimTitle,ImageToools.IM_IMAGE_OPTIONS);
            }else{
                ImageLoader.getInstance().displayImage(values.getAsString("subject"), fileDetailsSimTitle);
            }

            Uri image = Uri.parse(values.getAsString("icon"));
            ValidData.load(image, fileUserInfoIcon, 60, 60);
            fileDetailsTextContent.setText(values.getAsString("title"));
            fileTitle.setText(values.getAsString("tags"));
            fileDetailsPrice.setText("" + values.getAsInteger("collection"));
            fileDetailsNick.setText(values.getAsString("nick"));
            fileDetailsTags.setText(values.getAsString("personalized"));
            fileName.setText(values.getAsString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加介绍
     */
    private void imgsList() {
        for (int i = 0; i < imgs.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.shop_item_imagse, null);
            LinearLayout lin = (LinearLayout) view.findViewById(R.id.lin_shop_imgs);
            ImageView ima = (ImageView) view.findViewById(R.id.imagse_shop_item);
            ImageLoader.getInstance().displayImage(imgs.get(i).getAsString("imgs"), ima);
            lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> imgUrl = new ArrayList<String>();
                    for (int i = 0; i < imgs.size(); i++) {
                        imgUrl.add(imgs.get(i).getAsString("imgs"));
                    }
                    Intent intent1 = new Intent(context, PictureDisplayActivity.class);
                    intent1.putExtra("position", imgUrl.size());
                    intent1.putStringArrayListExtra("enlargeImage", imgUrl);
                    startActivity(intent1);
                }
            });
            fileDetailsImages.addView(view);
        }
    }


    /***
     * 相关的商品
     * */
    private void conAdapter() {
        if (shopList.size() <=0) {
            fileShopSpek.setVisibility(View.VISIBLE);
            fileDetailsGoods.setVisibility(View.GONE);
        } else {
            fileShopSpek.setVisibility(View.GONE);
            fileDetailsGoods.setVisibility(View.VISIBLE);
            conGoodsAdapter = new ConGoodsAdapter(shopList, context);
            fileDetailsGoods.setAdapter(conGoodsAdapter);
            ListTools.setListViewHeightBasedOnChildren(fileDetailsGoods);
        }
    }

    @OnClick(R.id.file_details_sim_title)
    void clikeBig() {
        ArrayList<String> imgUrl = new ArrayList<String>();
        imgUrl.add(values.getAsString("subject"));
        Intent intent1 = new Intent(context, PictureDisplayActivity.class);
        intent1.putExtra("position", imgUrl.size());
        intent1.putStringArrayListExtra("enlargeImage", imgUrl);
        startActivity(intent1);
    }


    private void groupClike() {

        radioPagesFile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.coll_con_no:
                        collCon.setVisibility(View.VISIBLE);
                        collConNo.setVisibility(View.GONE);
                        typecomm = 2;
                        setDelayMessage(2, 100);
                        collConNo.setChecked(false);
                        break;
                    case R.id.coll_con:
                        collCon.setVisibility(View.GONE);
                        collConNo.setVisibility(View.VISIBLE);
                        typecomm = 3;
                        setDelayMessage(2, 100);
                        collCon.setChecked(false);
                        break;
                    case R.id.chat:
                        //加入群聊
                        Intent intent = new Intent(context, ElcyGroupDeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("groupid", values.getAsString("groupid") + "");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        chat.setChecked(false);
                        break;
                    case R.id.faith:
                        //私聊群主
                        sendMessage();
                        faith.setChecked(false);
                        break;
                    case R.id.dis:
                        contectText.setText("");
                        parentnum = 0;
                        coid = id;
                        left();
                        dis.setChecked(false);
                        break;

                }

            }
        });

    }

    private void sendMessage() {
        if (SharedPreferenceUtil.hasKey("openid")) {
            if (SharedPreferenceUtil.read("openid", "").isEmpty()) {
                return;
            }
        } else {
            if (SharedPreferenceUtil.read("mobile","").trim().isEmpty()) {
                return;
            }
        }
        String mobile = values.getAsString("mobile");
        if (values.getAsString("mobile").equals("")) {
            mobile = values.getAsString("openid");
        } else {
            mobile = values.getAsString("mobile");
        }

        Intent intent = new Intent(ContentFileDetailsActivity.this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, mobile).putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE).putExtra("NICK", values.getAsString("nick"));
        if (UserInfo.getInstance().getInfo() == null || UserInfo.getInstance().getInfo().get(mobile) == null) {
            UserInfo.getInstance().addInfo(new UserInfo.User().setUid(mobile).setNick(values.getAsString("nick")).setIcon(values.getAsString("icon")));
        } else {
            UserInfo.getInstance().getInfo().get(mobile).setNick(values.getAsString("nick")).setIcon(values.getAsString("icon"));
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                ObjectSaveUtils.saveObject(ContentFileDetailsActivity.this, "USERICON", UserInfo.getInstance());
            }
        }.start();
        startActivity(intent);
    }

    private void initDelay() {
        mDelay = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLoading = new Loading(context, fileDetailsNick);
                        mLoading.setText("正在加载......");
                        mLoading.show();
                        new asyncTask().execute(1);
                        break;
                    case 2:
                        new asyncTask().execute(2);
                        break;
                    case 3:
                        new asyncTask().execute(5);
                        break;
                    case 4:
                        new asyncTask().execute(6);
                        break;
                }
            }
        };
    }

    @Override
    public void content(int posit, int nid, int uid, int parent) {
        //回复
        coid = nid;
        parentnum = parent;
        left();
    }

    //商品详情
    private void httpContent() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.content");
            map.put("id", coid + "");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("content", URLEncoder.encode(contentDetails, "UTF-8") + "");
            map.put("parent", parentnum + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                Log.e("", "" + json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpFollowStatus();
                    httpCollesStatus();
                    httpShop();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpCollesStatuscollection();
//                    bundle.putInt("what", 2);
                    break;
                case 5:
                    httpFollowStatusfollow();
                    break;
                case 6:
                    httpContent();
                    bundle.putInt("what", 6);
                    break;
                case 7:
                    httpS();
                    bundle.putInt("what", 7);
                    break;
                case 8:
                    h=ImageToools.loadImageFromNetwork(values.getAsString("subject"));
                    bundle.putInt("what", 8);
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
                    new asyncTask().execute(8);
                    break;
                case 2:
                    isColl();
                    break;
                case 6:
                    contectText.setText("");
                    new asyncTask().execute(7);
                    break;
                case 7:
                    commAdapter();
                    break;
                case 8:
                    initData();
                    if (!String.valueOf(uid).equals(SharedPreferenceUtil.read("id",""))) {
                        isColl();
                    }
                    imgsList();
                    conAdapter();
                    commAdapter();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.scrollTo(10, 10);
                        }
                    });
                    break;


            }
        }
    }

    private void commAdapter() {
        if (lists.size() > 0) {
            fileDetailsSpek.setVisibility(View.GONE);
        }
        commentAdapter = new CommentAdapter(context, list, lists, this);
        fileDetailsComment.setAdapter(commentAdapter);
        ListTools.setListViewHeightBasedOnChildren(fileDetailsComment);
    }

    //判断隐藏
    private void isColl() {
        if (isCancal == false) {
            fileDetailsAttention.setVisibility(View.VISIBLE);
            fileDetailsAttentionNo.setVisibility(View.GONE);
        } else if (isCancal == true) {
            fileDetailsAttention.setVisibility(View.GONE);
            fileDetailsAttentionNo.setVisibility(View.VISIBLE);
        }

        if (isSoll == false) {
            collCon.setVisibility(View.GONE);
            collConNo.setVisibility(View.VISIBLE);
        } else {
            collCon.setVisibility(View.VISIBLE);
            collConNo.setVisibility(View.GONE);
        }
    }

    //判断关注状态Community.follow
    private void httpFollowStatusfollow() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.follow");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("type", "2");
            map.put("type_id", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 115) {
                    if (typecomm == 4) {
                        isCancal = true;
                    } else if (typecomm == 5) {
                        isCancal = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断关注状态Community.follow
    private void httpFollowStatus() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkFollowStatus");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
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

    //判断收藏状态Community.collection
    private void httpCollesStatuscollection() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.collection");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("type", "0");
            map.put("type_id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 115) {
                    if (typeatt == 2) {
                        isSoll = false;
                    } else if (typeatt == 3) {
                        isSoll = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断收藏状态Community.collection
    private void httpCollesStatus() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkCollectStatus");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("type", "0");
            map.put("type_id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 121) {
                    isSoll = false;
                } else if (error == 0) {
                    isSoll = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 评价
     */
    //商品详情
    private void httpS() {
        if (list == null) {
            list = new ArrayList<>();
        } else if (list.size() > 0) {
            list.clear();
        }

        if (lists == null) {
            lists = new ArrayList<>();
        } else if (lists.size() > 0) {
            lists.clear();
        }

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.details");
            map.put("id", id + "");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlS(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlS(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONArray commentJson = new JSONArray(objData.getString("comment"));//图片介绍
            if (commentJson.length() > 0) {
                for (int i = 0; i < commentJson.length(); i++) {
                    JSONObject jsonObj = commentJson.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", jsonObj.getInt("id"));
                    contentValues.put("uid", jsonObj.getInt("uid"));
                    contentValues.put("nid", jsonObj.getInt("nid"));
                    contentValues.put("parent", jsonObj.getInt("parent"));
                    contentValues.put("content", jsonObj.getString("content"));
                    contentValues.put("createtime", jsonObj.getString("createtime"));
                    contentValues.put("nick", jsonObj.getString("nick"));
                    contentValues.put("icon", jsonObj.getString("icon"));
                    list.add(contentValues);
                    JSONArray commJson = new JSONArray(jsonObj.getString("case"));
                    if (commJson.length() > 0) {
                        List<ContentValues> cvx = new ArrayList<>();
                        for (int j = 0; j < commJson.length(); j++) {
                            JSONArray commJsons = commJson.getJSONArray(j);
                            if (commJsons.length() > 0) {
                                for (int g = 0; g < commJsons.length(); g++) {
                                    JSONObject jsonObjss = commJsons.getJSONObject(g);
                                    ContentValues content = new ContentValues();
                                    content.put("id", jsonObjss.getInt("id"));
                                    content.put("uid", jsonObjss.getInt("uid"));
                                    content.put("nid", jsonObjss.getInt("nid"));
                                    content.put("parent", jsonObjss.getInt("parent"));
                                    content.put("content", jsonObjss.getString("content"));
                                    content.put("createtime", jsonObjss.getString("createtime"));
                                    content.put("nick", jsonObjss.getString("nick"));
                                    cvx.add(content);
                                }
                            }
                        }
                        lists.add(i, cvx);
                    }
                }
            }

        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //商品详情
    private void httpShop() {
        if (values == null) {
            values = new ContentValues();
        } else if (values.size() > 0) {
            values.clear();
        }

        if (imgs == null) {
            imgs = new ArrayList<>();
        } else if (imgs.size() > 0) {
            imgs.clear();
        }
        if (list == null) {
            list = new ArrayList<>();
        } else if (list.size() > 0) {
            list.clear();
        }

        if (lists == null) {
            lists = new ArrayList<>();
        } else if (lists.size() > 0) {
            lists.clear();
        }

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.details");
            map.put("id", id + "");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlShop(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlShop(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objnote = new JSONObject(objData.getString("note"));


            values.put("id", objnote.getInt("id"));
            values.put("uid", objnote.getInt("uid"));
            values.put("cid", objnote.getInt("cid"));
            values.put("follow", objnote.getInt("follow"));
            values.put("status", objnote.getInt("status"));
            values.put("collection", objnote.getInt("collection"));
            values.put("subject", objnote.getString("subject"));
            values.put("title", objnote.getString("title"));
            values.put("content", objnote.getString("content"));
            values.put("tags", objnote.getString("tags"));
            values.put("personalized", objnote.getString("personalized"));
            values.put("nick", objnote.getString("nick"));
            values.put("icon", objnote.getString("icon"));
            values.put("groupid", objnote.getString("groupid"));
            values.put("mobile", objnote.getString("mobile"));
            values.put("openid", objnote.getString("openid"));
            JSONArray imagesJson = new JSONArray(objnote.getString("content_imgs"));//图片介绍
            if (imagesJson.length() > 0) {
                for (int i = 0; i < imagesJson.length(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put("imgs", imagesJson.get(i).toString());
                    imgs.add(cv);
                }

            }


            JSONArray commentJson = new JSONArray(objData.getString("comment"));//图片介绍
            if (commentJson.length() > 0) {
                for (int i = 0; i < commentJson.length(); i++) {
                    JSONObject jsonObj = commentJson.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", jsonObj.getInt("id"));
                    contentValues.put("uid", jsonObj.getInt("uid"));
                    contentValues.put("nid", jsonObj.getInt("nid"));
                    contentValues.put("parent", jsonObj.getInt("parent"));

                    contentValues.put("content", jsonObj.getString("content"));
                    contentValues.put("createtime", jsonObj.getString("createtime"));
                    contentValues.put("nick", jsonObj.getString("nick"));
                    contentValues.put("icon", jsonObj.getString("icon"));
                    list.add(contentValues);
                    JSONArray commJson = new JSONArray(jsonObj.getString("case"));
                    if (commJson.length() > 0) {
                        List<ContentValues> cvx = new ArrayList<>();
                        for (int j = 0; j < commJson.length(); j++) {
                            JSONArray commJsons = commJson.getJSONArray(j);
                            if (commJsons.length() > 0) {
                                for (int g = 0; g < commJsons.length(); g++) {
                                    JSONObject jsonObjss = commJsons.getJSONObject(g);
                                    ContentValues content = new ContentValues();
                                    content.put("id", jsonObjss.getInt("id"));
                                    content.put("uid", jsonObjss.getInt("uid"));
                                    content.put("nid", jsonObjss.getInt("nid"));
                                    content.put("parent", jsonObjss.getInt("parent"));
                                    content.put("content", jsonObjss.getString("content"));
                                    content.put("createtime", jsonObjss.getString("createtime"));
                                    content.put("nick", jsonObjss.getString("nick"));
                                    cvx.add(content);
                                }

                            }
                        }
                        lists.add(i, cvx);
                    }
                }
            }


            JSONArray goodsJson = new JSONArray(objData.getString("goods"));//g感兴趣的商品
            if (goodsJson.length() > 0) {
                for (int i = 0; i < goodsJson.length(); i++) {
                    JSONObject jsonObj = goodsJson.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));//id
                    cv.put("good_name", jsonObj.getString("good_name"));//标题
                    cv.put("good_image", jsonObj.getString("good_image"));//图
                    cv.put("price", jsonObj.getString("price"));//价格
                    shopList.add(cv);
                }
            }


        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享需要
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 对于的低端手机可能会有如下问题，从开发者app调到qq或者微信的授权界面，后台把开发者app杀死了，
     * 这样，授权成功没有回调了，可以用如下方式避免：（一般不需要添加，如有特殊需要，可以添加）
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄露
        UMShareAPI.get(this).release();
    }
}
