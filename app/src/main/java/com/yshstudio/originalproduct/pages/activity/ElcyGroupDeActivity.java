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


public class ElcyGroupDeActivity extends BaseActivity implements UserGroupAdapter.setBoolChose {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.group_list_de)
    ListView groupListDe;

    private boolean isit = false;
    private Context context;
    private List<ContentValues> contentValues = new ArrayList<>();
    private UserGroupAdapter userGroupAdapter;
    private List<String> strings = new ArrayList<>();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elcy_group_de);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("关联群聊");
        topRegitTitle.setText("加入");
        topRegitTitle.setVisibility(View.VISIBLE);
        userGroupAdapter = new UserGroupAdapter(context, contentValues, this);
        groupListDe.setAdapter(userGroupAdapter);
        Bundle bundle=this.getIntent().getExtras();
        uid=bundle.getString("groupid");
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    //加入群聊
    @OnClick(R.id.top_regit_title)
    void group() {
        if (strings.size() <= 0) {
            Toast.makeText(context, "请选择关联的群组", Toast.LENGTH_SHORT).show();
        } else {
            StringBuffer stringBufferPop = new StringBuffer();
            if (strings.size() > 0) {
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.size() == (i)) {
                        stringBufferPop.append(strings.get(i));
                    } else {
                        stringBufferPop.append(strings.get(i) + ",");
                    }
                }
            }
            Intent intent = new Intent(context, DeclareActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("groupId", stringBufferPop.toString());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void onClick(int pos, boolean is) {
        if (is) {
            strings.add(contentValues.get(pos).getAsInteger("id") + "");
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
                    if (contentValues.size() <= 0) {
                        Toast.makeText(context, "还没有群组！", Toast.LENGTH_LONG).show();
                    }
                    userGroupAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;
            }
        }
    }

    private void httpGoodsInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Group.groupData");
            map.put("groupid", uid);
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
                    cv.put("group_id", jsonObj.getLong("group_id"));
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
