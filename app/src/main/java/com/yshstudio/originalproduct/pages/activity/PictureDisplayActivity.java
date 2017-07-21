package com.yshstudio.originalproduct.pages.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PictureDisplayActivity extends BaseActivity {

    @BindView(R.id.picture_display_image)
    SimpleDraweeView imageView;
    @BindView(R.id.picture_display_text)
    TextView pictureDisplayText;
    int downX = 0;
    int moveX = 0;
    int upX = 0;
    int a = 1; //显示的图片位置
    int imageKey = 1;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 9;//设置缓冲空间存储大小
    private LruCache<String, Bitmap> mMemoryCache = new LruCache<>(cacheSize);
    Bitmap bitmap = null;
    private final String TAG = "PictureDisplayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_display);
        ButterKnife.bind(this);
        new Thread(runa).start();

        /**
         * 图片放大，缩小，拖坠，处理
         * */
//        imageView.setOnTouchListener(new TouchListener());
    }

    /**
     *  图片放大处理
     *  -----------无用----------
     * */
    private final class TouchListener implements View.OnTouchListener{
        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());


                    downX = (int) event.getRawX();
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:

                    moveX = (int) event.getRawX();

                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)

                    upX = (int) event.getRawX();
                    int pValue = upX - downX;//滑动的距离差
                    if (upX - downX > 120) {//右滑的时候
                        if (a <= mMemoryCache.size()) {
                            a++;
                            if (a > mMemoryCache.size()) {
                                a = 1;
                            }
                            AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
                            imageView.setAnimation(animation);
                            imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                            pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));
//                        a++;
//                        if (a > mMemoryCache.size()) {
//                            a = 1;
//                        }
                        }

                    } else if (upX - downX < -120) {//左滑
                        if (a > 0) {
                            a--;
                            if (a == 0) {
                                a = mMemoryCache.size();
                            }
                            AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
                            imageView.setAnimation(animation);
                            imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                            pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));
//                        a--;
//                        if (a == 0) {
//                            a = mMemoryCache.size();
//                        }
                        }

                    }

                    if (pValue == 0) {//点击触摸就关闭窗口
                        finish();
                    }

                        break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }
        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            float dd=dx * dx + dy * dy;
            /** 使用勾股定理返回两点之间的距离 */
            return (float)Math.sqrt(dd);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }

    }

    /**
     * 显示下载图片
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                updateImageView((Bitmap) msg.obj);
            }
        }
    };

    /**
     * //将bitmap添加到缓存中
     *
     * @param bm
     */
    private void updateImageView(Bitmap bm) {
        mMemoryCache.put("imageKey" + imageKey, bm);
        imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
        imageKey++;
    }

    private Runnable runa = new Runnable() {
        @Override
        public void run() {
            Intent intent = getIntent();
            int position = intent.getIntExtra("position",0);
            ArrayList<String> url = intent.getStringArrayListExtra("enlargeImage");
            Log.e(TAG, "a==" + position);
//            a = position;
            pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(url.size()));
            for (int i = 0; i < url.size(); i++) {
                bit(url.get(i).toString());
            }
        }
    };

    private void bit(String url) {
        byte[] data = new byte[0];
        try {
            data = getImage(url);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  //生成位图
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bitmap;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");   //设置请求方法为GET
        conn.setReadTimeout(5 * 1000);    //设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
        byte[] data = readInputStream(inputStream);     //获得图片的二进制数据
        return data;

    }

    public byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://获取点击的时候屏幕位置
                downX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_UP://手离开的时候位置
                upX = (int) event.getRawX();
                int pValue = upX - downX;//滑动的距离差
                if (upX - downX > 120) {//右滑的时候
                    if (a <= mMemoryCache.size()) {
                        a++;
                        if (a > mMemoryCache.size()) {
                            a = 1;
                        }
                        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
                        imageView.setAnimation(animation);
                        imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                        pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));
                    }

                } else if (upX - downX < -120) {//左滑
                    if (a > 0) {
                        a--;
                        if (a == 0) {
                            a = mMemoryCache.size();
                        }
                        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
                        imageView.setAnimation(animation);
                        imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                        pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));
                    }

                }

                if (pValue == 0) {//点击触摸就关闭窗口
                    finish();
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }
}
