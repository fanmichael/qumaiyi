package com.yshstudio.originalproduct.pages.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppOpenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOpen();
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断跳转
     */
    private void initOpen() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("action", "Dynamic.sys").add("version", getVersion()).add("type", "android").build();
        Request request = new Request.Builder()
                .url(Ini.Url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObjects = new JSONObject(jsonObject.getString("data"));
                    String value = jsonObjects.getString("value");
                    if (value.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final View view = View.inflate(AppOpenActivity.this, R.layout.activity_app_open, null);
                                setContentView(view);
                                AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
                                animation.setDuration(3000);
                                view.setAnimation(animation);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        if (SharedPreferenceUtil.hasKey("is")) {
                                            Intent intent = new Intent(AppOpenActivity.this, FristAdvActivity.class);
                                            startActivity(intent);
                                            destroyActitity();
                                        } else {
                                            Intent intent = new Intent(AppOpenActivity.this, AppStartActivity.class);
                                            startActivity(intent);
                                            destroyActitity();
                                        }

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        });

                    } else if (value.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AppOpenActivity.this, "软件已过期！", Toast.LENGTH_LONG).show();
                                AppContext.getInstance().logoutApp();
                                destroyActitity();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
