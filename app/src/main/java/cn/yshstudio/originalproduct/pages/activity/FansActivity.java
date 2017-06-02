package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
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
import cn.yshstudio.originalproduct.pages.adapter.FansAdapter;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;
/**
 * 关注---粉丝
 * **/
public class FansActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.fans_list)
    ListView fansList;
    private Context context;
    private List<ContentValues> fansValues = new ArrayList<>();
    private List<ContentValues> myfollowValues = new ArrayList<>();
    private List<ContentValues> groupValues = new ArrayList<>();
    private FansAdapter fansAdapter;
    private int uid;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        ButterKnife.bind(this);
        context = this;
        init();
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private void initFans() {
        fansAdapter = new FansAdapter(fansValues, context);
        fansList.setAdapter(fansAdapter);
    }

    private void initFollow() {
        fansAdapter = new FansAdapter(myfollowValues, context);
        fansList.setAdapter(fansAdapter);
    }
    private void initGroup() {
        fansAdapter = new FansAdapter(groupValues, context);
        fansList.setAdapter(fansAdapter);
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        uid = bundle.getInt("uid");
        type = bundle.getString("type");
        if (type.equals("fans")) {
            topTitle.setText(bundle.getString("name") + "的粉丝");
            new asyncTask().execute(1);
        }
        if (type.equals("follow")) {
            topTitle.setText(bundle.getString("name") + "的关注");
            new asyncTask().execute(2);
        }
        if(type.equals("group")){
            topTitle.setText(bundle.getString("name"));
            new asyncTask().execute(3);
        }

    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpUser();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpUser();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    httpGroup();
                    bundle.putInt("what", 3);
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
                    initFans();
                    break;
                case 2:
                    initFollow();
                    break;
                case 3:
                    initGroup();
                    break;
            }

        }
    }

    private void httpGroup() {//个人主页数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Group.getMoreGroup");
            map.put("uid", uid + "");
            map.put("page","1");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlGroup(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void xmlGroup(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONArray groupList = new JSONArray(objData.getString("group"));//ta 的商品
            if (groupList.length() > 0) {
                for (int i = 0; i < groupList.length(); i++) {
                    JSONObject jsonObj = groupList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("nick", jsonObj.getString("group_name"));//id
                    cv.put("icon", jsonObj.getString("icon"));
                    groupValues.add(cv);
                }
            }
        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void httpUser() {//个人主页数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "User.getUserInfoByTelOrOpenID");
            map.put("uid", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlFans(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlFans(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objUserInfo = new JSONObject(objData.getString("follow"));//个人信息

            JSONArray fansList = new JSONArray(objUserInfo.getString("fans"));//ta 的商品
            JSONArray myfollowList = new JSONArray(objUserInfo.getString("myfollow"));//ta 的商品

            if (fansList.length() > 0) {
                for (int i = 0; i < fansList.length(); i++) {
                    JSONObject jsonObj = fansList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("nick", jsonObj.getString("nick"));//id
                    cv.put("icon", jsonObj.getString("icon"));
                    fansValues.add(cv);
                }

            }

            if (myfollowList.length() > 0) {
                for (int i = 0; i < myfollowList.length(); i++) {
                    JSONObject jsonObj = myfollowList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("nick", jsonObj.getString("nick"));//id
                    cv.put("icon", jsonObj.getString("icon"));
                    myfollowValues.add(cv);
                }
            }
        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
