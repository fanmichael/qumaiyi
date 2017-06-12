package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.ValidData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FristAdvActivity extends BaseActivity {

    @BindView(R.id.sim_adv)
    SimpleDraweeView simAdv;
    @BindView(R.id.adv_go)
    Button advGo;
    private Context context;
    private String imag;
    private MyCountDownTimer mc;
    private boolean isGo = true;
    private String val = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_adv);
        ButterKnife.bind(this);
        context = this;
        new asyncTask().execute(1);
        mc = new MyCountDownTimer(5000, 1000);
        mc.start();
    }

    @OnClick(R.id.adv_go)
    void goLogin() {
        isGo = false;
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        destroyActitity();
    }

    /**
     * 异步请求
     */
    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpVideo();
                    bundle.putInt("what", 1);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int height = dm.heightPixels;
                    int width = dm.widthPixels;
                    if (!imag.equals("")) {
                        Uri imageUri = Uri.parse(imag);
                        ValidData.load(imageUri, simAdv, width, height);
                    }
                    break;
            }

        }
    }


    private void httpVideo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Ad.adList");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            imag = objData.getString("img");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            advGo.setText(millisUntilFinished / 1000 + "跳转广告");
        }

        @Override
        public void onFinish() {
            advGo.setText("正在跳转");
            if (isGo) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                destroyActitity();
            }
        }
    }


}
