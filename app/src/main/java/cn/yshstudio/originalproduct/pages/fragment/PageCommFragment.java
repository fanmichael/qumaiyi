package cn.yshstudio.originalproduct.pages.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.activity.ChoseNewsActivity;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class PageCommFragment extends BasicFragment {
    @BindView(R.id.pages_home)
    Button pagesHome;
    @BindView(R.id.page_shop)
    Button pageShop;
    @BindView(R.id.page_comm_frm)
    FrameLayout pageCommFrm;
    Unbinder unbinder;
    @BindView(R.id.chose_news)
    ImageView choseNews;


    private PagesFragment pagesFragment;
    private CommunityFragment communityFragment;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.page_comm_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        pageView();
        initView();
    }


    @OnClick(R.id.chose_news)
    void chose(){
        Intent intent = new Intent();
        intent.setClass(context, ChoseNewsActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.pages_home)
    void page() {
        pageView();
        initView();
    }

    @OnClick(R.id.page_shop)
    void shop() {
        shopView();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        communityFragment = new CommunityFragment();
        fragmentTransaction.replace(R.id.page_comm_frm, communityFragment);
        fragmentTransaction.commit();
    }

    /**
     * 加载Frment
     */
    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        pagesFragment = new PagesFragment();
        fragmentTransaction.replace(R.id.page_comm_frm, pagesFragment);
        fragmentTransaction.commit();
    }

    /**
     * 社区
     */
    private void pageView() {
        pagesHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbgone));
        pagesHome.setText("社区");
        pagesHome.setTextColor(getResources().getColor(R.color.white));

        pageShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbg));
        pageShop.setText("交易");
        pageShop.setTextColor(getResources().getColor(R.color.bd_top));
    }


    /**
     * 交易
     */
    private void shopView() {
        pagesHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbg));
        pagesHome.setText("社区");
        pagesHome.setTextColor(getResources().getColor(R.color.bd_top));

        pageShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbgone));
        pageShop.setText("交易");
        pageShop.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }
}
