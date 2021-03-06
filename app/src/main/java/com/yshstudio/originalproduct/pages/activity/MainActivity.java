package com.yshstudio.originalproduct.pages.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfo;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.activity.AdminActivity;
import com.yshstudio.originalproduct.chat.activity.ChatActivity;
import com.yshstudio.originalproduct.chat.activity.CreateGroupActivity;
import com.yshstudio.originalproduct.chat.activity.GroupListActivity;
import com.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import com.yshstudio.originalproduct.chat.util.UpdataGroupsInfo;
import com.yshstudio.originalproduct.chat.view.PopUpWindowMag;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.fragment.DynamicFragment;
import com.yshstudio.originalproduct.pages.fragment.NewsFragment;
import com.yshstudio.originalproduct.pages.fragment.PageCommFragment;
import com.yshstudio.originalproduct.tools.AppManager;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import com.yshstudio.originalproduct.tools.Util;
import com.yshstudio.originalproduct.tools.XPermissionUtils;

/**
 * 主页
 */
public class MainActivity extends FragmentActivity {

    @BindView(R.id.radio_pages)
    RadioGroup radioPages;
    @BindView(R.id.pages)
    RadioButton pages;
    @BindView(R.id.dynamic)
    RadioButton dynamic;
    @BindView(R.id.publish)
    RadioButton publish;
    @BindView(R.id.news)
    RadioButton news;
    @BindView(R.id.mine)
    RadioButton mine;
    private Context context;
    private PageCommFragment pagesFragment;
    private DynamicFragment dynamicFragment;
    //聊天界面
    private EaseConversationListFragment publishFragment;
    private NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setDefaultFragment();
        setFragmentChange();
        initChatSet();
        if( AppContext.cv==null){
            AppContext.cv=new ContentValues();
        }
        AppContext.cv.put("id", Integer.valueOf(SharedPreferenceUtil.read("id","")));//标记
        //异地登录，强制下线
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
                                    //执行强制退出
                                    if (SharedPreferenceUtil.hasKey("mobile") && SharedPreferenceUtil.hasKey("password")) {
                                        SharedPreferenceUtil.clear();
                                        SharedPreferenceUtil.insert("is",true);
                                        AppContext.cv.clear();
                                    }
                                    if (SharedPreferenceUtil.hasKey("id")) {
                                        SharedPreferenceUtil.clear();
                                        SharedPreferenceUtil.insert("is", true);
                                        AppContext.cv.clear();
                                    }
                                    if (EMClient.getInstance().isLoggedInBefore()) {
                                        EMClient.getInstance().logout(true);
                                        AppContext.getInstance().logoutApp();
                                        finish();
                                        Intent intent = new Intent(MainActivity.this, FristAdvActivity.class);
                                        startActivity(intent);
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


    private void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        pagesFragment = new PageCommFragment();
        transaction.replace(R.id.fra_layout, pagesFragment);
        transaction.commit();
    }

    private void setFragmentChange() {
        radioPages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.pages:
                        if (pagesFragment == null) {
                            pagesFragment = new PageCommFragment();
                        }
                        transaction.replace(R.id.fra_layout, pagesFragment);
                        break;
                    case R.id.dynamic:
                        if (dynamicFragment == null) {
                            dynamicFragment = new DynamicFragment();
                        }
                        transaction.replace(R.id.fra_layout, dynamicFragment);
                        break;
                    case R.id.news:
                        if (publishFragment == null) {
                            publishFragment = new EaseConversationListFragment();
                            initChatFragment();
                        }
                        transaction.replace(R.id.fra_layout, publishFragment);
                        break;
                    case R.id.mine:
                        if (newsFragment == null) {
                            newsFragment = new NewsFragment();
                        }
                        transaction.replace(R.id.fra_layout, newsFragment);
                        break;
                    case R.id.publish:
                        //发布消息
                        Intent intent = new Intent(context, PublishActivity.class);
                        context.startActivity(intent);
                        publish.setChecked(false);
                        break;

                }
                transaction.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.getAppManager().addActivity(this);
    }

    private void initChatFragment() {
        // fragment = new EaseConversationListFragment();

        publishFragment.setItemLongClickListener(new EaseConversationListFragment.ItemLongClickListener() {
            @Override
            public void onLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                PopUpWindowMag.popView(MainActivity.this, new PopUpWindowMag.DeleteCallback() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void delete() {
                        publishFragment.deleteConversation(position);
                        publishFragment.refresh();
                    }
                }, view);
            }
        });
        publishFragment.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateGroupActivity.class));
            }
        });
        publishFragment.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupListActivity.class));
            }
        });

        publishFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
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
                    publishFragment.refresh();
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
        UpdataGroupsInfo.getGroupInfo();
    }

    private void initChatSet() {
        try {
            UserLodingInFo.setInstance((UserLodingInFo) ObjectSaveUtils.getObject(MainActivity.this, "USERINFO"));
            UserLodingInFo.isLoading = true;
            UserInfo.setUserInfo((UserInfo) ObjectSaveUtils.getObject(MainActivity.this, "USERICON"));
            //fragment.refresh();
            //toast(UserLodingInFo.getInstance().getId() + UserLodingInFo.getInstance().getIcon());
        } catch (Exception e) {
            e.printStackTrace();

        }
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
                                                        //改变用户申请状态
                                                        SharedPreferenceUtil.insert("merchant", "1");
//                                                    if (SDK.obatinFirstPage(SDK.obtainCurrentApp()) != null) {
//                                                        SDK.obatinFirstPage(SDK.obtainCurrentApp()).evalJS("check_merchant(0);");
//                                                    }
                                                    }
                                                });
                                                break;
                                            case "11":
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //改变用户的申请状态
                                                        SharedPreferenceUtil.insert("merchant", "0");
                                                    }
                                                });
                                                break;
                                        }
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                            /*message.getChatType() == EMMessage.ChatType.Chat &&*/
                            if (!message.getFrom().equals("system")) {
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
                                    if (publishFragment == null) {
                                        return;
                                    }
                                    publishFragment.refresh();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
