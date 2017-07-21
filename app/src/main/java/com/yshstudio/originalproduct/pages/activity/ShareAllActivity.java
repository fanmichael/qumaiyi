package com.yshstudio.originalproduct.pages.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.share.WbShareCallback;
//import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;


/**
 * 分享，微信好友，朋友圈，qq好友，qq空间，微博
 */
public class ShareAllActivity extends BaseActivity   {

    @BindView(R.id.share_all_weixinp)
    RadioButton shareAllWeixinp;
    @BindView(R.id.share_all_weixink)
    RadioButton shareAllWeixink;
    @BindView(R.id.share_all_qqf)
    RadioButton shareAllQqf;
    @BindView(R.id.share_all_qqk)
    RadioButton shareAllQqk;
    @BindView(R.id.share_all_weibok)
    RadioButton shareAllWeibok;
    @BindView(R.id.share_all_group)
    RadioGroup shareAllGroup;
    @BindView(R.id.share_all_cal)
    Button shareAllCal;

    private Context context;
    private String type;

    /**
     * 三方
     */
//    private WbShareHandler wbShareHandler;
    private Tencent mTencent;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_all);
        ButterKnife.bind(this);
        context = this;
        shareAll();
        initView();
        init();
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
    }


    /**
     * 注册
     */
    private void initView() {
//        WbSdk.install(ShareAllActivity.this, new AuthInfo(ShareAllActivity.this, "3287794514", "https://api.weibo.com/oauth2/default.html", "all"));
//        wbShareHandler = new WbShareHandler(ShareAllActivity.this);
//        wbShareHandler.registerApp();
        mTencent = Tencent.createInstance("1105155596", this.getApplicationContext());
        api = WXAPIFactory.createWXAPI(this, com.yshstudio.originalproduct.tools.Constants.APP_ID, true);
        api.registerApp(com.yshstudio.originalproduct.tools.Constants.APP_ID);
    }

    /**
     * 微信分享
     */
    private void weiChat(int flag) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "请安装微信客户端", Toast.LENGTH_SHORT).show();
        }
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        //创建一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (type.equals("ShareActivity")) {
            webpage.webpageUrl = "http://fir.im/qmy01";
            msg.title = "去卖艺APP";
            // msg.description = "这是我做的一款天气类app，高端大气上档次，快来看看吧！";
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求，这个必须有，否则会出错
        req.message = msg;
        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }


    /**
     * qq分享
     */
    private void onClickShare(boolean share) {
        final Bundle params = new Bundle();
        if (share) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        if (type.equals("ShareActivity")) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, "去卖艺APP");
            // params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://fir.im/qmy01");
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SharedPreferenceUtil.read("icon",""));
//            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
        }
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(ShareAllActivity.this, params, new BaseUiListener());
            }
        });


    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 分享到qq空间..................
     */
    private void shareToQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);//类型
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "跳转URL");//必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add("http://www.beehood.com/uploads/allimg/150310/2-150310142133.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(ShareAllActivity.this, params, new BaseUiListener());
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        wbShareHandler.doResultIntent(intent, this);
    }

    @OnClick(R.id.share_all_cal)
    void backCal() {
        destroyActitity();
    }

    private void shareAll() {
        shareAllGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.share_all_weixinp:
                        weiChat(0);
                        shareAllWeixinp.setChecked(false);
                        break;
                    case R.id.share_all_weixink:
                        weiChat(1);
                        shareAllWeixink.setChecked(false);
                        break;
                    case R.id.share_all_qqf:
                        onClickShare(false);
                        shareAllQqf.setChecked(false);
                        break;
                    case R.id.share_all_qqk:
                        onClickShare(true);
//                        shareToQzone();
                        shareAllQqk.setChecked(false);
                        break;
                    case R.id.share_all_weibok:

                        sendMultiMessage(true, true);
                        shareAllWeibok.setChecked(false);
                        break;

                }
            }
        });

    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObject() {
        TextObject textObject = new TextObject();
        textObject.text = "去卖艺APP";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 微博分享
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        if (hasText) {
            weiboMessage.textObject = getTextObject();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
//        wbShareHandler.shareMessage(weiboMessage, false);
    }


//    @Override
//    public void onWbShareSuccess() {
//        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onWbShareCancel() {
//        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onWbShareFail() {
//        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
