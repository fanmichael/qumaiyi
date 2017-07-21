package com.yshstudio.originalproduct.pages.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.prompt.Loading;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class BasicFragment extends Fragment {

    /**
     * 延迟线程消息
     */
    protected Handler mDelay;
    public Loading mLoading = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( AppContext.cv==null){
            AppContext.cv=new ContentValues();
        }
        AppContext.cv.put("id", Integer.valueOf(SharedPreferenceUtil.read("id","")));//标记
    }

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
