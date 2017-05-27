package cn.com.shequnew.pages.config;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.com.shequnew.tools.AppManager;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class AppContext extends Application implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static AppContext appContext;
    public static ContentValues cv = new ContentValues();
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        MultiDex.install(this);
        mContext = this.getApplicationContext();
        Fresco.initialize(this);
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);
        // 获取系统默认的UncaughtException处理
        CrashReport.initCrashReport(getApplicationContext(), "23c6e1948f", false);

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }

    public static AppContext getInstance() {
        if (appContext == null) {
            return new AppContext();
        }
        return appContext;
    }

    public static void setInstance(AppContext instance) {
        AppContext.appContext = instance;
    }

    public void logoutApp() {
        AppManager.getAppManager().AppExit(mContext);
    }


}
