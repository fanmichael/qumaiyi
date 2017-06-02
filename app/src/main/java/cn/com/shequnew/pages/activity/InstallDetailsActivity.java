package cn.com.shequnew.pages.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.chat.util.ObjectSaveUtils;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.tools.FileUtil;
import cn.com.shequnew.tools.SharedPreferenceUtil;

/**
 * 设置
 * \
 */
public class InstallDetailsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.install_us)
    TextView installUs;
    @BindView(R.id.install_contact_us)
    TextView installContactUs;
    @BindView(R.id.install_share)
    TextView installShare;
    @BindView(R.id.install_feed)
    TextView installFeed;
    @BindView(R.id.install_version)
    TextView installVersion;
    @BindView(R.id.install_back_login)
    TextView installBackLogin;
    @BindView(R.id.preSale_progress)
    RelativeLayout preSaleProgress;
    @BindView(R.id.clear_this)
    TextView clearThis;
    @BindView(R.id.clear)
    LinearLayout clear;
    private Context context;
    private final int CLEAN_SUC = 1001;
    private final int CLEAN_FAIL = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_details);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        topTitle.setText("设置");
        topRegitTitle.setVisibility(View.GONE);
        clearThis.setVisibility(View.VISIBLE);
        preSaleProgress.setVisibility(View.GONE);
        try {
            clearThis.setText(FileUtil.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    @OnClick(R.id.clear)
    void clear() {
        //清楚缓存
        clearThis.setVisibility(View.GONE);
        preSaleProgress.setVisibility(View.VISIBLE);
        clearAppCache();
    }


    /**
     * 清除app缓存
     *
     * @param
     */
    public void clearAppCache() {
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    FileUtil.clearAllCache(context);
                    msg.what = CLEAN_SUC;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = CLEAN_FAIL;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CLEAN_FAIL:
                    clearThis.setVisibility(View.VISIBLE);
                    preSaleProgress.setVisibility(View.GONE);
                    clearThis.setText("0M");
                    break;
                case CLEAN_SUC:
                    clearThis.setVisibility(View.VISIBLE);
                    preSaleProgress.setVisibility(View.GONE);
                    clearThis.setText("0M");
                    break;
            }
        }

    };

    @OnClick(R.id.install_us)
    void us() {
        //关于我们
        Intent intent = new Intent(context, UsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "us");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.install_contact_us)
    void contactUs() {
        //联系我们
        Intent intent = new Intent(context, UsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "contact");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.install_share)
    void share() {
        //分享app
        Intent intent = new Intent(context, ShareActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.install_feed)
    void feed() {
        //反馈意见
        Intent intent = new Intent(context, FeedActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.install_version)
    void version() {
        //当前版本
        Intent intent = new Intent(context, UsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "version");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.install_back_login)
    void backLogin() {
        //退出登录
        diabackLogin();

    }

    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(InstallDetailsActivity.this, R.style.AlertDialog);
        dialog.setContentView(R.layout.back_login);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        dialog.getWindow().setAttributes(layoutParams);
        Button photograph = (Button) dialog.findViewById(R.id.calen);
        Button photographAdd = (Button) dialog.findViewById(R.id.sure);
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        photographAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferenceUtil.hasKey("mobile") && SharedPreferenceUtil.hasKey("password")) {
                    SharedPreferenceUtil.remove("mobile");
                    SharedPreferenceUtil.remove("password");
                    AppContext.cv.clear();
                }
                if (SharedPreferenceUtil.hasKey("id")) {
                    SharedPreferenceUtil.remove("type");
                    SharedPreferenceUtil.remove("id");
                    SharedPreferenceUtil.remove("nick");
                    SharedPreferenceUtil.remove("icon");
                    AppContext.cv.clear();
                }
                if (EMClient.getInstance().isLoggedInBefore())
                    EMClient.getInstance().logout(true);
                ObjectSaveUtils.saveObject(InstallDetailsActivity.this, "USERICON", UserInfo.getInstance());
                dialog.dismiss();
                AppContext.getInstance().logoutApp();
                Intent intent = new Intent(InstallDetailsActivity.this, FristAdvActivity.class);
                startActivity(intent);
            }
        });
    }


}
