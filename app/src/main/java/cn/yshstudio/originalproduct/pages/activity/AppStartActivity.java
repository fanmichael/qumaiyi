package cn.yshstudio.originalproduct.pages.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.tools.SharedPreferenceUtil;

/**
 * 启动页
 */
public class AppStartActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray;

    private int currentItem;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_app_start, null);
        setContentView(view);
        ButterKnife.bind(this);
        context = this;

//        //渐变动画
//        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
//        animation.setDuration(1000);
//        view.setAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                redirectTo();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

        if (SharedPreferenceUtil.hasKey("is")) {
            Intent intent = new Intent(AppStartActivity.this, FristAdvActivity.class);
            startActivity(intent);
            destroyActitity();
        } else {
            initView();
        }
    }


    private void initView() {
        imgIdArray = new int[]{R.drawable.background, R.drawable.start1, R.drawable.start22};
        tips = new ImageView[imgIdArray.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.slitwo);
            } else {
                tips[i].setBackgroundResource(R.drawable.slione);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,
                    ViewPager.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 4;
            layoutParams.rightMargin = 4;
            viewGroup.addView(imageView, layoutParams);
        }

        //将图片装载到数组中
        mImageViews = new ImageView[imgIdArray.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }
        //设置Adapter
        viewPager.setAdapter(new MyAdapter(this));
        viewPager.setFocusable(true);
        //设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem(0);

    }


    public class MyAdapter extends PagerAdapter {
        Activity activity = null;

        public MyAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position]);
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager) container).addView(mImageViews[position], 0);
            } catch (Exception e) {
                //handler something
            }
            return mImageViews[position];
        }


    }


    //跳转登陆
    private void redirectTo() {
        Intent intent = new Intent(AppStartActivity.this, FristAdvActivity.class);
        startActivity(intent);
        SharedPreferenceUtil.insert("is", true);
        destroyActitity();
    }

    private boolean flag = false;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position);
        currentItem = position;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                //拖的时候才进入下一页
                flag = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                flag = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                // 当前为最后一张，此时从右向左滑，则切换到第一张
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !flag) {
                    redirectTo();
                }
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (viewPager.getCurrentItem() == 0 && !flag) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 2);
                }
                break;
        }
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.slitwo);
            } else {
                tips[i].setBackgroundResource(R.drawable.slione);
            }
        }
    }

}
