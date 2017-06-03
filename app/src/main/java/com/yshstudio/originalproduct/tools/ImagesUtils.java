package com.yshstudio.originalproduct.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class ImagesUtils {
    public static final int GET_IMAGE_BY_CAMERA = 5001;
    public static final int GET_IMAGE_FROM_PHONE = 5002;
    public static final int CROP_IMAGE = 5003;
    public static Uri imageUriFromCamera;
    public static Uri cropImageUri;
    // 是否为裁剪微信二维码
    public static boolean isWeChact = false;

    public static void openCameraImage(final Activity activity) {
        ImagesUtils.imageUriFromCamera = ImagesUtils.createImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImagesUtils.imageUriFromCamera);
        activity.startActivityForResult(intent, ImagesUtils.GET_IMAGE_BY_CAMERA);
    }

    /**
     * public static void openLocalImage(final Activity activity) {
     * Intent intent = new Intent();
     * intent.setType("image/*");
     * intent.setAction(Intent.ACTION_GET_CONTENT);
     * activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
     * }
     */

    public static void cropImage(Activity activity, Uri srcUri) {
        ImagesUtils.cropImageUri = ImagesUtils.createImagePathUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcUri, "image/*");
        intent.putExtra("crop", "true");

        // //////////////////////////////////////////////////////////////
        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        // //////////////////////////////////////////////////////////////
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        // //////////////////////////////////////////////////////////////
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        // //////////////////////////////////////////////////////////////
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        // 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        // 不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
        // //////////////////////////////////////////////////////////////

        // aspectX aspectY 是裁剪框宽高的比例
        if (isWeChact) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // intent.putExtra("outputX", 400);
            // intent.putExtra("outputY", 400);
        } else {
            intent.putExtra("aspectX", 822);
            intent.putExtra("aspectY", 685);
            // outputX outputY 是裁剪后生成图片的宽高
            // intent.putExtra("outputX", 822);
            // intent.putExtra("outputY", 685);
        }

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImagesUtils.cropImageUri);
        intent.putExtra("return-data", false);

        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    public static void cropImage(Activity activity, String filePath) {
        ImagesUtils.cropImageUri = ImagesUtils.createImagePathUri(activity);
        Uri srcUri = null;
        Intent intent = new Intent("com.android.camera.action.CROP");
        String path = filePath;
        if (path != null) {
            path = Uri.decode(path);
            ContentResolver cr = activity.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                    .append("'" + path + "'").append(")");
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns._ID}, buff.toString(),
                    null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                // set _id value
                index = cur.getInt(index);
            }
            if (index == 0) {
                // do nothing
            } else {
                Uri uri_temp = Uri
                        .parse("content://media/external/images/media/" + index);
                if (uri_temp != null) {
                    srcUri = uri_temp;
                }
            }
            cur.close();
        }

        intent.setDataAndType(srcUri, "image/*");
        intent.putExtra("crop", "true");

        // //////////////////////////////////////////////////////////////
        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        // //////////////////////////////////////////////////////////////
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        // //////////////////////////////////////////////////////////////
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        // //////////////////////////////////////////////////////////////
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        // 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        // 不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
        // //////////////////////////////////////////////////////////////

        // aspectX aspectY 是裁剪框宽高的比例
        if (isWeChact) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // intent.putExtra("outputX", 400);
            // intent.putExtra("outputY", 400);
        } else {
            intent.putExtra("aspectX", 822);
            intent.putExtra("aspectY", 685);
            // outputX outputY 是裁剪后生成图片的宽高
            // intent.putExtra("outputX", 822);
            // intent.putExtra("outputY", 685);
        }

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImagesUtils.cropImageUri);
        intent.putExtra("return-data", false);

        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    private static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
//        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // 专入目标文件
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            File tempFile = new File(Environment.getExternalStorageDirectory()
                    + "/" + "picture" + "/" + imageName + ".jpg"); // 以时间秒为文件名
            File temp = new File(Environment.getExternalStorageDirectory()
                    + "/" + "tempFile");// 自已项目 文件夹
            if (!temp.exists()) {
                temp.mkdir();
            }
            imageFilePath = Uri.fromFile(temp);
        } else {
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        LogUtils.logI("", "生成的照片输出路径：" + imageFilePath.toString());
        return imageFilePath;
    }

    /**
     * 判断该文件是否是一个图片。
     */
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

}
