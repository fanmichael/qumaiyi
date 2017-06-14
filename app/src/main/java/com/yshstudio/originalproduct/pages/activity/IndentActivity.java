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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.pages.view.NumberAddSub;
import com.yshstudio.originalproduct.tools.PayTool;
import com.yshstudio.originalproduct.tools.ValidData;

/***
 * 我的订单支付
 * */
public class IndentActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.indent_go_address_details)
    LinearLayout addressLin;
    @BindView(R.id.lin_ass)
    LinearLayout linAss;
    @BindView(R.id.indent_test)
    TextView indentTest;
    @BindView(R.id.indent_address_name)
    TextView addressName;
    @BindView(R.id.nb_addsub_view)
    NumberAddSub numberAddSubView;
    @BindView(R.id.indent_address_phone)
    TextView indentPhone;
    @BindView(R.id.indent_address_details)
    TextView indentDetails;
    @BindView(R.id.indent_go)
    ImageView imageGo;
    @BindView(R.id.indent_icon)
    SimpleDraweeView indentIcon;
    @BindView(R.id.indent_nick)
    TextView indentNick;
    @BindView(R.id.indent__commodity_images)
    SimpleDraweeView indentImages;
    @BindView(R.id.indent__commodity_title)
    TextView indntTitle;
    @BindView(R.id.indent__commodity_grd)
    TextView indentGrd;
    @BindView(R.id.indent__commodity_nick)
    TextView indentPrice;
    @BindView(R.id.indent_all_price)
    TextView allPrice;
    @BindView(R.id.indent_weixin_item)
    CheckBox weixinPay;
    @BindView(R.id.indent_ship)
    TextView indentShip;
    @BindView(R.id.indent__commodity_number)
    TextView indentNumber;
    @BindView(R.id.indent_zhibao)
    CheckBox baoPay;
    @BindView(R.id.indent_btn)
    Button bntPay;
    int num = 1; //订单数量
    Handler mHandler;


    private ContentValues goods = new ContentValues();
    private ContentValues addr = new ContentValues();
    private Context context;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent);
        ButterKnife.bind(this);
        context = this;
        initView();

        numberAddSubView.setOnButtonClickListenter(new NumberAddSub.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                goods.put("num", value);
                allPrice.setText(allMoneyStr()+"元");
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                goods.put("num", value);
                allPrice.setText(allMoneyStr()+"元");
            }
        });
        weixinPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baoPay.isChecked()) {
                    baoPay.setChecked(false);
                }
            }
        });
        baoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weixinPay.isChecked()) {
                    weixinPay.setChecked(false);
                }
            }
        });
        new asyncTask().execute(1);
    }


    private String allMoneyStr(){
        double allPrices = ((Double.valueOf(goods.getAsString("price")) * goods.getAsInteger("num")) + Double.valueOf(goods.getAsString("ship")));
        return  ValidData.formatDouble4(allPrices);
    }


    private void initView() {
        topTitle.setText("我的订单");
        topRegitTitle.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //支付宝支付回调
                    case Ini.SDK_PAY_FLAG:
                        Intent buyIntent = new Intent(context, BuyDetailsActivity.class);
                        context.startActivity(buyIntent);
                        break;
                    case Ini.SDK_PAY_FLAG2:
                        PayTool.payZFB(IndentActivity.this, msg.obj.toString(), mHandler);
                        break;
                }
            }
        };
    }


    private void initData() {
        if (addr.containsKey("id")) {
            addressName.setText(addr.getAsString("name"));
            indentPhone.setText(addr.getAsString("mobile"));
            indentDetails.setText(addr.getAsString("address"));
            indentTest.setVisibility(View.GONE);
            linAss.setVisibility(View.VISIBLE);
        } else {
            linAss.setVisibility(View.GONE);
            indentTest.setVisibility(View.VISIBLE);
            indentTest.setText("还没创建收货地址，快去新建吧！");
        }
        Uri imageUri = Uri.parse(addr.getAsString("icon"));
        ValidData.load(imageUri, indentIcon, 60, 60);
        Uri image = Uri.parse(goods.getAsString("good_image"));
        ValidData.load(image, indentImages, 100, 80);
        indentNick.setText(addr.getAsString("nick"));
        indentShip.setText("运费：" + goods.getAsString("ship") + "元");
        indntTitle.setText(goods.getAsString("good_name"));
        indentGrd.setText("工期：" + goods.getAsInteger("maf_time") + "天");
        indentPrice.setText("￥" + goods.getAsString("price"));

        allPrice.setText(allMoneyStr()+"元");
        indentNumber.setText("库存："+goods.getAsInteger("good_num"));
        numberAddSubView.setMaxValue(goods.getAsInteger("good_num"));
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    /**
     * 支付
     */
    @OnClick(R.id.indent_btn)
    void btnPay() {
        if(!addr.containsKey("id")){
            Toast.makeText(context, "请选择地址！", Toast.LENGTH_LONG).show();
            return;
        }
        if(goods.getAsInteger("good_num") < goods.getAsInteger("num")){
            Toast.makeText(context, "购买数量不能大于库存！", Toast.LENGTH_LONG).show();
            return;
        }
        if (weixinPay.isChecked()) {
            PayTool.pay(IndentActivity.this, goods, addr, Ini.PAY_TYPE_WEIXIN, mHandler);
        } else if (baoPay.isChecked()) {
            PayTool.pay(IndentActivity.this, goods, addr, Ini.PAY_TYPE_ZFB, mHandler);
        } else {
            Toast.makeText(context, "请选择支付方式", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 跳转地址页面
     */
    @OnClick(R.id.indent_go_address_details)
    void goLinAddressDetails() {
        Intent intent = new Intent(context, SiteDetailsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==1){
            new asyncTask().execute(1);
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpAddressDetailsChose();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    bundle.putInt("what", 2);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            // removeLoading();
            switch (what) {
                case 1:
                    //初始加载数据
                    initData();
                    break;
                case 2:
                    break;

            }

        }
    }


    private void httpAddressDetailsChose() {//加载所有数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Orderid.initial");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAddress(json);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlAddress(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
            JSONObject goodsObject = new JSONObject(dataObject.getString("goods"));
            JSONObject addrObject = new JSONObject(dataObject.getString("addr"));

            if (goodsObject.length() > 0) {
                goods.put("id", goodsObject.getInt("id"));
                goods.put("uid", goodsObject.getInt("uid"));
                goods.put("cid", goodsObject.getInt("cid"));
                goods.put("maf_time", goodsObject.getInt("maf_time"));
                goods.put("ship", goodsObject.getString("ship"));//运费
                goods.put("good_name", goodsObject.getString("good_name"));
                goods.put("good_intro", goodsObject.getString("good_intro"));
                goods.put("good_image", goodsObject.getString("good_image"));
                goods.put("good_num", goodsObject.has("good_num")?goodsObject.getInt("good_num"):1);
                goods.put("price", goodsObject.getString("price"));
                goods.put("num", 1);
            }
            if (addrObject.length() > 0) {
                if (addrObject.has("id")) {
                    addr.put("id", addrObject.getInt("id"));
                    addr.put("uid", addrObject.getInt("uid"));
                    addr.put("name", addrObject.getString("name"));
                    addr.put("mobile", addrObject.getString("mobile"));
                    addr.put("address", addrObject.getString("address"));
                }
                addr.put("icon", addrObject.getString("icon"));
                addr.put("nick", addrObject.getString("nick"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

}
