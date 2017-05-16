package cn.com.shequnew.pages.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.tools.ImageUtils;
import cn.com.shequnew.tools.ValidData;

/**
 * 申请买主
 */
public class SellerlDetailsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.sell_name)
    EditText sellName;
    @BindView(R.id.sell_phone)
    EditText sellPhone;
    @BindView(R.id.sell_card)
    EditText sellCard;
    @BindView(R.id.sell_card_num)
    EditText sellCardNum;
    @BindView(R.id.sell_images)
    SimpleDraweeView sellImages;
    @BindView(R.id.sell_card_z)
    SimpleDraweeView sellCardZ;
    @BindView(R.id.sell_card_f)
    SimpleDraweeView sellCardF;
    @BindView(R.id.sell_context)
    EditText sellContext;
    @BindView(R.id.sell_card_news)
    SimpleDraweeView sellCardNews;
    @BindView(R.id.sell_news_details)
    TextView sellNewsDetails;

    private Context context;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/Execution/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private final static int CROP = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerl_details);
        ButterKnife.bind(this);
        context = this;
        initView();

    }

    private void initView() {
        topTitle.setText("申请卖主");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);
        topRegitTitle.setClickable(false);

    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.sell_images)
    void choseImages() {
        CharSequence[] items = {"相册选择", "拍照"};
        imageChooseItem(items);
    }

    @OnClick(R.id.sell_card_z)
    void imagez() {

    }


    public static void saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Uri uri = data.getData();
            Toast.makeText(context, "err****"+uri.toString(), Toast.LENGTH_LONG).show();
            sellImages.setImageURI(uri);

//            protraitBitmap = ImageUtils.loadImgThumbnail(uri.toString(), 200, 200);
//            sellImages.setImageBitmap(protraitBitmap);
        } else if (requestCode == 2 ) {
            Uri uri = data.getData();
            Toast.makeText(context, "err****"+uri.toString(), Toast.LENGTH_LONG).show();
            sellImages.setImageURI(uri);

//            protraitBitmap = ImageUtils.loadImgThumbnail(uri.toString(), 200, 200);
//            sellImages.setImageBitmap(protraitBitmap);

            if(uri == null){
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap
                    //spath :生成图片取个名字和路径包含类型
                    String timeStamp = new SimpleDateFormat(
                            "yyyyMMddHHmmss").format(new Date());
                    // 照片命名
                    String origFileName =  timeStamp + ".jpg";
                    saveImage(photo,origFileName);
                } else {
                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                //to do find the path of pic by uri
            }
        }






//        if (resultCode != Activity.RESULT_OK)
//            return;
//        switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
//            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
//                startActionCrop(origUri, cropUri);// 拍照后裁剪
//                break;
//            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
//            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
//                uploadNewPhoto();// 上传新照片
//                break;
//            default:
//                break;
//        }
    }


    private void uploadNewPhoto() {
        // 获取头像缩略图
        if (!ValidData.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
            sellImages.setImageBitmap(protraitBitmap);
        }
        if (protraitBitmap != null) {
            //  uploadHeaderImg(protraitFile);
        }
    }


    public void imageChooseItem(CharSequence[] items) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        String storageState = Environment
                                .getExternalStorageState();
                        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                            File savedir = new File(FILE_SAVEPATH);
                            if (!savedir.exists()) {
                                savedir.mkdirs();
                            }
                        } else {
                            // UIHelper.ToastMessage(UserInfo.this,
                            // "无法保存上传的头像，请检查SD卡是否挂载");
                            return;
                        }

                        // 输出裁剪的临时文件
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMddHHmmss").format(new Date());
                        // 照片命名
                        String origFileName = "hcc_" + timeStamp + ".jpg";
                        String cropFileName = "hcc_crop_" + timeStamp + ".jpg";

                        // 裁剪头像的绝对路径
                        protraitPath = FILE_SAVEPATH + cropFileName;
                        protraitFile = new File(protraitPath);

                        origUri = Uri.fromFile(new File(FILE_SAVEPATH,
                                origFileName));
                        cropUri = Uri.fromFile(protraitFile);

                        // 相册选图
                        if (item == 0) {
//                            startActionPickCrop(cropUri);

                            getImageFromAlbum();
                        }
                        // 手机拍照
                        else if (item == 1) {
//                            startActionCamera(origUri);
                            getImageFromCamera();
                        }


                    }
                }).create();
        alertDialog.show();

    }

    private void startActionCrop(Uri data, Uri output) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", output);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    /**
     * 选择图片裁剪
     *
     * @param output
     */
    private void startActionPickCrop(Uri output) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("output", output);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        startActivityForResult(Intent.createChooser(intent, "选择图片"),
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    /**
     * 相机拍照
     *
     * @param output
     */
    private void startActionCamera(Uri output) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }





    //本地相册
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
    }

    //相机
    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, 2);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }





}
