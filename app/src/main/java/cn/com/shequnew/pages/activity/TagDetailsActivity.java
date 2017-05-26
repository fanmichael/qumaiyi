package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
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
import cn.com.shequnew.R;
import cn.com.shequnew.pages.http.HttpConnectTool;

public class TagDetailsActivity extends BaseActivity {

    @BindView(R.id.expendlist)
    ExpandableListView expendlist;
    @BindView(R.id.tag_details_cal)
    Button tagDetailsCal;
    @BindView(R.id.tag_details_sumbit)
    Button tagDetailsSumbit;
    private int mSelect = -1;
    private Context context;
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> tag = new ArrayList<>();
    private List<List<ContentValues>> lists = new ArrayList<>();

    private String name = "";
    private int num;


    private MyExpandableListViewAdapter myExpandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_details);
        ButterKnife.bind(this);
        context = this;
        myExpandableListViewAdapter = new MyExpandableListViewAdapter(this);
        expendlist.setAdapter(myExpandableListViewAdapter);
        expendlist.setGroupIndicator(null);
        expendlist.setVerticalScrollBarEnabled(false);
        expendlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (tag.get(groupPosition).getAsString("name").isEmpty()) {
                    return true;
                }
                return false;
            }
        });

        expendlist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = expendlist.getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        expendlist.collapseGroup(i);
                        mSelect = -1;
                    }
                }
            }
        });


        new asyncTask().execute(1);
    }


    @OnClick(R.id.tag_details_cal)
    void cal() {
        destroyActitity();
    }

    @OnClick(R.id.tag_details_sumbit)
    void sumbit() {

        if (name.equals("")) {
            Toast.makeText(context, "请选择分类！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent();
            intent.putExtra("num", num + "");
            intent.putExtra("name", name);
            this.setResult(14, intent);
            destroyActitity();
        }


    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            switch (params[0]) {
                case 1:
                    httpGoodsInfoCate();
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
                    for (int i = 0; i < contentValues.size(); i++) {
                        if (contentValues.get(i).getAsInteger("parent") == 0) {
                            tag.add(contentValues.get(i));
                        }
                    }
                    List<ContentValues> values = new ArrayList<>();
                    for (int i = 0; i < tag.size(); i++) {
                        for (int j = 0; j < contentValues.size(); j++) {
                            if ((i + 1) == contentValues.get(j).getAsInteger("parent")) {
                                values.add(contentValues.get(j));
                            }
                        }

                        lists.add(i, values);
                    }
                    myExpandableListViewAdapter.notifyDataSetChanged();
                    //初始加载数据
                    break;

            }

        }
    }


    private void httpGoodsInfoCate() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.getAllCate");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                listXmlCate(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listXmlCate(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArr = new JSONArray(obj.getString("data"));
            if (jsonArr.length() > 0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("name", jsonObj.getString("name"));
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("parent", jsonObj.getInt("parent"));
                    contentValues.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
        private Context context;


        public MyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        public void changeSelected(int positon) { //刷新方法
            if (positon != mSelect) {
                mSelect = positon;
                notifyDataSetChanged();
            }
        }

        @Override
        public int getGroupCount() {
            return tag.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return lists.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return tag.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return lists.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if (tag == null || tag.size() < 0) {
                return convertView;
            }
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tag_one_item_layout, null);
                groupHolder = new GroupHolder();
                groupHolder.txt = (TextView) convertView.findViewById(R.id.text_tag_name);
                groupHolder.img = (ImageView) convertView.findViewById(R.id.tag_im);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            if (tag == null || tag.size() < 0) {
                return convertView;
            }

            if (!isExpanded) {
                groupHolder.img.setBackgroundResource(R.drawable.down);
            } else {
                groupHolder.img.setBackgroundResource(R.drawable.up);
            }
            groupHolder.txt.setText(tag.get(groupPosition).getAsString("name"));
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (lists == null || lists.get(groupPosition).size() < 0) {
                return convertView;
            }
            ItemHolder itemHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tag_itme, null);
                itemHolder = new ItemHolder();
                itemHolder.checkBox = (CheckBox) convertView.findViewById(R.id.tag_chose);
                itemHolder.txt = (TextView) convertView.findViewById(R.id.text_tag);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            if (lists == null || lists.get(groupPosition).size() < 0) {
                return convertView;
            }
            itemHolder.txt.setText(lists.get(groupPosition).get(childPosition).getAsString("name"));
            if (mSelect == childPosition) {
                itemHolder.checkBox.setChecked(true);
            } else {
                itemHolder.checkBox.setChecked(false);
            }
            itemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myExpandableListViewAdapter.changeSelected(childPosition);
                    name = lists.get(groupPosition).get(childPosition).getAsString("name");
                    num = lists.get(groupPosition).get(childPosition).getAsInteger("id");
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        class GroupHolder {
            public TextView txt;

            public ImageView img;
        }

        class ItemHolder {
            public CheckBox checkBox;

            public TextView txt;
        }

    }
}
