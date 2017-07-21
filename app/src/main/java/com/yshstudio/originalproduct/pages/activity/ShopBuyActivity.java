package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
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

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.BuyGoodsShopAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;


public class ShopBuyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BuyGoodsShopAdapter.setOnClickLoction {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;

    @BindView(R.id.shop_buy_all)
    Button shopBuyAll;
    @BindView(R.id.shop_buy_all_view)
    View buyAllView;

    @BindView(R.id.shop_buy_payment)
    Button shopBuyPayment;
    @BindView(R.id.shop_buy_payment_view)
    View buyPaymentView;

    @BindView(R.id.shop_buy_shipments)
    Button shopBuyShipments;
    @BindView(R.id.shop_buy_shipments_view)
    View buyShipmentsView;

    @BindView(R.id.shop_buy_order)
    Button shopBuyOder;
    @BindView(R.id.shop_buy_order_view)
    View buyOderView;

    @BindView(R.id.shop_buy_list)
    PullToRefreshListView buyList;
    @BindView(R.id.shop__swi_content)
    SwipeRefreshLayout collectSwiContent;


    private Context context;
    private ListView listView;
    private int page = 1;
    private int number;
    private ContentValues cv = new ContentValues();
    private String satrt = "0";
    private int shopId;
    private BuyGoodsShopAdapter buyGoodsAdapter;
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> buyLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_buy);
        context = this;
        ButterKnife.bind(this);
        initView();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    collectSwiContent.setEnabled(true);
                else
                    collectSwiContent.setEnabled(false);
            }
        });
    }

    private void initView() {
        topTitle.setText("发货管理");
        topRegitTitle.setVisibility(View.GONE);
        collectSwiContent.setOnRefreshListener(this);
        buyList.setOnRefreshListener(onListener2);
        buyList.getLoadingLayoutProxy(false, true).setPullLabel("上拉中");
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        buyList.getLoadingLayoutProxy(false, true).setRefreshingLabel("刷新中");
        buyList.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");
        // 获得可刷新的gridView
        listView = buyList.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.color.bgcolor_windowbg));
        listView.setDividerHeight(6);
        listView.setAdapter(buyGoodsAdapter());
        new asyncTask().execute(1);
    }

    public BuyGoodsShopAdapter buyGoodsAdapter() {
        if (buyGoodsAdapter == null) {
            buyGoodsAdapter = new BuyGoodsShopAdapter(context, buyLists, this);
            listView.setAdapter(buyGoodsAdapter);
        }
        return buyGoodsAdapter;
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
     * 已退款
     */
    @OnClick(R.id.shop_buy_order)
    void btnOrder() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        shopBuyOrder();
        satrt = "3";
        new asyncTask().execute(1);
    }

    /**
     * 未发货
     */
    @OnClick(R.id.shop_buy_all)
    void bntShop() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        satrt = "0";
        shopBuyAll();
        new asyncTask().execute(1);
    }

    /**
     * 已发货哦
     */
    @OnClick(R.id.shop_buy_payment)
    void btnPayment() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        satrt = "1";
        shopBuyPayment();
        new asyncTask().execute(1);
    }

    /**
     * 以签收
     */
    @OnClick(R.id.shop_buy_shipments)
    void btnShip() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        satrt = "2";
        shopBuyShipments();
        new asyncTask().execute(1);
    }


    /**
     * w未发货
     */
    private void shopBuyOrder() {
        shopBuyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));


        shopBuyOder.setTextColor(getResources().getColor(R.color.bd_top));
        buyOderView.setBackgroundColor(getResources().getColor(R.color.bd_top));

    }


    /**
     * w未发货
     */
    private void shopBuyAll() {
        shopBuyAll.setTextColor(getResources().getColor(R.color.bd_top));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        shopBuyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyOder.setTextColor(getResources().getColor(R.color.col_bg));
        buyOderView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * w已发货
     */
    private void shopBuyPayment() {
        shopBuyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyPayment.setTextColor(getResources().getColor(R.color.bd_top));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        shopBuyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyOder.setTextColor(getResources().getColor(R.color.col_bg));
        buyOderView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * w已签收
     */
    private void shopBuyShipments() {
        shopBuyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        shopBuyShipments.setTextColor(getResources().getColor(R.color.bd_top));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        shopBuyOder.setTextColor(getResources().getColor(R.color.col_bg));
        buyOderView.setBackgroundColor(getResources().getColor(R.color.grays));
    }


    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);
    }

    @Override
    public void lin(int posit, int id, int state, int status, String ddid, String logistics, String logistics_num) {
        //详情页
        Intent intent=new Intent(context,ShopBuyDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("id",id);
        bundle.putInt("state",state);
        bundle.putInt("status",status);
        bundle.putString("ddid",ddid);
        bundle.putString("logistics",logistics);
        bundle.putString("logistics_num",logistics_num);
        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    @Override
    public void cal(int posit, int id, String ddid) {

    }


    @Override
    public void buy(int posit, int id, String ddid) {
        //购买
        if (contentValues.get(posit).getAsInteger("state") == 0 && contentValues.get(posit).getAsInteger("status") == 0) {
            return;
//            holder.buy.setText("等待支付...");
        }
        if (contentValues.get(posit).getAsInteger("state") == 0 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("发货");
            Intent intent = new Intent(context, ExpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ddid", ddid);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        }
        if (contentValues.get(posit).getAsInteger("state") == 2 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("申请提现");
//            number = id;
//            new asyncTask().execute(3);
            return;
        }


        if (contentValues.get(posit).getAsInteger("state") == 3 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("申请退款...");
            return;

        }

        if (contentValues.get(posit).getAsInteger("state") == 4 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("退款成功");
            return;
        }

        if (contentValues.get(posit).getAsInteger("state") == 5 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("退款失败");
            return;
        }


        if (contentValues.get(posit).getAsInteger("state") == 7 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("提现审核中...");
            return;
        }

        if (contentValues.get(posit).getAsInteger("state") == 8 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("提现成功");
            return;
        }
        if (contentValues.get(posit).getAsInteger("state") == 9 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("提现失败");
            return;
        }
        if (contentValues.get(posit).getAsInteger("state") == 10 && contentValues.get(posit).getAsInteger("status") == 1) {
//            holder.buy.setText("提现失败");
            return;
        }
        /**
         * 查看物流
         * */
        if (contentValues.get(posit).getAsInteger("state") == 1 && contentValues.get(posit).getAsInteger("status") == 1) {
            Intent intent = new Intent(context, LogisticsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("com", contentValues.get(posit).getAsString("logistics"));
            bundle.putString("no", contentValues.get(posit).getAsString("logistics_num"));
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (100 == resultCode) {
            page = 1;
            new asyncTask().execute(1);
        }

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
                case 3:
                    //提现
                    htttpOrderid();
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
                    collectSwiContent.setRefreshing(false);//刷新完成
                    if (buyLists == null) {
                        buyLists = new ArrayList<>();
                    }
                    if (buyLists.size() > 0) {
                        buyLists.clear();
                    }
                    buyLists.addAll(contentValues);
                    buyGoodsAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (buyLists == null) {
                        buyLists = new ArrayList<>();
                    }
                    if (contentValues != null && contentValues.size() > 0) {
                        buyLists.addAll(contentValues);
                        buyGoodsAdapter.notifyDataSetChanged();
                        buyList.onRefreshComplete();
                    } else {
                        buyList.onRefreshComplete();
                        buyList.setMode(PullToRefreshBase.Mode.DISABLED);
                        Toast.makeText(context, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    page = 1;
                    new asyncTask().execute(1);
                    break;

            }

        }
    }


    /**
     * 申请提现
     */
    private void htttpOrderid() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", number + "");
            hashMap.put("state", "7");
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 所有的数据
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.getOrderInfo");
            hashMap.put("uid", SharedPreferenceUtil.read("id","") + "");
            hashMap.put("state", satrt);
            hashMap.put("type", "1");
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
        if (contentValues == null) {
            contentValues = new ArrayList<>();
        }
        if (contentValues.size() > 0) {
            contentValues.clear();
        }
        try {
            JSONObject obj = new JSONObject(data);
            if (obj.has("data")) {
                JSONArray jsonArrNote = new JSONArray(obj.getString("data"));
                if (jsonArrNote.length() > 0) {
                    for (int i = 0; i < jsonArrNote.length(); i++) {
                        JSONObject jsonObj = jsonArrNote.getJSONObject(i);
                        ContentValues note = new ContentValues();
                        note.put("id", jsonObj.getInt("id"));
                        note.put("uid", jsonObj.getInt("uid"));
                        note.put("shid", jsonObj.getInt("shid"));
                        note.put("flag", jsonObj.getInt("flag"));
                        note.put("address", jsonObj.getInt("address"));
                        note.put("maf_time", jsonObj.getInt("maf_time"));
                        note.put("goodsid", jsonObj.getInt("goodsid"));
                        note.put("num", jsonObj.getInt("num"));
                        note.put("status", jsonObj.getInt("status"));
                        note.put("state", jsonObj.getInt("state"));
                        note.put("channel", jsonObj.getInt("channel"));
                        note.put("ddid", jsonObj.getString("ddid"));
                        note.put("trade_name", jsonObj.getString("trade_name"));
                        note.put("money", jsonObj.getString("money"));
                        note.put("logistics", jsonObj.getString("logistics"));////com//////
                        note.put("logistics_num", jsonObj.getString("logistics_num"));///no////////////
                        note.put("good_image", jsonObj.getString("good_image"));
                        note.put("channel_num", jsonObj.getString("channel_num"));
                        note.put("totalmoney", jsonObj.getString("totalmoney"));
                        note.put("time", jsonObj.getString("time"));
                        note.put("nick", jsonObj.getString("nick"));
                        note.put("icon", jsonObj.getString("icon"));
                        contentValues.add(note);
                    }
                }
            } else {
                Toast.makeText(context, "没有数据了！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
