package cn.com.shequnew.pages.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.fragment.PublishDetailsContentFragment;
import cn.com.shequnew.pages.fragment.PublishDetailsShopFragment;


/**
 * 我的发布
 */
public class PublishDetailsActivity extends FragmentActivity {

    @BindView(R.id.publish_frm)
    FrameLayout publishFrm;
    @BindView(R.id.btn_deal)
    Button btnDeal;
    @BindView(R.id.btn_deal_shop)
    Button btnDealShop;
    @BindView(R.id.image_back)
    ImageView imageBack;


    private Context context;
    private PublishDetailsContentFragment publishDetailsContentFragment;
    private PublishDetailsShopFragment publishDetailsShopFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_details);
        ButterKnife.bind(this);
        context = this;
        initView();
        con();
    }

    @OnClick(R.id.image_back)
    void back() {
        finish();
    }

    /**
     * 内容
     */
    private void con() {
        btnDeal.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbgone));
        btnDeal.setText("内容");
        btnDeal.setTextColor(getResources().getColor(R.color.white));
        btnDealShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbg));
        btnDealShop.setText("商品");
        btnDealShop.setTextColor(getResources().getColor(R.color.bd_top));
    }

    /**
     * 商品
     */
    private void shop() {
        btnDeal.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbg));
        btnDeal.setText("内容");
        btnDeal.setTextColor(getResources().getColor(R.color.bd_top));
        btnDealShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbgone));
        btnDealShop.setText("商品");
        btnDealShop.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 加载Frment
     */
    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        publishDetailsContentFragment = new PublishDetailsContentFragment();
        fragmentTransaction.replace(R.id.publish_frm, publishDetailsContentFragment);
        fragmentTransaction.commit();
    }


    @OnClick(R.id.btn_deal)
    void deal() {
        con();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (publishDetailsContentFragment == null) {
            publishDetailsContentFragment = new PublishDetailsContentFragment();
        }
        transaction.replace(R.id.publish_frm, publishDetailsContentFragment);
        transaction.commit();
    }

    @OnClick(R.id.btn_deal_shop)
    void shopDeal() {
        shop();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (publishDetailsShopFragment == null) {
            publishDetailsShopFragment = new PublishDetailsShopFragment();
        }
        transaction.replace(R.id.publish_frm, publishDetailsShopFragment);
        transaction.commit();
    }


}
