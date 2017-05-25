package cn.com.shequnew.pages.config;

import android.app.Application;
import android.content.ContentValues;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.Thread.UncaughtExceptionHandler;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class AppContext extends Application implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static AppContext appContext;
    public static ContentValues cv=new ContentValues();
    public IWXAPI msgApi;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        Fresco.initialize(this);
        // 获取系统默认的UncaughtException处理
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //友盟配置三方平台的appkey
        PlatformConfig.setWeixin("wxd96b8dd3e5733967","de1d138b686684274a39964f931b7eea");
        PlatformConfig.setQQZone("1105155596","bbRRZKif6xhk679w");
        PlatformConfig.setSinaWeibo("3588954476","66ee7a15a0301492373f82a42a80cd62","http://www.sina.com");
//        Config.DEBUG = true;
        Config.isJumptoAppStore = true;
        UMShareAPI.get(this);

         msgApi = WXAPIFactory.createWXAPI(appContext, null);
// 将该app注册到微信
        msgApi.registerApp("wxd96b8dd3e5733967");


    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }


}
