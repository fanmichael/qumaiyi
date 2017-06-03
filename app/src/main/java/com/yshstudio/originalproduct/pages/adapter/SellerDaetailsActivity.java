package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.pages.activity.BaseActivity;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;

/**
 * 卖家资质
 */
public class SellerDaetailsActivity extends BaseActivity {


    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.GridView_sell)
    GridView GridViewSell;
    @BindView(R.id.sell_name_c)
    TextView sellNameC;
    private String name = "";
    private List<ContentValues> userImage = new ArrayList<>();
    private Context context;
    private SellDetailsAdapter sellDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_daetails);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    private void initView() {
        topTitle.setText("卖家资质");
        topRegitTitle.setVisibility(View.GONE);
        sellDetailsAdapter = new SellDetailsAdapter(userImage, context);
        GridViewSell.setAdapter(sellDetailsAdapter);
        new asyncTask().execute(1);
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
                    htttpMerchant();
                    bundle.putInt("what", 1);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            switch (what) {
                case 1:
                    sellNameC.setText(name);
                    sellDetailsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    /**
     * 头部数据
     */
    private void htttpMerchant() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Merchant.checkMerchantStatus");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("page", "1");
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
            JSONObject jsonArrGood = new JSONObject(obj.getString("data"));
            name = jsonArrGood.getString("summary");
            JSONArray jsonArray = new JSONArray(jsonArrGood.getString("photo"));
            for (int i = 0; i < jsonArray.length(); i++) {
                ContentValues note = new ContentValues();
                note.put("image", jsonArray.get(i).toString());
                userImage.add(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
