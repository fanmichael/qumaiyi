package cn.com.shequnew.pages.activity;

import android.os.Bundle;
import android.os.Handler;

import com.trello.rxlifecycle.components.RxActivity;

import cn.com.shequnew.pages.prompt.Loading;

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
