package cn.com.shequnew.pages.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.AppraiesimgeAdapter;
import cn.com.shequnew.tools.ImageToools;
import cn.com.shequnew.tools.TextContent;

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
    @BindView(R.id.sell_news_details)
    TextView sellNewsDetails;
    @BindView(R.id.sell_card_gridView)
    GridView sellCardGridView;

    private Uri imageUri;
    private File file;
    private Context context;
    private int type = 1;

    /**
     * 添加数据刷新
     */
    private AppraiesimgeAdapter appraiesimgeAdapter;
    /**
     * 添加图片
     */
    private List<ContentValues> contentValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerl_details);
        ButterKnife.bind(this);
        context = this;
        initView();
        ImageToools.verifyStoragePermissions(SellerlDetailsActivity.this);
    }


    private boolean initData() {
        boolean isIt = true;
        String mesg = "";
        if (sellName.getText().toString().trim().equals("")) {
            mesg = "请输入真实姓名！";
            isIt = false;
        }
        if (sellPhone.getText().toString().trim().equals("")) {
            mesg = "请输入电话号码！";
            isIt = false;
        }
        if (sellCard.getText().toString().trim().equals("")) {
            mesg = "请输入身份证号码！";
            isIt = false;
        }
        if (sellCardNum.getText().toString().trim().equals("")) {
            mesg = "请输入收款账号！";
            isIt = false;
        }
        if (sellContext.getText().toString().trim().equals("")) {
            mesg = "请输入资质说明！";
            isIt = false;
        }
        return isIt;
    }


    @OnClick(R.id.sell_news_details)
    void deal() {
        dealView();
    }

    /**
     * 条例
     */
    private void dealView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(SellerlDetailsActivity.this, R.style.AlertDialog);
        dialog.setContentView(R.layout.deal_content);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        dialog.getWindow().setAttributes(layoutParams);
        TextView content = (TextView) dialog.findViewById(R.id.deal_content);
        CheckBox chose = (CheckBox) dialog.findViewById(R.id.deal_chose);
        TextContent textContent = new TextContent();
        content.setText(textContent.shopEquities);
        chose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialog.dismiss();
                topRegitTitle.setClickable(true);
            }
        });

    }


    private void initView() {
        topTitle.setText("申请卖主");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);
        topRegitTitle.setClickable(false);
        contentValues.add(0, null);
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context);
        sellCardGridView.setAdapter(appraiesimgeAdapter);
        sellCardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    type = 4;
                    diabackLogin();
                }
            }
        });
    }


    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    /**
     * 本人照片
     */
    @OnClick(R.id.sell_images)
    void images() {
        type = 1;
        diabackLogin();
    }

    /**
     * 身份证正面
     */
    @OnClick(R.id.sell_card_z)
    void card() {
        type = 2;
        diabackLogin();
    }

    /**
     * 身份证反面
     */
    @OnClick(R.id.sell_card_f)
    void cardFil() {
        type = 3;
        diabackLogin();
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        File output = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);

    }

    /**
     * 相机
     */
    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(SellerlDetailsActivity.this, R.style.AlertDialog);
        dialog.setContentView(R.layout.chose_images);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        dialog.getWindow().setAttributes(layoutParams);
        Button photograph = (Button) dialog.findViewById(R.id.photograph);//拍照
        Button photographAdd = (Button) dialog.findViewById(R.id.photograph_add);//相册添加
        Button photographCal = (Button) dialog.findViewById(R.id.photograph_cal);
        photographCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Camera.getNumberOfCameras() > 0) {
                    takePhoto();
                } else {
                    Toast.makeText(context, "没有可用摄像头", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        photographAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                dialog.dismiss();
            }
        });
    }

    //本地相册
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
    }


    private File uri2File(Uri uri) {
        File file = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = SellerlDetailsActivity.this.managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            Uri uri = data.getData();

            switch (type) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:
                    ContentValues cv = new ContentValues();
                    cv.put("image", uri.toString());
                    contentValues.add(cv);
                    appraiesimgeAdapter.notifyDataSetChanged();
                    break;
            }
//            file = uri2File(uri);
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
                    String path = imageUri.getPath();

                    switch (type) {
                        case 1:

                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:
                            ContentValues cv = new ContentValues();
                            cv.put("image", imageUri.toString());
                            contentValues.add(cv);
                            appraiesimgeAdapter.notifyDataSetChanged();
                            break;
                    }
//                    file = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }
        }
    }
}
