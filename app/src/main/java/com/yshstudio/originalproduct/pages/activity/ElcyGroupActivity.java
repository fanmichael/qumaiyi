package com.yshstudio.originalproduct.pages.activity;

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
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.UserGroupAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;


public class ElcyGroupActivity extends BaseActivity implements UserGroupAdapter.setBoolChose {


    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.group_list)
    ListView groupList;

    private boolean isit = false;
    private Context context;
    private List<ContentValues> contentValues = new ArrayList<>();
    private UserGroupAdapter userGroupAdapter;
    private List<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elcy_group);
        ButterKnife.bind(this);
        context = this;
        topAll.setText("全选");
        topTitle.setText("我的群聊");
        topRegitTitle.setText("关联");
        topAll.setVisibility(View.VISIBLE);
        topRegitTitle.setVisibility(View.VISIBLE);
        userGroupAdapter = new UserGroupAdapter(context, contentValues, this);
        groupList.setAdapter(userGroupAdapter);
        new asyncTask().execute(1);
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    //全选
    @OnClick(R.id.top_all)
    void all() {
        if (isit == true) {
            return;
        }
        isit = true;
        for (int i = 0; i < contentValues.size(); i++) {
            contentValues.get(i).put("is", true);
        }
        new asyncTask().execute(2);
    }

    @OnClick(R.id.top_regit_title)
    void group() {
        if (strings.size() <= 0) {
            Toast.makeText(context, "请选择关联的群组", Toast.LENGTH_SHORT).show();
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
            intent.putExtra("group", stringBufferPop.toString());
            intent.putExtra("num", strings.size() + "");
            this.setResult(12, intent);
            destroyActitity();
        }

    }


    @Override
    public void onClick(int pos, boolean is) {
        if (is) {
            strings.add(contentValues.get(pos).getAsInteger("group_id") + "");
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
                case 2:
                    strings.size();
                    for (int i = 0; i < contentValues.size(); i++) {
                        strings.add(contentValues.get(i).getAsInteger("id") + "");
                    }
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
                    userGroupAdapter.notifyDataSetChanged();
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
            map.put("action", "Group.groupQuery");
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
                    cv.put("group_name", jsonObj.getString("group_name"));
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("group_id", jsonObj.getInt("group_id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("group_public", jsonObj.getString("group_public"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("is", false);
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
