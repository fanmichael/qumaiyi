package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
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
import cn.com.shequnew.pages.adapter.SellerDaetailsActivity;
import cn.com.shequnew.pages.adapter.SellerGoodsAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.tools.ValidData;


/**
 * 卖主资料
 */
public class SellerDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.seller_data)
    LinearLayout sellerData;
    @BindView(R.id.seller_detail)
    LinearLayout sellerDetail;
    @BindView(R.id.seller_list)
    PullToRefreshListView sellerList;
    @BindView(R.id.collect_swi_content)
    SwipeRefreshLayout collectSwiContent;
    @BindView(R.id.seller_icon)
    SimpleDraweeView sellerIcon;
    @BindView(R.id.seller_nick)
    TextView sellerNick;
    private ListView listView;

    private Context context;
    private int page = 1;
    private ContentValues user = new ContentValues();
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> users = new ArrayList<>();
    private SellerGoodsAdapter sellerGoodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);
        ButterKnife.bind(this);
        context = this;
        initView();
        new asyncTask().execute(1);
    }

    /**
     * 个人主页
     */
    @OnClick(R.id.seller_data)
    void sellerData() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", user.getAsInteger("uid"));
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * 买家资质
     */
    @OnClick(R.id.seller_detail)
    void sellerDaetails() {
        Intent intent = new Intent(context, SellerDaetailsActivity.class);
        context.startActivity(intent);
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private void initView() {
        topTitle.setText("卖主资料");
        topRegitTitle.setVisibility(View.GONE);
        collectSwiContent.setOnRefreshListener(this);
        sellerList.setOnRefreshListener(onListener2);
        sellerList.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        sellerList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        sellerList.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        sellerList.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = sellerList.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        listView.setAdapter(noteAdapter());
    }


    public SellerGoodsAdapter noteAdapter() {
        if (sellerGoodsAdapter == null) {
            sellerGoodsAdapter = new SellerGoodsAdapter(context, users);
            listView.setAdapter(sellerGoodsAdapter);
        }
        return sellerGoodsAdapter;
    }

    private void initData() {
        Uri imageUri = Uri.parse(user.getAsString("u_pic").trim());
        ValidData.load(imageUri, sellerIcon, 60, 60);
        sellerNick.setText(user.getAsString("name"));
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


    @Override
    public void onRefresh() {
        page = 1;
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
                    htttpMerchant();
                    htttpGoods();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpGoods();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    htttpGoods();
                    bundle.putInt("what", 3);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            switch (what) {
                case 1:
                    if (contentValues.size() > 0) {
                        if (users != null && users.size() > 0) {
                            users.clear();
                        }
                        users.addAll(contentValues);
                    }
                    initData();
                    sellerGoodsAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    collectSwiContent.setRefreshing(false);//刷新完成
                    if (contentValues.size() > 0) {
                        if (users != null && users.size() > 0) {
                            users.clear();
                        }
                        users.addAll(contentValues);
                    }
                    sellerGoodsAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    if (contentValues.size() > 0) {
                        users.addAll(contentValues);
                        sellerGoodsAdapter.notifyDataSetChanged();
                        sellerList.onRefreshComplete();
                    } else {
                        sellerGoodsAdapter.notifyDataSetChanged();
                        sellerList.onRefreshComplete();
                        sellerList.setMode(PullToRefreshBase.Mode.DISABLED);
                        Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }


    /**
     * 头部数据
     */
    private void htttpMerchant() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Merchant.checkMerchantStatus");
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
     * 下部数据
     */
    private void htttpGoods() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Goods.getSomeoneGoods");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("page", page + "");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                listXmlGoods(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据
     */
    private void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject jsonArrGood = new JSONObject(obj.getString("data"));
            user.put("id", jsonArrGood.getInt("id"));
            user.put("uid", jsonArrGood.getInt("uid"));
            user.put("name", jsonArrGood.getString("name"));
            user.put("u_pic", jsonArrGood.getString("u_pic"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析数据
     */
    private void listXmlGoods(String data) {
        if (contentValues == null) {
            contentValues = new ArrayList<>();
        }
        if (contentValues.size() > 0) {
            contentValues.clear();
        }
        try {
            JSONObject obj = new JSONObject(data);
            if (obj.has("data")) {
                JSONArray jsonArrGood = new JSONArray(obj.getString("data"));
                if (jsonArrGood.length() > 0) {
                    for (int i = 0; i < jsonArrGood.length(); i++) {
                        JSONObject jsonObj = jsonArrGood.getJSONObject(i);
                        ContentValues note = new ContentValues();
                        note.put("id", jsonObj.getInt("id"));
                        note.put("uid", jsonObj.getInt("uid"));
                        note.put("good_name", jsonObj.getString("good_name"));
                        note.put("good_image", jsonObj.getString("good_image"));
                        note.put("price", jsonObj.getString("price"));
                        note.put("time", jsonObj.getString("time"));
                        note.put("user_nick", jsonObj.getString("user_nick"));
                        note.put("user_icon", jsonObj.getString("user_icon"));
                        contentValues.add(note);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
