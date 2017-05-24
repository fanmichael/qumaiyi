package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
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
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.tools.ListTools;

/**
 * 我的收藏
 */
public class CollectDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.collect_note)
    Button collectNote;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.collect_view_note)
    View collectViewNote;
    @BindView(R.id.collect_goods)
    Button collectGoods;
    @BindView(R.id.collect_view_goods)
    View collectViewGoods;
    @BindView(R.id.collect_list)
    PullToRefreshListView collectList;
    @BindView(R.id.collect_swi)
    SwipeRefreshLayout collectSwi;

    private ListView listView = null;
    private Context context;
    private boolean isChose = true;
    private int page = 1;
    private List<ContentValues> listNote = new ArrayList<>();
    private List<ContentValues> listGood = new ArrayList<>();
    private List<ContentValues> listNoteCate = new ArrayList<>();
    private List<ContentValues> listGoodCate = new ArrayList<>();
    private UserDynamicAdapter userDynamicAdapter;//动态
    private UserGoodsAdapter goodsAdapter;//商品


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_details);
        ButterKnife.bind(this);
        context = this;
        initView();
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    /**
     * 加载视图
     */
    private void initView() {
        topTitle.setText("我的收藏");
        topRegitTitle.setVisibility(View.GONE);
        collectSwi.setOnRefreshListener(this);
        collectList.setOnRefreshListener(onListener2);
        collectList.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        collectList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        collectList.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        collectList.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = collectList.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        listView.setAdapter(noteAdapter());
    }


    private UserDynamicAdapter noteAdapter() {
        if (userDynamicAdapter == null) {
            userDynamicAdapter = new UserDynamicAdapter(context, listNote, null);
            listView.setAdapter(userDynamicAdapter);
        }
        return userDynamicAdapter;
    }

    private UserGoodsAdapter goodsAdapter() {
        if (goodsAdapter == null) {
            goodsAdapter = new UserGoodsAdapter(context, listGood, null);
            listView.setAdapter(goodsAdapter);
        }
        return goodsAdapter;
    }


    PullToRefreshBase.OnRefreshListener2<ListView> onListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            page = page + 1;
            new asyncTask().execute(3);
        }
    };


    /**
     * 动态
     */
    @OnClick(R.id.collect_note)
    void note() {
        initNote();
        collectList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        isChose = true;
        listView.setAdapter(noteAdapter());
        userDynamicAdapter.notifyDataSetChanged();
    }

    /**
     * 商品
     */
    @OnClick(R.id.collect_goods)
    void goods() {
        initGoods();
        collectList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        isChose = false;
        listView.setAdapter(goodsAdapter());
        goodsAdapter.notifyDataSetChanged();
    }

    private void initNote() {
        collectNote.setTextColor(getResources().getColor(R.color.bd_top));
        collectGoods.setTextColor(getResources().getColor(R.color.col_bg));
        collectViewNote.setBackgroundColor(getResources().getColor(R.color.bd_top));
        collectViewGoods.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    private void initGoods() {
        collectNote.setTextColor(getResources().getColor(R.color.col_bg));
        collectGoods.setTextColor(getResources().getColor(R.color.bd_top));
        collectViewNote.setBackgroundColor(getResources().getColor(R.color.grays));
        collectViewGoods.setBackgroundColor(getResources().getColor(R.color.bd_top));
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new asyncTask().execute(2);
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
                    htttpCollect();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpCollect();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    htttpCollect();
                    bundle.putInt("what", 3);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
//            removeLoading();
            switch (what) {
                case 1:
                    if (listNoteCate.size() > 0) {
                        listNote.addAll(listNoteCate);
                    }
                    if (listGoodCate.size() > 0) {
                        listGood.addAll(listGoodCate);
                    }
                    userDynamicAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    collectSwi.setRefreshing(false);//刷新完成
                    if(listNote.size()>0){
                        listNote.clear();
                    }
                    if(listGood.size()>0){
                        listGood.clear();
                    }
                    if (listNoteCate.size() > 0) {
                        listNote.addAll(listNoteCate);
                    }
                    if (listGoodCate.size() > 0) {
                        listGood.addAll(listGoodCate);
                    }
                    if (isChose) {
                        userDynamicAdapter.notifyDataSetChanged();
                    } else {
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    if (listNoteCate.size() > 0) {
                        listNote.addAll(listNoteCate);
                        if (isChose) {
                            userDynamicAdapter.notifyDataSetChanged();
                        } else {
                            goodsAdapter.notifyDataSetChanged();
                        }
                        collectList.onRefreshComplete();
                    } else {
                        if (isChose) {
                            userDynamicAdapter.notifyDataSetChanged();
                        } else {
                            goodsAdapter.notifyDataSetChanged();
                        }
                        collectList.onRefreshComplete();
                        collectList.setMode(PullToRefreshBase.Mode.DISABLED);
                        Toast.makeText(context,"没有更多数据了！",Toast.LENGTH_SHORT).show();
                    }
                    if (listGoodCate.size() > 0) {
                        listGood.addAll(listGoodCate);
                        if (isChose) {
                            userDynamicAdapter.notifyDataSetChanged();
                        } else {
                            goodsAdapter.notifyDataSetChanged();
                        }
                        collectList.onRefreshComplete();
                    } else {
                        if (isChose) {
                            userDynamicAdapter.notifyDataSetChanged();
                        } else {
                            goodsAdapter.notifyDataSetChanged();
                        }
                        collectList.onRefreshComplete();
                        collectList.setMode(PullToRefreshBase.Mode.DISABLED);
                        Toast.makeText(context,"没有更多数据了！",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }

        }
    }


    /**
     * 首次加载请求
     */
    private void htttpCollect() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Community.myCollection");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("page", page + "");
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

        if (listNoteCate == null) {
            listNoteCate = new ArrayList<>();
        }
        if (listGoodCate == null) {
            listGoodCate = new ArrayList<>();
        }
        if (listNoteCate.size() > 0) {
            listNoteCate.clear();
        }

        if (listGoodCate.size() > 0) {
            listGoodCate.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONArray jsonArrNote = new JSONArray(objData.getString("note"));
            JSONArray jsonArrGood = new JSONArray(objData.getString("goods"));
            if (jsonArrNote.length() > 0) {
                for (int i = 0; i < jsonArrNote.length(); i++) {
                    JSONObject jsonObj = jsonArrNote.getJSONObject(i);
                    ContentValues note = new ContentValues();
                    note.put("id", jsonObj.getInt("id"));
                    note.put("uid", jsonObj.getInt("uid"));
                    note.put("file_type", jsonObj.getInt("file_type"));
                    note.put("subject", jsonObj.getString("subject"));
                    if(jsonObj.has("video_img")){
                        note.put("video_img", jsonObj.getString("video_img"));
                    }
                    note.put("title", jsonObj.getString("title"));
                    note.put("content", jsonObj.getString("content"));
                    note.put("tags", jsonObj.getString("tags"));
                    note.put("nick", jsonObj.getString("nick"));
                    note.put("icon", jsonObj.getString("icon"));
                    listNoteCate.add(note);

                }
            }
            if (jsonArrGood.length() > 0) {
                for (int i = 0; i < jsonArrGood.length(); i++) {
                    JSONObject jsonObj = jsonArrGood.getJSONObject(i);
                    ContentValues note = new ContentValues();
                    note.put("id", jsonObj.getInt("id"));
                    note.put("uid", jsonObj.getInt("uid"));
                    note.put("maf_time", jsonObj.getInt("maf_time"));
                    note.put("good_image", jsonObj.getString("good_image"));
                    note.put("good_name", jsonObj.getString("good_name"));
                    note.put("description", jsonObj.getString("description"));
                    note.put("price", jsonObj.getString("price"));
                    note.put("nick", jsonObj.getString("nick"));
                    note.put("icon", jsonObj.getString("icon"));
                    listGoodCate.add(note);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
