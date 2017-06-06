package com.yshstudio.originalproduct.pages.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.trello.rxlifecycle.components.RxActivity;

import com.yshstudio.originalproduct.pages.prompt.Loading;
import com.yshstudio.originalproduct.tools.Util;

/**
 * 父类
 * */
public class BaseActivity extends RxActivity{

    protected Loading mLoading = null;
    /**
     * 延迟线程消息
     */
    protected Handler mDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        if (!Util.isNetworkAvailable(BaseActivity.this)) {
            Toast.makeText(getApplicationContext(), "检查网络！", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void destroyActitity() {
        finish();

    }

    protected void removeDelayMessage(int what) {
        if (mDelay != null && mDelay.hasMessages(what)) {
            mDelay.removeMessages(what);
        }
    }
    protected void setDelayMessage(int what, int time) {
        if (mDelay != null) {
            removeDelayMessage(what);
                mDelay.sendEmptyMessageDelayed(what, time);
        }
    }

    // 关闭loading
    protected void removeLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

}
