package com.yshstudio.originalproduct.pages.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

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
import butterknife.Unbinder;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.pages.activity.ElcyGroupActivity;
import com.yshstudio.originalproduct.pages.activity.ElevancyShopActivity;
import com.yshstudio.originalproduct.pages.activity.TagsActivity;
import com.yshstudio.originalproduct.pages.activity.TagsItemActivity;
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

/**
 * Created by Administrator on 2017/5/17 0017.
 * 图片
 */

public class ContentFragment extends BasicFragment implements AppraiesimgeAdapter.deleteFile{

    @BindView(R.id.content_imag)
    SimpleDraweeView contentImag;
    @BindView(R.id.add_content_img)
    TextView addContentImg;
    @BindView(R.id.content_name)
    EditText contentName;
    @BindView(R.id.content_type_name)
    TextView contentTypeName;
    @BindView(R.id.content_type_lin)
    LinearLayout contentTypeLin;
    @BindView(R.id.content_add_image)
    TextView contentAddImage;
    @BindView(R.id.content_img_details)
    EditText contentImgDetails;
    @BindView(R.id.contect_gridView)
    MyGridView contectGridView;
    @BindView(R.id.content_add_tags)
    LinearLayout contentAddTags;
    @BindView(R.id.content_text_qun)
    TextView contentTextQun;
    @BindView(R.id.contect_lin_qun)
    LinearLayout contectLinQun;
    @BindView(R.id.shop_content_num)
    TextView shopContentNum;
    @BindView(R.id.content_lin_add_shop)
    LinearLayout contentLinAddShop;
    @BindView(R.id.content_trade)
    TextView contentTrade;
    @BindView(R.id.content_add_sumit)
    Button contentAddSumit;
    Unbinder unbinder;
    @BindView(R.id.tag_num)
    TextView tagNum;
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
    private Context context;
    private String goods = "";
    private String group = "";
    private String tag = "";
    private String tagsId = "";
    private boolean choseBtn = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.content_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ImageToools.verifyStoragePermissions(getActivity());
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context, 2, false,this);
        contectGridView.setAdapter(appraiesimgeAdapter);
    }

    /**
     * 加入图片
     */
    @OnClick(R.id.content_add_image)
    void addImage() {
        type = 2;
        diabackLogin();
    }

    /**
     * 所属分类
     */
    @OnClick(R.id.content_type_lin)
    void linType() {
        Intent intent = new Intent();
        intent.setClass(context, TagsItemActivity.class);
        startActivityForResult(intent, 14);
    }


    /**
     * 添加标签
     */
    @OnClick(R.id.content_add_tags)
    void addTags() {
        Intent intent = new Intent();
        intent.setClass(context, TagsActivity.class);
        startActivityForResult(intent, 13);
    }

    /**
     * 关联的群
     */
    @OnClick(R.id.contect_lin_qun)
    void linQun() {
        Intent intent = new Intent();
        intent.setClass(context, ElcyGroupActivity.class);
        startActivityForResult(intent, 12);
    }

    /**
     * 关联的商品
     */
    @OnClick(R.id.content_lin_add_shop)
    void linShop() {
        Intent intent = new Intent();
        intent.setClass(context, ElevancyShopActivity.class);
        startActivityForResult(intent, 11);
    }

    /**
     * 发布
     */
    @OnClick(R.id.content_add_sumit)
    void sumit() {
        if (choseBtn) {
            initData();
        } else {
            Toast.makeText(context, "请阅读，勾选知识产权承诺！", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @OnClick(R.id.content_trade)
    void shopXo() {
        dealView();
    }

    private void dealView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialog);
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
                    contentAddSumit.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn));
                } else {
                    contentAddSumit.setBackgroundDrawable(getResources().getDrawable(R.drawable.chose_no));
                }

            }
        });

    }

    /**
     * 添加主题图
     */
    @OnClick(R.id.add_content_img)
    void addImag() {
        type = 1;
        diabackLogin();
    }

    @OnClick(R.id.content_imag)
    void addImages(){
        type = 1;
        diabackLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 11) {
            goods = data.getStringExtra("goods");
            String num = data.getStringExtra("num");
            shopContentNum.setText("已关联" + num + "商品");
        }
        if (resultCode == 12) {
            group = data.getStringExtra("group");
            String number = data.getStringExtra("num");
            contentTextQun.setText("已关联" + number + "群组");
        }
        if (resultCode == 13) {
            tag = data.getStringExtra("tag");
            String number = data.getStringExtra("num");
            tagNum.setText("已关联" + number + "标签");
        }
        if (resultCode == 14) {
            String ts = data.getStringExtra("name");
            tagsId = data.getStringExtra("num");
            contentTypeName.setText(ts);
        }

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                switch (type) {
                    case 1:
                        Uri uri = data.getData();
                        ValidData.load(uri, contentImag, 90, 90);
                        sellImagesFile=new File(GetPathVideo.getPath(context, uri));
                        break;
                    case 2:
                        if(contentValues !=null && contentValues.size()>0){
                            contentValues.clear();
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
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
                    String path = imageUri.getPath();
                    switch (type) {
                        case 1:
                            sellImagesFile=new File(GetPathVideo.getPath(context, imageUri));
                            ValidData.load(imageUri, contentImag, 70, 70);
                            break;
                        case 2:
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
                    Toast.makeText(getActivity(), "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }
        }


    }

    /**
     * 相机
     */
    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialog);
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
            MultiImageSelector selector = MultiImageSelector.create(getActivity());
            selector.showCamera(showCamera);
            selector.count(maxNum);
            selector.multi();
            selector.origin(mSelectPath);
            selector.start(ContentFragment.this,1);
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
        Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
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


    private void initData() {
        boolean isit = true;
        String msg = "";
        if (contentName.getText().toString().trim().equals("")) {
            msg = "请输入标题！";
            isit = false;
        }
        if (contentImgDetails.getText().toString().trim().equals("")) {
            msg = "请输入添加内容！";
            isit = false;
        }
        if (tagsId.equals("")) {
            msg = "请选择分类！";
            isit = false;
        }
        if (tag.equals("")) {
            msg = "请选择标签！";
            isit = false;
        }
        if(files.size()<=0){
            msg = "请载入图片！";
            isit = false;
        }
        if(file==null){
            msg = "请添加主题图片！";
            isit = false;
        }
//        if (tagsId.equals("")) {
//            msg = "请选择分类！";
//            isit = false;
//        }
//        if (tagsId.equals("")) {
//            msg = "请选择分类！";
//            isit = false;
//        }

        if (isit) {
            mLoading = new Loading(context, contentAddSumit);
            mLoading.setText("正在提交......");
            mLoading.show();
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void deleteFile(int pos) {
        if(files != null && files.size()>0){
        files.remove(pos);
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
                        getActivity().onBackPressed();
                    } else if (error == 121) {
                        Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
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
            map.put("action", "Community.addNote");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("cid", tagsId);
            map.put("title", contentName.getText().toString().trim());
            map.put("content", contentImgDetails.getText().toString().trim());
            map.put("tags", tag);
            map.put("groupid", group);
            map.put("goodsid", goods);
            map.put("type", "0");
            Map<String, File> file = new HashMap<String, File>();
            file.put("cover", sellImagesFile);
            if (files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    file.put("show[" + i + "]", files.get(i));
                }
            }
            String json = HttpConnectTool.post(map, file);
            if (!json.equals("")) {
                listXml(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int error;

    public void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            if (obj.has("error")) {
                error = obj.getInt("error");
            } else {
                Toast.makeText(context, "提交失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
