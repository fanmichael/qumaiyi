package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.SpecialAdapter;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
/**
 * 专题
 * */
public class SpecialNoteActivity extends BaseActivity {


    public int sid;
    public String title;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.list_special)
    ListView listSpecial;
    private SpecialAdapter specialAdapter;
    private Context context;

    private List<ContentValues> contentValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_note);
        context=this;
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        sid = bundle.getInt("sid");
        topTitle.setText("专题");
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back(){
        destroyActitity();
    }

    private void init(){
        specialAdapter=new SpecialAdapter(contentValues,context,title);
        listSpecial.setAdapter(specialAdapter);
    }



    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpInfo();
                    bundle.putInt("what", 1);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                        init();
                  //  specialAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }


    private void httpInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.getSpecialNote");
            map.put("sid", sid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlInfo(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void xmlInfo(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objs = new JSONObject(obj.getString("data"));
            JSONArray list = new JSONArray(objs.getString("note"));
            title = objs.getString("title");
            if(list.length()>0){
                for (int i = 0; i < list.length(); i++) {
                    JSONObject jsonObj = list.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("sid", jsonObj.getInt("sid"));
                    cv.put("content", jsonObj.getString("content"));
                    cv.put("img", jsonObj.getString("img"));
                    contentValues.add(cv);
                }
            }else{
                Toast.makeText(context,"还没有数据。。。",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


}
