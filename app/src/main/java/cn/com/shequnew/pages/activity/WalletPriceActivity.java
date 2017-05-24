package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.http.HttpConnectTool;

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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_price);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("提现");
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private void initView() {
        boolean it = true;
        String msg = "";
        if (walletPriceNumber.getText().toString().trim().equals("")) {
            msg = "请输入金额";
            it = false;
        }
        if (walletNumber.getText().toString().trim().equals("")) {
            msg = "请输入账号";
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
        initView();
    }


    private void httpWallet() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("action", "User.forgotPassword");
//            hashMap.put("mobile", phone);
//            hashMap.put("password", pwd);
//            hashMap.put("code", tag);
            String json = HttpConnectTool.post(hashMap);
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
                    break;

            }

        }
    }


}
