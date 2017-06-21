package com.yshstudio.originalproduct.pages.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.ListTools;
import com.yshstudio.originalproduct.tools.TextContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页-
 */
public class UsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.us_phone)
    ImageView usPhone;
    @BindView(R.id.us)
    LinearLayout us;
    @BindView(R.id.version_text)
    TextView versionText;
    @BindView(R.id.version)
    LinearLayout version;
    @BindView(R.id.contect)
    TextView contect;
    @BindView(R.id.li_con)
    TextView liCon;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.emi)
    TextView emi;
    @BindView(R.id.web)
    TextView web;
    @BindView(R.id.list)
    ListView list;

    private Context context;
    private Content content;
    private List<ContentValues> contentValues=new ArrayList<>();
    private ContentValues aboutUsValues=new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us);
        ButterKnife.bind(this);
        context = this;
        initView();
    }
    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    /**
     * 打电话
     * */
    @OnClick(R.id.us_phone)
    void usPhone() {
        try {
            String number = aboutUsValues.getAsString("tel");
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载判断视图
     * */
    private void initView() {
        topRegitTitle.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        String tyep = bundle.getString("type");
        if (tyep.equals("us")) {
            topTitle.setText("关于我们");
            us.setVisibility(View.VISIBLE);
            version.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            new asyncTask().execute(2);
        } else if (tyep.equals("contact")) {
            topTitle.setText("联系我们");
            version.setVisibility(View.GONE);
            us.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            new asyncTask().execute(1);
        } else if (tyep.equals("version")) {
            topTitle.setText("当前版本");
            us.setVisibility(View.GONE);
            version.setVisibility(View.VISIBLE);
            versionText.setText(getVersion().trim());
        }

    }


    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "当前版本" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 联系我们Adapter
     * */
    private class Content extends BaseAdapter {

        private Context context;

        public Content(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return contentValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.content, null);
                holder.adv = (TextView) convertView.findViewById(R.id.adv);
                holder.adv_s = (TextView) convertView.findViewById(R.id.adv_s);
                holder.adv_e = (TextView) convertView.findViewById(R.id.adv_e);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ContentValues cv = contentValues.get(position);

            if (cv.getAsString("title").equals("")) {
                holder.adv.setVisibility(View.GONE);
            } else {
                holder.adv.setVisibility(View.VISIBLE);
                holder.adv.setText(cv.getAsString("title"));
            }
            if (cv.getAsString("content").equals("")) {
                holder.adv_s.setVisibility(View.GONE);
            } else {
                holder.adv_s.setVisibility(View.VISIBLE);
                holder.adv_s.setText(cv.getAsString("content"));
            }
            if (cv.getAsString("contact_way").equals("")) {
                holder.adv_e.setVisibility(View.GONE);
            } else {
                holder.adv_e.setVisibility(View.VISIBLE);
                holder.adv_e.setText(cv.getAsString("contact_way"));
            }
            return convertView;
        }

        public final class ViewHolder {
            public TextView adv;
            public TextView adv_s;
            public TextView adv_e;
        }

    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpLinkUs();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpAboutUs();
                    bundle.putInt("what", 2);
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
                    content = new Content(context);
                    list.setAdapter(content);
                    ListTools.setListViewHeightBasedOnChildren(list);
                    break;
                case 2:
                    intData();
                    break;
            }
        }
    }

    private void httpLinkUs() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Contactus.linkUs");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlLinkUs(json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void xmlLinkUs(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray objData=new JSONArray(obj.getString("data"));
            if(objData.length()>0){
                for (int i=0;i<objData.length();i++){
                    JSONObject jsdata = objData.getJSONObject(i);
                   ContentValues values=new ContentValues();
                    values.put("title",jsdata.getString("title"));
                    values.put("content",jsdata.getString("content"));
                    values.put("contact_way",jsdata.getString("contact_way"));
                    contentValues.add(values);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void httpAboutUs() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Aboutus.aboutUs");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAboutUs(json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void xmlAboutUs(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray objData=new JSONArray(obj.getString("data"));
            if(objData.length()>0){
                for (int i=0;i<objData.length();i++){
                    JSONObject jsdata = objData.getJSONObject(i);
                    aboutUsValues.put("synopsis",jsdata.getString("synopsis"));
                    aboutUsValues.put("idea",jsdata.getString("idea"));
                    aboutUsValues.put("tel",jsdata.getString("tel"));
                    aboutUsValues.put("email",jsdata.getString("email"));
                    aboutUsValues.put("url",jsdata.getString("url"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载关于我们
     * */
    private void intData() {
        contect.setText(aboutUsValues.getAsString("synopsis"));
        liCon.setText(aboutUsValues.getAsString("idea"));
        phone.setText("客服热线："+aboutUsValues.getAsString("tel"));
        emi.setText("邮箱："+aboutUsValues.getAsString("email"));
        web.setText("公司网址："+aboutUsValues.getAsString("url"));
    }

















}
