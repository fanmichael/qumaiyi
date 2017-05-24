package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import cn.com.shequnew.R;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.view.NumberAddSub;
import cn.com.shequnew.tools.ValidData;

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
    @BindView(R.id.indent_zhibao)
    CheckBox baoPay;
    @BindView(R.id.indent_btn)
    Button bntPay;

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
                Toast.makeText(context, "AddClick Vaule==" + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                Toast.makeText(context, "SubClick Vaule==" + value, Toast.LENGTH_SHORT).show();
            }
        });
        new asyncTask().execute(1);
    }

    private void initView() {
        topTitle.setText("我的订单");
        topRegitTitle.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
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

        indntTitle.setText(goods.getAsString("good_name"));
        indentGrd.setText("工期：" + goods.getAsInteger("maf_time") + "天");
        indentPrice.setText("￥" + goods.getAsString("price"));
        allPrice.setText(goods.getAsString("price") + "元");
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
        super.onActivityResult(requestCode, resultCode, data);
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
                goods.put("price", goodsObject.getString("price"));
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
