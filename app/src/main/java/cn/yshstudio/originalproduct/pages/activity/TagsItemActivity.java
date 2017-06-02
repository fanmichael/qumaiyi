package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.adapter.TagItemAdapter;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;

public class TagsItemActivity extends BaseActivity implements TagItemAdapter.setOnclick {


    @BindView(R.id.tag_list)
    ListView tagList;
    @BindView(R.id.tag_cal)
    Button tagCal;
    @BindView(R.id.tag_sumbit)
    Button tagSumbit;
    private TagItemAdapter tagItemAdapter;
    private List<ContentValues> tags = new ArrayList<>();
    private Context context;
    private int num = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags2);
        ButterKnife.bind(this);
        context = this;
        tagItemAdapter = new TagItemAdapter(tags, context, this);
        tagList.setAdapter(tagItemAdapter);
        new asyncTask().execute(1);
    }

    @OnClick(R.id.tag_cal)
    void cal() {
        destroyActitity();
    }

    @OnClick(R.id.tag_sumbit)
    void sumbit() {
        if (num == -1) {
            Toast.makeText(context, "请选择分类！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("num", tags.get(num).getAsInteger("id") + "");
        intent.putExtra("name", tags.get(num).getAsString("name") + "");
        this.setResult(14, intent);
        destroyActitity();
    }

    @Override
    public void chose(int pos) {
        num = pos;
        tagItemAdapter.changeSelected(pos);
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
                    tagItemAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;

            }

        }
    }

    /**
     * 发布商品
     */
    private void httpGoodsInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.getAllCate");
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
            JSONArray jsonArr = new JSONArray(obj.getString("data"));
            if (jsonArr.length() > 0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("status", jsonObj.getInt("status"));
                    cv.put("name", jsonObj.getString("name"));
                    tags.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
