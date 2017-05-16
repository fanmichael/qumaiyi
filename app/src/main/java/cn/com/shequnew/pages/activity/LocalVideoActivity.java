package cn.com.shequnew.pages.activity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;

/**
 * 播放视频
 */
public class LocalVideoActivity extends BaseActivity {

    @BindView(R.id.video)
    VideoView video;
    @BindView(R.id.test)
    SimpleDraweeView test;
    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        ButterKnife.bind(this);
        initView();

        Bitmap bitmap = createVideoThumbnail(url, 200, 150);
        test.setImageBitmap(bitmap);
    }

//    MediaController mc = new MediaController(this);
//        mc.setVisibility(View.INVISIBLE);
//        videoView.setMediaController(mc);


    private void initView() {
        Uri uri = Uri.parse(url);
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.VISIBLE);
        video.setMediaController(mc);
        video.setOnCompletionListener(new MyPlayerOnCompletionListener());
        video.setVideoURI(uri);
        video.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}
