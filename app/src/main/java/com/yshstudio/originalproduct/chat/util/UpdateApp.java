package com.yshstudio.originalproduct.chat.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.bean.UpdateRequest;
import com.yshstudio.originalproduct.chat.bean.UpdateResponse;
import com.yshstudio.originalproduct.chat.net.ComUnityRequest;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kongqing on 17-3-16.
 */

public class UpdateApp {
    public Context getContext() {
        return context;
    }

    private static final int NTIFYID = 0X01;

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;
    private String apkname = "QUMAIYI";
    private NotificationCompat.Builder builder;

    private void install(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS)
                , "myApp.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile
                    (context, "com.a520wcf.chapter11.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }

    private void update(String url) {
        if (builder == null) {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification;
        builder.setSmallIcon(R.mipmap.icon);
        builder.setContentTitle("下载");
        builder.setContentText("正在下载");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.notify(NTIFYID, builder.build());
        //builder.setProgress(100, 0, false);
        try {
            URL               url1     = new URL(url);
            HttpURLConnection conn     = (HttpURLConnection) url1.openConnection();
            InputStream       in       = conn.getInputStream();
            File              file     = new File(Environment.getExternalStorageDirectory(), apkname);
            FileOutputStream  out      = new FileOutputStream(file);
            int               i;
            int               progCont = conn.getContentLength();
            byte[]            buffer   = new byte[1024];
            int               now      = 0;
            int               loopcont = progCont / 100;
            int               a        = 0;
            while ((i = in.read(buffer)) != -1) {
                now = now + 1024;
                out.write(buffer, 0, i);
                out.flush();
                if (now / loopcont > a) {
                    a++;
                    builder.setProgress(progCont, now, false);
                    manager.notify(NTIFYID, builder.build());
                }
            }
            manager.cancel(NTIFYID);
            in.close();
            out.close();
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), apkname)), "application/vnd.android.package-archive");
            context.startActivity(intent);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            manager.cancel(NTIFYID);
        } catch (IOException e) {
            e.printStackTrace();
            manager.cancel(NTIFYID);
        }
    }

    public void checkoutVertion() {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "";
        }
        ComUnityRequest.getAPI().update(new UpdateRequest().setVersion(version)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("E", e.getMessage());
            }

            @Override
            public void onNext(final UpdateResponse updateResponse) {
                if (updateResponse.getError() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("版本更新").setPositiveButton("版本更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    update(updateResponse.getData().getApk_url());

                                }
                            }.start();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
                    alertDialog.show();
                }
            }
        });
    }
}
