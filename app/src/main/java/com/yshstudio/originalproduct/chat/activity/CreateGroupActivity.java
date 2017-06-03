package com.yshstudio.originalproduct.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.bean.LoadUpResponse;
import com.yshstudio.originalproduct.chat.net.ComUnityRequest;
import com.yshstudio.originalproduct.chat.util.ImageUtil;
import com.yshstudio.originalproduct.chat.util.PermissionUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.schedulers.Schedulers;

public class CreateGroupActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.group_name)
    EditText groupName;
    @BindView(R.id.group_notify)
    EditText groupNotify;
    private Unbinder unbinder;
    private Uri imageUri;
    private String name;
    private String notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        unbinder = ButterKnife.bind(this);
        init();
    }

    ProgressDialog dialog;

    private void showProg(String s) {
        dialog.setMessage(s);
        dialog.show();
    }

    private void dimissProg() {
        dialog.dismiss();
    }

    private void init() {
        dialog = new ProgressDialog(CreateGroupActivity.this);
        titleBar.setTitle("建群");
        titleBar.setRightText("建立");
        titleBar.setLeftImageResource(R.drawable.arrowleft);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateGroupActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    return;
                }
                showProg("正在上传");
                name = groupName.getText().toString();
                notify = groupNotify.getText().toString();
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                try {
                    final EMGroup group = EMClient.getInstance().groupManager().createGroup(name, "", new String[]{}, "", option);
                    // Toast.makeText(CreateGroupActivity.this, "" + group.getGroupId(), Toast.LENGTH_SHORT).show();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            File file = new File(getRealPathFromURI(imageUri));
                            final File file1 = new File(Environment.getExternalStoragePublicDirectory
                                    (Environment.DIRECTORY_DOWNLOADS) + "thunder" + file.getName());
                            ImageUtil.compressImage(file.getPath(), file1.getPath(), 8);
                            // Toast.makeText(CreateGroupActivity.this, "" + file.getName(), Toast.LENGTH_SHORT).show();
                            Map<String, RequestBody> parmes = new HashMap<String, RequestBody>();
                            parmes.put("action", toreRequestBody("Group.groupInsert"));
                            parmes.put("uid", toreRequestBody(UserLodingInFo.getInstance().getId()));
                            parmes.put("group_id", toreRequestBody(group.getGroupId()));
                            parmes.put("group_name", toreRequestBody(group.getGroupName()));
                            parmes.put("group_public", toreRequestBody(notify));
                            parmes.put("icon\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file1));

                            ComUnityRequest.getAPI().uploadImage(parmes).subscribeOn(Schedulers.io()).subscribe(new Observer<LoadUpResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(final LoadUpResponse loadUpResponse) {
                                    if (loadUpResponse.getError() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CreateGroupActivity.this.dimissProg();
                                                CreateGroupActivity.this.finish();
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        file1.delete();
                                                    }
                                                }.start();
                                            }
                                        });
                                    }
                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateGroupActivity.this, "" + loadUpResponse.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });*/
                                }
                            });
                        }
                    }.start();

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.verifyStoragePermissions(CreateGroupActivity.this);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                // intent.addCategory(Intent.CATEGORY_OPENABLE);
                // intent.setType("image/*");
                /*if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                }*/
                startActivityForResult(intent, 0x00);
            }
        });

    }

    @NonNull
    private RequestBody toreRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && requestCode == 0x00 && data.getData() != null) {
            avatar.setImageURI(data.getData());
            imageUri = data.getData();
            //Toast.makeText(this, "" + getRealPathFromURI(data.getData()), Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
