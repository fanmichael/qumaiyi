package cn.com.shequnew.pages.fragment;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ContentFileDetailsActivity;
import cn.com.shequnew.pages.activity.LoginActivity;
import cn.com.shequnew.pages.adapter.ContentApapter;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.view.PullToRefreshSwipeMenuListView;
import cn.com.shequnew.pages.view.pulltorefresh.interfaces.IXListViewListener;
import cn.com.shequnew.pages.view.swipemenu.bean.SwipeMenu;
import cn.com.shequnew.pages.view.swipemenu.bean.SwipeMenuItem;
import cn.com.shequnew.pages.view.swipemenu.interfaces.OnMenuItemClickListener;
import cn.com.shequnew.pages.view.swipemenu.interfaces.SwipeMenuCreator;

/**
 * 我的发布-内容
 */
public class PublishDetailsContentFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener, IXListViewListener {


    @BindView(R.id.collect_swi_content)
    SwipeRefreshLayout collectSwiContent;
    Unbinder unbinder;
    @BindView(R.id.collect_list_content)
    PullToRefreshSwipeMenuListView collectListContent;

    private int page = 1;
    private Context context;
    private ContentApapter contentApapter;
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<ContentValues> contentValuesCon = new ArrayList<>();
    private Handler mHandler;
    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_publish_details_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initView();
        page = 1;
        new asyncTask().execute(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 加载视图
     */
    private void initView() {
        collectSwiContent.setOnRefreshListener(this);
        contentApapter = new ContentApapter(context, contentValuesCon);
        collectListContent.setAdapter(contentApapter);
        collectListContent.setPullLoadEnable(true);
        collectListContent.setPullRefreshEnable(false);
        collectListContent.setXListViewListener(this);
        mHandler = new Handler();
        final SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(Color.rgb(255, 0, 0)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(getResources().getColor(R.color.white));
                menu.addMenuItem(openItem);
            }
        };

        collectListContent.setMenuCreator(creator);
        collectListContent.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                id = contentValuesCon.get(position).getAsInteger("id");
                deleteData();

            }
        });
        collectListContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", contentValuesCon.get(position).getAsInteger("id"));
                bundle.putInt("uid", contentValuesCon.get(position).getAsInteger("uid"));
                intent.putExtras(bundle);
                intent.setClass(context, ContentFileDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }


    private void constart() {
        collectListContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


    }

    private void deleteData() {
        View view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.back_login, null);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setView(view);
        TextView title = (TextView) view.findViewById(R.id.title_con);
        TextView name = (TextView) view.findViewById(R.id.title_name);
        name.setVisibility(View.VISIBLE);
        title.setText("删除后将不可查看！你确定要删除吗？");
        Button cal = (Button) view.findViewById(R.id.calen);
        Button sure = (Button) view.findViewById(R.id.sure);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new asyncTask().execute(3);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        page = 1;
        new asyncTask().execute(1);
    }

    @Override
    public void onRefresha() {
    }

    @Override
    public void onLoadMore() {
        collectListContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                load();
            }
        }, 100);
    }

    private void load() {
        page = page + 1;
        new asyncTask().execute(2);
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
                    htttpCollect();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpCollect();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    htttpDeteleCollect();
                    bundle.putInt("what", 3);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            switch (what) {
                case 1:
                    if (contentValues != null && contentValues.size() > 0) {
                        if (contentValuesCon != null && contentValuesCon.size() > 0) {
                            contentValuesCon.clear();
                        }
                        contentValuesCon.addAll(contentValues);
                    }
                    contentApapter.notifyDataSetChanged();
                    collectSwiContent.setRefreshing(false);//刷新完成
                    break;
                case 2:
                    if (contentValues != null && contentValues.size() > 0) {
                        contentValuesCon.addAll(contentValues);
                    }
                    contentApapter.notifyDataSetChanged();
                    break;
                case 3:
                    new asyncTask().execute(1);
                    break;

            }
        }
    }

    /**
     * 首次加载请求
     */
    private void htttpCollect() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Community.myNote");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("page", page + "");
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     */
    private void htttpDeteleCollect() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Community.note_updeta");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("id", page + "");
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

        if (contentValues == null) {
            contentValues = new ArrayList<>();
        }
        if (contentValues.size() > 0) {
            contentValues.clear();
        }

        try {
            JSONObject obj = new JSONObject(data);
            int error = obj.getInt("error");
            if (error == 101) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        collectListContent.setPullLoadEnable(false);
                        collectListContent.stopLoadMore();
//                        collectListContent.setFastScrollEnabled(false);
                    }
                }, 100);

            } else {
                JSONArray jsonArrGood = new JSONArray(obj.getString("data"));
                if (jsonArrGood.length() > 0) {
                    for (int i = 0; i < jsonArrGood.length(); i++) {
                        JSONObject jsonObj = jsonArrGood.getJSONObject(i);
                        ContentValues note = new ContentValues();
                        note.put("id", jsonObj.getInt("id"));
                        note.put("uid", jsonObj.getInt("uid"));
                        note.put("file_type", jsonObj.getInt("file_type"));
                        note.put("subject", jsonObj.getString("subject"));
                        note.put("title", jsonObj.getString("title"));
                        note.put("content", jsonObj.getString("content"));
                        note.put("tags", jsonObj.getString("tags"));
                        note.put("nick", jsonObj.getString("nick"));
                        note.put("icon", jsonObj.getString("icon"));
                        contentValues.add(note);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
