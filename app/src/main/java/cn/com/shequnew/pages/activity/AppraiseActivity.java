package cn.com.shequnew.pages.activity;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.com.shequnew.R;
import cn.com.shequnew.pages.adapter.AppraiesimgeAdapter;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.tools.ImageToools;

/**
 * 评价
 */
public class AppraiseActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.add_lin)
    LinearLayout addLin;
    @BindView(R.id.lin_hig)
    LinearLayout linHig;
    @BindView(R.id.appraise)
    EditText appraise;
    @BindView(R.id.item_grds1)
    ImageView itemGrds1;
    @BindView(R.id.item_grds2)
    ImageView itemGrds2;
    @BindView(R.id.item_grds3)
    ImageView itemGrds3;
    @BindView(R.id.item_grds4)
    ImageView itemGrds4;
    @BindView(R.id.item_grds5)
    ImageView itemGrds5;
    @BindView(R.id.adv_Board_gridView)
    GridView advBoardGridView;

    private String content = "";
    private String ddid = "";
    private int ster = 0;
    private int i = 1;
    private Uri imageUri;
    private Context context;
    private File file;
    private List<ContentValues> contentValues = new ArrayList<>();
    private ArrayList<String> listImgPath = new ArrayList<String>();
    private AppraiesimgeAdapter appraiesimgeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        ButterKnife.bind(this);
        context = this;
        initView();
        ImageToools.verifyStoragePermissions(AppraiseActivity.this);
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
        startActivityForResult(intent, 2);

    }


    private void initView() {
        topTitle.setText("评价");
        topRegitTitle.setVisibility(View.VISIBLE);
        topRegitTitle.setText("提交");
        Bundle bundle = this.getIntent().getExtras();
        ddid = bundle.getString("ddid");
        contentValues.add(0, null);
        appraiesimgeAdapter = new AppraiesimgeAdapter(contentValues, context,1);
        advBoardGridView.setAdapter(appraiesimgeAdapter);
        advBoardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (i >= 4) {
                        Toast.makeText(context, "只能添加三张照片！", Toast.LENGTH_SHORT).show();
                    } else {
                        diabackLogin();
                    }


                }
            }
        });
    }

    /**
     * 相机
     */
    private void diabackLogin() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Dialog dialog = new Dialog(AppraiseActivity.this, R.style.AlertDialog);
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
        Cursor actualimagecursor = AppraiseActivity.this.managedQuery(uri, proj, null, null, null);
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
            file = uri2File(uri);
            ContentValues cv = new ContentValues();
            cv.put("image", uri.toString());
            contentValues.add(i, cv);
            i++;
            appraiesimgeAdapter.notifyDataSetChanged();
            new asyncTask().execute(2);
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {
                    /**
                     * 该uri就是照片文件夹对应的uri
                     */
                    String path = imageUri.getPath();
                    file = new File(path);
                    ContentValues cv = new ContentValues();
                    cv.put("image", imageUri.toString());
                    contentValues.add(i, cv);
                    i++;
                    appraiesimgeAdapter.notifyDataSetChanged();
                    new asyncTask().execute(2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("tag", "失败");
            }
        }
    }


    @OnClick(R.id.item_grds1)
    void grds1() {
        itemGrds1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds2.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds3.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds4.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds5.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        ster = 1;
    }

    @OnClick(R.id.item_grds2)
    void grds2() {
        itemGrds1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds2.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds3.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds4.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds5.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        ster = 2;
    }

    @OnClick(R.id.item_grds3)
    void grds3() {
        itemGrds1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds2.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds3.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds4.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        itemGrds5.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        ster = 3;
    }

    @OnClick(R.id.item_grds4)
    void grds4() {
        itemGrds1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds2.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds3.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds4.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds5.setBackgroundDrawable(getResources().getDrawable(R.drawable.grds));
        ster = 4;
    }

    @OnClick(R.id.item_grds5)
    void grds5() {

        itemGrds1.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds2.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds3.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds4.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        itemGrds5.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
        ster = 5;
    }

    @OnClick(R.id.top_regit_title)
    void regit() {// 提交
        content = appraise.getText().toString().trim();
        boolean isit = true;
        if (ster == 0) {
            Toast.makeText(context, "请评价星级", Toast.LENGTH_SHORT).show();
            isit = false;
        }
        if (content.equals("")) {
            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
            isit = false;
        }
        if (isit) {
            new asyncTask().execute(1);
        }

    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    htttpAll();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    htttpimages();
                    bundle.putInt("what", 2);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
//            removeLoading();
            switch (what) {
                case 1:
                    destroyActitity();
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


    private void htttpimages() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.update");
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
            String jsonImage = jsonObject.getString("data");
            if (jsonImage != null) {
                listImgPath.add(jsonImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }


    private void htttpAll() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "Orderid.orderpl");
            hashMap.put("ddid", ddid);
            hashMap.put("star", ster + "");
            hashMap.put("cont", content);
            for (int i = 0; i < listImgPath.size(); i++) {
                hashMap.put("img", listImgPath.get(i));
            }
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
