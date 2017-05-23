package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;

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
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("rid", rid);
            hashMap.put("type", type);
            hashMap.put("content", infoContent.getText().toString().trim());
            String json = HttpConnectTool.post(hashMap);
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
                    destroyActitity();
                    break;

            }

        }
    }


}
