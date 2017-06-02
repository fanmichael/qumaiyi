package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.adapter.UserGoodsElcyAdapter;
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;

/**
 * 关联的商品
 */
public class ElevancyShopActivity extends BaseActivity implements UserGoodsElcyAdapter.setBoolChose {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.elevancy_list)
    ListView elevancyList;
    private Context context;
    private List<ContentValues> contentValuesList = new ArrayList<>();
    private UserGoodsElcyAdapter userGoodsElcyAdapter;
    private List<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevancy_shop);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("我的商品");
        topRegitTitle.setText("关联");
        topRegitTitle.setVisibility(View.VISIBLE);
        userGoodsElcyAdapter = new UserGoodsElcyAdapter(context, contentValuesList, this);
        elevancyList.setAdapter(userGoodsElcyAdapter);
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    /**
     * 关联
     */
    @OnClick(R.id.top_regit_title)
    void elevancy() {
        if (strings.size() <= 0) {
            Toast.makeText(context, "请选择关联的商品", Toast.LENGTH_SHORT).show();
        } else {
            StringBuffer stringBufferPop = new StringBuffer();
            if (strings.size() > 0) {
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.size() == (i + 1)) {
                        stringBufferPop.append(strings.get(i));
                    } else {
                        stringBufferPop.append(strings.get(i) + ",");
                    }
                }
            }
            Intent intent = new Intent();
            intent.putExtra("goods", stringBufferPop.toString());
            intent.putExtra("num", strings.size() + "");
            this.setResult(11, intent);
            destroyActitity();
        }
    }


    /**
     * 选中还是未选择
     */
    @Override
    public void onClick(int pos, boolean is) {
        if (is) {
            strings.add(contentValuesList.get(pos).getAsInteger("id") + "");
        } else {
            if (strings.size() == 1) {
                strings.clear();
            } else {
                strings.remove(pos);
            }
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            switch (params[0]) {
                case 1:
                    httpGoodsInfo();
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
                    userGoodsElcyAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;

            }

        }
    }


    /**
     * 发布商品
     */
    private void httpGoodsInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.goodsInfo");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArr = new JSONArray(obj.getString("data"));
            if (jsonArr.length() > 0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("price", jsonObj.getString("price"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("nick", jsonObj.getString("nick"));
                    cv.put("icon", jsonObj.getString("icon"));
                    contentValuesList.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
