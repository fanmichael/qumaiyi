package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.yshstudio.originalproduct.pages.adapter.AllcateDetailsAdapter;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;

public class AllCateDetailsActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.GridView_details)
    GridView GridViewDetails;
    private List<ContentValues> contentValues = new ArrayList<>();
    private AllcateDetailsAdapter detailsAdapter;
    private Context context;
    private int cid=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cate_details);
        ButterKnife.bind(this);
        context=this;
        initData();
        new asyncTask().execute(1);
    }


    private void initData(){
        final Bundle bundle = this.getIntent().getExtras();
        cid=bundle.getInt("id");
        String name=bundle.getString("name");
        topTitle.setText(name);
        detailsAdapter=new AllcateDetailsAdapter(contentValues,context);
        GridViewDetails.setAdapter(detailsAdapter);
        GridViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,ShopDetailsActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt("id",contentValues.get(position).getAsInteger("id"));
                bundle1.putInt("uid",contentValues.get(position).getAsInteger("uid"));
                intent.putExtras(bundle1);
                context.startActivity(intent);
            }
        });
    }


    @OnClick(R.id.image_back)
    void back(){
        destroyActitity();
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpDetails();
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
                    //初始加载数据
                    detailsAdapter.notifyDataSetChanged();
                    break;

            }

        }
    }


    private void httpDetails() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.getCateGoods");
            map.put("cid", cid+"");
            map.put("page", "1");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAllcate(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlAllcate(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray objData = new JSONArray(obj.getString("data"));
            if (objData.length() > 0) {
                for (int i = 0; i < objData.length(); i++) {
                    JSONObject jsonObj = objData.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("origin", jsonObj.getInt("origin"));
                    cv.put("type", jsonObj.getInt("type"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("good_intro", jsonObj.getString("good_intro"));
                    cv.put("price", jsonObj.getString("price"));
                    contentValues.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
