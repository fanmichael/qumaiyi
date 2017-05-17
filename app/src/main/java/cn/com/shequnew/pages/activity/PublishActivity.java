package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.config.AppContext;

/**
 * 发布
 */
public class PublishActivity extends BaseActivity {

    public Context context;
    @BindView(R.id.publish_content)
    Button publishContent;
    @BindView(R.id.publish_shop)
    Button publishShop;
    @BindView(R.id.publish_cancle)
    Button publishCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        context = this;
    }


    /**
     * 发布内容
     */
    @OnClick(R.id.publish_content)
    void publishContent() {
        //发布图文----发布视频
        Intent intent = new Intent(context, VideoContentActivity.class);
        context.startActivity(intent);
    }

    /**
     * 发布商品
     */
    @OnClick(R.id.publish_shop)
    void publishShop() {
        if (AppContext.cv.getAsString("merchant").equals("0")) {
            //申请卖主
            Intent intent = new Intent(context, SellerlDetailsActivity.class);
            context.startActivity(intent);
            destroyActitity();
        } else {
            //发布商品

        }

    }


    /**
     * 取消按钮
     */
    @OnClick(R.id.publish_cancle)
    void cancle() {
        destroyActitity();
    }

}
