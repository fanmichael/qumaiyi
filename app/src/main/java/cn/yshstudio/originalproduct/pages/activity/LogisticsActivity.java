package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.adapter.LogisticsAdapter;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;

/**
 * 物流信息
 */
public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.list_logistics)
    ListView listLogistics;
    private Context context;


    private String com;
    private String no;
    private LogisticsAdapter logisticsAdapter;
    private List<ContentValues> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("物流信息");
        Bundle bundle = this.getIntent().getExtras();
        com = bundle.getString("com");
        no = bundle.getString("no");
        new asyncTask().execute(1);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    /**
     * 异步加载数据
     */
    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    htttpAll();
                    bundle.putInt("what", 1);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
//            removeLoading();
            switch (what) {
                case 1:
                    logisticsAdapter = new LogisticsAdapter(context, values);
                    listLogistics.setAdapter(logisticsAdapter);
                    break;
            }

        }
    }


    /**
     * 首次加载请求
     */
    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "exp.query");
            hashMap.put("com", com);
            hashMap.put("no", no);
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析数据
     */
    private void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject jsonObject = new JSONObject(obj.getString("data"));
            JSONObject jsonresult = new JSONObject(jsonObject.getString("result"));
            JSONArray jsonArrNote = new JSONArray(jsonresult.getString("list"));

            if (jsonArrNote.length() > 0) {
                for (int i = 0; i < jsonArrNote.length(); i++) {
                    JSONObject jsonObj = jsonArrNote.getJSONObject(i);
                    ContentValues note = new ContentValues();
                    note.put("datetime", jsonObj.getString("datetime"));
                    note.put("remark", jsonObj.getString("remark"));
                    values.add(note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
