package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.os.Bundle;

import cn.com.shequnew.R;


public class ElevancyShopActivity extends BaseActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevancy_shop);
        context = this;
    }
}
