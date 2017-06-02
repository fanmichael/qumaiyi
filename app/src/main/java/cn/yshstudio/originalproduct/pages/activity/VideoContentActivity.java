package cn.yshstudio.originalproduct.pages.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.fragment.ContentFragment;
import cn.yshstudio.originalproduct.pages.fragment.VideoContentFragment;

/**
 * 视频及图文内容切换
 */
public class VideoContentActivity extends BaseActivity {

    @BindView(R.id.video_back)
    ImageView videoBack;
    @BindView(R.id.video_deal)
    Button videoDeal;
    @BindView(R.id.btn_video_shop)
    Button btnVideoShop;
    @BindView(R.id.video_content_frm)
    FrameLayout videoContentFrm;


    private Context context;
    /**
     * 视频
     */
    private VideoContentFragment videoContentFragment;
    /**
     * 图文
     */
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);
        ButterKnife.bind(this);
        context = this;
        con();
        initView();
    }

    @OnClick(R.id.video_back)
    void back() {
        destroyActitity();
    }


    @OnClick(R.id.video_deal)
    void imgContent() {
        con();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (contentFragment == null) {
            contentFragment = new ContentFragment();
        }
        transaction.replace(R.id.video_content_frm, contentFragment);
        transaction.commit();
    }

    @OnClick(R.id.btn_video_shop)
    void videoContent() {
        shop();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (videoContentFragment == null) {
            videoContentFragment = new VideoContentFragment();
        }
        transaction.replace(R.id.video_content_frm, videoContentFragment);
        transaction.commit();
    }


    /**
     * 加载Frment
     */
    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        contentFragment = new ContentFragment();
        fragmentTransaction.replace(R.id.video_content_frm, contentFragment);
        fragmentTransaction.commit();
    }


    /**
     * 图文内容
     */
    private void con() {
        videoDeal.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbgone));
        videoDeal.setText("图文内容");
        videoDeal.setTextColor(getResources().getColor(R.color.white));
        btnVideoShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbg));
        btnVideoShop.setText("视频内容");
        btnVideoShop.setTextColor(getResources().getColor(R.color.bd_top));
    }

    /**
     * 视频内容
     */
    private void shop() {
        videoDeal.setBackgroundDrawable(getResources().getDrawable(R.drawable.shopbg));
        videoDeal.setText("图文内容");
        videoDeal.setTextColor(getResources().getColor(R.color.bd_top));
        btnVideoShop.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbgone));
        btnVideoShop.setText("视频内容");
        btnVideoShop.setTextColor(getResources().getColor(R.color.white));
    }


}
