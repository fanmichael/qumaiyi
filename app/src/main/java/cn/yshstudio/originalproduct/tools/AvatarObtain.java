package cn.yshstudio.originalproduct.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class AvatarObtain {

    public interface AvatarObtainCallback {

        public void obtainAvatarSuccess(String path);
        public void obtainAvatarFailure();
    }

    private Activity mActivity;
    private AvatarObtainCallback mCallback;

    private boolean isReduction; // 图片是否裁剪

    private Uri photoUri;

    private final int TAKE_PHOTO = 1;// 拍照
    private final int CROP_PHOTO = 2;// 裁剪图片
    private final int GET_PHOTO_FROM_ALBUM = 3; // 从相册返回的图片

    public AvatarObtain(Activity activity, AvatarObtainCallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    /*
     * 通过拍照获取头像
     */
    public void getAvatarByCamera(boolean isReduction) {
        this.isReduction = isReduction;
        Intent getImageByCamera = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            ContentValues values = new ContentValues();
            photoUri = mActivity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoUri);
            mActivity.startActivityForResult(getImageByCamera, TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 从相册获取头像
     */
    public void getAvatarFromAlbum(boolean isReduction) {
        this.isReduction = isReduction;

        String action = checkSDKVersion()?Intent.ACTION_OPEN_DOCUMENT:Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(action,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
//      intent.addCategory(Intent.CATEGORY_OPENABLE);
        mActivity.startActivityForResult(intent, GET_PHOTO_FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case TAKE_PHOTO: // 处理拍照返回的图片
                handlePhotoByCamera(data);
                break;

            case CROP_PHOTO: // 处理已经裁剪过的图片
                handlePhotoByCrop(data);
                break;

            case GET_PHOTO_FROM_ALBUM: // 处理从相册返回的图片

                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData();

                if (selectedImage == null) {
                    if (data.getExtras() != null) {
                        selectedImage = (Uri) data.getExtras().getParcelable(
                                Intent.EXTRA_STREAM);
                    }
                }
                _handleImage(data, selectedImage);
                break;
        }

    }

    private void handlePhotoByCrop(Intent data) {
        if (data != null) {

            ContentResolver cr = mActivity.getContentResolver();
            if (photoUri != null) {
                Cursor cursor = cr.query(photoUri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String crop_loacal_path = cursor.getString(1);
                    cursor.close();
                    cursor = null;
                    mCallback.obtainAvatarSuccess(crop_loacal_path);
                }
            }
        } else {
            mCallback.obtainAvatarFailure();
        }
    }

    private void handlePhotoByCamera(Intent data) {
        String loacal_path = "";
        ContentResolver cr = mActivity.getContentResolver();
        if (photoUri != null) {
            Cursor cursor = cr.query(photoUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                loacal_path = cursor.getString(1);
                cursor.close();
                cursor = null;
            }
        }

        if (TextUtils.isEmpty(loacal_path)) {
            Bundle extras = data != null ? data.getExtras() : null;
            if (extras != null) {
                Uri uri_img = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                if (uri_img != null) {
                    loacal_path = getAbsolutePathByUri(mActivity, uri_img);

                }
            }
        }

        if (!"".equals(loacal_path)) {
            File imageFile = new File(loacal_path);// 通过路径创建保存文件
            if (imageFile.exists() && imageFile.length() > 0) {
                photoUri = Uri.fromFile(imageFile);// 获取文件的Uri
                _handleImage(data, photoUri);
            } else {
                imageFile.delete();
            }
        }

    }

    /*
     * 处理返回的原始图片
     */
    private String m_crop_before_src_img_path;

    private void _handleImage(Intent data, Uri uri) {
        m_crop_before_src_img_path = getAbsolutePathByUri(mActivity, uri);
        if (m_crop_before_src_img_path != null) {
            if (isReduction) {
                zoomPhoto(Uri.fromFile(new File(m_crop_before_src_img_path)), 80);
            } else {
                finish(m_crop_before_src_img_path);
            }
        } else {
            mCallback.obtainAvatarFailure();
        }

    }

    /*
     * 缩放图片
     */
    private void zoomPhoto(Uri uri, int size) {

        Intent getImage = new Intent("com.android.camera.action.CROP");
        getImage.setDataAndType(uri, "image/*");
        getImage.putExtra("crop", "true");
        getImage.putExtra("aspectX", 1);
        getImage.putExtra("aspectY", 1);
        getImage.putExtra("outputX", 800);
        getImage.putExtra("outputY", 800);
        getImage.putExtra("return-data", false);
        getImage.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ContentValues values = new ContentValues();
        photoUri = mActivity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        getImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        mActivity.startActivityForResult(getImage, CROP_PHOTO);

    }

    private void finish(String path) {
        if (TextUtils.isEmpty(path)) {
            mCallback.obtainAvatarFailure();
        } else {
            mCallback.obtainAvatarSuccess(path);
        }

    }

    private String getAbsolutePathByUri(Activity activity, Uri uri) {
        String path = "";
        if (checkSDKVersion() && DocumentsContract.isDocumentUri(activity, uri)) {
            String whiloId = DocumentsContract.getDocumentId(uri);
            String id = whiloId.split(":")[1];
            String[] proj = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, sel,
                    new String[] { id }, null);
            int columnIndex = cursor.getColumnIndex(proj[0]);
            if (cursor.moveToFirst()) {
                // DATA字段就是本地资源的全路径
                path = cursor.getString(columnIndex);
            }
            // 切记要关闭游标
            cursor.close();
            return path;
        }

        // can post image
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(uri, proj, // Which
                // columns
                // to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        try {
            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
            if (TextUtils.isEmpty(path)) {
                // 如果游标为空说明获取的已经是绝对路径了
                path = uri.getPath();
            }

        } finally {
            // if(cursor!=null)cursor.close(); // Do not call close() on a
            // cursor obtained using this method,
        }
        return path;
    }

    //检查sdk版本是否高于19
    private boolean checkSDKVersion(){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
    }

}
