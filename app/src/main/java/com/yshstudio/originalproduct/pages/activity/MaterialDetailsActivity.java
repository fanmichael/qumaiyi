package com.yshstudio.originalproduct.pages.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.hyphenate.easeui.model.UserLodingInFo;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.util.ObjectSaveUtils;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.tools.ImageToools;
import com.yshstudio.originalproduct.tools.ValidData;

/**
 * 个人信息
 */
public class MaterialDetailsActivity extends BaseActivity {

    @BindView(R.id.material_icon_image)
    SimpleDraweeView materialIconImage;
    @BindView(R.id.material_icon_layout)
    RelativeLayout materialIconLayout;
    @BindView(R.id.text_nick)
    TextView textNick;
    @BindView(R.id.text_material_nick)
    TextView textMaterialNick;
    @BindView(R.id.material_nick_layout)
    RelativeLayout materialNickLayout;
    @BindView(R.id.text_material_gender)
    TextView textMaterialGender;
    @BindView(R.id.material_gender_layout)
    RelativeLayout materialGenderLayout;
    @BindView(R.id.text_material_location)
    TextView textMaterialLocation;
    @BindView(R.id.material_location_layout)
    RelativeLayout materialLocationLayout;
    @BindView(R.id.text_material_personalized)
    TextView textMaterialPersonalized;
    @BindView(R.id.material_personalized_layout)
    LinearLayout materialPersonalizedLayout;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    private Context context;
    private int grends;
    private String address;
    private int error;
    private Uri imageUri;
    private File file;
    private Uri jsonImage;
    private String im="";
    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptionsAddress;
    private List<String> grend = new ArrayList<>();
    private List<String> province = new ArrayList<>();
    private List<List<String>> city = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_details);
        ButterKnife.bind(this);
        context = this;
        init();
        initView();
        initOptionPickerGrend();
        jsonTxt();
        initOptionPicker();
        ImageToools.verifyStoragePermissions(MaterialDetailsActivity.this);
    }


    @OnClick(R.id.top_regit_title)
    void addCatech() {
        setResult(101);
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        destroyActitity();

    }

    /**
     * 解析联动数据
     */
    private void jsonTxt() {
        AssetManager manager = getAssets();
        try {
            String data = ValidData.readStream(manager.open("address.txt"));
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                province.add(jsonObject.getString("name"));
                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("subArea"));
                List<String> ss = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObjectj = jsonArray1.getJSONObject(j);
                    ss.add(jsonObjectj.getString("name"));
                }
                city.add(i, ss);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    /**
     * 三级联动
     */
    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptionsAddress = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                address = "";
                address = province.get(options1) + " " + city.get(options1).get(options2);
                new asyncTask().execute(2);

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setSubmitColor(getResources().getColor(R.color.bd_top))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.col_bg))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.bg))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                //.setLabels("省", "市", "区")//设置选择的三级单位
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();

        pvOptionsAddress.setPicker(province, city);//二级选择器

    }


    /**
     * 修改头像
     */
    @OnClick(R.id.material_icon_layout)
    void iconLayout() {
        diabackLogin();
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        File output = new File(file, System.currentTimeMillis() + ".jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         * 将文件转化为uri
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 7);

    }

    /**
     * 相机
     */
    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(MaterialDetailsActivity.this, R.style.AlertDialog);
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
        startActivityForResult(intent, 8);
    }


    private File uri2File(Uri uri) {
        File file = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = MaterialDetailsActivity.this.managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
    }


    /**
     * 昵称
     */
    @OnClick(R.id.material_nick_layout)
    void nickLayout() {
        Intent nickIntent = new Intent(context, UpdateNickActivity.class);
        startActivityForResult(nickIntent, 1);
    }

    /**
     * 性别
     */
    @OnClick(R.id.material_gender_layout)
    void genderLayout() {
        pvOptions.show();
    }

    private void initOptionPickerGrend() {//条件选择器初始化
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                grends = options1;
                new asyncTask().execute(1);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setSubmitColor(getResources().getColor(R.color.bd_top))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.col_bg))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.bg))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();

        pvOptions.setPicker(grend);//一级选择器

    }

    private void initView() {
        grend.add(0, "女");
        grend.add(1, "男");
    }


    /**
     * 地区
     */
    @OnClick(R.id.material_location_layout)
    void locationLayout() {
        pvOptionsAddress.show();
    }

    /**
     * 签名
     */
    @OnClick(R.id.material_personalized_layout)
    void personalizedLayout() {
        Intent perIntent = new Intent(context, UpdatePersonalizedActivity.class);
        startActivityForResult(perIntent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                jsonImage = uri;
                file = uri2File(uri);
                new asyncTask().execute(3);
            }
        }
        if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
                    jsonImage = imageUri;
                    String path = imageUri.getPath();
                    file = new File(path);
                    new asyncTask().execute(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }

        }

        switch (resultCode) {
            case 1:
                textMaterialNick.setText(AppContext.cv.getAsString("nick"));
                updateNick(AppContext.cv.getAsString("nick"));
                break;
            case 2:
                textMaterialPersonalized.setText(AppContext.cv.getAsString("personalized"));
                break;
        }
    }

    /**
     * 加载数据
     */
    private void init() {
        topTitle.setText("个人资料");
        topRegitTitle.setText("保存");
        topRegitTitle.setVisibility(View.VISIBLE);
        if (!AppContext.cv.containsKey("icon")) {
            return;
        }
        Uri imageUri = Uri.parse(AppContext.cv.getAsString("icon"));
        materialIconImage.setImageURI(imageUri);
        textMaterialNick.setText(AppContext.cv.getAsString("nick"));
        textMaterialGender.setText(six());
        textMaterialLocation.setText(AppContext.cv.getAsString("location"));
        textMaterialPersonalized.setText(AppContext.cv.getAsString("personalized"));
    }

    @OnClick(R.id.image_back)
    void imageBack() {
        setResult(101);
        destroyActitity();
    }


    private String six() {
        String six = "";
        if (AppContext.cv.getAsInteger("gender") == 0) {
            six = "女";
        } else {
            six = "男";
        }
        return six;
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpGender(grends);
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpAddress();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    htttpimages();
                    bundle.putInt("what", 3);
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
                        AppContext.cv.put("gender", grends);
                        textMaterialGender.setText(six());
                    } else {
                        return;
                    }
                    break;
                case 2:
                    if (error == 0) {
                        AppContext.cv.put("location", address);
                        textMaterialLocation.setText(AppContext.cv.getAsString("location"));
                    } else {
                        return;
                    }
                    break;
                case 3:
                    if (jsonImage != null && !jsonImage.equals("")) {
                        AppContext.cv.put("icon", jsonImage.toString());
                        Uri imageUri = Uri.parse(AppContext.cv.getAsString("icon"));
                        ValidData.load(imageUri, materialIconImage, 60, 60);
                    }
                    updateIcon(im);
                    break;


            }

        }
    }

    private void httpAddress() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "User.update");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("location", URLEncoder.encode(address.trim(), "UTF-8") + "");
            map.put("name", "location");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlComm(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void httpGender(int gender) {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "User.update");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        map.put("gender", gender + "");
        map.put("name", "gender");
        String json = HttpConnectTool.post(map);
        if (!json.equals("")) {
            xmlComm(json);
        }
    }

    private void xmlComm(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            error = obj.getInt("error");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void htttpimages() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.update");
            hashMap.put("uid", AppContext.cv.getAsInteger("id") + "");
            hashMap.put("name", "icon");
            Map<String, File> files = new HashMap<String, File>();
            files.put("icon", file);
            String json = HttpConnectTool.post(hashMap, files);
            if (!json.equals("")) {
                xmlChose(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void xmlChose(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            im= jsonObject.getString("data");
            if (!im.equals("") && im != null) {
                Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * 聊天修改头像
     * */
    private void updateIcon(String icon){
        UserLodingInFo.getInstance().setIcon(icon);
        ObjectSaveUtils.saveObject(context, "USERINFO", UserLodingInFo.getInstance());

    }

    /**
     * 聊天修改昵称
     * */
    private void updateNick(String nick){
        UserLodingInFo.getInstance().setNick(nick);
        ObjectSaveUtils.saveObject(context, "USERINFO", UserLodingInFo.getInstance());
    }


}
