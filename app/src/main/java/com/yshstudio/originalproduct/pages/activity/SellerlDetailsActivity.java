package com.yshstudio.originalproduct.pages.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.adapter.AppraiesimgeAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.pages.prompt.Loading;
import com.yshstudio.originalproduct.pages.view.MyGridView;
import com.yshstudio.originalproduct.tools.GetPathVideo;
import com.yshstudio.originalproduct.tools.ImageToools;
import com.yshstudio.originalproduct.tools.TextContent;
import com.yshstudio.originalproduct.tools.ValidData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    MyGridView sellCardGridView;
    @BindView(R.id.scor_seller)
    ScrollView scorSeller;
    @BindView(R.id.lin_seller_hide)
    LinearLayout linSellerHide;

    private File sellImagesFile;
    private File sellCardZFile;
    private File sellCardFFile;
    private List<File> files = new ArrayList<>();

    private Uri imageUri;
    private File file;
    private Context context;
    private int type = 1;
    private boolean choseBtn = false;
    private int error;
    private int isSeller;


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
        new asyncTask().execute(2);
        initView();
        ImageToools.verifyStoragePermissions(SellerlDetailsActivity.this);
    }


    @OnClick(R.id.top_regit_title)
    void submit() {
        /**提交资料*/
        if (choseBtn) {
            initData();
        } else {
            Toast.makeText(context, "请阅读、勾选条例及隐私协议", Toast.LENGTH_LONG).show();
            return;
        }
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
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (dm.widthPixels * 0.8);
        layoutParams.height = (int) (dm.heightPixels * 0.9);
        dialog.getWindow().setAttributes(layoutParams);
        TextView content = (TextView) dialog.findViewById(R.id.deal_content);
        final CheckBox chose = (CheckBox) dialog.findViewById(R.id.deal_chose);
        chose.setChecked(choseBtn);
        TextContent textContent = new TextContent();
        content.setText(textContent.shopEquities);
        chose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                choseBtn = chose.isChecked();
                dialog.dismiss();
                if (choseBtn) {
                    topRegitTitle.setTextColor(getResources().getColor(R.color.bd_top));
                } else {
                    topRegitTitle.setTextColor(getResources().getColor(R.color.bd_top_chose));
                }
            }
        });

    }


    private void initView() {
        topTitle.setText("申请卖主");
        topRegitTitle.setText("提交");
        topRegitTitle.setVisibility(View.VISIBLE);
        topRegitTitle.setTextColor(getResources().getColor(R.color.bd_top_chose));
        contentValues.add(0, null);
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context, 2, true);
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
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                switch (type) {
                    case 1:
                        ValidData.load(uri, sellImages, 70, 70);
//                        sellImagesFile = uri2File(uri);
                        sellImagesFile=new File(GetPathVideo.getPath(context, uri));
                        break;
                    case 2:
                        ValidData.load(uri, sellCardZ, 70, 70);
//                        sellCardZFile = uri2File(uri);
                        sellCardZFile=new File(GetPathVideo.getPath(context, uri));
                        break;
                    case 3:
                        ValidData.load(uri, sellCardF, 70, 70);
//                        sellCardFFile = uri2File(uri);
                        sellCardFFile=new File(GetPathVideo.getPath(context, uri));
                        break;
                    case 4:
//                        file = uri2File(uri);
                        file=new File(GetPathVideo.getPath(context, uri));
                        files.add(file);
                        ContentValues cv = new ContentValues();
                        cv.put("image", uri.toString());
                        contentValues.add(cv);
                        appraiesimgeAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
//                    String path = imageUri.getPath();
                    switch (type) {
                        case 1:
//                            sellImagesFile = new File(path);
                            sellImagesFile=new File(GetPathVideo.getPath(context, imageUri));
                            ValidData.load(imageUri, sellImages, 70, 70);
                            break;
                        case 2:
//                            sellCardZFile = new File(path);
                            sellCardZFile=new File(GetPathVideo.getPath(context, imageUri));
                            ValidData.load(imageUri, sellCardZ, 70, 70);
                            break;
                        case 3:
//                            sellCardFFile = new File(path);
                            sellCardFFile=new File(GetPathVideo.getPath(context, imageUri));
                            ValidData.load(imageUri, sellCardF, 70, 70);
                            break;
                        case 4:
//                            file = new File(path);
                            file=new File(GetPathVideo.getPath(context, imageUri));
                            files.add(file);
                            ContentValues cv = new ContentValues();
                            cv.put("image", imageUri.toString());
                            contentValues.add(cv);
                            appraiesimgeAdapter.notifyDataSetChanged();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }
        }
    }

    /**
     * 异步请求
     */
    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpApply();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpSellIs();
                    bundle.putInt("what", 2);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                    if (error == 115) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    }
                    if (error == 108) {
                        Toast.makeText(context, "正在审核中", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    }
                    break;
                case 2:
                    if(isSeller==1){
                        return;
                    }else if(isSeller==0){
                        Toast.makeText(context, "申请已提交，请等待。。。", Toast.LENGTH_LONG).show();
                        scorSeller.setVisibility(View.GONE);
                        linSellerHide.setVisibility(View.VISIBLE);
                        topRegitTitle.setVisibility(View.GONE);
                    }else if(isSeller==2){
                        Toast.makeText(context, "您还不是卖主，快去申请吧", Toast.LENGTH_LONG).show();
                        scorSeller.setVisibility(View.VISIBLE);
                        linSellerHide.setVisibility(View.GONE);
                        topRegitTitle.setVisibility(View.VISIBLE);
                    }
                    break;
            }

        }
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
        if (sellImagesFile == null) {
            mesg = "请上传本人照片！";
            isIt = false;
        }
        if (sellCardZFile == null) {
            mesg = "请上传身份证正面！";
            isIt = false;
        }
        if (sellCardFFile == null) {
            mesg = "请上传身份证反面！";
            isIt = false;
        }
        if (file == null) {
            mesg = "请上传资质照片！";
            isIt = false;
        }

        if (isIt) {
            mLoading = new Loading(context, topRegitTitle);
            mLoading.setText("正在提交......");
            mLoading.show();
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, mesg, Toast.LENGTH_SHORT).show();
        }

        return isIt;
    }


    /**
     * 验证卖主
     * */
    private void httpSellIs(){
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "Merchant.checkMerchantStatus");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        String json = HttpConnectTool.post(map);
            if(!json.equals("")){
                xmlSellIS(json);
            }
    }



    private void xmlSellIS(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsData=new JSONObject(jsonObject.getString("data"));
            isSeller=jsData.getInt("auditing");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }


    /**
     * 申请卖主
     */
    private void httpApply() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Merchant.apply");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("mobile", sellPhone.getText().toString().trim());
            map.put("card", sellCard.getText().toString().trim());
            map.put("number", sellCardNum.getText().toString().trim());
            map.put("summary", sellContext.getText().toString().trim());
            map.put("name", sellName.getText().toString().trim());
            Map<String, File> file = new HashMap<String, File>();
            file.put("icon", sellImagesFile);
            for (int i = 0; i < files.size(); i++) {
                file.put("mer[" + i + "]", files.get(i));
            }
            List<File> fileList = new ArrayList<>();
            fileList.add(sellCardZFile);
            fileList.add(sellCardFFile);
            for (int j = 0; j < fileList.size(); j++) {
                file.put("photo[" + j + "]", fileList.get(j));
            }
            String json = HttpConnectTool.post(map, file);
            if (!json.equals("")) {
                xmlApply(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlApply(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            error = jsonObject.getInt("error");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}
