package cn.com.shequnew.pages.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hyphenate.easeui.ui.EaseConversationListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.fragment.DynamicFragment;
import cn.com.shequnew.pages.fragment.NewsFragment;
import cn.com.shequnew.pages.fragment.PageCommFragment;

/**
 * 主页
 */
public class MainActivity extends FragmentActivity {

    @BindView(R.id.radio_pages)
    RadioGroup radioPages;
    @BindView(R.id.pages)
    RadioButton pages;
    @BindView(R.id.dynamic)
    RadioButton dynamic;
    @BindView(R.id.publish)
    RadioButton publish;
    @BindView(R.id.news)
    RadioButton news;
    @BindView(R.id.mine)
    RadioButton mine;
    private Context context;
    private PageCommFragment pagesFragment;
    private DynamicFragment dynamicFragment;
    //聊天界面
    private EaseConversationListFragment publishFragment;
    private NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setDefaultFragment();
        setFragmentChange();
    }


    private void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        pagesFragment = new PageCommFragment();
        transaction.replace(R.id.fra_layout, pagesFragment);
        transaction.commit();
    }

    private void setFragmentChange() {
        radioPages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.pages:
                        if (pagesFragment == null) {
                            pagesFragment = new PageCommFragment();
                        }
                        transaction.replace(R.id.fra_layout, pagesFragment);
                        break;
                    case R.id.dynamic:
                        if (dynamicFragment == null) {
                            dynamicFragment = new DynamicFragment();
                        }
                        transaction.replace(R.id.fra_layout, dynamicFragment);
                        break;
                    case R.id.news:
                        if (publishFragment == null) {
                            // publishFragment = new EaseConversationListFragment();
                        }
                        // transaction.replace(R.id.fra_layout, publishFragment);
                        break;
                    case R.id.mine:
                        if (newsFragment == null) {
                            newsFragment = new NewsFragment();
                        }
                        transaction.replace(R.id.fra_layout, newsFragment);
                        break;
                    case R.id.publish:
                        //发布消息
                        Intent intent = new Intent(context, PublishActivity.class);
                        context.startActivity(intent);
                        publish.setChecked(false);
                        break;

                }
                transaction.commit();
            }
        });
    }

}
