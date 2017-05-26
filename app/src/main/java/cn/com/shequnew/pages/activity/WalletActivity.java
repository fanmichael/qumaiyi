package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.WalletAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;

/**
 * 提现
 */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.wallet_price)
    TextView walletPrice;
    @BindView(R.id.wallet_details)
    ListView walletDetails;
    private Context context;

    private WalletAdapter adapter;
    private List<ContentValues> contentValues = new ArrayList<>();
    private String allMoney = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("我的钱包");
        topRegitTitle.setText("提现");
        topRegitTitle.setVisibility(View.VISIBLE);
        adapter = new WalletAdapter(context, contentValues);
        walletDetails.setAdapter(adapter);
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    //提现
    @OnClick(R.id.top_regit_title)
    void wallet() {
        Intent intent = new Intent(context, WalletPriceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("allPrice", allMoney);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    /**
     * 首次请求加载
     */
    private void httpWallet() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.userBalance");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
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
            JSONObject jsonMoney = new JSONObject(jsonArrNote.getString("user_info"));
            allMoney = jsonMoney.getString("balance");
            JSONArray jsonArray = new JSONArray(jsonArrNote.getString("money_info"));
            if (jsonArray.length() <= 0) {
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put("id", jsonObject.getInt("id"));
                cv.put("uid", jsonObject.getInt("uid"));
                cv.put("type", jsonObject.getInt("type"));
                cv.put("state", jsonObject.getInt("state"));
                cv.put("money", jsonObject.getString("money"));
                cv.put("time", jsonObject.getString("time"));
                contentValues.add(cv);
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
                    walletPrice.setText(allMoney);
                    adapter.notifyDataSetChanged();
                    break;

            }

        }
    }

}
