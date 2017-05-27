package cn.com.shequnew.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.Map;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.MainActivity;
import cn.com.shequnew.pages.config.AppContext;

import static android.R.attr.thumb;


/**
 * 版 权：方直科技
 * 作 者：陈景坤
 * 创 建 日 期：2017/5/18
 * 描 述：
 */


public class UtilsUmeng {

    /**  第三方登录
     * @param activity  登录的activity
     * @param context  上下文
     * @param whereTag   SHARE_MEDIA是分享的平台标志，如微信是SHARE_MEDIA.WEIXIN
     */
    public  static  void  Login(final Activity activity , final Context context, SHARE_MEDIA whereTag){
        //授权回调

         UMAuthListener umAuthListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //授权开始的回调
            }
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                Log.e("#################","suncee");
                AppContext.cv.put("nick", data.get("name"));//昵称
                if (platform.equals(SHARE_MEDIA.QQ)||platform.equals(SHARE_MEDIA.SINA)){
                    AppContext.cv.put("id", data.get("uid"));//标记id
                }else if (platform.equals(SHARE_MEDIA.WEIXIN)){
                    AppContext.cv.put("id", data.get("id"));//标记id
                }
                if ("女".equals(data.get("gender"))){
                    AppContext.cv.put("gender", 0);//性别
                }else {
                    AppContext.cv.put("gender", 1);//性别
                }
                AppContext.cv.put("icon", data.get("iconurl"));//头像
                if (platform.equals(SHARE_MEDIA.SINA)){
                    AppContext.cv.put("location", data.get("location"));//地址
                }else {
                    AppContext.cv.put("location", data.get("province")+data.get("city"));//地址
                }
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText( context, "Authorize fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
//                Toast.makeText( context, "Authorize cancel", Toast.LENGTH_SHORT).show();
            }
        };
        UMShareAPI.get(activity).getPlatformInfo(activity,whereTag, umAuthListener);
    }

    /**  第三方分享
     * @param activity  分享的activity
     * @param url 分享的链接地址
     * @param content  上下文对象
     */
    public  static  void share(final Activity activity, final String url, final String content){
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(activity,mPermissionList,123);
        }

        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_ROUNDED_SQUARE);
        config.setCancelButtonVisibility(true);
         final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat","platform"+platform);
                Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(activity,platform + "分享失败", Toast.LENGTH_SHORT).show();
                if(t!=null){
                    Log.d("throw","throw:"+t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(activity,platform + " 分享取消", Toast.LENGTH_SHORT).show();
            }
        };
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if ( !UMShareAPI.get(activity).isInstall(activity, share_media)){
                            Toast.makeText(activity,"请先安装客户端",Toast.LENGTH_LONG).show();
                            return;
                        }
                        UMWeb  web = new UMWeb(url);
                        web.setTitle("去卖艺");//标题
                        web.setThumb(new UMImage(activity,R.drawable.logo));  //缩略图
                        web.setDescription(content);//描述
        new ShareAction(activity).withMedia(web).withText(url).setPlatform(share_media).setCallback(umShareListener).share();
                    }
                }).open(config);
//

    }

}
