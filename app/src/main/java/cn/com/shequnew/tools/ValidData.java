package cn.com.shequnew.tools;

/**
 * Created by Administrator on 2017/4/17 0017.
 */


import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import cn.com.shequnew.inc.Ini;

/**
 * 验证表单特性
 * */
public class ValidData {
    // 验证数字
    public static Boolean validDigits(String pDigits) {
        if (pDigits == null
                || !Pattern.compile(Ini._REG_DIGITS).matcher(pDigits).matches()) {
            return false;
        }
        return pDigits.length() > 0 ? true : false;
    }
    // 验证整数
    public static Boolean validInt(String pId) {
        if (!Pattern.compile(Ini._REG_INT).matcher(pId).matches()) {
            return false;
        }
        return pId.length() > 0 ? true : false;
    }
    // 验证整数(包含0)
    public static Boolean validIntNew(String pId) {
        if (!Pattern.compile(Ini._REG_INTNEW).matcher(pId).matches()) {
            return false;
        }
        return pId.length() > 0 ? true : false;
    }
    // 验证价格
    public static Boolean validPrice(String pPrice) {
        if (!Pattern.compile(Ini._REG_PRICE).matcher(pPrice).matches()) {
            return false;
        }
        return pPrice.length() > 0 ? true : false;
    }
    // 验证价格
    public static Boolean validPriceTwo(String pPrice) {
        if (!Pattern.compile(Ini._REG_PRICE_TWO).matcher(pPrice).matches()) {
            return false;
        }
        return pPrice.length() > 0 ? true : false;
    }
    // 验证数量
    public static Boolean validAmount(String pAmount) {
        if (!Pattern.compile(Ini._REG_AMOUNT).matcher(pAmount).matches()) {
            return false;
        }
        return pAmount.length() > 0 ? true : false;
    }
    // 验证手机
    public static Boolean validMobile(String pMobile) {
        if (pMobile == null
                || !Pattern.compile(Ini._REG_MOBILE).matcher(pMobile).matches()) {
            return false;
        }
        return pMobile.length() <= 0 ? false : true;
    }

    //验证密码
    public static Boolean validPaw(String pwa){
        if (pwa == null
                || !Pattern.compile(Ini._REG_PAWSS).matcher(pwa).matches()) {
            return false;
        }
        return pwa.length() <= 0 ? false : true;
    }

    public static void load(Uri uri, SimpleDraweeView draweeView, int width, int height){
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width,height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();

        } catch (IOException e) {
            return "";
        }
    }

    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

}
