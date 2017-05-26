package cn.com.shequnew.pages.activity;

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
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.BuyGoodsAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;

/**
 * 购买记录--我的订单
 */
public class BuyDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BuyGoodsAdapter.setOnClickLoction {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;

    @BindView(R.id.buy_all)
    Button buyAll;
    @BindView(R.id.buy_all_view)
    View buyAllView;
    @BindView(R.id.buy_payment)
    Button buyPayment;
    @BindView(R.id.buy_payment_view)
    View buyPaymentView;
    @BindView(R.id.buy_shipments)
    Button buyShipments;
    @BindView(R.id.buy_shipments_view)
    View buyShipmentsView;
    @BindView(R.id.buy_deliveries)
    Button buyDeliveries;
    @BindView(R.id.buy_deliveries_view)
    View buyDeliveriesView;
    @BindView(R.id.buy_evaluate)
    Button buyEvaluate;
    @BindView(R.id.buy_evaluate_view)
    View buyEvaluateView;
    @BindView(R.id.buy_list)
    PullToRefreshListView buyList;
    @BindView(R.id.collect_swi_content)
    SwipeRefreshLayout collectSwiContent;

    private Context context;
    private ListView listView;
    private int page = 1;
    private int shopId;
    private BuyGoodsAdapter buyGoodsAdapter;
    // 订单状态 0待发货 1待收货 2待评价 6待付款 all全部
    private String state = "all";

    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> buyLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_details);
        ButterKnife.bind(this);
        context = this;
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
        topTitle.setText("我的订单");
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

    public BuyGoodsAdapter buyGoodsAdapter() {
        if (buyGoodsAdapter == null) {
            buyGoodsAdapter = new BuyGoodsAdapter(context, buyLists, 1, this);
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
     * 刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);
    }

    /**
     * 全部
     */
    @OnClick(R.id.buy_all)
    void buyAllData() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        state = "all";
        buyAll();
        page = 1;
        new asyncTask().execute(1);
    }

    /**
     * 待付款
     */
    @OnClick(R.id.buy_payment)
    void buyPaymentData() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        state = "6";
        buyPayment();
        page = 1;
        new asyncTask().execute(1);

    }

    /**
     * 待发货
     */
    @OnClick(R.id.buy_shipments)
    void buyShipmentsData() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        state = "0";
        buyShipments();
        page = 1;
        new asyncTask().execute(1);
    }

    /**
     * 待收款
     */
    @OnClick(R.id.buy_deliveries)
    void buyDeliveriesData() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        state = "1";
        buyDeliveries();
        page = 1;
        new asyncTask().execute(1);
    }

    /**
     * 待评价
     */
    @OnClick(R.id.buy_evaluate)
    void buyEvaluateData() {
        buyList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        state = "2";
        buyEvaluate();
        page = 1;
        new asyncTask().execute(1);
    }

    /**
     * 全部
     */
    private void buyAll() {
        buyAll.setTextColor(getResources().getColor(R.color.bd_top));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        buyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyDeliveries.setTextColor(getResources().getColor(R.color.col_bg));
        buyDeliveriesView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyEvaluate.setTextColor(getResources().getColor(R.color.col_bg));
        buyEvaluateView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * 待付款
     */
    private void buyPayment() {
        buyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyPayment.setTextColor(getResources().getColor(R.color.bd_top));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        buyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyDeliveries.setTextColor(getResources().getColor(R.color.col_bg));
        buyDeliveriesView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyEvaluate.setTextColor(getResources().getColor(R.color.col_bg));
        buyEvaluateView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * 待发货
     */
    private void buyShipments() {
        buyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyShipments.setTextColor(getResources().getColor(R.color.bd_top));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        buyDeliveries.setTextColor(getResources().getColor(R.color.col_bg));
        buyDeliveriesView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyEvaluate.setTextColor(getResources().getColor(R.color.col_bg));
        buyEvaluateView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * 待收款
     */
    private void buyDeliveries() {
        buyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyDeliveries.setTextColor(getResources().getColor(R.color.bd_top));
        buyDeliveriesView.setBackgroundColor(getResources().getColor(R.color.bd_top));

        buyEvaluate.setTextColor(getResources().getColor(R.color.col_bg));
        buyEvaluateView.setBackgroundColor(getResources().getColor(R.color.grays));
    }

    /**
     * 待评价
     */
    private void buyEvaluate() {
        buyAll.setTextColor(getResources().getColor(R.color.col_bg));
        buyAllView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyPayment.setTextColor(getResources().getColor(R.color.col_bg));
        buyPaymentView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyShipments.setTextColor(getResources().getColor(R.color.col_bg));
        buyShipmentsView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyDeliveries.setTextColor(getResources().getColor(R.color.col_bg));
        buyDeliveriesView.setBackgroundColor(getResources().getColor(R.color.grays));

        buyEvaluate.setTextColor(getResources().getColor(R.color.bd_top));
        buyEvaluateView.setBackgroundColor(getResources().getColor(R.color.bd_top));
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
                    calBuy(shopId);
                    bundle.putInt("what", 2);
                    break;
                case 4:
                    htttpOrder();
                    bundle.putInt("what", 3);
                    break;
                case 5:
                    htttpOrd();
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
                    if (contentValues != null && contentValues.size() > 0) {
                        buyLists.addAll(contentValues);
                    }
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
                    /**待付款-取消订单*/
                    page = 1;
                    new asyncTask().execute(1);
                    break;
            }

        }
    }

    /**
     * 取消订单
     */
    private void calBuy(int id) {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", id + "");
            hashMap.put("state", "6");
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 首次加载请求
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidQuery");
            hashMap.put("id", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("page", page + "");
            hashMap.put("state", state);
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
                        note.put("maf_time", jsonObj.getInt("maf_time"));
                        note.put("goodsid", jsonObj.getInt("goodsid"));
                        note.put("num", jsonObj.getInt("num"));
                        note.put("status", jsonObj.getInt("status"));
                        note.put("state", jsonObj.getInt("state"));
                        note.put("ordermoney", jsonObj.getInt("ordermoney"));
                        note.put("ddid", jsonObj.getString("ddid"));
                        note.put("trade_name", jsonObj.getString("trade_name"));
                        note.put("money", jsonObj.getString("money"));
                        note.put("logistics", jsonObj.getString("logistics"));
                        note.put("logistics_num", jsonObj.getString("logistics_num"));
                        note.put("image", jsonObj.getString("image"));
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

    /**
     * 详情
     */
    @Override
    public void lin(int posit, int id, String ddid) {
        Intent intent = new Intent(context, BuyItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("state", buyLists.get(posit).getAsInteger("state"));
        bundle.putInt("status", buyLists.get(posit).getAsInteger("status"));
        bundle.putString("ddid", ddid);
        bundle.putInt("id", buyLists.get(posit).getAsInteger("id"));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 取消订单
     */
    @Override
    public void cal(int posit, int id, String ddid) {
        if (state.equals("6")) {
            shopId = buyLists.get(posit).getAsInteger("id");
            new asyncTask().execute(3);//待付款取消订单
        }
    }

    /**
     * 付款
     */
    @Override
    public void buy(int posit, int id, String ddid) {
        if (buyLists.get(posit).getAsInteger("state") == 0 && buyLists.get(posit).getAsInteger("status") == 0
                || buyLists.get(posit).getAsInteger("state") == 6 && buyLists.get(posit).getAsInteger("status") == 0) {
            //付款
            Intent intent = new Intent(context, BuyItemDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("state", buyLists.get(posit).getAsInteger("state"));
            bundle.putInt("status", buyLists.get(posit).getAsInteger("status"));
            bundle.putString("ddid", ddid);
            bundle.putInt("id", buyLists.get(posit).getAsInteger("id"));
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        if (buyLists.get(posit).getAsInteger("state") == 1 && buyLists.get(posit).getAsInteger("status") == 1) {
            //确认收货
            shopId = buyLists.get(posit).getAsInteger("id");
            new asyncTask().execute(5);
        }
        if (buyLists.get(posit).getAsInteger("state") == 2 && buyLists.get(posit).getAsInteger("status") == 1) {
            //评价
            Intent intent = new Intent(context, AppraiseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ddid", ddid);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }
        if (buyLists.get(posit).getAsInteger("state") == 3 && buyLists.get(posit).getAsInteger("status") == 1) {
            //退款中。。
            return;
        }
        if (buyLists.get(posit).getAsInteger("state") == 4 && buyLists.get(posit).getAsInteger("status") == 1) {
            //已完成
            return;
        }
        if (buyLists.get(posit).getAsInteger("state") == 0 && buyLists.get(posit).getAsInteger("status") == 1) {
            //申请退款
            shopId = buyLists.get(posit).getAsInteger("id");
            new asyncTask().execute(4);
        }
    }

    private void htttpOrder() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", shopId + "");
            hashMap.put("state", "0");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject get = new JSONObject(json);
                int error = get.getInt("error");
                if (error == 0) {
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void htttpOrd() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderidstate");
            hashMap.put("id", shopId + "");
            hashMap.put("state", "1");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject get = new JSONObject(json);
                int error = get.getInt("error");
                if (error == 0) {
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        new asyncTask().execute(1);

    }
}
