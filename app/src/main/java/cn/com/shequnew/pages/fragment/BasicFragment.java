package cn.com.shequnew.pages.fragment;


import android.app.Fragment;
import android.os.Handler;

import cn.com.shequnew.pages.prompt.Loading;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class BasicFragment extends Fragment {

    /**
     * 延迟线程消息
     */
    protected Handler mDelay;
    public Loading mLoading = null;

    protected void removeDelayMessage(int what) {
        if (mDelay != null && mDelay.hasMessages(what)) {
            mDelay.removeMessages(what);
        }
    }
    protected void setDelayMessage(int what, int time) {
        if (mDelay != null) {
            removeDelayMessage(what);
            // if (!isDestroy) {
            mDelay.sendEmptyMessageDelayed(what, time);
            // }
        }
    }

    // 关闭loading
    public void removeLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }
}
