package cn.com.shequnew.pages.activity;

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
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.AllCateAdapter;
import cn.com.shequnew.pages.http.HttpConnectTool;

public class AllCateActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.GridView)
    android.widget.GridView GridView;
    private AllCateAdapter allCateAdapter;
    private Context context;
    private int sid=1;
    private List<ContentValues> contentValues=new ArrayList<>();
    private List<ContentValues> contentCate=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cate);
        ButterKnife.bind(this);
        context=this;
        init();
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back(){
        destroyActitity();
    }


    private void init(){
        final Bundle bundle = this.getIntent().getExtras();
        sid = bundle.getInt("id");
        String name=bundle.getString("name");
        topTitle.setText(name);
        allCateAdapter=new AllCateAdapter(contentCate,context);
        GridView.setAdapter(allCateAdapter);
        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,AllCateDetailsActivity.class);
                Bundle bundleDetails=new Bundle();
                bundleDetails.putString("name",contentCate.get(position).getAsString("name"));
                bundleDetails.putInt("id",contentCate.get(position).getAsInteger("id"));
                intent.putExtras(bundleDetails);
                context.startActivity(intent);
            }
        });
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpAllCate();
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
                    for(int i=0;i<contentValues.size();i++){
                        if(contentValues.get(i).getAsInteger("parent")==sid){
                            contentCate.add(contentValues.get(i));
                        }
                    }
                    allCateAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;

            }

        }
    }

    private void httpAllCate(){
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.getAllCate");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAllcate(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlAllcate(String data){
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray objData = new JSONArray(obj.getString("data"));
            if(objData.length()>0){
                for (int i=0;i<objData.length();i++){
                    JSONObject jsonObj = objData.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id",jsonObj.getInt("id"));
                    cv.put("name",jsonObj.getString("name"));
                    cv.put("parent",jsonObj.getInt("parent"));
                    cv.put("image",jsonObj.getString("image"));
                    contentValues.add(cv);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



}
