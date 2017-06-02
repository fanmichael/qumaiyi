package cn.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.chat.activity.ChatActivity;
import cn.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import cn.yshstudio.originalproduct.inc.Ini;
import cn.yshstudio.originalproduct.pages.adapter.CommentAdapter;
import cn.yshstudio.originalproduct.pages.adapter.ConGoodsAdapter;
import cn.yshstudio.originalproduct.pages.adapter.ShopImagesAdapter;
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.pages.http.HttpConnectTool;
import cn.yshstudio.originalproduct.pages.prompt.Loading;
import cn.yshstudio.originalproduct.pages.view.MyVideoView;
import cn.yshstudio.originalproduct.tools.ImageToools;
import cn.yshstudio.originalproduct.tools.ListTools;
import cn.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import cn.yshstudio.originalproduct.tools.UtilsUmeng;
import cn.yshstudio.originalproduct.tools.ValidData;
import io.vov.vitamio.widget.VideoView;

/**
 * 播放视频, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener
 */
public class LocalVideoActivity extends BaseActivity implements CommentAdapter.setOnClickLoction {
    @BindView(R.id.image_back_coll)
    ImageView imageBackColl;
    @BindView(R.id.top_title_coll)
    TextView topTitleColl;
    @BindView(R.id.collect)
    ImageView collect;
    @BindView(R.id.collect_re)
    ImageView collectRe;
    @BindView(R.id.share_coll)
    ImageView shareColl;
    @BindView(R.id.video_content_cal)
    ImageView videoContentCal;
    @BindView(R.id.video_contect_text)
    EditText videoContectText;
    @BindView(R.id.video_content_sumbit)
    Button videoContentSumbit;
    @BindView(R.id.video_content_lin)
    LinearLayout videoContentLin;
    @BindView(R.id.video_coll_con_no)
    RadioButton videoCollConNo;
    @BindView(R.id.video_coll_con)
    RadioButton videoCollCon;
    @BindView(R.id.video_chat)
    RadioButton videoChat;
    @BindView(R.id.video_faith)
    RadioButton videoFaith;
    @BindView(R.id.video_dis)
    RadioButton videoDis;
    @BindView(R.id.video_radio_pages_file)
    RadioGroup videoRadioPagesFile;
    @BindView(R.id.video_lan)
    LinearLayout videoLan;
    @BindView(R.id.video_test)
    SimpleDraweeView videoTest;
    @BindView(R.id.video_title)
    TextView videoTitle;
    @BindView(R.id.video_tags)
    TextView videoTags;
    @BindView(R.id.video_details_price)
    TextView videoDetailsPrice;
    @BindView(R.id.video_user_info_icon)
    SimpleDraweeView videoUserInfoIcon;
    @BindView(R.id.video_details_nick)
    TextView videoDetailsNick;
    @BindView(R.id.video_details_tags)
    TextView videoDetailsTags;
    @BindView(R.id.video_details_attention)
    LinearLayout videoDetailsAttention;
    @BindView(R.id.video_details_attention_no)
    LinearLayout videoDetailsAttentionNo;
    @BindView(R.id.vide_details_goods)
    ListView videDetailsGoods;
    @BindView(R.id.video_details_tip)
    TextView videoDetailsTip;
    @BindView(R.id.video_group_tip)
    TextView videoGroupTip;
    @BindView(R.id.video_details_comment)
    ListView videoDetailsComment;
    @BindView(R.id.video_scrollview)
    ScrollView videoScrollview;
    @BindView(R.id.video_images_play)
    ImageView videoImagesPlay;
    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_details_images)
    ListView videoDetailsImages;
    @BindView(R.id.video_details_info_from)
    LinearLayout videoDetailsInfoFrom;
    @BindView(R.id.video_ln)
    FrameLayout videoLn;

    @BindView(R.id.buffer)
    VideoView mVideoView;
    @BindView(R.id.probar)
    ProgressBar pb;
    @BindView(R.id.download_rate)
    TextView downloadRateView;
    @BindView(R.id.load_rate)
    TextView loadRateView;
    @BindView(R.id.video_play_re)
    RelativeLayout videoPlayRe;
    @BindView(R.id.video_play)
    MyVideoView videoPlay;

    private Context context;
    //主题
    private ContentValues values = new ContentValues();
    //评论
    private List<ContentValues> list = new ArrayList<>();
    private List<List<ContentValues>> lists = new ArrayList<>();
    //商品
    private List<ContentValues> goodsList = new ArrayList<>();
    private List<ContentValues> imgs = new ArrayList<>();

    private List<ContentValues> shopList = new ArrayList<>();
    //评价内容
    private String contentDetails = "";
    public int id;
    public int uid;
    private int coid;
    private int parentnum;
    private int typecomm;
    private boolean isCancal;
    private int typeatt;
    private boolean isSoll;
    //评论
    private CommentAdapter commentAdapter;
    private ShopImagesAdapter shopImagesAdapter;//图片介绍
    private ConGoodsAdapter conGoodsAdapter;


    //视频地址
    private MediaController mMediaController;
//    private CustomMediaController mCustomMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        //获得当前窗体对象
//        Window window = LocalVideoActivity.this.getWindow();
//        //设置当前窗体为全屏显示
//        window.setFlags(flag, flag);
        // 必须写这个，初始化加载库文件
//        Vitamio.initialize(this);
//        //设置视频解码监听
//        if (!LibsChecker.checkVitamioLibs(this)) {
//            return;
//        }
        setContentView(R.layout.activity_local_video);
        ButterKnife.bind(this);
        context = this;
        groupClike();
        topTitleColl.setText("视频详情");
        collect.setVisibility(View.GONE);
        collectRe.setVisibility(View.GONE);
        initDelay();
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        uid = bundle.getInt("uid");
        if (String.valueOf(uid).equals(AppContext.cv.getAsInteger("id"))) {
//        if (uid == AppContext.cv.getAsInteger("id")) {
//            videoLan.setVisibility(View.GONE);
            videoFaith.setVisibility(View.GONE);
            videoDetailsAttention.setVisibility(View.GONE);
            videoDetailsAttentionNo.setVisibility(View.GONE);
            collect.setVisibility(View.GONE);
            collectRe.setVisibility(View.GONE);
        }
        setDelayMessage(1, 100);
        videoDetailsImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> imgUrl = new ArrayList<String>();
                for (int i = 0; i < imgs.size(); i++) {
                    imgUrl.add(imgs.get(i).getAsString("imgs"));
                }
                Intent intent1 = new Intent(context, PictureDisplayActivity.class);
                intent1.putExtra("position", imgUrl.size());
                intent1.putStringArrayListExtra("enlargeImage", imgUrl);
                startActivity(intent1);
            }
        });

        videoPlayRe.setVisibility(View.GONE);


    }


    /**
     * 举报
     */
    @OnClick(R.id.video_details_info_from)
    void inFrom() {
        Intent intent = new Intent(context, InfromActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("rid", "" + id);
        bundle.putString("type", "2");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
            videoLn.setVisibility(View.VISIBLE);
            videoPlay.setVisibility(View.GONE);
        }
    }

    /**
     * 播放视频
     */
    @OnClick(R.id.video_images_play)
    void videoPlay() {
        videoLn.setVisibility(View.GONE);
        videoPlay.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(values.getAsString("subject"));
        videoPlay.setMediaController(new MediaController(this));
        videoPlay.setOnCompletionListener(new MyPlayerOnCompletionListener());
        videoPlay.setVideoURI(uri);
        //开始播放视频
        videoLn.setVisibility(View.GONE);
        videoPlay.setVisibility(View.VISIBLE);
        videoPlay.start();

        videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
//                            videoPlay.setBackgroundColor(Color.TRANSPARENT);
                        videoTest.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
            }
        });


//        //开始播放视频
//        videoLn.setVisibility(View.GONE);
//        videoPlayRe.setVisibility(View.VISIBLE);
//        Uri uri = Uri.parse(values.getAsString("subject"));
//        mMediaController = new MediaController(this);
//        mCustomMediaController = new CustomMediaController(this, mVideoView, this);
//        mCustomMediaController.setVideoName("白火锅 x 红火锅");
//        mVideoView.setVideoURI(uri);//设置视频播放地址
//        mVideoView.setMediaController(mCustomMediaController);
//        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
//        mMediaController.show(5000);
//        mVideoView.requestFocus();
//        mVideoView.setOnInfoListener(this);
//        mVideoView.setOnBufferingUpdateListener(this);
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setPlaybackSpeed(1.0f);
//            }
//        });
    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        loadRateView.setText(percent + "%");
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        switch (what) {
//            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                if (mVideoView.isPlaying()) {
//                    mVideoView.pause();
//                    pb.setVisibility(View.VISIBLE);
//                    downloadRateView.setText("");
//                    loadRateView.setText("");
//                    downloadRateView.setVisibility(View.VISIBLE);
//                    loadRateView.setVisibility(View.VISIBLE);
//                }
//                break;
//            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                mVideoView.start();
//                pb.setVisibility(View.GONE);
//                downloadRateView.setVisibility(View.GONE);
//                loadRateView.setVisibility(View.GONE);
//                break;
//            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                downloadRateView.setText("" + extra + "kb/s" + "  ");
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        //屏幕切换时，设置全屏
//        if (mVideoView != null) {
//            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
//        }
//        super.onConfigurationChanged(newConfig);
//    }

    /**
     * 数据
     */
    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        videoDetailsPrice.setText(values.getAsInteger("follow") + "");
        Uri imageUri = Uri.parse(values.getAsString("icon"));
        ValidData.load(imageUri, videoUserInfoIcon, 60, 60);
        videoDetailsNick.setText(values.getAsString("nick"));
        videoDetailsTags.setText(values.getAsString("personalized"));
        videoTags.setText(values.getAsString("tags"));
        videoTitle.setText(values.getAsString("title"));
        Uri videoImage = Uri.parse(values.getAsString("video_img"));
        ValidData.load(videoImage, videoTest, width, 150);
        videoName.setText(values.getAsString("content"));
        videoImagesPlay.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.video_content_cal)
    void cal() {
        right();
        videoContectText.setText("");
    }

    @OnClick(R.id.video_content_sumbit)
    void sumit() {
        right();
        contentDetails = videoContectText.getText().toString().trim();
        if (contentDetails.equals("")) {
            Toast.makeText(context, "请输入评语", Toast.LENGTH_SHORT).show();
        } else {
            new asyncTask().execute(6);
        }
    }

    @OnClick(R.id.video_user_info_icon)
    void userInfo() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", uid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 点击关注
     */
    @OnClick(R.id.video_details_attention)
    void atten() {
        typeatt = 4;
        videoDetailsAttention.setVisibility(View.GONE);
        videoDetailsAttentionNo.setVisibility(View.VISIBLE);
        setDelayMessage(3, 100);
    }

    /**
     * 取消关注
     */
    @OnClick(R.id.video_details_attention_no)
    void attenNo() {
        typeatt = 5;
        videoDetailsAttention.setVisibility(View.VISIBLE);
        videoDetailsAttentionNo.setVisibility(View.GONE);
        setDelayMessage(3, 100);
    }


    /**
     * 提示隐藏
     */
    private void goodTip() {
        if (goodsList.size() > 0) {
            videoGroupTip.setVisibility(View.GONE);
            videDetailsGoods.setVisibility(View.VISIBLE);
        } else {
            videoGroupTip.setVisibility(View.VISIBLE);
            videDetailsGoods.setVisibility(View.GONE);
        }

        if (shopList.size() > 0) {
            videoDetailsTip.setVisibility(View.GONE);
            videoDetailsComment.setVisibility(View.VISIBLE);
        } else {
            videoDetailsTip.setVisibility(View.VISIBLE);
            videoDetailsComment.setVisibility(View.GONE);
        }


    }

    /**
     * 回复
     */
    @Override
    public void content(int posit, int nid, int uid, int parent) {
        videoContectText.setText("");
        coid = nid;
        parentnum = parent;
        left();
    }

    /**
     * 加载评论
     */
    private void commAdapter() {
        goodTip();
        commentAdapter = new CommentAdapter(context, list, lists, this);
        videoDetailsComment.setAdapter(commentAdapter);
        ListTools.setListViewHeightBasedOnChildren(videoDetailsComment);
    }

    /**
     * 添加介绍
     */
    private void imgsList() {
        shopImagesAdapter = new ShopImagesAdapter(context, imgs);
        videoDetailsImages.setAdapter(shopImagesAdapter);
        ListTools.setListViewHeightBasedOnChildren(videoDetailsImages);
    }

    /**
     * 相关商品
     */
    private void conAdapter() {
        conGoodsAdapter = new ConGoodsAdapter(shopList, context);
        videDetailsGoods.setAdapter(conGoodsAdapter);
        ListTools.setListViewHeightBasedOnChildren(videDetailsGoods);
    }


    /**
     * 返回
     */
    @OnClick(R.id.image_back_coll)
    void back() {
        destroyActitity();
    }

    /**
     * 进评价动画
     */
    private void left() {//jin
        videoContentLin.setVisibility(View.VISIBLE);
        videoRadioPagesFile.setVisibility(View.GONE);
        videoContentLin.setAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    /**
     * 出1进评价动画
     */
    private void right() {//chu
        videoContentLin.setVisibility(View.GONE);
        videoRadioPagesFile.setVisibility(View.VISIBLE);
        videoContentLin.setAnimation(AnimationUtils.makeOutAnimation(this, false));
    }


    /**
     * 底部点击事件
     */
    private void groupClike() {

        videoRadioPagesFile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.video_coll_con_no:
                        //取消收藏
                        videoCollCon.setVisibility(View.VISIBLE);
                        videoCollConNo.setVisibility(View.GONE);
                        typecomm = 2;
                        setDelayMessage(2, 100);
                        videoCollConNo.setChecked(false);
                        break;
                    case R.id.video_coll_con:
                        //收藏
                        videoCollCon.setVisibility(View.GONE);
                        videoCollConNo.setVisibility(View.VISIBLE);
                        typecomm = 3;
                        setDelayMessage(2, 100);
                        videoCollCon.setChecked(false);
                        break;
                    case R.id.video_chat:
                        //加入群聊
                        Intent intent = new Intent(context, ElcyGroupDeActivity.class);
                        context.startActivity(intent);
                        videoChat.setChecked(false);
                        break;
                    case R.id.video_faith:
                        //私聊群主
                        sendMessage();
                        videoFaith.setChecked(false);
                        break;
                    case R.id.video_dis:
                        //评论
                        videoContectText.setText("");
                        coid=id;
                        parentnum = 0;
                        left();
                        videoDis.setChecked(false);
                        break;

                }

            }
        });

    }

    private void sendMessage() {
        if (SharedPreferenceUtil.hasKey("id")) {
            if (SharedPreferenceUtil.read("id", "").isEmpty()) {
                return;
            }
        } else {
            if (AppContext.cv.getAsString("mobile").trim().isEmpty()) {
                return;
            }
        }
        Intent intent = new Intent(LocalVideoActivity.this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, values.getAsString("mobile")).putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE).putExtra("NICK", values.getAsString("nick"));
        if (UserInfo.getInstance().getInfo() == null || UserInfo.getInstance().getInfo().get(values.getAsString("mobile")) == null) {
            UserInfo.getInstance().addInfo(new UserInfo.User().setUid(values.getAsString("mobile")).setNick(values.getAsString("nick")).setIcon(values.getAsString("icon")));
        } else {
            UserInfo.getInstance().getInfo().get(values.getAsString("mobile")).setNick(values.getAsString("nick")).setIcon(values.getAsString("icon"));
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                ObjectSaveUtils.saveObject(LocalVideoActivity.this, "USERICON", UserInfo.getInstance());
            }
        }.start();
        startActivity(intent);
    }

    /**
     * 延迟加载
     */
    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLoading = new Loading(
                                context, videoTest);
                        mLoading.setText("正在加载......");
                        mLoading.show();
                        new asyncTask().execute(1);
                        break;
                    case 2:
                        new asyncTask().execute(2);
                        break;
                    case 3:
                        new asyncTask().execute(5);
                        break;
                    case 4:
                        new asyncTask().execute(6);
                        break;
                }
            }
        };
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpFollowStatus();
                    httpCollesStatus();
                    httpShop();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpCollesStatuscollection();
//                    bundle.putInt("what", 2);
                    break;
                case 5:
                    httpFollowStatusfollow();
                    break;
                case 6:
                    httpContent();
                    bundle.putInt("what", 6);
                    break;
                case 7:
                    httpS();
                    bundle.putInt("what", 7);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                    initView();
                    if (String.valueOf(uid).equals(AppContext.cv.getAsInteger("id"))) {
                        isColl();
                    }
                    imgsList();
                    conAdapter();
                    commAdapter();
                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            videoScrollview.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    break;
                case 2:
                    isColl();
                    break;
                case 6:
                    new asyncTask().execute(7);
                    break;
                case 7:
                    commAdapter();
                    break;


            }
        }
    }

    /**
     * 判断关注状态
     */
    private void httpFollowStatusfollow() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.follow");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "2");
            map.put("type_id", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 115) {
                    if (typecomm == 4) {
                        isCancal = true;
                    } else if (typecomm == 5) {
                        isCancal = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断关注状态Community.follow
     */
    private void httpFollowStatus() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkFollowStatus");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "2");
            map.put("type_id", uid + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 121) {
                    isCancal = false;
                } else if (error == 0) {
                    isCancal = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //判断收藏状态Community.collection
    private void httpCollesStatuscollection() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.collection");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "0");
            map.put("type_id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 115) {
                    if (typeatt == 2) {
                        isSoll = false;
                    } else if (typeatt == 3) {
                        isSoll = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断收藏状态Community.collection
     */
    private void httpCollesStatus() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.checkCollectStatus");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("type", "0");
            map.put("type_id", id + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                JSONObject obj = new JSONObject(json);
                int error = obj.getInt("error");
                if (error == 121) {
                    isSoll = false;
                } else if (error == 0) {
                    isSoll = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 评价
     */
    private void httpS() {
        if (list == null) {
            list = new ArrayList<>();
        } else if (list.size() > 0) {
            list.clear();
        }

        if (lists == null) {
            lists = new ArrayList<>();
        } else if (lists.size() > 0) {
            lists.clear();
        }

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.details");
            map.put("id", id + "");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlS(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlS(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONArray commentJson = new JSONArray(objData.getString("comment"));//图片介绍
            if (commentJson.length() > 0) {
                for (int i = 0; i < commentJson.length(); i++) {
                    JSONObject jsonObj = commentJson.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", jsonObj.getInt("id"));
                    contentValues.put("uid", jsonObj.getInt("uid"));
                    contentValues.put("nid", jsonObj.getInt("nid"));
                    contentValues.put("parent", jsonObj.getInt("parent"));
                    contentValues.put("content", jsonObj.getString("content"));
                    contentValues.put("createtime", jsonObj.getString("createtime"));
                    contentValues.put("nick", jsonObj.getString("nick"));
                    contentValues.put("icon", jsonObj.getString("icon"));
                    list.add(contentValues);
                    JSONArray commJson = new JSONArray(jsonObj.getString("case"));
                    if (commJson.length() > 0) {
                        List<ContentValues> cvx = new ArrayList<>();
                        for (int j = 0; j < commJson.length(); j++) {
                            JSONArray commJsons = commJson.getJSONArray(j);
                            if (commJsons.length() > 0) {
                                for (int g = 0; g < commJsons.length(); g++) {
                                    JSONObject jsonObjss = commJsons.getJSONObject(g);
                                    ContentValues content = new ContentValues();
                                    content.put("id", jsonObjss.getInt("id"));
                                    content.put("uid", jsonObjss.getInt("uid"));
                                    content.put("nid", jsonObjss.getInt("nid"));
                                    content.put("parent", jsonObjss.getInt("parent"));
                                    content.put("content", jsonObjss.getString("content"));
                                    content.put("createtime", jsonObjss.getString("createtime"));
                                    content.put("nick", jsonObjss.getString("nick"));
                                    cvx.add(content);
                                }
                            }
                        }
                        lists.add(i, cvx);
                    }
                }
            }

        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //商品详情-首次加载
    private void httpShop() {
        if (values == null) {
            values = new ContentValues();
        } else if (values.size() > 0) {
            values.clear();
        }

        if (imgs == null) {
            imgs = new ArrayList<>();
        } else if (imgs.size() > 0) {
            imgs.clear();
        }
        if (list == null) {
            list = new ArrayList<>();
        } else if (list.size() > 0) {
            list.clear();
        }

        if (lists == null) {
            lists = new ArrayList<>();
        } else if (lists.size() > 0) {
            lists.clear();
        }

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.details");
            map.put("id", id + "");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlShop(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlShop(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objnote = new JSONObject(objData.getString("note"));


            values.put("id", objnote.getInt("id"));
            values.put("uid", objnote.getInt("uid"));
            values.put("cid", objnote.getInt("cid"));
            values.put("follow", objnote.getInt("follow"));
            values.put("status", objnote.getInt("status"));
            values.put("subject", objnote.getString("subject"));
            if (objnote.has("video_img")) {
                values.put("video_img", objnote.getString("video_img"));
            }
            values.put("title", objnote.getString("title"));
            values.put("content", objnote.getString("content"));
            values.put("tags", objnote.getString("tags"));
            values.put("personalized", objnote.getString("personalized"));
            values.put("nick", objnote.getString("nick"));
            values.put("icon", objnote.getString("icon"));
            values.put("mobile", objnote.getString("mobile"));
            JSONArray imagesJson = new JSONArray(objnote.getString("content_imgs"));//图片介绍
            if (imagesJson.length() > 0) {
                for (int i = 0; i < imagesJson.length(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put("imgs", imagesJson.get(i).toString());
                    imgs.add(cv);
                }

            }


            JSONArray commentJson = new JSONArray(objData.getString("comment"));//图片介绍
            if (commentJson.length() > 0) {
                for (int i = 0; i < commentJson.length(); i++) {
                    JSONObject jsonObj = commentJson.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", jsonObj.getInt("id"));
                    contentValues.put("uid", jsonObj.getInt("uid"));
                    contentValues.put("nid", jsonObj.getInt("nid"));
                    contentValues.put("parent", jsonObj.getInt("parent"));

                    contentValues.put("content", jsonObj.getString("content"));
                    contentValues.put("createtime", jsonObj.getString("createtime"));
                    contentValues.put("nick", jsonObj.getString("nick"));
                    contentValues.put("icon", jsonObj.getString("icon"));
                    list.add(contentValues);
                    JSONArray commJson = new JSONArray(jsonObj.getString("case"));
                    if (commJson.length() > 0) {
                        List<ContentValues> cvx = new ArrayList<>();
                        for (int j = 0; j < commJson.length(); j++) {
                            JSONArray commJsons = commJson.getJSONArray(j);
                            if (commJsons.length() > 0) {
                                for (int g = 0; g < commJsons.length(); g++) {
                                    JSONObject jsonObjss = commJsons.getJSONObject(g);
                                    ContentValues content = new ContentValues();
                                    content.put("id", jsonObjss.getInt("id"));
                                    content.put("uid", jsonObjss.getInt("uid"));
                                    content.put("nid", jsonObjss.getInt("nid"));
                                    content.put("parent", jsonObjss.getInt("parent"));
                                    content.put("content", jsonObjss.getString("content"));
                                    content.put("createtime", jsonObjss.getString("createtime"));
                                    content.put("nick", jsonObjss.getString("nick"));
                                    cvx.add(content);
                                }

                            }
                        }
                        lists.add(i, cvx);
                    }
                }
            }


            JSONArray goodsJson = new JSONArray(objData.getString("goods"));//g感兴趣的商品
            if (goodsJson.length() > 0) {
                for (int i = 0; i < goodsJson.length(); i++) {
                    JSONObject jsonObj = goodsJson.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));//id
                    cv.put("good_name", jsonObj.getString("good_name"));//标题
                    cv.put("good_image", jsonObj.getString("good_image"));//图
                    cv.put("price", jsonObj.getString("price"));//价格
                    shopList.add(cv);
                }
            }


        } catch (JSONException er) {
            er.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回复-评论
    private void httpContent() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.content");
            map.put("id", coid + "");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("content", URLEncoder.encode(contentDetails, "UTF-8") + "");
            map.put("parent", parentnum + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                Log.e("", "" + json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断状态隐藏
    private void isColl() {
        if (isCancal == false) {
            videoDetailsAttention.setVisibility(View.VISIBLE);
            videoDetailsAttentionNo.setVisibility(View.GONE);
        } else if (isCancal == true) {
            videoDetailsAttention.setVisibility(View.GONE);
            videoDetailsAttentionNo.setVisibility(View.VISIBLE);
        }

        if (isSoll == false) {
            videoCollCon.setVisibility(View.GONE);
            videoCollConNo.setVisibility(View.VISIBLE);
        } else if (isSoll == true) {
            videoCollCon.setVisibility(View.VISIBLE);
            videoCollConNo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.share_coll)
    void shareColl() {
        ImageToools.requestCameraPermission(context);
        UtilsUmeng.share(LocalVideoActivity.this, Ini.ShareCommunity_Url + id, values.getAsString("content"));
    }

}
