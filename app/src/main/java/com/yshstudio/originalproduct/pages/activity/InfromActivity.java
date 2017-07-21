package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;

/**
 * 举报
 */
public class InfromActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.info_content)
    EditText infoContent;
    private Context context;
    private String type;
    private String rid;
    private int error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrom);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("举报");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        rid = bundle.getString("rid");
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.top_regit_title)
    void sumit() {
        if (infoContent.getText().toString().trim().equals("")) {
            Toast.makeText(context, "请输入举报内容！", Toast.LENGTH_SHORT).show();
        } else {
            new asyncTask().execute(1);
        }
    }


    private void httpUpdatePwd() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Report.putReport");
            hashMap.put("uid", SharedPreferenceUtil.read("id","") + "");
            hashMap.put("rid", rid);
            hashMap.put("type", type);
            hashMap.put("content", URLEncoder.encode(infoContent.getText().toString().trim(), "UTF-8") + "");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                JSONObject jsonObject = new JSONObject(json);
                error = jsonObject.getInt("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpUpdatePwd();
                    bundle.putInt("what", 1);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            // removeLoading();
            switch (what) {
                case 1:
                    if (error == 0) {
                        Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    } else if(error==122){
                        Toast.makeText(context, "您已举报过，请等待系统审核！", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    }else {
                        Toast.makeText(context, "参数提交有误！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
