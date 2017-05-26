package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.inc.Ini;
import cn.com.shequnew.tools.UtilsUmeng;

import static android.R.attr.id;

/**
 * 分享app
 * */
public class ShareActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.btn_share)
    Button btnShare;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        context=this;
        initvView();
    }

    private void initvView(){
        topTitle.setText("分享APP");
        topRegitTitle.setVisibility(View.GONE);
    }

    @OnClick(R.id.image_back)
    void back(){
        destroyActitity();
    }

    @OnClick(R.id.btn_share)
    void share(){
//        Intent intent=new Intent(context,ShareAllActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("type","ShareActivity");
//        intent.putExtras(bundle);
//        context.startActivity(intent);
        UtilsUmeng.share(ShareActivity.this,"https://fir.im/qmy01","去卖艺APP");

    }

}
