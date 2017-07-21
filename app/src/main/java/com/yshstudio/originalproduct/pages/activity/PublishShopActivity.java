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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;
import com.yshstudio.originalproduct.tools.TextContent;
import com.yshstudio.originalproduct.tools.ValidData;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 发布商品
 */
public class PublishShopActivity extends BaseActivity implements AppraiesimgeAdapter.deleteFile{

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.publish_shop_img)
    SimpleDraweeView publishShop;
    @BindView(R.id.publish_shop_add)
    TextView publishShopAdd;
    @BindView(R.id.publish_shop_name)
    EditText publishShopName;
    @BindView(R.id.publish_shop_tags_name)
    TextView publishShopTagsName;
    @BindView(R.id.publish_shop_tags)
    LinearLayout publishShopTags;
    @BindView(R.id.publish_shop_num)
    TextView publishShopNum;
    @BindView(R.id.publish_shop_content)
    EditText publishShopContent;
    @BindView(R.id.publish_shop_price)
    EditText publishShopPrice;
    @BindView(R.id.publish_shop_exp_price)
    EditText publishShopExpPrice;
    @BindView(R.id.publish_shop_time)
    EditText publishShopTime;
    @BindView(R.id.publish_shop_tile)
    EditText publishShopTile;
    @BindView(R.id.publish_shop_image)
    MyGridView publishShopImage;
    @BindView(R.id.publish_shop_type)
    TextView publishShopType;
    @BindView(R.id.publish_shop_chose)
    LinearLayout publishShopChose;
    @BindView(R.id.publish_shop_xi)
    TextView publishShopXi;
    @BindView(R.id.publish_shop_sumit)
    Button publishShopSumit;
    @BindView(R.id.publish_shop_number)
    EditText publishShopNumber;
    private String tagsId = "";
    private Context context;
    /**
     * 添加数据刷新
     */
    private AppraiesimgeAdapter appraiesimgeAdapter;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private ArrayList<String> mSelectPath=new ArrayList<>();
    /**
     * 添加图片
     */
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private Uri imageUri;
    private int type = 1;
    private File sellImagesFile;
    private File file;
    private boolean choseBtn = false;
    private int error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_shop);
        ButterKnife.bind(this);
        context = this;
        topTitle.setText("发布商品");
        ImageToools.verifyStoragePermissions(PublishShopActivity.this);
        contentValues.add(0, null);
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context, 2, true,this);
        publishShopImage.setAdapter(appraiesimgeAdapter);
        publishShopImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    type = 2;
                    diabackLogin();
                }
            }
        });

        publishShopContent.addTextChangedListener(mTextWatcher);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = publishShopContent.getSelectionStart();
            editEnd = publishShopContent.getSelectionEnd();
            publishShopNum.setText(temp.length() + "/50");
            if (temp.length() > 50) {
                Toast.makeText(context, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                publishShopContent.setText(s);
                publishShopContent.setSelection(tempSelection);
            }
        }
    };

    /**
     * 判断非空
     */
    private void initData() {
        boolean isit = true;
        String msg = "";
        if (publishShopName.getText().toString().trim().equals("")) {
            msg = "商品名字不能为空！";
            isit = false;
        }
        if (publishShopContent.getText().toString().trim().equals("")) {
            msg = "商品简介不能为空！";
            isit = false;
        }
        if (publishShopPrice.getText().toString().trim().equals("")) {
            msg = "商品价格不能为空！";
            isit = false;
        }
        if (Double.valueOf(publishShopPrice.getText().toString().trim().equals("") ? "0":publishShopPrice.getText().toString().trim())<0.01) {
            msg = "商品价格要大于1分钱！";
            isit = false;
        }
        if (tagsId.equals("")) {
            msg = "请选择所属分类！";
            isit = false;
        }
        if (publishShopExpPrice.getText().toString().trim().equals("")) {
            msg = "商品运费不能为空！";
            isit = false;
        }
        if (Double.valueOf(publishShopExpPrice.getText().toString().trim().equals("") ? "0":publishShopExpPrice.getText().toString().trim())<0.01) {
            msg = "商品运费要大于1分钱！";
            isit = false;
        }
        if(publishShopNumber.getText().toString().trim().equals("")){
            msg = "商品库存不能为空！";
            isit = false;
        }
        if(Integer.valueOf(publishShopNumber.getText().toString().trim().equals("") ? "0":publishShopNumber.getText().toString().trim())<=0){
            msg = "商品库存必须大于零！";
            isit = false;
        }
        if (publishShopTime.getText().toString().trim().equals("")) {
            msg = "商品工期不能为空！";
            isit = false;
        }
        if (!ValidData.validPrice(publishShopTime.getText().toString().trim())) {
            msg = "商品工期不能小于零！";
            isit = false;
        }
        if (publishShopTile.getText().toString().trim().equals("")) {
            msg = "商品参数不能为空！";
            isit = false;
        }
        if (sellImagesFile == null) {
            msg = "请添加商品封面！";
            isit = false;
        }
        if (files.size() <= 0) {
            msg = "请添加商品展示图片！";
            isit = false;
        }

        if (isit) {
            mLoading = new Loading(context, publishShopSumit);
            mLoading.setText("正在提交......");
            mLoading.show();
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return;
        }

    }


    @OnClick(R.id.image_back)
    void back() {
        diaback();
    }


    private void diaback() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(PublishShopActivity.this, R.style.AlertDialog);
        dialog.setContentView(R.layout.back_login);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (dm.widthPixels * 0.8);
        layoutParams.height = (int) (dm.heightPixels * 0.9);
        dialog.getWindow().setAttributes(layoutParams);
        TextView title = (TextView) dialog.findViewById(R.id.title_name);
        TextView content = (TextView) dialog.findViewById(R.id.title_con);
        title.setVisibility(View.VISIBLE);
        title.setText("发布商品");
        content.setText("确认退出发布商品？");
        Button photograph = (Button) dialog.findViewById(R.id.calen);
        Button photographAdd = (Button) dialog.findViewById(R.id.sure);
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        photographAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyActitity();
                dialog.dismiss();
            }
        });
    }


    /**
     * 添加主页图片
     */
    @OnClick(R.id.publish_shop_add)
    void addImage() {
        type = 1;
        diabackLogin();
    }

    @OnClick(R.id.publish_shop_img)
    void addImages() {
        type = 1;
        diabackLogin();
    }


    /**
     * 添加分类
     */
    @OnClick(R.id.publish_shop_tags)
    void addTags() {
        Intent intent = new Intent();
        intent.setClass(context, TagDetailsActivity.class);
        startActivityForResult(intent, 14);
    }

    /**
     * 是否原创
     */
    @OnClick(R.id.publish_shop_chose)
    void chose() {
        diaChoseType();
    }

    /**
     * 知识产权
     */
    @OnClick(R.id.publish_shop_xi)
    void shopXo() {
        dealView();
    }

    /**
     * 提交
     */
    @OnClick(R.id.publish_shop_sumit)
    void sumbit() {
        if (choseBtn) {
            initData();
        } else {
            Toast.makeText(context, "请阅读，勾选知识产权承诺！", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void deleteFile(int pos) {
        if(files != null && files.size()>0){
            files.remove(pos-1);
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
                    httpShop();
                    bundle.putInt("what", 1);
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
                    if (error == 0) {
                        Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
                        destroyActitity();
                    }
                    break;
            }

        }
    }


    /**
     * 发布商品
     */
    private void httpShop() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.tradeRelease");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("cid", tagsId);
            map.put("good_name", publishShopName.getText().toString().trim());
            map.put("good_intro", publishShopContent.getText().toString().trim());
            map.put("price", publishShopPrice.getText().toString().trim());
            map.put("good_num",publishShopNumber.getText().toString().trim());
            map.put("ship", publishShopExpPrice.getText().toString().trim());
            map.put("maf_time", publishShopTime.getText().toString().trim());
            map.put("description", publishShopTile.getText().toString().trim());
            Map<String, File> file = new HashMap<String, File>();
            file.put("cover", sellImagesFile);
            if (files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    file.put("show[" + i + "]", files.get(i));
                }
            }
            String json = HttpConnectTool.post(map, file);
            if (!json.equals("")) {
                JSONObject jsonObject = new JSONObject(json);
                error = jsonObject.getInt("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 14) {
            String ts = data.getStringExtra("name");
            tagsId = data.getStringExtra("num");
            publishShopTagsName.setText(ts);
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                switch (type) {
                    case 1:
                        ValidData.load(uri, publishShop, 90, 90);
//                        sellImagesFile = uri2File(uri);
                        sellImagesFile=new File(GetPathVideo.getPath(context, uri));
                        break;
                    case 2:
                        if(contentValues !=null && contentValues.size()>0){
                            contentValues.clear();
                            contentValues.add(0, null);
                        }
                        if(files!=null && files.size()>0){
                            files.clear();
                        }
                        mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                        for(String p: mSelectPath) {
                            Uri  uri1 = Uri.parse("file://"+p);
                            file = new File(GetPathVideo.getPath(context, uri1));
                            files.add(file);
                            ContentValues cv = new ContentValues();
                            cv.put("image", uri1.toString());
                            contentValues.add(cv);
                            appraiesimgeAdapter.notifyDataSetChanged();
                        }
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
                    String path = imageUri.getPath();
                    switch (type) {
                        case 1:
//                            sellImagesFile = new File(path);
                            sellImagesFile=new File(GetPathVideo.getPath(context, imageUri));
                            ValidData.load(imageUri, publishShop, 70, 70);
                            break;
                        case 2:
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


    private void dealView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(PublishShopActivity.this, R.style.AlertDialog);
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
        content.setText(textContent.equities);
        chose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                choseBtn = chose.isChecked();
                dialog.dismiss();
                if (choseBtn) {
                    publishShopSumit.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn));
                } else {
                    publishShopSumit.setBackgroundDrawable(getResources().getDrawable(R.drawable.chose_no));
                }

            }
        });

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
    private void diaChoseType() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(PublishShopActivity.this, R.style.AlertDialog);
        dialog.setContentView(R.layout.chose_images);
        dialog.show();
        // 设置对话框大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        dialog.getWindow().setAttributes(layoutParams);
        Button photograph = (Button) dialog.findViewById(R.id.photograph);
        Button photographAdd = (Button) dialog.findViewById(R.id.photograph_add);
        Button photographCal = (Button) dialog.findViewById(R.id.photograph_cal);
        photograph.setText("是");
        photographAdd.setText("否");
        photographCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishShopType.setText("是");
                dialog.dismiss();
            }
        });

        photographAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishShopType.setText("否");
                dialog.dismiss();
            }
        });
    }


    /**
     * 相机
     */
    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(PublishShopActivity.this, R.style.AlertDialog);
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
                if(type==2){
                    choseMoreImage();
                }else{
                    getImageFromAlbum();
                }
                dialog.dismiss();
            }
        });
    }
    /** 多图选择上传 */
    private void choseMoreImage(){
        boolean showCamera=false;
        int maxNum = 1000;
        MultiImageSelector selector = MultiImageSelector.create(context);
        selector.showCamera(showCamera);
        selector.count(maxNum);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(PublishShopActivity.this,1);
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
        Cursor actualimagecursor = PublishShopActivity.this.managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
    }


}
