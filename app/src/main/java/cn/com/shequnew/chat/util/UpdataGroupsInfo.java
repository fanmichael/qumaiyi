package cn.com.shequnew.chat.util;

import com.hyphenate.easeui.model.GroupImageBean;
import com.hyphenate.easeui.model.GroupsInfo;
import com.hyphenate.easeui.model.UserLodingInFo;

import cn.com.shequnew.chat.RXbus.RxBus;
import cn.com.shequnew.chat.bean.RequestGroupImage;
import cn.com.shequnew.chat.net.ComUnityRequest;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Created by kongqing on 17-3-10.
 */

public class UpdataGroupsInfo {
    public static void getGroupInfo() {
        RequestGroupImage requestGroupImage = new RequestGroupImage();
        requestGroupImage.setUid(UserLodingInFo.getInstance().getId());
        ComUnityRequest.getAPI().downloadImage(requestGroupImage).subscribeOn(Schedulers.io()).subscribe(new Observer<GroupImageBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(final Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onNext(final GroupImageBean groupImageBean) {
                if (groupImageBean.getData() == null) {
                    return;
                }
                GroupsInfo.getGroupImageBean().setData(groupImageBean.getData());
                RxBus.getDefault().post(groupImageBean);
            }
        });
    }
}
