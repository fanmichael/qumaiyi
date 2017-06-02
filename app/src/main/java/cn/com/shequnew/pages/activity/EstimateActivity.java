package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import cn.com.shequnew.pages.adapter.EstimateAdapter;
import cn.com.shequnew.pages.http.HttpConnectTool;

/**
 * 评价
 */
public class EstimateActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.estimate_list)
    PullToRefreshListView buyList;

    private ListView listView;
    private Context context;
    private int id;
    private int page = 1;
    private EstimateAdapter estimateAdapter;
    private List<ContentValues> values = new ArrayList<>();
    private List<ContentValues> estimates = new ArrayList<>();
    private List<List<ContentValues>> esList = new ArrayList<>();
    private List<List<ContentValues>> estimateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);
        context = this;
        ButterKnife.bind(this);
        initViewData();
        new asyncTask().execute(1);
    }

    private void initViewData() {
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        topTitle.setText("评价");

        buyList.setOnRefreshListener(onListener2);
        buyList.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        buyList.setMode(PullToRefreshBase.Mode.DISABLED);
        buyList.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        buyList.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = buyList.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        listView.setAdapter(estimateAdapter());
    }

    private EstimateAdapter estimateAdapter() {
        if (estimateAdapter == null) {
            estimateAdapter = new EstimateAdapter(context, estimates, estimateList);
            listView.setAdapter(estimateAdapter);
        }

        return estimateAdapter;
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


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
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
                    htttpAll();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpAll();
                    bundle.putInt("what", 2);
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
                    if (estimates.size() > 0) {
                        estimates.clear();
                    }
                    if (estimateList.size() > 0) {
                        estimateList.clear();
                    }
                    estimates.addAll(values);
                    estimateList.addAll(esList);
                    estimateAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (values.size() > 0) {
                        estimates.addAll(values);
                    }
                    if (esList.size() > 0) {
                        estimateList.addAll(esList);
                    }
                    estimateAdapter.notifyDataSetChanged();
                    buyList.onRefreshComplete();
                    buyList.setMode(PullToRefreshBase.Mode.DISABLED);
                    Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


    /**
     * 首次加载请求
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.getGoodsContent");
            hashMap.put("id", id + "");
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
        if (values.size() > 0) {
            values.clear();
        }
        if (esList.size() > 0) {
            esList.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            int error = obj.getInt("error");
            if (error == 0) {
                JSONArray jsonArrNote = new JSONArray(obj.getString("data"));
                if (jsonArrNote.length() > 0) {
                    for (int i = 0; i < jsonArrNote.length(); i++) {
                        JSONObject jsonObj = jsonArrNote.getJSONObject(i);
                        ContentValues note = new ContentValues();
                        note.put("comment", jsonObj.getString("comment"));
                        note.put("time", jsonObj.getString("time"));
                        note.put("nick", jsonObj.getString("nick"));
                        note.put("icon", jsonObj.getString("icon"));
                        int num = 0;
                        JSONObject jsonObjs = new JSONObject(jsonObj.getString("num"));
                        for (int j = 0; j < jsonObjs.length(); j++) {
                            num++;
                        }
                        note.put("num", num);
                        values.add(note);
                        // 接口数据
                        JSONArray commJson = new JSONArray(jsonObj.getString("img"));
                        if (commJson.length() > 0) {
                            List<ContentValues> cvx = new ArrayList<>();
                            for (int j = 0; j < commJson.length(); j++) {
                                ContentValues content = new ContentValues();
                                content.put("image", commJson.get(j).toString());
                                cvx.add(content);
                            }
                            esList.add(i, cvx);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
