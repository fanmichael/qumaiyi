package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.fragment.DynamicFragment;
import cn.com.shequnew.pages.fragment.NewsFragment;
import cn.com.shequnew.pages.fragment.PageCommFragment;
import cn.com.shequnew.pages.fragment.PagesFragment;
import cn.com.shequnew.pages.fragment.PublishFragment;
/**
 * 主页
 * */
public class MainActivity extends FragmentActivity {

    @BindView(R.id.radio_pages)
    RadioGroup radioPages;
    private Context context;
    private PageCommFragment pagesFragment;
    private DynamicFragment dynamicFragment;
    private PublishFragment publishFragment;
    private NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setDefaultFragment();
        setFragmentChange();
    }


    private void setDefaultFragment()
    {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        pagesFragment = new PageCommFragment();
        transaction.replace(R.id.fra_layout, pagesFragment);
        transaction.commit();
    }

    private void setFragmentChange() {
        radioPages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.pages:
                        if(pagesFragment==null){
                            pagesFragment = new PageCommFragment();
                        }
                        transaction.replace(R.id.fra_layout, pagesFragment);
                        break;
                    case R.id.dynamic:
                        if(dynamicFragment==null){
                            dynamicFragment = new DynamicFragment();
                        }
                        transaction.replace(R.id.fra_layout, dynamicFragment);
                        break;
                    case R.id.news:
                        if(publishFragment==null){
                            publishFragment = new PublishFragment();
                        }
                        transaction.replace(R.id.fra_layout, publishFragment);
                        break;
                    case R.id.mine:
                        if(newsFragment==null){
                            newsFragment = new NewsFragment();
                        }
                        transaction.replace(R.id.fra_layout, newsFragment);
                        break;
                    case R.id.publish:
                        //发布消息
                        Intent intent=new Intent(context,PublishActivity.class);
                        context.startActivity(intent);
                        break;

                }
                transaction.commit();
            }
        });
    }
}
