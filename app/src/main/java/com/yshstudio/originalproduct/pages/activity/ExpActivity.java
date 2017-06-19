package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.ExpAdapter;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;

/***
 * 发货
 * */
public class ExpActivity extends BaseActivity implements ExpAdapter.setonClick {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.exp_num)
    EditText expNum;
    @BindView(R.id.lin_exp)
    LinearLayout linExp;
    @BindView(R.id.list_exp)
    ListView listExp;
    @BindView(R.id.exp_image)
    ImageView expImage;
    @BindView(R.id.exp_image_it)
    ImageView expImageIt;
    @BindView(R.id.lin_exp_it)
    LinearLayout linExpIt;

    private String ddid = "";
    private String comName = "";
    private String no = "";
    private Context context;
    private List<ContentValues> contentValues = new ArrayList<>();
    private ExpAdapter expAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        ButterKnife.bind(this);
        context = this;
        initView();


//        listExp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                expAdapter.changeSelected(position);
//            }
//        });

    }

    private void initView() {
        topTitle.setText("物流公司");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);
        Bundle bundle = this.getIntent().getExtras();
        ddid = bundle.getString("ddid");
        expAdapter = new ExpAdapter(contentValues, context, this);
        listExp.setAdapter(expAdapter);
        listExp.setVisibility(View.GONE);
        new asyncTask().execute(1);
    }

    @OnClick(R.id.top_regit_title)
    void sumbit() {
        no = expNum.getText().toString().trim();
        if (no.equals("")) {
            Toast.makeText(context, "请输入单号！", Toast.LENGTH_SHORT).show();
        } else {
            if (comName.equals("")) {
                Toast.makeText(context, "请选择物流！", Toast.LENGTH_SHORT).show();
            } else {
                new asyncTask().execute(2);
            }
        }
    }

    @Override
    public void onClick(int pos, String com, String no) {
        comName = no;
        expAdapter.changeSelected(pos);
    }

    @OnClick(R.id.lin_exp)
    void exp() {
        listExp.setVisibility(View.VISIBLE);
        linExp.setVisibility(View.GONE);
        linExpIt.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.lin_exp_it)
    void expIt() {
        listExp.setVisibility(View.GONE);
        linExp.setVisibility(View.VISIBLE);
        linExpIt.setVisibility(View.GONE);
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
                    htttpExpcom();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpExp();
                    bundle.putInt("what", 2);
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
                    expAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    setResult(100);
                    destroyActitity();
                    break;

            }

        }
    }


    /**
     * 发货
     */
    private void htttpExp() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Logistics.deliverGoods");
            hashMap.put("ddid", ddid);
            hashMap.put("logistics", URLEncoder.encode(comName, "UTF-8")+"");
            hashMap.put("logistics_num", URLEncoder.encode(no, "UTF-8")+"");
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    /**
     * 请求物流
     */
    private void htttpExpcom() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "exp.getComs");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
            }
            listXml(json);
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
            JSONObject objs = new JSONObject(obj.getString("data"));
            JSONArray jsonArrNote = new JSONArray(objs.getString("result"));
            if (jsonArrNote.length() > 0) {
                for (int i = 0; i < jsonArrNote.length(); i++) {
                    JSONObject jsonObj = jsonArrNote.getJSONObject(i);
                    ContentValues note = new ContentValues();
                    note.put("com", jsonObj.getString("com"));
                    note.put("no", jsonObj.getString("no"));
                    note.put("chose", 0);
                    contentValues.add(note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
