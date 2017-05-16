package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.CommentAdapter;
import cn.com.shequnew.pages.adapter.ShopImagesAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.tools.ListTools;
import cn.com.shequnew.tools.ValidData;

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
    SimpleDraweeView fileDetailsSimTitle;
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
    ListView fileDetailsImages;
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
    private boolean isSoll = false;//是否收藏
    private ContentValues values = new ContentValues();
    private List<ContentValues> imgs = new ArrayList<>();
    private List<ContentValues> list = new ArrayList<>();
    private List<List<ContentValues>> lists = new ArrayList<>();
    private ShopImagesAdapter shopImagesAdapter;//图片介绍
    private CommentAdapter commentAdapter;/////////////////////
    private Handler handlers;

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

        if (uid == AppContext.cv.getAsInteger("id")) {
            lan.setVisibility(View.GONE);
            fileDetailsAttention.setVisibility(View.GONE);
            fileDetailsAttentionNo.setVisibility(View.GONE);
            collect.setVisibility(View.GONE);
            collectRe.setVisibility(View.GONE);
        }

        setDelayMessage(1, 100);
    }

    @OnClick(R.id.image_back_coll)
    void back() {
        destroyActitity();
    }


    @OnClick(R.id.share_coll)
    void share() {
        //分享

    }

    @OnClick(R.id.file_details_attention)
    void attention() {
        mLoading = new Loading(
                context, fileDetailsAttention);
        mLoading.setText("正在提交......");
        mLoading.show();
        typeatt = 4;
        setDelayMessage(3, 100);
    }

    @OnClick(R.id.file_details_attention_no)
    void attno() {
        mLoading = new Loading(
                context, fileDetailsAttention);
        mLoading.setText("正在提交......");
        mLoading.show();
        typeatt = 5;
        setDelayMessage(3, 100);
    }


    @OnClick(R.id.content_cal)
    void cal() {
        right();
    }

    @OnClick(R.id.content_sumbit)
    void sumbit() {
        right();
        contentDetails = contectText.getText().toString().trim();
        if (contentDetails.equals("")) {
            Toast.makeText(context, "请输入评语", Toast.LENGTH_SHORT).show();
        } else {
            setDelayMessage(4, 100);//回复
        }
    }


    private void left() {//jin
        contentLin.setVisibility(View.VISIBLE);
        radioPagesFile.setVisibility(View.GONE);
        contentLin.setAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    private void right() {//chu
        contentLin.setVisibility(View.GONE);
        radioPagesFile.setVisibility(View.VISIBLE);
        contentLin.setAnimation(AnimationUtils.makeOutAnimation(this, false));
    }


    private void initData() {
        try {
            Uri imageUri = Uri.parse(values.getAsString("subject"));
            ValidData.load(imageUri, fileDetailsSimTitle, 300, 150);
            Uri image = Uri.parse(values.getAsString("icon"));
            ValidData.load(image, fileUserInfoIcon, 60, 60);
            fileDetailsTextContent.setText(values.getAsString("title"));
            fileTitle.setText(values.getAsString("tags"));
            fileDetailsPrice.setText("" + values.getAsInteger("follow"));
            fileDetailsNick.setText(values.getAsString("nick"));
            fileDetailsTags.setText(values.getAsString("personalized"));
            fileName.setText(values.getAsString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imgsList() {
        shopImagesAdapter = new ShopImagesAdapter(context, imgs);
        fileDetailsImages.setAdapter(shopImagesAdapter);
        ListTools.setListViewHeightBasedOnChildren(fileDetailsImages);
    }


    private void groupClike() {

        radioPagesFile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.coll_con_no:
                        mLoading = new Loading(
                                context, fileDetailsAttention);
                        mLoading.setText("正在提交......");
                        mLoading.show();
                        typecomm = 2;
                        setDelayMessage(2, 100);
                        collConNo.setChecked(false);
                        break;
                    case R.id.coll_con:
                        mLoading = new Loading(
                                context, fileDetailsAttention);
                        mLoading.setText("正在提交......");
                        mLoading.show();
                        typecomm = 3;
                        setDelayMessage(2, 100);
                        collCon.setChecked(false);
                        break;
                    case R.id.chat:
                        Intent intent = new Intent(context, LocalVideoActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.faith:
                        break;
                    case R.id.dis:
                        parentnum = 0;
                        left();
                        dis.setChecked(false);
                        break;

                }

            }
        });

    }

    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLoading = new Loading(
                                context, fileDetailsNick);
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

    private void httpContent() {//商品详情
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.content");
            map.put("id", coid + "");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("content", URLEncoder.encode(contentDetails, "UTF-8") + "");
            map.put("parent", parentnum + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                Log.e("", "444444444" + json);
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
                    bundle.putInt("what", 2);
                    break;
                case 5:
                    httpFollowStatusfollow();
                    bundle.putInt("what", 2);
                    break;
                case 6:
                    httpContent();
                    bundle.putInt("what", 6);
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
                    initData();
                    isColl();
                    imgsList();
                    scrollview.smoothScrollTo(0, 0);
                    commAdapter();
                    break;
                case 2:
                    isColl();
                    break;
                case 6:
                    setDelayMessage(1, 100);
                    break;


            }
        }
    }

    private void commAdapter() {
        commentAdapter = new CommentAdapter(context, list, lists, this);
        fileDetailsComment.setAdapter(commentAdapter);
        ListTools.setListViewHeightBasedOnChildren(fileDetailsComment);
    }


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
        } else if (isSoll == true) {
            collCon.setVisibility(View.VISIBLE);
            collConNo.setVisibility(View.GONE);
        }
    }


    private void httpFollowStatusfollow() {//判断关注状态Community.follow
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.follow");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
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

    private void httpFollowStatus() {//判断关注状态Community.follow
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

    private void httpCollesStatuscollection() {//判断收藏状态Community.collection
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.collection");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
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

    private void httpCollesStatus() {//判断收藏状态Community.collection
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkCollectStatus");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
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

    private void httpShop() {//商品详情
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
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
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
            values.put("subject", objnote.getString("subject"));
            values.put("title", objnote.getString("title"));
            values.put("content", objnote.getString("content"));
            values.put("tags", objnote.getString("tags"));
            values.put("personalized", objnote.getString("personalized"));
            values.put("nick", objnote.getString("nick"));
            values.put("icon", objnote.getString("icon"));


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


//            JSONArray goodsJson = new JSONArray(objData.getString("goods"));//g感兴趣的商品
//            if (goodsJson.length() > 0) {
//                for (int i = 0; i < goodsJson.length(); i++) {
//                    JSONObject jsonObj = goodsJson.getJSONObject(i);
//                    ContentValues cv = new ContentValues();
//                    cv.put("id", jsonObj.getInt("id"));//id
//                    cv.put("uid", jsonObj.getInt("uid"));
//                    cv.put("cid", jsonObj.getInt("cid"));
//                    cv.put("maf_time", jsonObj.getInt("maf_time"));//天数
//                    cv.put("good_name", jsonObj.getString("good_name"));//标题
//                    cv.put("good_image", jsonObj.getString("good_image"));//图
//                    cv.put("price", jsonObj.getString("price"));//价格
//                    cv.put("icon", jsonObj.getString("icon"));//价格
//                    cv.put("nick", jsonObj.getString("nick"));//价格
//                    shopsList.add(cv);
//                }
//
//            }


        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
