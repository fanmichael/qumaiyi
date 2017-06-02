package cn.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;

/**
 * 反馈意见
 */
public class FeedActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.feed)
    EditText feed;
    private Context context;
    private String content = "";
    private int error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        topTitle.setText("反馈意见");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);


    }

    @OnClick(R.id.top_regit_title)
    void sumber() {
        content = feed.getText().toString().trim();

        if (content.equals("")) {
            Toast.makeText(context, "输入不能为空！", Toast.LENGTH_SHORT).show();
        } else {

            new asyncTask().execute(1);
        }
    }


    private void httpNick() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "Feedback.putFeedback");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        map.put("content", content);
        String json = HttpConnectTool.post(map);
        if (!json.equals("")) {
            xmlComm(json);
        }
    }

    private void xmlComm(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            error = obj.getInt("error");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpNick();
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
                    if (error == 0) {
                        destroyActitity();
                    } else {
                        return;
                    }
                    break;


            }

        }
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


}
