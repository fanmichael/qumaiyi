package cn.com.shequnew.pages.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.com.shequnew.R;

/**
 * 播放视频
 */
public class LocalVideoActivity extends BaseActivity {


    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        ButterKnife.bind(this);
        initView();

//        Bitmap bitmap = createVideoThumbnail(url, 200, 150);
//        test.setImageBitmap(bitmap);
    }

//    MediaController mc = new MediaController(this);
//        mc.setVisibility(View.INVISIBLE);
//        videoView.setMediaController(mc);


    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        int id = bundle.getInt("id");
        int uid = bundle.getInt("uid");


        Uri uri = Uri.parse(url);
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.VISIBLE);
//        video.setMediaController(mc);
//        video.setOnCompletionListener(new MyPlayerOnCompletionListener());
//        video.setVideoURI(uri);
//        video.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


}
