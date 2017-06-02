package cn.com.shequnew.pages.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class PublishDetailsShopFragment extends BasicFragment {

    @BindView(R.id.shop_note)
    Button shopNote;
    @BindView(R.id.shop_view_note)
    View shopViewNote;
    @BindView(R.id.shop_buy)
    Button shopBuy;
    @BindView(R.id.shop_buy_view)
    View shopBuyView;
    @BindView(R.id.shop_cal)
    Button shopCal;
    @BindView(R.id.shop_cal_view)
    View shopCalView;
    Unbinder unbinder;
    @BindView(R.id.publish_one)
    FrameLayout publishOne;


    private PublishDetailsShopCalFragment publishDetailsShopCalFragment;
    private PublishDetailsShopBuyFragment publishDetailsShopBuyFragment;
    private PublishDetailsShopDetailsFragment publishDetailsShopDetailsFragment;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_publish_details_shop, container, false);
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
        initShop();
    }

    private void initShop() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        publishDetailsShopDetailsFragment = new PublishDetailsShopDetailsFragment();
        fragmentTransaction.replace(R.id.publish_one, publishDetailsShopDetailsFragment);
        fragmentTransaction.commit();
    }


    @OnClick(R.id.shop_note)
    void shopBtn() {
        shop();
        initShop();
    }

    @OnClick(R.id.shop_buy)
    void buyBtn() {
        buy();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        publishDetailsShopBuyFragment = new PublishDetailsShopBuyFragment();
        fragmentTransaction.replace(R.id.publish_one, publishDetailsShopBuyFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.shop_cal)
    void calBtn() {
        cal();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        publishDetailsShopCalFragment = new PublishDetailsShopCalFragment();
        fragmentTransaction.replace(R.id.publish_one, publishDetailsShopCalFragment);
        fragmentTransaction.commit();


    }


    /**
     * 已售
     */
    private void buy() {
        shopNote.setTextColor(getResources().getColor(R.color.col_bg));
        shopViewNote.setBackgroundColor(getResources().getColor(R.color.grays));
        shopBuy.setTextColor(getResources().getColor(R.color.bd_top));
        shopBuyView.setBackgroundColor(getResources().getColor(R.color.bd_top));
        shopCal.setTextColor(getResources().getColor(R.color.col_bg));
        shopCalView.setBackgroundColor(getResources().getColor(R.color.grays));

    }

    /**
     * 下架
     */
    private void cal() {
        shopNote.setTextColor(getResources().getColor(R.color.col_bg));
        shopViewNote.setBackgroundColor(getResources().getColor(R.color.grays));
        shopBuy.setTextColor(getResources().getColor(R.color.col_bg));
        shopBuyView.setBackgroundColor(getResources().getColor(R.color.grays));
        shopCal.setTextColor(getResources().getColor(R.color.bd_top));
        shopCalView.setBackgroundColor(getResources().getColor(R.color.bd_top));

    }

    /**
     * 在售
     */
    private void shop() {
        shopNote.setTextColor(getResources().getColor(R.color.bd_top));
        shopViewNote.setBackgroundColor(getResources().getColor(R.color.bd_top));
        shopBuy.setTextColor(getResources().getColor(R.color.col_bg));
        shopBuyView.setBackgroundColor(getResources().getColor(R.color.grays));
        shopCal.setTextColor(getResources().getColor(R.color.col_bg));
        shopCalView.setBackgroundColor(getResources().getColor(R.color.grays));
    }
}
