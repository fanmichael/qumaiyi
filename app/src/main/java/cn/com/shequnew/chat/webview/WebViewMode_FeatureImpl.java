/*
package cn.com.shequnew.chat.webview;

import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfo;
import com.hyphenate.easeui.model.UserLodingInFo;


import cn.com.shequnew.chat.RXbus.RxBus;
import cn.com.shequnew.chat.activity.ChatActivity;
import cn.com.shequnew.chat.eventtype.JSEvent;
import cn.com.shequnew.chat.eventtype.LoginIM;
import cn.com.shequnew.chat.eventtype.PushWebView;




*/
/**
 * 通过代码注册5+扩展插件示例类
 **//*

public class WebViewMode_FeatureImpl implements IFeature {

    @Override
    public String execute(final IWebview pWebViewImpl, String pActionName,
                          String[] pJsArgs) {
        switch (pActionName) {
            case "sendMessage":
                if (pJsArgs[0].trim().isEmpty()) {
                    return null;
                }
                Intent intent = new Intent(pWebViewImpl.getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, pJsArgs[0]).putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE).putExtra("NICK", pJsArgs[2]);
                if (UserInfo.getInstance().getInfo() == null || UserInfo.getInstance().getInfo().get(pJsArgs[0]) == null) {
                    UserInfo.getInstance().addInfo(new UserInfo.User().setUid(pJsArgs[0]).setNick(pJsArgs[2]).setIcon(pJsArgs[1]));
                } else {
                    UserInfo.getInstance().getInfo().get(pJsArgs[0]).setUid(pJsArgs[0]).setNick(pJsArgs[2]).setIcon(pJsArgs[1]);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ObjectSaveUtils.saveObject(pWebViewImpl.getActivity(), "USERICON", UserInfo.getInstance());
                    }
                }.start();
                pWebViewImpl.getActivity().startActivity(intent);

                break;
            case "exit":
                RxBus.getDefault().post(new JSEvent().setEventType("hideNativeView"));
                if (EMClient.getInstance().isLoggedInBefore())
                    EMClient.getInstance().logout(true);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ObjectSaveUtils.saveObject(pWebViewImpl.getContext(), "USERICON", UserInfo.getInstance());
                    }
                }.start();
                break;
            case "login":
                UserLodingInFo.getInstance().setIcon(pJsArgs[3]).setId(pJsArgs[0]).setNick(pJsArgs[2]).setMobile(pJsArgs[1]);
                ObjectSaveUtils.saveObject(pWebViewImpl.getContext(), "USERINFO", UserLodingInFo.getInstance());
                RxBus.getDefault().post(new LoginIM().setIcon(pJsArgs[3]).setId(pJsArgs[0]).setNick(pJsArgs[2]).setMobile(pJsArgs[1]));
                break;
            case "hideNativeView":
                RxBus.getDefault().post(new JSEvent().setEventType("hideNativeView"));
                break;
            case "showNativeView":
                RxBus.getDefault().post(new JSEvent().setEventType("showNativeView"));
                break;
            case "pushWebView":
                //RxBus.getDefault().post(new JSEvent().setEventType("hideNativeView"));

                RxBus.getDefault().post(new PushWebView().setTYPE(PushWebView.PUSH));
                break;
            case "backNews":
                //RxBus.getDefault().post(new JSEvent().setEventType("showNativeView"));

                RxBus.getDefault().post(new PushWebView().setTYPE(PushWebView.BACK));
                break;
            case "updateHeader":
                UserLodingInFo.getInstance().setIcon(pJsArgs[0]);
                ObjectSaveUtils.saveObject(pWebViewImpl.getContext(), "USERINFO", UserLodingInFo.getInstance());
                break;
            case "updateNick":
                UserLodingInFo.getInstance().setNick(pJsArgs[0]);
                ObjectSaveUtils.saveObject(pWebViewImpl.getContext(), "USERINFO", UserLodingInFo.getInstance());
                break;
            case "selectVideo":
                RxBus.getDefault().post(new JSEvent().setEventType("VIDEO"));
                break;
        }

        return null;
    }

    @Override
    public void init(AbsMgr pFeatureMgr, String pFeatureName) {
        //初始化Feature
    }

    @Override
    public void dispose(String pAppid) {

    }

}
*/
