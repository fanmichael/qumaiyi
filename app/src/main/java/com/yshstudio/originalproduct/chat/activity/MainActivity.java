/*
package cn.com.shequnew.chat.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.GroupImageBean;
import com.hyphenate.easeui.model.UserInfo;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;
import cn.com.shequnew.chat.RXbus.RxBus;
import cn.com.shequnew.chat.eventtype.JSEvent;
import cn.com.shequnew.chat.eventtype.LoginIM;
import cn.com.shequnew.chat.eventtype.PushWebView;
import cn.com.shequnew.chat.util.PermissionUtil;
import cn.com.shequnew.chat.util.UpdataGroupsInfo;
import cn.com.shequnew.chat.util.UpdateApp;
import cn.com.shequnew.chat.view.EMFrameLayout;
import cn.com.shequnew.chat.view.PopUpWindowMag;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.frame)
    EMFrameLayout frame;
    @BindView(R.id.activity_main)
    FrameLayout activityMain;
    EaseConversationListFragment fragment;
    @BindView(R.id.webview)
    FrameLayout webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PermissionUtil.verifyStoragePermissions(this);
        initH5(savedInstanceState);
        initJsEvent();
        init();
        //update();
        //if (SDK.obtainAllIWebview().get(0).obtainFullUrl().endsWith("login.html"))
        // toast(SDK.obtainAllIWebview().get(0).obtainFullUrl());
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                switch (i) {
                    case EMError.USER_LOGIN_ANOTHER_DEVICE:
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("tag", "onDisconnected: " + SDK.obatinFirstPage(SDK.obtainCurrentApp()));
                                    if (SDK.obatinFirstPage(SDK.obtainCurrentApp()) != null) {
                                        SDK.obatinFirstPage(SDK.obtainCurrentApp()).evalJS("logoff();");

                                    }
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mEntryProxy.onActivityExecute(this, ISysEventListener.SysEventType.onCreateOptionMenu, menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEntryProxy.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        mEntryProxy.onResume(this);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getFlags() != 0x10600000) {// 非点击icon调用activity时才调用newintent事件
            mEntryProxy.onNewIntent(this, intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void update() {
        UpdateApp updaApp = new UpdateApp();
        updaApp.setContext(MainActivity.this);
        updaApp.checkoutVertion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEntryProxy.onStop(this);
        jsevent.unsubscribe();
        jsevent.unsubscribe();
        loginIM.unsubscribe();
        pushWebView.unsubscribe();
        updataGroupImage.unsubscribe();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, ISysEventListener.SysEventType.onKeyDown, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, ISysEventListener.SysEventType.onKeyUp, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean _ret = mEntryProxy.onActivityExecute(this, ISysEventListener.SysEventType.onKeyLongPress, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyLongPress(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        try {
            int temp = this.getResources().getConfiguration().orientation;
            if (mEntryProxy != null) {
                mEntryProxy.onConfigurationChanged(this, temp);
            }
            super.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x01) {
            //
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                File file = new File(getRealPathFromURI(uri));
                if (file.getName().endsWith(".mp4")) {
                    Toast.makeText(this, "" + file.getName(), Toast.LENGTH_LONG).show();
                    uploadVideo(file);
                }
                return;
                */
/*Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                cursor.moveToFirst();
                // String imgNo = cursor.getString(0); // 图片编号
                String v_path = cursor.getString(1); // 图片文件路径
                String v_size = cursor.getString(2); // 图片大小
                String v_name = cursor.getString(3); // 图片文件名*//*

            }
        }
        mEntryProxy.onActivityExecute(this, ISysEventListener.SysEventType.onActivityResult, new Object[]{requestCode, resultCode, data});
    }

    private Subscription jsevent;
    private Subscription loginIM;
    private Subscription pushWebView;
    private Subscription updataGroupImage;
    private Subscription selectVideo;

    private void uploadVideo(File file) {


        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";

        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAImg7aetBxp8Kn", "mgUv74WJqepnQOPIj5LF8C30GYSlzH");

        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        String name = "video/" + System.currentTimeMillis() + file.getName();
        PutObjectRequest put = new PutObjectRequest("qumaiyi", name, file.getPath());

        ObjectMetadata metadata = new ObjectMetadata();
        // 指定Content-Type
        metadata.setContentType("application/octet-stream");
        // user自定义metadata
        metadata.addUserMetadata("x-oss-meta-name1", "value1");
        put.setMetadata(metadata);
        */
/*PutObjectRequest put = new PutObjectRequest("qumaiyi", , file.getPath());

// 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });*//*


        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "1234Video", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

// task.cancel(); // 可以取消任务

    }

    private void initJsEvent() {
        if (selectVideo == null) {
            selectVideo = RxBus.getDefault().toObservable(JSEvent.class).subscribe(new Observer<JSEvent>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(JSEvent jsEvent) {
                    if (jsEvent.getEventType().equals("VIDEO")) {
                        Intent intent = new Intent();
                        intent.setType("video*/
/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 0x01);
                    }
                }
            });
        }
        if (updataGroupImage == null) {
            updataGroupImage = RxBus.getDefault().toObservable(GroupImageBean.class).subscribe(new Observer<GroupImageBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(GroupImageBean groupImageBean) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragment.refresh();
                        }
                    });
                }
            });
        }
        if (pushWebView == null) {
            pushWebView = RxBus.getDefault().toObservable(PushWebView.class).subscribe(new Observer<PushWebView>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    toast(e.getMessage());
                }

                @Override
                public void onNext(final PushWebView pushWebView) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (pushWebView.getTYPE()) {
                                case PushWebView.PUSH:

                            */
/*fragment.setEnable(false);*//*

                            */
/*frame.setVisibility(View.GONE);*//*

                                    translateFrame(0, -1, 0, 0);
                                    frame.setEnabled(false);
                                    break;
                                case PushWebView.BACK:
                            */
/*fragment.setEnable(true);*//*

                            */
/*frame.setVisibility(View.VISIBLE);*//*

                                    translateFrame(-1, 0, 0, 0);
                                    frame.setEnabled(true);
                                    break;
                            }
                        }
                    });
                }
            });
        }
        if (loginIM == null) {
            loginIM = RxBus.getDefault().toObservable(LoginIM.class).subscribe(new Observer<LoginIM>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(LoginIM loginIM) {
                    loginIM(loginIM.getMobile());
                    //loginIM("jiangruicheng");
                }
            });
        }
        if (jsevent == null) {
            jsevent = RxBus.getDefault().toObservable(JSEvent.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JSEvent>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(final JSEvent jsEvent) {

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (jsEvent.getEventType()) {
                                    case "hideNativeView":
                                        frame.clearAnimation();
                                        //fragment.setEnable(false);
                                        frame.setVisibility(View.GONE);
                                        frame.setEnabled(false);
                                        break;
                                    case "showNativeView":
                                        frame.clearAnimation();
                                        //fragment.setEnable(true);
                                        frame.setVisibility(View.VISIBLE);
                                        frame.setEnabled(true);
                                        break;
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void translateFrame(float xfrom, float xto, float yfrom, float yto) {
        TranslateAnimation translateAnimation;
        final AnimationSet animationSet;
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, xfrom, Animation.RELATIVE_TO_SELF, xto, Animation.RELATIVE_TO_SELF, yfrom, Animation.RELATIVE_TO_SELF, yto);
        animationSet = new AnimationSet(true);
        translateAnimation.setFillAfter(true);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(500);
        */
/*translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frame.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*//*

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (frame.getVisibility() == View.VISIBLE) {
                    frame.setAnimation(animationSet);
                    animationSet.startNow();
                }
            }
        });

    }

    EntryProxy mEntryProxy = null;

    private void initH5(Bundle savedInstanceState) {
        if (mEntryProxy == null) {
            //FrameLayout f = new FrameLayout(this);
            // 创建5+内核运行事件监听
            WebappModeListener wm = new WebappModeListener(this, webview);
            // 初始化5+内核
            mEntryProxy = EntryProxy.init(this, wm);

            // 启动5+内核
            mEntryProxy.onCreate(this, savedInstanceState, SDK.IntegratedMode.WEBAPP, null);
            //setContentView(f);
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void init() {
        try {
            UserLodingInFo.setInstance((UserLodingInFo) ObjectSaveUtils.getObject(MainActivity.this, "USERINFO"));
            UserLodingInFo.isLoading = true;
            UserInfo.setUserInfo((UserInfo) ObjectSaveUtils.getObject(MainActivity.this, "USERICON"));
            //fragment.refresh();
            //toast(UserLodingInFo.getInstance().getId() + UserLodingInFo.getInstance().getIcon());
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.getMessage() + e.toString());
        }
        frame.setVisibility(View.GONE);
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(final List<EMMessage> list) {
                final StringBuffer s = new StringBuffer();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (EMMessage message : list) {
                            if (message.getFrom().equals("system")) {
                                try {
                                    if (message.getStringAttribute("message_type") != null)
                                        switch (message.getStringAttribute("message_type")) {
                                            case "10":
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (SDK.obatinFirstPage(SDK.obtainCurrentApp()) != null) {
                                                            SDK.obatinFirstPage(SDK.obtainCurrentApp()).evalJS("check_merchant(0);");
                                                        }
                                                    }
                                                });
                                                break;
                                            case "11":
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (SDK.obatinFirstPage(SDK.obtainCurrentApp()) != null) {
                                                            SDK.obatinFirstPage(SDK.obtainCurrentApp()).evalJS("check_merchant(1);");
                                                        }
                                                    }
                                                });
                                                break;
                                        }
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (message.getChatType() == EMMessage.ChatType.Chat && !message.getFrom().equals("system")) {
                                if (UserInfo.getInstance().getInfo().get(message.getFrom()) == null) {
                                    UserInfo.getInstance().addInfo(new UserInfo.User().setUid(message.getFrom()).setNick(UserInfo.getNick(message)).setIcon(UserInfo.getIcon(message)));
                                } else {
                                    UserInfo.getInstance().getInfo().get(message.getFrom()).setIcon(UserInfo.getIcon(message)).setNick(UserInfo.getNick(message)).setUid(message.getFrom());
                                }
                            }

                        }
                    }
                }.start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (EMMessage e : list) {
                                    fragment.refresh();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
        fragment = new EaseConversationListFragment();
        fragment.setItemLongClickListener(new EaseConversationListFragment.ItemLongClickListener() {
            @Override
            public void onLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                PopUpWindowMag.popView(MainActivity.this, new PopUpWindowMag.DeleteCallback() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void delete() {
                        fragment.deleteConversation(position);
                        fragment.refresh();
                    }
                }, view);
            }
        });
        fragment.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateGroupActivity.class));
            }
        });
        fragment.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupListActivity.class));
            }
        });

        fragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                if (conversation.conversationId().equals("system")) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    Bundle messages = new Bundle();
                    List<EMMessage> listmesg = conversation.loadMoreMsgFromDB(conversation.getLastMessage().getMsgId(), conversation.getAllMsgCount());
                    listmesg.add(conversation.getLastMessage());
                    messages.putParcelableArrayList("MESG", (ArrayList<? extends Parcelable>) listmesg);
                    intent.putExtra("MESG", messages);
                    startActivity(intent);
                    conversation.markAllMessagesAsRead();
                    fragment.refresh();
                    return;
                }
                int type = 0;
                switch (conversation.getType()) {
                    case Chat:
                        type = EaseConstant.CHATTYPE_SINGLE;
                        break;
                    case ChatRoom:
                        type = EaseConstant.CHATTYPE_CHATROOM;
                        break;
                    case GroupChat:
                        type = EaseConstant.CHATTYPE_GROUP;
                        break;

                }
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()).putExtra(EaseConstant.EXTRA_CHAT_TYPE, type));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment).commit();
        UpdataGroupsInfo.getGroupInfo();
    }

    private void loginIM(final String username) {

        if (EMClient.getInstance().isLoggedInBefore()) {
            */
/*EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login(username);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });*//*

        } else {
            login(username);
        }
    }

    private void login(String username) {
        EMClient.getInstance().login(UserLodingInFo.getInstance().getMobile(), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        fragment.refresh();
                        */
/*if (EMClient.getInstance().groupManager().getAllGroups() != null) {
                            for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
                                try {
                                    EMClient.getInstance().groupManager().destroyGroup(group.getGroupId());
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }*//*

                        fragment.refresh();
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast(s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private void sendMesg() {
    */
/*button.setVisibility(View.GONE);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EMMessage emMessage = EMMessage.createTxtSendMessage("我爱你", "zangziye");
            emMessage.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // toast("success" + "send");
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    toast(s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            EMClient.getInstance().chatManager().sendMessage(emMessage);
        }
    });*//*

    }

    private void toast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                break;
        }
    }
}

class WebappModeListener implements ICore.ICoreStatusListener, IOnCreateSplashView {
    Activity activity;
    View splashView = null;
    ViewGroup rootView;
    IApp app = null;
    ProgressDialog pd = null;

    public WebappModeListener(Activity activity, ViewGroup rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

    */
/**
     * 5+内核初始化完成时触发
     *//*

    @Override
    public void onCoreInitEnd(ICore coreHandler) {

        // 表示Webapp的路径在 file:///android_asset/apps/HelloH5
        String appBasePath = "/apps/H58167E1D";


        // 设置启动参数,可在页面中通过plus.runtime.arguments;方法获取到传入的参数
        String args = "{url:'http://www.baidu.com'}";

        // 启动启动独立应用的5+ Webapp
        app = SDK.startWebApp(activity, appBasePath, args, new IWebviewStateListener() {
            // 设置Webview事件监听，可在监监听内获取WebIvew加载内容的进度
            @Override
            public Object onCallBack(int pType, Object pArgs) {
                switch (pType) {
                    case IWebviewStateListener.ON_WEBVIEW_READY:
                        // WebApp准备加载事件
                        // 准备完毕之后添加webview到显示父View中，
                        // 设置排版不显示状态，避免显示webview时html内容排版错乱问题
                        View view = ((IWebview) pArgs).obtainApp().obtainWebAppRootView().obtainMainView();
                        view.setVisibility(View.INVISIBLE);

                        if (view.getParent() != null) {
                            ((ViewGroup) view.getParent()).removeView(view);
                        }
                        rootView.addView(view, 0);
                        break;
                    case IWebviewStateListener.ON_PAGE_STARTED:
                        // 首页面开始加载事件
                        // pd = ProgressDialog.show(activity, "加载中", "0/100");
                        break;
                    case IWebviewStateListener.ON_PROGRESS_CHANGED:
                        // WebApp首页面加载进度变化事件
                        */
/*if (pd != null) {
                            pd.setMessage(pArgs + "/100");
                        }*//*

                        break;
                    case IWebviewStateListener.ON_PAGE_FINISHED:
                        // WebApp首页面加载完成事件
                        if (pd != null) {
                            pd.dismiss();
                            pd = null;
                        }
                        // 页面加载完毕，设置显示webview
                        // 如果不需要显示spalsh页面将此行代码移动至onCloseSplash事件内
                        app.obtainWebAppRootView().obtainMainView().setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        }, this);

        app.setIAppStatusListener(new IApp.IAppStatusListener() {
            // 设置APP运行事件监听
            @Override
            public boolean onStop() {
                // 应用运行停止时调用
                rootView.removeView(app.obtainWebAppRootView().obtainMainView());
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onStart() {
                // 独立应用启动时触发事件
            }

            @Override
            public void onPause(IApp arg0, IApp arg1) {
                // WebApp暂停运行时触发事件
            }

            @Override
            public String onStoped(boolean arg0, String arg1) {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }

    @Override
    public void onCoreReady(ICore coreHandler) {
        // 初始化5+ SDK，
        // 5+SDK的其他接口需要在SDK初始化后才能調用
        SDK.initSDK(coreHandler);
        // 设置当前应用可使用的5+ API
        SDK.requestAllFeature();
    }

    @Override
    public boolean onCoreStop() {
        // 当返回false时候回关闭activity
        return false;
    }

    // 在Widget集成时如果不需要显示splash页面可按照如下步骤操作
    // 1 删除onCreateSplash方法内的代码
    // 2 将5+mainView添加到rootview时将页面设置为不可见
    // 3 在onCloseSplash方法中将5+mainView设置为可见
    // 4 修改androidmanifest.xml文件 将SDK_WebApp的主题设置为透明
    // 注意！
    // 如果不显示splash页面会造成用户点击后页面短时间内会没有变化，
    // 可能会给用户造成程序没响应的错觉，
    // 所以开发者需要对5+内核启动到5+应用页面显示之间的这段事件进行处理

    @Override
    public Object onCreateSplash(Context pContextWrapper) {
        splashView = new FrameLayout(activity);
        splashView.setBackgroundResource(RInformation.DRAWABLE_SPLASH);
        rootView.addView(splashView);
        return null;
    }

    @Override
    public void onCloseSplash() {
        rootView.removeView(splashView);
    }
}*/
