package cn.com.shequnew.pages.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import cn.com.shequnew.R;
/**
 * 启动页
 * */
public class AppStartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = View.inflate(this, R.layout.activity_app_start, null);
        setContentView(view);

        //渐变动画
        AlphaAnimation animation=new AlphaAnimation(0.6f,1.0f);
        animation.setDuration(1000);
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //跳转登陆
    private void redirectTo(){
        Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
        startActivity(intent);
        destroyActitity();
    }




}
