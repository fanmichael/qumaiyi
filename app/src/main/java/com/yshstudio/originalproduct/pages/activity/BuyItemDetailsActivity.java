package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.PayTool;
import com.yshstudio.originalproduct.tools.ValidData;

/**
 * 订单详情
 */
public class BuyItemDetailsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.buy_ddid)
    TextView buyDdid;
    @BindView(R.id.buy_daddress_name)
    TextView buyDaddressName;
    @BindView(R.id.buy_daddress_phone)
    TextView buyDaddressPhone;
    @BindView(R.id.buy_daddress_details)
    TextView buyDaddressDetails;
    @BindView(R.id.buy_details_icon)
    SimpleDraweeView buyDetailsIcon;
    @BindView(R.id.buy_details_nick)
    TextView buyDetailsNick;
    @BindView(R.id.buy_details_images)
    SimpleDraweeView buyDetailsImages;
    @BindView(R.id.buy_details_title)
    TextView buyDetailsTitle;
    @BindView(R.id.buy_details_grd)
    TextView buyDetailsGrd;
    @BindView(R.id.buy_details_price)
    TextView buyDetailsPrice;
    @BindView(R.id.buy_details_time)
    TextView buyDetailsTime;
    @BindView(R.id.buy_details_layout)
    LinearLayout buyDetailsLayout;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.buy_order_price)
    TextView buyOrderPrice;
    @BindView(R.id.buy_go_btn)
    Button buyGoBtn;
    @BindView(R.id.indent_weixin_details)
    CheckBox indentWeixinDetails;
    @BindView(R.id.lin_weixin_pay)
    LinearLayout linWeixinPay;
    @BindView(R.id.indent_zhibao_details)
    CheckBox indentZhibaoDetails;
    @BindView(R.id.lin_zhifu_pay)
    LinearLayout linZhifuPay;
    @BindView(R.id.top_all)
    TextView topAll;

    private String ddid;
    private ContentValues order = new ContentValues();
    private ContentValues addr = new ContentValues();
    private String orderAddress = "";
    private int state;
    private int status;
    private int id;
    private Context context;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item_details);
        ButterKnife.bind(this);
        context = this;
        initData();
        topTitle.setText("付款");
        indentWeixinDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indentZhibaoDetails.isChecked()) {
                    indentZhibaoDetails.setChecked(false);
                }
            }
        });

        indentZhibaoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indentWeixinDetails.isChecked()) {
                    indentWeixinDetails.setChecked(false);
                }
            }
        });

    }


    private void btmCol() {
        buyGoBtn.setBackgroundColor(context.getResources().getColor(R.color.bd_top));
        buyGoBtn.setTextColor(context.getResources().getColor(R.color.white));
        buyGoBtn.setClickable(true);
        buyGoBtn.setEnabled(true);
    }

    /**
     * 加载视图
     */
    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        ddid = bundle.getString("ddid");
        state = bundle.getInt("state");
        status = bundle.getInt("status");
        id = bundle.getInt("id");
        if (state == 0 && status == 0
                || state == 6 && status == 0) {
            buyGoBtn.setText("付款");
            btmCol();
            linZhifuPay.setVisibility(View.VISIBLE);
            linWeixinPay.setVisibility(View.VISIBLE);
            //付款
        }
        if (state == 1 && status == 1) {
            //确认收货
            buyGoBtn.setText("确认收货");
            btmCol();
        }
        if (state == 2 && status == 1) {
            //评价
            buyGoBtn.setText("评价");
            btmCol();
        }
        if (state == 3 && status == 1) {
            //退款中。。
            buyGoBtn.setText("退款中...");
            buyGoBtn.setBackgroundColor(context.getResources().getColor(R.color.white));
            buyGoBtn.setTextColor(context.getResources().getColor(R.color.col_bg));
            buyGoBtn.setClickable(false);
            buyGoBtn.setEnabled(false);
        }
        if (state == 4 && status == 1) {
            //已完成
            buyGoBtn.setText("已完成");
            buyGoBtn.setBackgroundColor(context.getResources().getColor(R.color.white));
            buyGoBtn.setTextColor(context.getResources().getColor(R.color.col_bg));
            buyGoBtn.setClickable(false);
            buyGoBtn.setEnabled(false);
        }
        if (state == 0 && status == 1) {
            //申请退款
            buyGoBtn.setText("申请退款。。");
            btmCol();
        }
        new asyncTask().execute(1);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //支付宝支付回调
                    case Ini.SDK_PAY_FLAG:
//                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
                        setResult(1);
                        finish();
                        break;
                    case Ini.SDK_PAY_FLAG2:
                        PayTool.payZFB(BuyItemDetailsActivity.this, msg.obj.toString(), mHandler);
                        break;
                }
            }
        };
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    //跳转评价页
    @OnClick(R.id.buy_go_btn)
    void gotoBtn() {

        if (state == 0 && status == 0
                || state == 6 && status == 0) {
            //付款
            if (indentWeixinDetails.isChecked()) {
                PayTool.pay(BuyItemDetailsActivity.this, order, addr, Ini.PAY_TYPE_WEIXIN, mHandler);
            } else if (indentZhibaoDetails.isChecked()) {
                PayTool.pay(BuyItemDetailsActivity.this, order, addr, Ini.PAY_TYPE_ZFB, mHandler);
            } else {
                Toast.makeText(context, "请选择支付方式", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (state == 1 && status == 1) {
            //确认收货
            new asyncTask().execute(2);
            return;
        }
        if (state == 2 && status == 1) {
            //评价
            Intent intent = new Intent(context, AppraiseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ddid", ddid);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        if (state == 3 && status == 1) {
            //退款中。。
            return;
        }
        if (state == 4 && status == 1) {
            //已完成
            return;
        }
        if (state == 0 && status == 1) {
            //申请退款
            new asyncTask().execute(3);
        }
    }


    /**
     * 申请退款
     */
    private void htttpOrder() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", id + "");
            hashMap.put("state", "0");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject get = new JSONObject(json);
                int error = get.getInt("error");
                if (error == 0) {
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     */
    private void htttpOrd() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", id + "");
            hashMap.put("state", "1");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject get = new JSONObject(json);
                int error = get.getInt("error");
                if (error == 0) {
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
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
                    htttpOrd();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    htttpOrder();
                    bundle.putInt("what", 3);
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
                    initViewData();
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }

        }
    }


    /**
     * 首次加载请求----订单详情（订单）
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.getOrderInfo");
            hashMap.put("ddid", ddid);
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
            JSONObject jsonOrder = new JSONObject(jsonArrNote.getString("order"));
            JSONObject jsonUser = new JSONObject(jsonArrNote.getString("user"));
            JSONObject jsonGoods = new JSONObject(jsonArrNote.getString("goods"));
            if (jsonArrNote.getString("addr") == null || jsonArrNote.getString("addr").equals("null") || jsonArrNote.getString("addr").equals("")) {
                orderAddress = "没有添加地址";
            } else {
                JSONObject jsonAddr = new JSONObject(jsonArrNote.getString("addr"));
                addr.put("id", jsonAddr.getInt("id"));
                addr.put("uid", jsonAddr.getInt("uid"));
                addr.put("name", jsonAddr.getString("name"));
                addr.put("mobile", jsonAddr.getString("mobile"));
                addr.put("address", jsonAddr.getString("address"));
            }
            order.put("id", jsonOrder.getInt("id"));
            order.put("num", jsonOrder.getInt("num"));
            order.put("uid", jsonOrder.getInt("uid"));
            order.put("shid", jsonOrder.getInt("shid"));
            order.put("address", jsonOrder.getInt("address"));
            order.put("goodsid", jsonOrder.getInt("goodsid"));
            order.put("totalmoney", jsonOrder.getInt("totalmoney"));
            order.put("ddid", jsonOrder.getString("ddid"));
            order.put("money", jsonOrder.getString("money"));
            order.put("trade_name", jsonOrder.getString("trade_name"));
            order.put("nick", jsonUser.getString("nick"));
            order.put("icon", jsonUser.getString("icon"));
            order.put("good_image", jsonGoods.getString("good_image"));
            order.put("maf_time", jsonGoods.getInt("maf_time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViewData() {
        buyDdid.setText(ddid);
        if (orderAddress.equals("")) {
            buyDaddressName.setText(addr.getAsString("name"));
            buyDaddressPhone.setText(addr.getAsString("mobile"));
            buyDaddressDetails.setText(addr.getAsString("address"));
        } else {
            buyDaddressName.setText(orderAddress);
            buyDaddressPhone.setVisibility(View.GONE);
            buyDaddressDetails.setVisibility(View.GONE);
        }
        if(order.getAsString("icon").equals("")){
            return;
        }
        Uri imageUri = Uri.parse(order.getAsString("icon"));
        Uri image = Uri.parse(order.getAsString("good_image"));
        ValidData.load(imageUri, buyDetailsIcon, 30, 30);
        ValidData.load(image, buyDetailsImages, 100, 80);
        buyDetailsNick.setText(order.getAsString("nick"));
        buyDetailsTitle.setText(order.getAsString("trade_name"));
        buyDetailsGrd.setText("工期：" + order.getAsInteger("maf_time") + "天");
        buyDetailsPrice.setText("￥" + order.getAsString("money"));
        buyDetailsTime.setText("x" + order.getAsInteger("num"));
        buyOrderPrice.setText("合计：" + order.getAsInteger("totalmoney"));
    }
}