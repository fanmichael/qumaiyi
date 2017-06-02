package cn.yshstudio.originalproduct.pages.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.activity.BuyDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.CollectDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.InstallDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.MaterialDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.PublishDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.SellerDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.SellerlDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.ShopBuyActivity;
import cn.yshstudio.originalproduct.pages.activity.SiteDetailsActivity;
import cn.yshstudio.originalproduct.pages.activity.WalletActivity;
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class NewsFragment extends BasicFragment {

    @BindView(R.id.my_icon)
    SimpleDraweeView myIcon;
    @BindView(R.id.my_nice)
    TextView myNice;
    @BindView(R.id.my_image_layout)
    LinearLayout myImageLayout;
    @BindView(R.id.my_material_layout)
    LinearLayout myMaterialLayout;
    @BindView(R.id.my_sellerl_layout)
    LinearLayout mySellerlLayout;
    @BindView(R.id.my_buy_layout)
    LinearLayout myBuyLayout;
    @BindView(R.id.my_publish_layout)
    LinearLayout myPublishLayout;
    @BindView(R.id.my_collect_layout)
    LinearLayout myCollectLayout;
    @BindView(R.id.my_site_layout)
    LinearLayout mySiteLayout;
    @BindView(R.id.my_install_layout)
    LinearLayout myInstallLayout;
    Unbinder unbinder;
    @BindView(R.id.my_sellerl_layout_details)
    LinearLayout mySellerlLayoutDetails;
    @BindView(R.id.buy_type)
    TextView buyType;
    @BindView(R.id.my_image_icon)
    SimpleDraweeView myImageIcon;
    @BindView(R.id.my_sellerl_layout_shop_buy)
    LinearLayout mySellerlLayoutShopBuy;
    @BindView(R.id.my_wallet_layout)
    LinearLayout myWalletLayout;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
    }

    /**
     * 加载数据
     */
    private void initView() {
        if (!AppContext.cv.containsKey("icon")) {
            return;
        }
        Uri imageUri = Uri.parse(AppContext.cv.getAsString("icon"));
        ValidData.load(imageUri, myIcon, 60, 60);
        ValidData.load(imageUri, myImageIcon, 250, 250);
        myNice.setText(AppContext.cv.getAsString("nick"));
        if ("0".equals(AppContext.cv.getAsString("merchant"))) {
            mySellerlLayoutDetails.setVisibility(View.GONE);
            mySellerlLayoutShopBuy.setVisibility(View.GONE);
            mySellerlLayout.setVisibility(View.VISIBLE);
            buyType.setText("购买记录");
        } else {
            buyType.setText("我的订单");
            mySellerlLayoutDetails.setVisibility(View.VISIBLE);
            mySellerlLayoutShopBuy.setVisibility(View.VISIBLE);
            mySellerlLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 101) {
            Uri imageUri = Uri.parse(AppContext.cv.getAsString("icon"));
            ValidData.load(imageUri, myIcon, 60, 60);
            ValidData.load(imageUri, myImageIcon, 250, 250);
        }
    }

    /**
     * 个人资料
     */
    @OnClick(R.id.my_material_layout)
    void material() {
        Intent materialIntent = new Intent();
        materialIntent.setClass(mContext, MaterialDetailsActivity.class);
        startActivityForResult(materialIntent, 101);
//        mContext.startActivity(materialIntent);
    }

    /**
     * 我的钱包
     */
    @OnClick(R.id.my_wallet_layout)
    void wallet() {
        Intent walletIntent = new Intent();
        walletIntent.setClass(mContext, WalletActivity.class);
        mContext.startActivity(walletIntent);
    }

    /**
     * 卖主资料
     */
    @OnClick(R.id.my_sellerl_layout_details)
    void sellerDetails() {
        Intent sellerlDetailIntent = new Intent();
        sellerlDetailIntent.setClass(mContext, SellerDetailsActivity.class);
        mContext.startActivity(sellerlDetailIntent);

    }

    /**
     * 发货管理
     */
    @OnClick(R.id.my_sellerl_layout_shop_buy)
    void shopBuy() {
        Intent shopBuyIntent = new Intent();
        shopBuyIntent.setClass(mContext, ShopBuyActivity.class);
        mContext.startActivity(shopBuyIntent);

    }

    /**
     * 申请卖主
     */
    @OnClick(R.id.my_sellerl_layout)
    void sellerl() {
        Intent sellerlIntent = new Intent();
        sellerlIntent.setClass(mContext, SellerlDetailsActivity.class);
        mContext.startActivity(sellerlIntent);
    }


    /**
     * 购买记录----我的订单
     */
    @OnClick(R.id.my_buy_layout)
    void buy() {
        Intent buyIntent = new Intent();
        buyIntent.setClass(mContext, BuyDetailsActivity.class);
        mContext.startActivity(buyIntent);

    }

    /**
     * 我的发布
     */
    @OnClick(R.id.my_publish_layout)
    void publish() {
        Intent publishIntent = new Intent();
        publishIntent.setClass(mContext, PublishDetailsActivity.class);
        mContext.startActivity(publishIntent);
    }

    /**
     * 我的收藏
     */
    @OnClick(R.id.my_collect_layout)
    void collect() {
        Intent collectIntent = new Intent();
        collectIntent.setClass(mContext, CollectDetailsActivity.class);
        mContext.startActivity(collectIntent);
    }

    /**
     * 我的地址
     */
    @OnClick(R.id.my_site_layout)
    void site() {
        Intent siteIntent = new Intent();
        siteIntent.setClass(mContext, SiteDetailsActivity.class);
        mContext.startActivity(siteIntent);

    }

    /**
     * 设置
     */
    @OnClick(R.id.my_install_layout)
    void install() {
        Intent installIntent = new Intent();
        installIntent.setClass(mContext, InstallDetailsActivity.class);
        mContext.startActivity(installIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }
}
