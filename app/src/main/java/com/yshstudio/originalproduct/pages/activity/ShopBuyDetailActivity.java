package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.ValidData;

/**
 * 发货管理的订单详情
 */
public class ShopBuyDetailActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.buy_detail_image)
    SimpleDraweeView buyDetailImage;
    @BindView(R.id.buy_detail_title)
    TextView buyDetailTitle;
    @BindView(R.id.buy_detail_grd)
    TextView buyDetailGrd;
    @BindView(R.id.buy_detail_price)
    TextView buyDetailPrice;
    @BindView(R.id.buy_detail_time)
    TextView buyDetailTime;
    @BindView(R.id.buy_detail_icon)
    SimpleDraweeView buyDetailIcon;
    @BindView(R.id.buy_address_name)
    TextView buyAddressName;
    @BindView(R.id.buy_address_phone)
    TextView buyAddressPhone;
    @BindView(R.id.buy_address_details)
    TextView buyAddressDetails;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.btn_shop_detail)
    Button btnShopDetail;
    private ContentValues order = new ContentValues();
    private Context context;
    private int id;
    private int state;
    private int status;
    private String ddid;
    private String logistics;
    private String logistics_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_buy_detail);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("订单详情");
        initData();
    }


    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        state = bundle.getInt("state");
        status = bundle.getInt("status");
        ddid = bundle.getString("ddid");
        logistics = bundle.getString("logistics");
        logistics_num = bundle.getString("logistics_num");

        if (state == 0 && status == 0) {
            btnShopDetail.setText("等待支付...");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }
        if (state == 0 && status == 1) {
            btnShopDetail.setText("发货");
        }
        if (state == 2 && status == 1) {
            btnShopDetail.setText("钱已转到账户余额");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }


        if (state == 3 && status == 1) {
            btnShopDetail.setText("申请退款...");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }

        if (state == 4 && status == 1) {
            btnShopDetail.setText("退款成功...");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }

        if (state == 10 && status == 1) {
            btnShopDetail.setText("订单已完成");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }

        if (state == 5 && status == 1) {
            btnShopDetail.setText("退款失败...");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }


        if (state == 7 && status == 1) {
            btnShopDetail.setText("钱已转到账户余额");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }

        if (state == 8 && status == 1) {
            btnShopDetail.setText("钱已转到账户余额");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }
        if (state == 9 && status == 1) {
            btnShopDetail.setText("钱已转到账户余额");
            btnShopDetail.setClickable(false);
            btnShopDetail.setEnabled(false);
        }
        /**
         * 查看物流
         * */
        if (state == 1 && status == 1) {
            btnShopDetail.setText("查看物流");
        }
        new asyncTask().execute(1);

    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    @OnClick(R.id.btn_shop_detail)
    void btnShop() {
        if (state == 0 && status == 1) {
            Intent intent = new Intent(context, ExpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ddid", ddid);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        }
        if (state == 2 && status == 1) {
            new asyncTask().execute(2);
        }

        /**
         * 查看物流
         * */
        if (state == 1 && status == 1) {
            Intent intent = new Intent(context, LogisticsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("com", logistics);
            bundle.putString("no", logistics_num);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }

    /**
     * 申请提现
     */
    private void htttpOrderid() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", id + "");
            hashMap.put("state", "7");
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 首次加载请求----订单详情（订单）
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.getOrderDetail");
            hashMap.put("orderid", id + "");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析数据
     */
    private void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject jsonArrNote = new JSONObject(obj.getString("data"));
            order.put("id", jsonArrNote.getInt("id"));
            order.put("num", jsonArrNote.getInt("num"));
            order.put("uid", jsonArrNote.getInt("uid"));
            order.put("ddid", jsonArrNote.getString("ddid"));
            order.put("money", jsonArrNote.getString("money"));
            order.put("trade_name", jsonArrNote.getString("trade_name"));
            order.put("icon", jsonArrNote.getString("icon"));
            order.put("good_image", jsonArrNote.getString("good_image"));
            order.put("maf_time", jsonArrNote.getInt("maf_time"));
            JSONArray jsonArray = new JSONArray(jsonArrNote.getString("info"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 1) {
                    order.put("name", jsonObject.getString("name"));
                } else if (i == 2) {
                    order.put("mobile", jsonObject.getString("mobile"));
                } else if (i == 3) {
                    order.put("address", jsonObject.getString("address"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 异步加载数据
     */
    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    htttpAll();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpOrderid();
                    bundle.putInt("what", 2);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
//            removeLoading();
            switch (what) {
                case 1:
                    initView();
                    break;
                case 2:

                    break;

            }

        }
    }


    /**
     * 赋值
     */
    private void initView() {
        if (!order.containsKey("icon")) {
            return;
        }
        Uri imageUri = Uri.parse(order.getAsString("icon"));
        Uri image = Uri.parse(order.getAsString("good_image"));
        ValidData.load(imageUri, buyDetailIcon, 30, 30);
        ValidData.load(image, buyDetailImage, 100, 80);
        buyDetailTitle.setText(order.getAsString("trade_name"));
        buyDetailGrd.setText("工期：" + order.getAsInteger("maf_time") + "天");
        buyDetailPrice.setText("￥" + order.getAsString("money"));
        buyDetailTime.setText("x" + order.getAsInteger("num"));
        buyAddressName.setText(order.getAsString("name"));
        buyAddressPhone.setText(order.getAsString("mobile"));
        buyAddressDetails.setText(order.getAsString("address"));
    }


}
