package com.yshstudio.originalproduct.tools;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public final class Constants{
    public static final String APP_ID = "wxd96b8dd3e5733967";
    public static final String APP_KEY="de1d138b686684274a39964f931b7eea";
    public static final String APPQQ_ID="1105155596";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }


    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions IM_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.ic_empty)
//            .showImageForEmptyUri(R.drawable.ic_error)
//            .showImageOnFail(R.drawable.ic_error)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//			.displayer(new RoundedBitmapDisplayer(5)) // default
            // 可以设置动画，比如圆角或者渐�?
            .cacheInMemory(true).cacheOnDisc(true)// 是否缓存在SD卡
            .build();

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

}
