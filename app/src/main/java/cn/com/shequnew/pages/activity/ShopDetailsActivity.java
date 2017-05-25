package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.ShopImagesAdapter;
import cn.com.shequnew.pages.adapter.UserGoodsShopAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.tools.ListTools;
import cn.com.shequnew.tools.ValidData;


/**
 * 商品详情
 */
public class ShopDetailsActivity extends BaseActivity implements UserGoodsShopAdapter.setOnClickLoction {

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
    @BindView(R.id.btn_shop_details)
    Button btnShopDetails;
    @BindView(R.id.lan)
    LinearLayout lan;
    @BindView(R.id.shop_details_sim_title)
    SimpleDraweeView shopDetailsSimTitle;
    @BindView(R.id.shop_details_text_content)
    TextView shopDetailsTextContent;
    @BindView(R.id.shop_details_price)
    TextView shopDetailsPrice;
    @BindView(R.id.shop_details_fam_time)
    TextView shopDetailsFamTime;
    @BindView(R.id.shop_details_soll_sell)
    ImageView shopDetailsSollSell;
    @BindView(R.id.shop_details_estimate)
    TextView shopDetailsEstimate;
    @BindView(R.id.linayout_estimate)
    LinearLayout linayoutEstimate;
    @BindView(R.id.shop_user_info_icon)
    SimpleDraweeView shopUserInfoIcon;
    @BindView(R.id.shop_details_nick)
    TextView shopDetailsNick;
    @BindView(R.id.shop_details_tags)
    TextView shopDetailsTags;
    @BindView(R.id.shop_details_attention)
    LinearLayout shopDetailsAttention;
    @BindView(R.id.shop_details_attention_no)
    LinearLayout shopDetailsAttentionNo;
    @BindView(R.id.shop_details_intro)
    TextView shopDetailsIntro;
    @BindView(R.id.shop_details_parameter)
    TextView shopDetailsParameter;
    @BindView(R.id.shop_details_images)
    ListView shopDetailsImages;
    @BindView(R.id.shop_details_like)
    ListView shopDetailsLike;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.shop_details_info_from)
    LinearLayout shopDetailsInfoFrom;

    private ContentValues values = new ContentValues();
    private List<ContentValues> imagesList = new ArrayList<>();
    private List<ContentValues> shopsList = new ArrayList<>();
    private Context context;
    public int id;
    public int uid;
    private int typecomm;
    private int typeatt;
    private boolean isSart = false;//是否出售
    private boolean isCancal = false;//是否关注
    private boolean isSoll = false;//是否收藏

    private UserGoodsShopAdapter goodsAdapter;//喜欢的商品
    private ShopImagesAdapter shopImagesAdapter;//图片介绍


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ButterKnife.bind(this);
        context = this;
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        uid = bundle.getInt("uid");
        if (uid == AppContext.cv.getAsInteger("id")) {
            lan.setVisibility(View.GONE);
            shopDetailsAttention.setVisibility(View.GONE);
            shopDetailsAttentionNo.setVisibility(View.GONE);
            collect.setVisibility(View.GONE);
            collectRe.setVisibility(View.GONE);
        }
        topTitleColl.setText("商品详情");
        initDelay();
        setDelayMessage(1, 100);
        shopDetailsImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> imgUrl = new ArrayList<String>();
                for (int i = 0; i < imagesList.size(); i++) {
                    imgUrl.add(imagesList.get(i).getAsString("imgs"));
                }
                Intent intent1 = new Intent(context, PictureDisplayActivity.class);
                intent1.putExtra("position", imgUrl.size());
                intent1.putStringArrayListExtra("enlargeImage", imgUrl);
                startActivity(intent1);
            }
        });

    }

    @OnClick(R.id.shop_details_sim_title)
    void clikeBig() {
        ArrayList<String> imgUrl = new ArrayList<String>();
        imgUrl.add(values.getAsString("good_image"));
        Intent intent1 = new Intent(context, PictureDisplayActivity.class);
        intent1.putExtra("position", imgUrl.size());
        intent1.putStringArrayListExtra("enlargeImage", imgUrl);
        startActivity(intent1);
    }

    /**
     * 评价
     */
    @OnClick(R.id.shop_details_info_from)
    void infoFrom() {
        Intent intent = new Intent(context, InfromActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("rid", "" + id);
        bundle.putString("type", "1");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    private void initGoods() {
        goodsAdapter = new UserGoodsShopAdapter(context, shopsList, this);
        shopDetailsLike.setAdapter(goodsAdapter);
        ListTools.setListViewHeightBasedOnChildren(shopDetailsLike);
    }

    private void initImages() {
        shopImagesAdapter = new ShopImagesAdapter(context, imagesList);
        shopDetailsImages.setAdapter(shopImagesAdapter);
        ListTools.setListViewHeightBasedOnChildren(shopDetailsImages);
    }


    @OnClick(R.id.image_back_coll)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.share_coll)
    void shareColl() {
        //分享
        Intent intent = new Intent(context, ShareAllActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "ShopDetailsActivity");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @OnClick(R.id.shop_user_info_icon)
    void userInfo() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", values.getAsInteger("uid"));
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @OnClick(R.id.btn_shop_details)
    void buySolle() {
        //立即购买
        if (isSart == false) {
            return;
        } else {
            Intent intent = new Intent(context, IndentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @OnClick(R.id.collect)
    void imagseColl() {
        //收藏
        mLoading = new Loading(
                context, collect);
        mLoading.setText("正在加载......");
        mLoading.show();
        typeatt = 2;
        setDelayMessage(2, 100);
    }

    @OnClick(R.id.collect_re)
    void cillRe() {
        //取消收藏
        mLoading = new Loading(
                context, collectRe);
        mLoading.setText("正在加载......");
        mLoading.show();
        typeatt = 3;
        setDelayMessage(3, 100);
    }

    @OnClick(R.id.shop_details_attention)
    void shopAtten() {
        //关注
        shopDetailsAttention.setVisibility(View.GONE);
        shopDetailsAttentionNo.setVisibility(View.VISIBLE);
        typecomm = 4;
        setDelayMessage(4, 100);

    }

    @OnClick(R.id.shop_details_attention_no)
    void shopNoAtten() {
        //取消关注
        shopDetailsAttention.setVisibility(View.VISIBLE);
        shopDetailsAttentionNo.setVisibility(View.GONE);
        typecomm = 5;
        setDelayMessage(5, 100);
    }

    @OnClick(R.id.linayout_estimate)
    void shopEstimate() {
        Intent intent = new Intent(context, EstimateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLoading = new Loading(
                                context, shopDetailsSollSell);
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
                        new asyncTask().execute(4);
                        break;
                    case 5:
                        new asyncTask().execute(4);
                        break;
                }
            }
        };
    }

    @Override
    public void shopDetails(int posit, int id, int uid) {
        destroyActitity();
        Intent intent = new Intent(context, ShopDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("uid", uid);
        intent.putExtras(bundle);
        context.startActivity(intent);


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
                case 3:
                    httpCollesStatuscollection();
                    bundle.putInt("what", 3);
                    break;
                case 4:
                    httpFollowStatusfollow();
//                    bundle.putInt("what", 4);
                    break;
                case 5:
                    httpFollowStatusfollow();
                    bundle.putInt("what", 5);
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
                    isColl();
                    initGoods();
                    initImages();
                    scrollview.smoothScrollTo(0, 0);
                    break;
                case 2:
                    isColl();
                    break;
                case 3:
                    isColl();
                    break;
                case 4:
                    isColl();
                    break;
                case 5:
                    isColl();
                    break;
            }
        }
    }


    private void isColl() {
        if (isCancal == false) {
            shopDetailsAttention.setVisibility(View.GONE);
            shopDetailsAttentionNo.setVisibility(View.VISIBLE);
        } else {
            shopDetailsAttention.setVisibility(View.VISIBLE);
            shopDetailsAttentionNo.setVisibility(View.GONE);
        }

        if (isSoll == false) {
            collect.setVisibility(View.VISIBLE);
            collectRe.setVisibility(View.GONE);
        } else {
            collect.setVisibility(View.GONE);
            collectRe.setVisibility(View.VISIBLE);
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
                        isSoll = true;
                    } else if (typeatt == 3) {
                        isSoll = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private void httpCollesStatus() {//判断收藏状态
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkCollectStatus");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "1");
            map.put("type_id", uid + "");
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
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.goodInfo");
            map.put("id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlShop(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        Uri imageUri = Uri.parse(values.getAsString("good_image"));
        ValidData.load(imageUri, shopDetailsSimTitle, 300, 150);
        shopDetailsTextContent.setText(values.getAsString("good_name"));
        shopDetailsPrice.setText("￥" + values.getAsString("price"));
        shopDetailsFamTime.setText("工期：" + values.getAsInteger("maf_time"));

        try {
            Uri image = Uri.parse(values.getAsString("icon"));
            ValidData.load(image, shopUserInfoIcon, 60, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }


        shopDetailsNick.setText(values.getAsString("nick"));
        shopDetailsTags.setText(values.getAsString("personalized"));
        shopDetailsParameter.setText(values.getAsString("description"));
        shopDetailsIntro.setText(values.getAsString("good_intro"));
        shopDetailsEstimate.setText("评价（" + values.getAsInteger("comment_count") + ")");

        if (values.getAsInteger("state") == 1) {
            shopDetailsSollSell.setVisibility(View.VISIBLE);
            btnShopDetails.setBackgroundColor(getResources().getColor(R.color.gray));
            btnShopDetails.setText("已售出");
            isSart = false;
        } else {
            shopDetailsSollSell.setVisibility(View.GONE);
            btnShopDetails.setBackgroundColor(getResources().getColor(R.color.bd_top));
            btnShopDetails.setText("立即购买");
            isSart = true;
        }

    }


    private void xmlShop(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));

            values.put("id", objData.getInt("id"));
            values.put("uid", objData.getInt("uid"));
            values.put("cid", objData.getInt("cid"));
            values.put("ship", objData.getInt("ship"));
            values.put("state", objData.getInt("state"));//0一首
            values.put("comment_count", objData.getInt("comment_count"));//评价
            values.put("maf_time", objData.getInt("maf_time"));
            values.put("good_name", objData.getString("good_name"));
            values.put("price", objData.getString("price"));
            values.put("good_intro", objData.getString("good_intro"));
            values.put("description", objData.getString("description"));
            values.put("personalized", objData.getString("personalized"));
            values.put("nick", objData.getString("nick"));
            values.put("icon", objData.getString("icon"));
            values.put("good_image", objData.getString("good_image"));


            JSONArray imagesJson = new JSONArray(objData.getString("good_imgs"));//图片介绍
            if (imagesJson.length() > 0) {
                for (int i = 0; i < imagesJson.length(); i++) {
                    JSONObject jsonObj = imagesJson.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("imgs", jsonObj.getString("imgs"));
                    imagesList.add(cv);
                }

            }

            JSONArray goodsJson = new JSONArray(objData.getString("goods"));//g感兴趣的商品
            if (goodsJson.length() > 0) {
                for (int i = 0; i < goodsJson.length(); i++) {
                    JSONObject jsonObj = goodsJson.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));//id
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));//天数
                    cv.put("good_name", jsonObj.getString("good_name"));//标题
                    cv.put("good_image", jsonObj.getString("good_image"));//图
                    cv.put("price", jsonObj.getString("price"));//价格
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("ship", jsonObj.getString("ship"));//运费
                    cv.put("nick", jsonObj.getString("nick"));
                    shopsList.add(cv);
                }

            }


        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
