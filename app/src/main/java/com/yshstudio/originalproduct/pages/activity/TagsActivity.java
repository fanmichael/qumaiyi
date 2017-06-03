package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.TagsAdapter;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;


public class TagsActivity extends BaseActivity implements TagsAdapter.setOnclick {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_all)
    TextView topAll;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.add_tag)
    Button addTag;
    @BindView(R.id.tags_gridView)
    GridView tagsGridView;
    @BindView(R.id.tag_gridView)
    GridView tagGridView;

    private TagsAdapter tagsAdapter;
    private TagsAdapter tagsAdapterDe;

    private Context context;
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> cvs = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("添加标题");
        topRegitTitle.setText("保存");
        topRegitTitle.setVisibility(View.VISIBLE);
        tagsAdapter = new TagsAdapter(contentValues, context, 1, null);
        tagGridView.setAdapter(tagsAdapter);
        tagsAdapterDe = new TagsAdapter(cvs, context, 2, this);
        tagsGridView.setAdapter(tagsAdapterDe);
        tagGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < cvs.size(); i++) {
                    if (cvs.get(i).getAsString("name").equals(contentValues.get(position).getAsString("name"))) {
                        Toast.makeText(context, "已添加过该标签", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                cvs.add(contentValues.get(position));
                tagsAdapterDe.notifyDataSetChanged();
            }
        });
        new asyncTask().execute(1);
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    /**
     * 保存
     */
    @OnClick(R.id.top_regit_title)
    void smit() {
        if (cvs.size() < 0) {
            Toast.makeText(context, "请添加标签！", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < cvs.size(); i++) {
            strings.add(cvs.get(i).getAsString("name"));
        }
        StringBuffer stringBufferPop = new StringBuffer();
        if (strings.size() > 0) {
            for (int i = 0; i < strings.size(); i++) {
                if (strings.size() == (i + 1)) {
                    stringBufferPop.append(strings.get(i));
                } else {
                    stringBufferPop.append(strings.get(i) + ",");
                }

            }
        }

        Intent intent = new Intent();
        intent.putExtra("tag", stringBufferPop.toString());
        intent.putExtra("num", strings.size() + "");
        this.setResult(13, intent);
        destroyActitity();

    }

    @OnClick(R.id.add_tag)
    void add() {
        if (editContent.getText().toString().trim().equals("")) {
            Toast.makeText(context, "请输入标签！", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < cvs.size(); i++) {
                if (cvs.get(i).getAsString("name").equals(editContent.getText().toString().trim())) {
                    Toast.makeText(context, "已添加过该标签", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            ContentValues cv = new ContentValues();
            cv.put("name", editContent.getText().toString().trim());
            cvs.add(cv);
            tagsAdapterDe.notifyDataSetChanged();
            editContent.setText("");
        }

    }

    @Override
    public void onDelete(int pos) {
        cvs.remove(pos);
        tagsAdapterDe.notifyDataSetChanged();
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
                    tagsAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;

            }

        }
    }


    private void httpGoodsInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.tags");
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
                    cv.put("name", jsonObj.getString("name"));
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("status", jsonObj.getInt("status"));
                    contentValues.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


}
