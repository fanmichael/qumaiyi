package com.yshstudio.originalproduct.pages.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.yshstudio.originalproduct.pages.config.AppContext;

/**
 * Created by Administrator on 2017/6/3 0003.
 */

public class ScreenSize {
    private static DisplayMetrics mDm = null;

    /**
     * 返回屏幕宽度值 px
     *
     *            调用上下文
     * @return int 宽度
     */
    public static int getWidth() {
        DisplayMetrics dm = initSize();
        return dm.widthPixels;
    }

    /**
     * 返回屏幕高度值 px
     *
     *            调用上下文
     * @return int 高度
     */
    public static int getHeight() {
        DisplayMetrics dm = initSize();
        return dm.heightPixels;
    }

    public static float getFloatDensity() {
        DisplayMetrics dm = initSize();
        return dm.density;
    }

    public static int getIntDensity() {
        DisplayMetrics dm = initSize();
        return dm.densityDpi;
    }


    /**
     * 初始化屏幕尺寸
     *
     *            调用上下文
     */
    public static DisplayMetrics initSize() {
        if(mDm == null) {
            // 获得系统服务
            WindowManager manager = (WindowManager) AppContext.appContext
                    .getSystemService(Context.WINDOW_SERVICE);
            // 获得尺寸信息
            mDm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(mDm);
        }
        return mDm;
    }

    public static int getRealHeight() {
        int screenHeight = getHeight();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_HOME);

        if (hasBackKey && hasHomeKey) {
            // 没有虚拟按键
        } else {
            // 有虚拟按键：99%可能。
            int keyHeight = dp2px(48);
            screenHeight += keyHeight;
        }

        return screenHeight;
    }

    /** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getFloatDensity() + 0.5f);
    }

    /** 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getFloatDensity() + 0.5f);
    }
}
