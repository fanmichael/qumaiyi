package cn.com.shequnew.pages.config;

import android.app.Application;
import android.content.ContentValues;

import com.facebook.drawee.backends.pipeline.Fresco;

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
        Fresco.initialize(this);
        // 获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }


}
