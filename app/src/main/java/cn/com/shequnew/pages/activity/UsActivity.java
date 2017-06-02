package cn.com.shequnew.pages.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.AllCateAdapter;
import cn.com.shequnew.tools.ListTools;
import cn.com.shequnew.tools.TextContent;
import cn.com.shequnew.tools.ValidData;

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
    private TextContent textContent = new TextContent();
    private Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void intData() {

        contect.setText(textContent.content);
        liCon.setText(textContent.qontect_li);
        phone.setText(textContent.qontect_phone);
        emi.setText(textContent.qontect_emi);
        web.setText(textContent.qontect_web);
    }


    private void initViewData() {
        content = new Content(context);
        list.setAdapter(content);
        ListTools.setListViewHeightBasedOnChildren(list);
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.us_phone)
    void usPhone() {
        try {
            String number = "400-12345678";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        topRegitTitle.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        String tyep = bundle.getString("type");
        if (tyep.equals("us")) {
            topTitle.setText("关于我们");
            us.setVisibility(View.VISIBLE);
            version.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            intData();
        } else if (tyep.equals("contact")) {
            topTitle.setText("联系我们");
            version.setVisibility(View.GONE);
            us.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            initViewData();
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


    private class Content extends BaseAdapter {

        private Context context;

        public Content(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return textContent.setData().size();
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
            ContentValues cv = textContent.setData().get(position);

            if (cv.getAsString("a").equals("")) {
                holder.adv.setVisibility(View.GONE);
            } else {
                holder.adv.setVisibility(View.VISIBLE);
                holder.adv.setText(cv.getAsString("a"));
            }
            if (cv.getAsString("c").equals("")) {
                holder.adv_s.setVisibility(View.GONE);
            } else {
                holder.adv_s.setVisibility(View.VISIBLE);
                holder.adv_s.setText(cv.getAsString("c"));
            }
            if (cv.getAsString("b").equals("")) {
                holder.adv_e.setVisibility(View.GONE);
            } else {
                holder.adv_e.setVisibility(View.VISIBLE);
                holder.adv_e.setText(cv.getAsString("b"));
            }
            return convertView;
        }

        public final class ViewHolder {
            public TextView adv;
            public TextView adv_s;
            public TextView adv_e;
        }

    }


}
