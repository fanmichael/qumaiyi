package com.yshstudio.originalproduct.pages.fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
import com.yshstudio.originalproduct.pages.view.MyProgressDialog;
import com.yshstudio.originalproduct.tools.GetPathVideo;
import com.yshstudio.originalproduct.tools.ImageToools;
import com.yshstudio.originalproduct.tools.TextContent;

import static com.umeng.socialize.utils.Log.TAG;

/**
 * 视频
 */
public class VideoContentFragment extends BasicFragment {

    @BindView(R.id.video_title_name)
    EditText videoTitleName;
    @BindView(R.id.video_add_img)
    TextView videoAddImg;
    @BindView(R.id.video_content)
    EditText videoContent;
    @BindView(R.id.video_gridView)
    MyGridView videoGridView;
    @BindView(R.id.video_tags)
    LinearLayout videoTags;
    @BindView(R.id.video_group)
    LinearLayout videoGroup;
    @BindView(R.id.video_shop)
    LinearLayout videoShop;
    @BindView(R.id.video_details)
    TextView videoDetails;
    @BindView(R.id.publish_video)
    Button publishVideo;
    Unbinder unbinder;
    @BindView(R.id.video_tags_num)
    TextView videoTagsNum;
    @BindView(R.id.video_group_num)
    TextView videoGroupNum;
    @BindView(R.id.video_shop_num)
    TextView videoShopNum;
    @BindView(R.id.voide_pu)
    Button voidePu;
    @BindView(R.id.video_images)
    SimpleDraweeView videoImages;
    @BindView(R.id.voide_chose)
    Button voideChose;
    @BindView(R.id.video_type_name)
    TextView videoTypeName;
    @BindView(R.id.video_type_lin)
    LinearLayout videoTypeLin;

    private Context context;
    /**
     * 添加数据刷新
     */
    private AppraiesimgeAdapter appraiesimgeAdapter;
    /**
     * 添加图片
     */
    private List<ContentValues> contentValues = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private Uri imageUri;
    private File file;
    private File videoFile;
    private String goods = "";
    private String group = "";
    private String tag = "";
    private String tagsId = "";
    /**
     * 视频全称地址
     */
    private String videoAddress = "";
    private int error;
    private MyProgressDialog pd;
    /**
     * 视频的第一帧
     */
    private File fristFile;

    private boolean choseBtn = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.video_content_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ImageToools.verifyStoragePermissions(getActivity());
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context, 2, false);
        videoGridView.setAdapter(appraiesimgeAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 11) {
            goods = data.getStringExtra("goods");
            String num = data.getStringExtra("num");
            videoShopNum.setText("已关联" + num + "商品");
        }
        if (resultCode == 12) {
            group = data.getStringExtra("group");
            String number = data.getStringExtra("num");
            videoGroupNum.setText("已关联" + number + "群组");
        }
        if (resultCode == 13) {
            tag = data.getStringExtra("tag");
            String number = data.getStringExtra("num");
            videoTagsNum.setText("已关联" + number + "标签");
        }
        if (resultCode == 14) {
            String ts = data.getStringExtra("name");
            tagsId = data.getStringExtra("num");
            videoTypeName.setText(ts);
        }

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri uri = data.getData();
                file = uri2File(uri);
                files.add(file);
                ContentValues cv = new ContentValues();
                cv.put("image", uri.toString());
                contentValues.add(cv);
                appraiesimgeAdapter.notifyDataSetChanged();
            }

        }

        if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
                    String path = imageUri.getPath();
                    file = new File(path);
                    files.add(file);
                    ContentValues cv = new ContentValues();
                    cv.put("image", imageUri.toString());
                    contentValues.add(cv);
                    appraiesimgeAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }
        }
        if (requestCode == 3) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri uri = data.getData();
                Log.e("videoMent", "uri: " + uri);
                String path = GetPathVideo.getPath(context, uri);
                Log.e("videoMent", "path: " + path);
                videoImage(path);
                videoFile = new File(path);
            }
        }

    }

    /**
     * 视频第一帧转换成本地图片
     */
    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        String name = "" + System.currentTimeMillis();
        File output = new File(file, name + ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            String path = Environment.getExternalStorageDirectory() + "/拍照/" + name + ".jpg";
            Log.e("path", "" + path);
            fristFile = new File(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("cxccc", fristFile.getName() + "" + fristFile.length());

    }


    /**
     * 所属分类
     */
    @OnClick(R.id.video_type_lin)
    void videoType() {
        Intent intent = new Intent();
        intent.setClass(context, TagsItemActivity.class);
        startActivityForResult(intent, 14);
    }

    /**
     * 获取视频图
     */
    private void videoImage(String path) {
        Bitmap bitmap = createVideoThumbnail(path);
        Bitmap bit = ThumbnailUtils.extractThumbnail(bitmap, 100, 80);
        videoImages.setImageBitmap(bit);
        saveBitmapFile(bit);
    }

    /**
     * 视频第一帧
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        // MediaMetadataRetriever is available on API Level 8
        // but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();
            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);
            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } catch (java.lang.InstantiationException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }


    /**
     * 提交判断
     */
    private void initData() {
        String msg = "";
        boolean isIt = true;
        if (videoTitleName.getText().toString().trim().equals("")) {
            msg = "请输入标题！";
            isIt = false;
        }
        if (videoContent.getText().toString().trim().equals("")) {
            msg = "请输入内容介绍！";
            isIt = false;
        }
        if (videoAddress.equals("")) {
            msg = "请上传视频！";
            isIt = false;
        }
        if (isIt) {
            mLoading = new Loading(context, publishVideo);
            mLoading.setText("正在提交......");
            mLoading.show();
            new asyncTask().execute(1);
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return;
        }

    }


    /**
     * 添加图片
     */
    @OnClick(R.id.video_add_img)
    void imgs() {
        diabackLogin();
    }


    /**
     * 关联的标签
     */
    @OnClick(R.id.video_tags)
    void videoTags() {
        Intent intent = new Intent();
        intent.setClass(context, TagsActivity.class);
        startActivityForResult(intent, 13);
    }

    /**
     * 关联的群组
     */
    @OnClick(R.id.video_group)
    void videoGroup() {
        Intent intent = new Intent();
        intent.setClass(context, ElcyGroupActivity.class);
        startActivityForResult(intent, 12);

    }


    /**
     * 关联的商品
     */
    @OnClick(R.id.video_shop)
    void videShop() {
        Intent intent = new Intent();
        intent.setClass(context, ElevancyShopActivity.class);
        startActivityForResult(intent, 11);
    }


    /**
     * 请阅读协议
     */
    @OnClick(R.id.video_details)
    void videoDe() {
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
                    publishVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn));
                } else {
                    publishVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.chose_no));
                }
            }
        });

    }

    /**
     * 发布视屏
     */
    @OnClick(R.id.publish_video)
    void sumit() {
        if (choseBtn) {
            initData();
        } else {
            Toast.makeText(context, "请阅读，勾选知识产权承诺！", Toast.LENGTH_LONG).show();
            return;
        }

    }

    /**
     * 选择视频文件
     */
    @OnClick(R.id.voide_chose)
    void choseVideoFile() {
        chooseVideo();
    }

    /**
     * 上传视频文件
     */
    @OnClick(R.id.voide_pu)
    void videoPu() {
        if (videoFile.length() < 0) {
            Toast.makeText(context, "请选择视频！", Toast.LENGTH_LONG).show();
        } else {
            pd = new MyProgressDialog(context);
            pd.setProgressStyle(MyProgressDialog.STYLE_HORIZONTAL);
            pd.setMessage("上传视频中。。。");
            pd.setCancelable(false);
            pd.show();
            uploadVideo(videoFile);
        }
    }

    /**
     * 视频
     */
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        getActivity().startActivityForResult(intent, 3);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 3);
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


    /**
     * 发布视频
     */
    private void httpVideo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.addNote");
            map.put("uid", AppContext.cv.getAsInteger("id") + "");
            map.put("cid", tagsId);
            map.put("title", videoTitleName.getText().toString().trim());
            map.put("content", videoContent.getText().toString().trim());
            map.put("tags", tag);
            map.put("groupid", group);
            map.put("goodsid", goods);
            map.put("type", "1");
            map.put("cover", videoAddress);
            Map<String, File> file = new HashMap<String, File>();
            file.put("video_img", fristFile);
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


    /**
     * 异步请求
     */
    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpVideo();
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
     * 上传视频到阿里云
     */
    private void uploadVideo(File file) {
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAImg7aetBxp8Kn", "mgUv74WJqepnQOPIj5LF8C30GYSlzH");
        OSS oss = new OSSClient(context, endpoint, credentialProvider);
        String name = "video/" + System.currentTimeMillis() + file.getName();
        PutObjectRequest put = new PutObjectRequest("qumaiyi", name, file.getPath());
        ObjectMetadata metadata = new ObjectMetadata();
        // 指定Content-Type
        metadata.setContentType("application/mp4");
        // user自定义metadata
        metadata.addUserMetadata("x-oss-meta-name1", "value1");
        put.setMetadata(metadata);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                pd.setMax((int) totalSize);
                pd.setProgress((int) (currentSize));
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (request.getObjectKey() != null && !request.getObjectKey().equals("")) {
                    videoAddress = "http://qumaiyi.oss-cn-shenzhen.aliyuncs.com/" + request.getObjectKey();
//                    videoAddress = request.getObjectKey();
                } else {
                    Toast.makeText(context, "上传失败！", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // task.cancel(); // 可以取消任务

    }


}

