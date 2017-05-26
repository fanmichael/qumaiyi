package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import cn.com.shequnew.pages.adapter.MoreAdapter;
import cn.com.shequnew.pages.adapter.UserGoodsShopAdapter;
import cn.com.shequnew.pages.http.HttpConnectTool;

/**
 * 更多加载
 */
public class MoreActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, UserGoodsShopAdapter.setOnClickLoction {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.moer_list)
    PullToRefreshListView mPullToRefreshListView;
    @BindView(R.id.sl_more)
    SwipeRefreshLayout slMore;
    private ListView listView = null;
    private MoreAdapter moreAdapter;
    public String cid;
    public int page = 1;
    public String hot;
    private Context context;
    private List<ContentValues> moreList = new ArrayList<>();
    private List<ContentValues> cahe = new ArrayList<>();
    private List<ContentValues> newList = new ArrayList<>();
    private List<ContentValues> newListData = new ArrayList<>();
    private UserGoodsShopAdapter goodsAdapter;//喜欢的商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        initDelay();
        initView();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    slMore.setEnabled(true);
                else
                    slMore.setEnabled(false);
            }
        });

    }

    private UserGoodsShopAdapter initDataAndAdapterNew() {
        if (newList == null) {
            newList = new ArrayList<ContentValues>();
        }
        if (goodsAdapter == null) {
            goodsAdapter = new UserGoodsShopAdapter(context, newListData, this);
            listView.setAdapter(goodsAdapter);
        }
        return goodsAdapter;
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        cid = bundle.getString("cid");
        hot = bundle.getString("hot");
        if (hot.equals("0")) {
            topTitle.setText("最新内容");
        } else if (hot.equals("1")) {
            topTitle.setText("热门内容");
        } else if (hot.equals("2")) {
            topTitle.setText("新品");
        } else if (hot.equals("3")) {
            topTitle.setText("推荐");
        } else if (hot.equals("4")) {
            topTitle.setText("热门");
        }
        slMore.setOnRefreshListener(this);
        mPullToRefreshListView.setOnRefreshListener(onListener2);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//Mode.PULL_FROM_START
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = mPullToRefreshListView.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);

        if (hot.equals("0")) {
            listView.setAdapter(initDataAndAdapter());
            setDelayMessage(1, 100);
        } else if (hot.equals("1")) {
            listView.setAdapter(initDataAndAdapter());
            setDelayMessage(1, 100);
        } else if (hot.equals("2")) {
            listView.setAdapter(initDataAndAdapterNew());
            setDelayMessage(4, 100);
        } else if (hot.equals("3")) {
            listView.setAdapter(initDataAndAdapterNew());
            setDelayMessage(4, 100);
        } else if (hot.equals("4")) {
            listView.setAdapter(initDataAndAdapterNew());
            setDelayMessage(4, 100);
        }
    }


    PullToRefreshBase.OnRefreshListener2<ListView> onListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            page = page + 1;
            setDelayMessage(5, 100);
        }
    };


    private MoreAdapter initDataAndAdapter() {
        if (moreList == null) {
            moreList = new ArrayList<ContentValues>();
        }
        if (cahe == null) {
            cahe = new ArrayList<ContentValues>();
        }
        if (moreAdapter == null) {
            moreAdapter = new MoreAdapter(moreList, context);
            listView.setAdapter(moreAdapter);
        }
        return moreAdapter;
    }


    /**
     * 延迟线程消息
     */
    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        new asyncTask().execute(1);
                        break;
                    case 2:
                        new asyncTask().execute(2);
                        break;
                    case 3:
                        new asyncTask().execute(3);
                        break;
                    case 4:
                        new asyncTask().execute(4);
                        break;
                    case 5:
                        new asyncTask().execute(5);
                        break;
                }
            }
        };
    }

    @Override
    public void shopDetails(int posit, int id, int uid) {
        Intent intent = new Intent(context, ShopDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("uid", uid);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpMore();
                    bundle.putInt("what", 1);
                    break;
                case 4:
                    httpComm();
                    bundle.putInt("what", 4);
                    break;
                case 5:
                    if (hot.equals("0")) {
                        httpMore();
                    } else if (hot.equals("1")) {
                        httpMore();
                    } else if (hot.equals("2")) {
                        httpComm();
                    } else if (hot.equals("3")) {
                        httpComm();
                    } else if (hot.equals("4")) {
                        httpComm();
                    }
                    bundle.putInt("what", 5);
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
                    slMore.setRefreshing(false);//刷新完成
                    if (moreList.size() > 0) {
                        moreList.clear();
                    }
                    if (cahe.size() > 0) {
                        moreList.addAll(cahe);
                    }
                    moreAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    slMore.setRefreshing(false);//刷新完成
                    if (newListData.size() > 0) {
                        newListData.clear();
                    }
                    if (newList.size() > 0) {
                        newListData.addAll(newList);
                    }
                    goodsAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    if (moreList == null) {
                        moreList = new ArrayList<>();
                    }
                    if (cahe != null && cahe.size() > 0) {
                        moreList.addAll(cahe);
                        if (hot.equals("0")) {
                            moreAdapter.notifyDataSetChanged();
                        } else if (hot.equals("1")) {
                            moreAdapter.notifyDataSetChanged();
                        } else if (hot.equals("2")) {
                            goodsAdapter.notifyDataSetChanged();
                        } else if (hot.equals("3")) {
                            goodsAdapter.notifyDataSetChanged();
                        } else if (hot.equals("4")) {
                            goodsAdapter.notifyDataSetChanged();
                        }
                        mPullToRefreshListView.onRefreshComplete();
                    } else {
                        mPullToRefreshListView.onRefreshComplete();
                        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                        Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }
    }


    private void httpMore() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.more");
            map.put("cid", cid);
            map.put("page", page + "");
            map.put("hot", hot);
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlMore(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void xmlMore(String data) {
        if (cahe.size() > 0) {
            cahe.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONArray jsonArrNews = new JSONArray(objData.getString("note"));
            if (jsonArrNews.length() > 0) {
                for (int i = 0; i < jsonArrNews.length(); i++) {
                    JSONObject jsonObj = jsonArrNews.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("file_type", jsonObj.getInt("file_type"));
                    cv.put("title", jsonObj.getString("title"));
                    cv.put("subject", jsonObj.getString("subject"));
                    if (jsonObj.has("video_img")) {
                        cv.put("video_img", jsonObj.getString("video_img"));
                    }
                    cv.put("tags", jsonObj.getString("tags"));
                    cv.put("content", jsonObj.getString("content"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("nick", jsonObj.getString("nick"));
                    cahe.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }


    private void httpComm() {//新品
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.category");
            if (hot.equals("2")) {
                map.put("type", "new");
            } else if (hot.equals("3")) {
                map.put("type", "recommend");
            } else if (hot.equals("4")) {
                map.put("type", "hot");
            }
            map.put("page", page + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlComm(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlComm(String data) {
        if (newList.size() > 0) {
            newList.clear();
        }
        try {
            JSONObject obj = new JSONObject(data);
            if (obj.getInt("error") == 0) {
                JSONArray objData = new JSONArray(obj.getString("data"));
                if (objData.length() > 0) {
                    for (int i = 0; i < objData.length(); i++) {
                        JSONObject jsonObj = objData.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put("id", jsonObj.getInt("id"));
                        cv.put("uid", jsonObj.getInt("uid"));
                        cv.put("cid", jsonObj.getInt("cid"));
                        cv.put("good_name", jsonObj.getString("good_name"));
                        cv.put("good_image", jsonObj.getString("good_image"));
                        cv.put("price", jsonObj.getString("price"));
                        cv.put("maf_time", jsonObj.getInt("maf_time"));
                        cv.put("origin", jsonObj.getInt("origin"));
                        cv.put("type", jsonObj.getInt("type"));
                        cv.put("nick", jsonObj.getString("nick"));
                        cv.put("icon", jsonObj.getString("icon"));
                        newList.add(cv);
                    }
                }
            } else {
                Toast.makeText(context, "没有数据了！", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRefresh() {
        page = 1;
        if (hot.equals("0")) {
            setDelayMessage(1, 100);
        } else if (hot.equals("1")) {
            setDelayMessage(1, 100);
        } else if (hot.equals("2")) {
            setDelayMessage(4, 100);
        } else if (hot.equals("3")) {
            setDelayMessage(4, 100);
        } else if (hot.equals("4")) {
            setDelayMessage(4, 100);
        }
    }
}
