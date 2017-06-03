package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;

public class UpdateNickActivity extends BaseActivity {

    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.edit_material_nick)
    EditText editMaterialNick;
    public Context context;
    public String srtNick;
    private int error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    @OnClick(R.id.left_title)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.right_title)
    void suer() {
        srtNick = editMaterialNick.getText().toString().trim();
        new asyncTask().execute(1);

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
                        AppContext.cv.put("nick", srtNick);
                        setResult(1);
                        destroyActitity();
                    } else {
                        return;
                    }
                    break;


            }

        }
    }


    private void httpNick() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "User.update");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        map.put("nick", srtNick);
        map.put("name", "nick");
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


    /**
     * 记载控件
     */
    private void init() {
        leftTitle.setText("取消");
        centerTitle.setText("昵称");
        rightTitle.setText("确定");
        editMaterialNick.setText(AppContext.cv.getAsString("nick"));
    }


}
