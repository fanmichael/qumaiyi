package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现
 */
public class WalletPriceActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.wallet_price_number)
    EditText walletPriceNumber;
    @BindView(R.id.wallet_number)
    EditText walletNumber;
    @BindView(R.id.wallet_sumbit)
    Button walletSumbit;
    @BindView(R.id.wallet_name)
    EditText walletName;
    @BindView(R.id.wallet_phone)
    EditText walletPhone;
    @BindView(R.id.walle_pert)
    TextView wallePert;
    private Context context;
    private String allPrice;
    private int error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_price);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("提现");
        Bundle bundle = this.getIntent().getExtras();
        allPrice = bundle.getString("allPrice");
        String p=bundle.getString("p");
        wallePert.setText("提示：请核实支付宝账号(收取佣金"+p+"%)");
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private void initView() {
        boolean it = true;
        String msg = "";
        if (walletName.getText().toString().trim().equals("")) {
            msg = "请输入姓名";
            it = false;
        }
        if (walletPhone.getText().toString().trim().equals("")) {
            msg = "请输入手机号";
            it = false;
        }
        if (walletPriceNumber.getText().toString().trim().equals("")) {
            msg = "请输入金额";
            it = false;
        }
        double mon = Double.valueOf(!walletPriceNumber.getText().toString().trim().equals("") ? walletPriceNumber.getText().toString().trim() : "0");
        if (mon <= 0.09) {
            msg = "金额必须大于,等于0.1";
            it = false;
        }

        if (walletNumber.getText().toString().trim().equals("")) {
            msg = "请输入账号";
            it = false;
        }
        double money = Double.parseDouble(!allPrice.equals("") ? allPrice:"0");
        if (mon > money) {
            msg = "输入金额不能大于余额！";
            it = false;
        }

        if (it) {
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return;
        }


    }


    //提交
    @OnClick(R.id.wallet_sumbit)
    void sumit() {
        walletSumbit.setFocusable(false);
        walletSumbit.setClickable(false);
        initView();
    }


    private void httpWallet() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.Enchashment");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("name", URLEncoder.encode(walletName.getText().toString().trim(), "UTF-8") + "");
            hashMap.put("mobile", walletPhone.getText().toString().trim());
            hashMap.put("money", walletPriceNumber.getText().toString().trim());
            hashMap.put("account", walletNumber.getText().toString().trim());
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                error = obj.getInt("error");
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
                    httpWallet();
                    bundle.putInt("what", 1);
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
                    if (error == 115) {
                        Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        destroyActitity();
                    } else {
                        Toast.makeText(context, "输入金额大于余额！", Toast.LENGTH_SHORT).show();
                        walletSumbit.setFocusable(true);
                        walletSumbit.setClickable(true);
                    }
                    break;
            }

        }
    }


}
