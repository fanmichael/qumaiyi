package cn.com.shequnew.pages.config;

import android.app.Application;
import android.content.ContentValues;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class AppContext extends Application implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static AppContext appContext;
    public static ContentValues cv = new ContentValues();


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        MultiDex.install(this);
        Fresco.initialize(this);
        //EaseUI.getInstance().init(this, null);
       // EMClient.getInstance().setDebugMode(true);
        // 获取系统默认的UncaughtException处理
        CrashReport.initCrashReport(getApplicationContext(), "23c6e1948f", false);

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }


}
