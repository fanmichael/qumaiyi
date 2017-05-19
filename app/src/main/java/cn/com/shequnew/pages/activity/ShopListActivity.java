package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import cn.com.shequnew.pages.adapter.UserDynamicAdapter;
import cn.com.shequnew.pages.adapter.UserGoodsAdapter;
import cn.com.shequnew.pages.http.HttpConnectTool;

public class ShopListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.shop_list)
    PullToRefreshListView mPullToRefreshListView;
    @BindView(R.id.list_sw)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    private ListView listView;
    private Context context;
    private List<ContentValues> values = new ArrayList<>();
    private List<ContentValues> list = new ArrayList<>();
    private ContentValues shop = new ContentValues();
    private int uid;
    private int page = 1;
    private int type;

    private UserGoodsAdapter adapter;
    private UserDynamicAdapter userDynamicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        context = this;
        Bundle bundle = this.getIntent().getExtras();
        uid = bundle.getInt("uid");
        type = bundle.getInt("type");
        topTitle.setText(bundle.getString("name"));
        initView();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });
    }

    private void initView() {
        refreshLayout.setOnRefreshListener(this);
        mPullToRefreshListView.setOnRefreshListener(onListener2);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);///
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = mPullToRefreshListView.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        if (type == 1) {
            listView.setAdapter(initDataAndAdapter());
            new asyncTask().execute(1);
        } else if(type == 2){
            listView.setAdapter(initDyDataAndAdapter());
            new asyncTask().execute(3);
        }

    }

    PullToRefreshBase.OnRefreshListener2<ListView> onListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            page = page + 1;
            if (type == 1) {
                new asyncTask().execute(2);
            } else  if(type == 2){
                new asyncTask().execute(4);
            }

        }
    };

    private UserDynamicAdapter initDyDataAndAdapter() {
        if (userDynamicAdapter == null) {
            userDynamicAdapter = new UserDynamicAdapter(context, list, shop);
            listView.setAdapter(userDynamicAdapter);
        }
        return userDynamicAdapter;
    }


    private UserGoodsAdapter initDataAndAdapter() {
        if (adapter == null) {
            adapter = new UserGoodsAdapter(context, list, shop);
            listView.setAdapter(adapter);
        }
        return adapter;
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpGoods();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpGoods();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    httpDynamic();
                    bundle.putInt("what", 3);
                    break;
                case 4:
                    httpDynamic();
                    bundle.putInt("what", 4);
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
                    refreshLayout.setRefreshing(false);//刷新完成
                    if (list.size() > 0) {
                        list.clear();
                    }
                    list.addAll(values);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (values.size() > 0) {
                        list.addAll(values);
                    }
                    adapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                    mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    refreshLayout.setRefreshing(false);//刷新完成
                    if (list.size() > 0) {
                        list.clear();
                    }
                    list.addAll(values);
                    userDynamicAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    if (values.size() > 0) {
                        list.addAll(values);
                    }
                    userDynamicAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                    mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


    private void httpDynamic() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Dynamic.getAllDynamic");
            map.put("uid", uid + "");
            map.put("page", page + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlDynamic(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlDynamic(String data) {
        if (values.size() > 0) {
            values.clear();
        }
        try {
            JSONObject obj = new JSONObject(data);
            if(obj.getInt("error")==0){
                JSONObject objData = new JSONObject(obj.getString("data"));
                JSONObject objuser = new JSONObject(objData.getString("user"));
                JSONArray fansList = new JSONArray(objData.getString("dynamic"));//ta
                shop.put("nick", objuser.getString("nick"));
                shop.put("icon", objuser.getString("icon"));
                if (fansList.length() > 0) {
                    for (int i = 0; i < fansList.length(); i++) {
                        JSONObject jsonObj = fansList.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put("id", jsonObj.getInt("id"));
                        cv.put("uid", jsonObj.getInt("uid"));
                        if (jsonObj.has("file_type")) {
                            cv.put("file_type", jsonObj.getInt("file_type"));
                            cv.put("tags", jsonObj.getString("tags"));
                        }
                        cv.put("title", jsonObj.getString("comment"));
                        cv.put("subject", jsonObj.getString("img"));
                        values.add(cv);
                    }
                }
            }
        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void httpGoods() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Goods.getMoreGoods");
            map.put("uid", uid + "");
            map.put("page", page + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlFans(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlFans(String data) {
        if (values.size() > 0) {
            values.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objuser = new JSONObject(objData.getString("user"));
            JSONArray fansList = new JSONArray(objData.getString("goods"));//ta 的商品
            shop.put("nick", objuser.getString("nick"));
            shop.put("icon", objuser.getString("icon"));

            if (fansList.length() > 0) {
                for (int i = 0; i < fansList.length(); i++) {
                    JSONObject jsonObj = fansList.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("price", jsonObj.getString("price"));
                    values.add(cv);
                }
            }
        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);
    }
}
