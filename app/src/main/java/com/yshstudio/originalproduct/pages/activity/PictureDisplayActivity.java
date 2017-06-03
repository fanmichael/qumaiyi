package com.yshstudio.originalproduct.pages.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.yshstudio.originalproduct.R;


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
            int position = intent.getIntExtra("position", -1);
            ArrayList<String> url = intent.getStringArrayListExtra("enlargeImage");
            Log.e(TAG, "a==" + position);
            a = position;
            pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(url.size()));
//            bit("http://qmy.51edn.com/upload/icons/20170509/049939874ca7289dbbfeabea355f947f.jpg");//先显示传进来的第一张
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

//            Looper.prepare();// 必须调用此方法，要不然会报错
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

                Log.e("NSC", "upX-downX = " + (upX - downX));
                if (upX - downX > 120) {//右滑的时候
                    Log.e("NSC", "a = " + a + ":" + mMemoryCache.size());
                    if (a <= mMemoryCache.size()) {
                        // LogUtil.e(TAG, mMemoryCache.get("imageKey"+a).getWidth()+":"+mMemoryCache.get("imageKey"+a).getHeight());
                        //mMemoryCache.get("imageKey"+a).getWidth();
                        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
//                        Animation rInAnimIn = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
                        imageView.setAnimation(animation);
                        imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                        pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));

                        a++;
                        if (a > mMemoryCache.size()) {
                            a = 1;
                        }
                    }

                } else if (upX - downX < -120) {//左滑
                    Log.e("NSC", "aaaa = " + a + ":" + mMemoryCache.size());
                    if (a > 0) {
                        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
//                        Animation lInAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
                        imageView.setAnimation(animation);
                        imageView.setImageBitmap(mMemoryCache.get("imageKey" + a));
                        pictureDisplayText.setText((Integer.valueOf(a)) + "/" + Integer.valueOf(mMemoryCache.size()));
                        a--;
                        if (a == 0) {
                            a = mMemoryCache.size();
                        }
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
