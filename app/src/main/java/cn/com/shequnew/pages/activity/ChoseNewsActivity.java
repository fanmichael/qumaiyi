package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.tools.ValidData;

/**
 * 搜索
 */
public class ChoseNewsActivity extends BaseActivity {

    @BindView(R.id.image_go_back)
    ImageView imageGoBack;
    @BindView(R.id.chose_gone)
    TextView choseGone;
    @BindView(R.id.edit_chose_news)
    EditText editChoseNews;
    @BindView(R.id.chose_remove)
    ImageView choseRemove;
    @BindView(R.id.chose_text)
    TextView choseText;
    @BindView(R.id.redact_chose)
    LinearLayout redactChose;
    @BindView(R.id.comm_chose)
    LinearLayout commChose;
    @BindView(R.id.user_chose)
    LinearLayout userChose;
    @BindView(R.id.chose_content)
    LinearLayout choseContent;
    @BindView(R.id.chose_shop)
    LinearLayout choseShop;
    @BindView(R.id.chose_user)
    LinearLayout choseUser;
    private Context context;
    private String content;
    private List<ContentValues> contentUser = new ArrayList<>();
    private List<ContentValues> contentGood = new ArrayList<>();
    private List<ContentValues> contentNote = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_news);
        ButterKnife.bind(this);
        context = this;
        editChoseNews.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                choseGone.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0 && choseRemove.getVisibility() != View.VISIBLE) {
                    choseRemove.setVisibility(View.VISIBLE);
                } else if (text.length() <= 0 && choseRemove.getVisibility() == View.VISIBLE) {
                    choseRemove.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @OnClick(R.id.chose_remove)
    void clearEdit() {
        editChoseNews.setText("");
    }

    @OnClick(R.id.image_go_back)
    void imageBack() {
        destroyActitity();
    }

    @OnClick(R.id.chose_text)
    void chose() {
        content = editChoseNews.getText().toString().trim();
        if (content.equals("")) {
            Toast.makeText(context, "请输入搜索内容", Toast.LENGTH_SHORT).show();
        } else {
            mLoading = new Loading(
                    context, choseText);
            mLoading.setText("正在加载......");
            mLoading.show();
            new asyncTask().execute(1);
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpChose();
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
                    if (contentNote.size() > 0) {
                        choseContent.setVisibility(View.VISIBLE);
                    } else {
                        choseContent.setVisibility(View.GONE);
                    }

                    if (contentUser.size() > 0) {
                        choseUser.setVisibility(View.VISIBLE);
                    } else {
                        choseUser.setVisibility(View.GONE);
                    }
                    if (contentGood.size() > 0) {
                        choseShop.setVisibility(View.VISIBLE);
                    } else {
                        choseShop.setVisibility(View.GONE);
                    }
                    initView();
                    break;

            }

        }
    }


    private void initView() {
        for (int i = 0; i < contentNote.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.pages_item_chose, null);
            LinearLayout lay = (LinearLayout) view.findViewById(R.id.pages_iten_layout);
            SimpleDraweeView pagesIcon = (SimpleDraweeView) view.findViewById(R.id.pages_item_icon);//头像
            SimpleDraweeView pagesSubject = (SimpleDraweeView) view.findViewById(R.id.pages_item_subject);//大图
            TextView pagesTags = (TextView) view.findViewById(R.id.pages_tags_item_text); //标签
            TextView pagesTitle = (TextView) view.findViewById(R.id.pages_title_text);//标题
            ImageView pagesFileM = (ImageView) view.findViewById(R.id.pages_file_m);//视频
            ImageView pagesFileF = (ImageView) view.findViewById(R.id.pages_file_f);//文件
            TextView pagesItemNick = (TextView) view.findViewById(R.id.pages_item_nick_text);//昵称
            TextView pagesSign = (TextView) view.findViewById(R.id.pages_sign_item_text);//签名
            ImageView plvideo = (ImageView) view.findViewById(R.id.play_video);
            Uri imageUri = Uri.parse(contentNote.get(i).getAsString("icon"));
            ValidData.load(imageUri, pagesIcon, 30, 30);
            pagesItemNick.setText(contentNote.get(i).getAsString("nick"));
            pagesTags.setText(contentNote.get(i).getAsString("tags"));
            pagesTitle.setText(contentNote.get(i).getAsString("title"));

            if (contentNote.get(i).getAsInteger("file_type") == 0) {
                pagesFileM.setVisibility(View.GONE);
                pagesFileF.setVisibility(View.VISIBLE);
            } else if (contentNote.get(i).getAsInteger("file_type") == 1) {
                pagesFileM.setVisibility(View.VISIBLE);
                pagesFileF.setVisibility(View.GONE);
            }

            pagesSign.setVisibility(View.GONE);

            if (contentNote.get(i).getAsInteger("file_type") == 0) {
                Uri imageUris = Uri.parse(contentNote.get(i).getAsString("subject"));
                ValidData.load(imageUris, pagesSubject, 100, 80);
                plvideo.setVisibility(View.GONE);
            } else {
                Uri imageUris = Uri.parse(contentNote.get(i).getAsString("video_img"));
                ValidData.load(imageUris, pagesSubject, 100, 80);
                plvideo.setVisibility(View.VISIBLE);
            }
            final int type = contentNote.get(i).getAsInteger("file_type");
            final String str = contentNote.get(i).getAsString("nick");
            final int id = contentNote.get(i).getAsInteger("id");
            final int uid = contentNote.get(i).getAsInteger("uid");
            lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", id);
                        bundle.putInt("uid", uid);
                        intent.putExtras(bundle);
                        intent.setClass(context, ContentFileDetailsActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", id);
                        bundle.putInt("uid", uid);
                        intent.putExtras(bundle);
                        intent.setClass(context, LocalVideoActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            redactChose.addView(view);

        }
        for (int i = 0; i < contentUser.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_chose_item, null);
            LinearLayout userLayout = (LinearLayout) view.findViewById(R.id.user_layout);
            SimpleDraweeView userIcon = (SimpleDraweeView) view.findViewById(R.id.user_icon);
            TextView userName = (TextView) view.findViewById(R.id.user_name);
            TextView userTitle = (TextView) view.findViewById(R.id.user_title);
            Uri imageUri = Uri.parse(contentUser.get(i).getAsString("icon"));
            ValidData.load(imageUri, userIcon, 60, 60);
            userName.setText(contentUser.get(i).getAsString("nick"));
            userTitle.setText(contentUser.get(i).getAsString("personalized"));
            final int id = contentUser.get(i).getAsInteger("id");
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("uid", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            userChose.addView(view);
        }

        for (int i = 0; i < contentGood.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.commend_item_list, null);
            LinearLayout comm_layout = (LinearLayout) view.findViewById(R.id.comm_layout);
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_images);
            TextView commTitle = (TextView) view.findViewById(R.id.comm_commodity_title);
            TextView commGrd = (TextView) view.findViewById(R.id.comm_commodity_grd);
            SimpleDraweeView commIcon = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_icon);
            TextView commNick = (TextView) view.findViewById(R.id.comm_commodity_nick);
            TextView commPrice = (TextView) view.findViewById(R.id.comm_commodity_price);

            Uri imageUri = Uri.parse(contentGood.get(i).getAsString("good_image"));
            ValidData.load(imageUri, simpleDraweeView, 100, 80);
            Uri imageIcon = Uri.parse(contentGood.get(i).getAsString("icon"));
            ValidData.load(imageIcon, commIcon, 30, 30);
            commTitle.setText(contentGood.get(i).getAsString("good_name"));
            commGrd.setText("工期：" + contentGood.get(i).getAsInteger("maf_time") + "天");
            commNick.setText(contentGood.get(i).getAsString("nick"));
            commPrice.setText("￥" + contentGood.get(i).getAsString("price"));
            final int id = contentGood.get(i).getAsInteger("id");
            final int uid = contentGood.get(i).getAsInteger("uid");
            comm_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putInt("uid", uid);
                    intent.putExtras(bundle);
                    intent.setClass(context, ShopDetailsActivity.class);
                    context.startActivity(intent);

                }
            });
            commChose.addView(view);
        }


    }


    private void httpChose() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "User.searchUserAndGoodsAndNote");
            map.put("content", content);
            map.put("page", "1");
            String json = HttpConnectTool.post(map);
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
            JSONObject objData = new JSONObject(jsonObject.getString("data"));
            JSONArray jsonUser = new JSONArray(objData.getString("user"));
            JSONArray jsonGoods = new JSONArray(objData.getString("goods"));
            JSONArray jsonNote = new JSONArray(objData.getString("note"));

            if (jsonUser.length() > 0) {
                for (int i = 0; i < jsonUser.length(); i++) {
                    JSONObject jsonObj = jsonUser.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("gender", jsonObj.getInt("gender"));
                    cv.put("nick", jsonObj.getString("nick"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("location", jsonObj.getString("location"));
                    cv.put("personalized", jsonObj.getString("personalized"));
                    contentUser.add(cv);

                }
            }
            if (jsonGoods.length() > 0) {
                for (int i = 0; i < jsonGoods.length(); i++) {
                    JSONObject jsonObj = jsonGoods.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("origin", jsonObj.getInt("origin"));
                    cv.put("type", jsonObj.getInt("type"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("good_intro", jsonObj.getString("good_intro"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("description", jsonObj.getString("description"));
                    cv.put("price", jsonObj.getString("price"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("nick", jsonObj.getString("nick"));
                    contentGood.add(cv);
                }
            }
            if (jsonNote.length() > 0) {
                for (int i = 0; i < jsonNote.length(); i++) {
                    JSONObject jsonObj = jsonNote.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("follow", jsonObj.getInt("follow"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("file_type", jsonObj.getInt("file_type"));
                    cv.put("title", jsonObj.getString("title"));
                    cv.put("subject", jsonObj.getString("subject"));
                    if(jsonObj.has("video_img")){
                        cv.put("video_img", jsonObj.getString("video_img"));
                    }
                    cv.put("subject_type", jsonObj.getString("subject_type"));
                    cv.put("tags", jsonObj.getString("tags"));
                    cv.put("content", jsonObj.getString("content"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("nick", jsonObj.getString("nick"));
                    contentNote.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }


}
