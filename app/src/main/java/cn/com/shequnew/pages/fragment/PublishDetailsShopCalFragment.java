package cn.com.shequnew.pages.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ShopDetailsActivity;
import cn.com.shequnew.pages.adapter.UserGoodsPullAdapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.view.PullToRefreshSwipeMenuListView;
import cn.com.shequnew.pages.view.pulltorefresh.interfaces.IXListViewListener;
import cn.com.shequnew.pages.view.swipemenu.bean.SwipeMenu;
import cn.com.shequnew.pages.view.swipemenu.bean.SwipeMenuItem;
import cn.com.shequnew.pages.view.swipemenu.interfaces.OnMenuItemClickListener;
import cn.com.shequnew.pages.view.swipemenu.interfaces.SwipeMenuCreator;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class PublishDetailsShopCalFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener, IXListViewListener {


    Unbinder unbinder;
    @BindView(R.id.shop_list_pull_cal)
    PullToRefreshSwipeMenuListView shopListPullCal;
    @BindView(R.id.collect_swi_cal)
    SwipeRefreshLayout collectSwiCal;

    private List<ContentValues> contentShop = new ArrayList<>();
    private List<ContentValues> contentCol = new ArrayList<>();
    private UserGoodsPullAdapter goodsAdapter;//商品
    private Context context;
    private int page = 1;
    private Handler mHandler;
    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_publish_details_shop_cal, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        page = 1;
        initView();
        shopListPullCal.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    collectSwiCal.setEnabled(true);
                else
                    collectSwiCal.setEnabled(false);
            }
        });
        new asyncTask().execute(1);
    }

    private void initView() {
        collectSwiCal.setOnRefreshListener(this);
        goodsAdapter = new UserGoodsPullAdapter(context, contentCol);
        shopListPullCal.setAdapter(goodsAdapter);
        shopListPullCal.setPullLoadEnable(true);
        shopListPullCal.setPullRefreshEnable(false);
        shopListPullCal.setXListViewListener(this);
        mHandler = new Handler();
        final SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(Color.rgb(255, 0, 0)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(getResources().getColor(R.color.white));
                menu.addMenuItem(openItem);
            }
        };

        shopListPullCal.setMenuCreator(creator);
        shopListPullCal.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                id = contentCol.get(position).getAsInteger("id");
                deleteData();
            }
        });
        shopListPullCal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", contentCol.get(position - 1).getAsInteger("id"));
                bundle.putInt("uid", contentCol.get(position - 1).getAsInteger("uid"));
                intent.putExtras(bundle);
                intent.setClass(context, ShopDetailsActivity.class);
                context.startActivity(intent);
            }
        });


    }

    private void deleteData() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialog);
        dialog.setContentView(R.layout.back_login);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        dialog.getWindow().setAttributes(layoutParams);
        TextView title = (TextView) dialog.findViewById(R.id.title_con);
        TextView name = (TextView) dialog.findViewById(R.id.title_name);
        name.setVisibility(View.VISIBLE);
        title.setText("确定要删除吗？");
        Button cal = (Button) dialog.findViewById(R.id.calen);
        Button sure = (Button) dialog.findViewById(R.id.sure);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new asyncTask().execute(3);
                dialog.dismiss();
            }
        });
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);
    }

    @Override
    public void onRefresha() {

    }

    @Override
    public void onLoadMore() {
        page = page + 1;
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
                    htttpCollectDetale();
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
                    if (contentShop != null && contentShop.size() > 0) {
                        if (contentCol != null && contentCol.size() > 0) {
                            contentCol.clear();
                        }
                        contentCol.addAll(contentShop);
                    }
                    goodsAdapter.notifyDataSetChanged();
                    collectSwiCal.setRefreshing(false);//刷新完成
                    break;
                case 2:
                    if (contentShop != null && contentShop.size() > 0) {
                        contentCol.addAll(contentShop);
                    }
                    goodsAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    new asyncTask().execute(1);
                    break;
            }
        }
    }

    /**
     * 首次加载请求
     */
    private void htttpCollectDetale() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Trade.mygoodsupdate");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("id", id + "");
            hashMap.put("status", "0");
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 首次加载请求
     */
    private void htttpCollect() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Trade.myGoods");
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


    private void clearList() {
        if (contentShop == null) {
            contentShop = new ArrayList<>();
        }
        if (contentShop.size() > 0) {
            contentShop.clear();
        }
    }

    /**
     * 解析数据
     */
    private void listXml(String data) {
        clearList();
        try {
            JSONObject obj = new JSONObject(data);
            if (!obj.has("data")) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        shopListPullCal.setPullLoadEnable(false);
                        shopListPullCal.stopLoadMore();
                    }
                }, 100);
            } else {
                JSONArray jsonArrGood = new JSONArray(obj.getString("data"));
                if (jsonArrGood.length() > 0) {
                    for (int i = 0; i < jsonArrGood.length(); i++) {
                        JSONObject jsonObj = jsonArrGood.getJSONObject(i);
                        ContentValues note = new ContentValues();
                        if (jsonObj.getInt("status") == 3) {
                            note.put("id", jsonObj.getInt("id"));
                            note.put("uid", jsonObj.getInt("uid"));
                            note.put("cid", jsonObj.getInt("cid"));
                            note.put("status", jsonObj.getInt("status"));
                            note.put("good_name", jsonObj.getString("good_name"));
                            note.put("good_image", jsonObj.getString("good_image"));
                            note.put("price", jsonObj.getString("price"));
                            note.put("maf_time", jsonObj.getString("maf_time"));
                            note.put("nick", jsonObj.getString("nick"));
                            note.put("icon", jsonObj.getString("icon"));
                            contentShop.add(note);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
