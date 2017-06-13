package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;


public class DeclareActivity extends BaseActivity {

    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.group_apply)
    EditText groupApply;

    private int error;
    private Context context;
    private String groupId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare);
        ButterKnife.bind(this);
        context = this;
        leftTitle.setText("取消");
        centerTitle.setText("申请说明");
        rightTitle.setText("提交");
        Bundle bundle = this.getIntent().getExtras();
        groupId = bundle.getString("groupId");
    }


    @OnClick(R.id.left_title)
    void cal() {
        destroyActitity();
    }

    @OnClick(R.id.right_title)
    void sumbit() {
        if (groupApply.getText().toString().trim().equals("")) {
            Toast.makeText(context, "请输入申请内容！", Toast.LENGTH_SHORT).show();
            rightTitle.setClickable(true);
            rightTitle.setFocusable(true);
        } else {
            rightTitle.setClickable(false);
            rightTitle.setFocusable(false);
            new asyncTask().execute(1);
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            switch (params[0]) {
                case 1:
                    httpGoodsInfo();
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
                        Toast.makeText(context, "验证成功！", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    } else {
                        Toast.makeText(context, "验证失败！", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    }

                    break;

            }

        }
    }


    private void httpGoodsInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Group.joinGroup");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("group_id", groupId);
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject jsonObject = new JSONObject(json);
                error = jsonObject.getInt("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}