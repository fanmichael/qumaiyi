package com.yshstudio.originalproduct.pages.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import butterknife.Unbinder;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.DynamicAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;

/**
 * Created by Administrator on 2017/4/15 0015.
 * 动态
 */

public class DynamicFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener {

    public Context context;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.dynamic_sw)
    SwipeRefreshLayout swipeRefreshLayout;

    Unbinder unbinder;
    @BindView(R.id.dynamic_list)
    PullToRefreshListView mPullToRefreshListView;
    @BindView(R.id.List_progress)
    RelativeLayout ListProgress;

    private ListView listView = null;
    private int page = 1;
    private List<ContentValues> dyList = new ArrayList<>();
    private List<ContentValues> cache = new ArrayList<>();
    private DynamicAdapter dynamicAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initDelay();
        initView();
        showProgress();
        setDelayMessage(1, 100);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });

    }

    private void initView() {
        imageBack.setVisibility(View.INVISIBLE);
        topTitle.setText("动态");
        swipeRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshListView.setOnRefreshListener(onListener2);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = mPullToRefreshListView.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        listView.setAdapter(initDataAndAdapter());

    }

    /**
     * 显示菜单加载框
     */
    private void showProgress() {
        ListProgress.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    /**
     * 隐藏菜单加载框
     */
    private void hideProgress() {
        ListProgress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    PullToRefreshBase.OnRefreshListener2<ListView> onListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            page = page + 1;
            new asyncTask().execute(2);
        }
    };

    private DynamicAdapter initDataAndAdapter() {
        if (dyList == null) {
            dyList = new ArrayList<ContentValues>();
        }
        if (cache == null) {
            cache = new ArrayList<ContentValues>();
        }

        if (dynamicAdapter == null) {
            dynamicAdapter = new DynamicAdapter(context, dyList);
            listView.setAdapter(dynamicAdapter);
        }
        return dynamicAdapter;
    }


    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);

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
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    htttpDyindex();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpDyindex();
                    bundle.putInt("what", 2);
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
                    if(swipeRefreshLayout==null){
                        return;
                    }
                    swipeRefreshLayout.setRefreshing(false);//刷新完成
                    hideProgress();
                    if (dyList.size() > 0) {
                        dyList.clear();
                    }
                    dyList.addAll(cache);
                    dynamicAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (cache.size() > 0) {
                        dyList.addAll(cache);
                    }
                    dynamicAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                    mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    break;

            }

        }
    }

    /**
     * 首次加载请求
     */
    private void htttpDyindex() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Dynamic.index");
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
        if (cache.size() > 0) {
            cache.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            int tag = obj.getInt("error");
            if (tag == 199) {
                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArrNews = new JSONArray(obj.getString("data"));
                for (int i = 0; i < jsonArrNews.length(); i++) {
                    JSONObject jsonObj = jsonArrNews.getJSONObject(i);
                    ContentValues ccv = new ContentValues();
                    ccv.put("status", jsonObj.getInt("status"));
                    ccv.put("from_id", jsonObj.getInt("from_id"));
                    ccv.put("uid", jsonObj.getInt("uid"));
                    ccv.put("push_time", jsonObj.getString("push_time"));
                    ccv.put("nick", jsonObj.getString("nick"));
                    ccv.put("icon", jsonObj.getString("icon"));
                    if (jsonObj.getInt("status") == 0) {
                        JSONArray ss = new JSONArray(jsonObj.getString("notes"));
                        for (int j = 0; j < ss.length(); j++) {
                            JSONObject sss = ss.getJSONObject(j);
                            ccv.put("subject", sss.getString("subject"));
                            if (sss.has("video_img")) {
                                ccv.put("video_img", sss.getString("video_img"));
                            }
                            ccv.put("title", sss.getString("title"));
                            ccv.put("tags", sss.getString("tags"));
                            ccv.put("file_type", sss.getInt("file_type"));
                            ccv.put("sign", sss.getInt("sign"));
                            ccv.put("good_name", "");
                            ccv.put("good_image", "");
                            ccv.put("price", "");
                            ccv.put("sign", "");
                            ccv.put("maf_time", "");

                        }
                    } else if (jsonObj.getInt("status") == 1) {
                        JSONObject ss = new JSONObject(jsonObj.getString("goods"));
                        ccv.put("subject", "");
                        ccv.put("title", "");
                        ccv.put("file_type", "");
                        ccv.put("sign", "");
                        ccv.put("video_img", "");
                        ccv.put("good_name", ss.getString("good_name"));
                        ccv.put("good_image", ss.getString("good_image"));
                        ccv.put("price", ss.getString("price"));
                        ccv.put("maf_time", ss.getInt("maf_time"));
                    }
                    cache.add(ccv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}