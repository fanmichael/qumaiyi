package cn.com.shequnew.pages.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ChoseNewsActivity;
import cn.com.shequnew.pages.activity.ContentFileDetailsActivity;
import cn.com.shequnew.pages.activity.LocalVideoActivity;
import cn.com.shequnew.pages.activity.MoreActivity;
import cn.com.shequnew.pages.activity.SpecialNoteActivity;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.pages.view.SlideShowView;
import cn.com.shequnew.tools.Util;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

/**
 * 首页
 */
public class PagesFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.pages_news_layout)
    LinearLayout pagesNewsLayout;
    @BindView(R.id.pages_hot_layout)
    LinearLayout pagesHotLayout;
    @BindView(R.id.sw_ly)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.slideshowView)
    SlideShowView slideshowView;
    Unbinder unbinder;
    @BindView(R.id.rad_dynamic_wu)
    RadioButton radDynamicWu;
    @BindView(R.id.rad_dynamic_gu)
    RadioButton radDynamicGu;
    @BindView(R.id.rad_dynamic_yang)
    RadioButton radDynamicYang;
    @BindView(R.id.rad_dynamic_jue)
    RadioButton radDynamicJue;
    @BindView(R.id.radio_dynamic)
    RadioGroup radioDynamic;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.info_pages)
    LinearLayout infoPages;

    @BindView(R.id.pagse_news_text_more)
    TextView pagseNewsTextMore;
    @BindView(R.id.pages_news_more)
    LinearLayout pagesNewsMore;
    @BindView(R.id.pages_hot_text_more)
    TextView pagesHotTextMore;
    @BindView(R.id.pagse_hot_more)
    LinearLayout pagseHotMore;

    private Context context;
    //最新
    private List<ContentValues> newaList = new ArrayList<>();
    //最热
    private List<ContentValues> hotList = new ArrayList<>();
    //广告图
    private List<ContentValues> imagesUrls = new ArrayList<>();
    //位图
    private List<ContentValues> imagesList = new ArrayList<>();
    //ming
    private List<ContentValues> namesList = new ArrayList<>();
    private String type = "1";
    private String[] imageUrls;
    private String[] imageUris = new String[]{
            "http://www.baidu.com",
            "http://www.sina.com.cn",
            "http://www.taobao.com",
            "http://www.tudou.com"};
    private List<Map<String, String>> imageList = new ArrayList<Map<String, String>>();
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pages_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initDelay();
        setDelayMessage(1, 100);
        initView();
    }

    public void initView() {
        swipeRefreshLayout.setOnRefreshListener(this);

//        swipeRefreshLayout.setColorScheme(android.R.color.black, android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_light, android.R.color.holo_red_light);
        radioDynamicChose();
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageMargin(10);


    }

    /**
     * 专题
     */
    private void info() {
        for (int i = 0; i < imagesList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.info_images_litem_layout, null);
            FrameLayout fra_layout_itme = (FrameLayout) view.findViewById(R.id.fra_layout_itme);
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.info_commodity_images);
            TextView textView = (TextView) view.findViewById(R.id.info_title);
            Uri imageUri = Uri.parse(imagesList.get(i).getAsString("img"));
            ValidData.load(imageUri, simpleDraweeView, 100, 40);
            textView.setText(imagesList.get(i).getAsString("title"));
            final Integer id = imagesList.get(i).getAsInteger("id");
            fra_layout_itme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("sid", id);
                    intent.putExtras(bundle);
                    intent.setClass(context, SpecialNoteActivity.class);
                    context.startActivity(intent);
                }
            });
            infoPages.addView(view);
        }
    }


    class MyAdapter extends PagerAdapter {
        //private List<View> mListViews;
        private List<ContentValues> contentValues;
        private Context context;

        public MyAdapter(Context context, List<ContentValues> contentValues) {
            this.context = context;
            this.contentValues = contentValues;
        }

        @Override
        public int getCount() {
            return contentValues.size();
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            //super.destroyItem(container, position, object);
            ((ViewPager) arg0).removeView((View) arg2);
//            container.removeView(contentValues.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            FrameLayout lin = (FrameLayout) inflater.inflate(
                    R.layout.info_images_litem_layout, null);
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) lin.findViewById(R.id.info_commodity_images);
            TextView textView = (TextView) lin.findViewById(R.id.info_title);
            Uri imageUri = Uri.parse(imagesList.get(position).getAsString("img"));
            ValidData.load(imageUri, simpleDraweeView, 100, 40);
            textView.setText(imagesList.get(position).getAsString("title"));
            container.addView(lin);
            // views.add(lin);
            return lin;
        }
    }


    private void radioDynamicChose() {
        radioDynamic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rad_dynamic_wu:
                        btnTextFo();
                        setDelayMessage(1, 1000);
                        break;
                    case R.id.rad_dynamic_gu:
                        btnTextOne();
                        setDelayMessage(2, 1000);
                        break;
                    case R.id.rad_dynamic_yang:
                        btnTextTwo();
                        setDelayMessage(3, 1000);
                        break;
                    case R.id.rad_dynamic_jue:
                        btnTextSe();
                        setDelayMessage(4, 1000);
                        break;

                }
            }
        });
    }


    @OnClick(R.id.pagse_news_text_more)
    void newsMore() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("cid", type);
        bundle.putString("hot", "0");
        intent.putExtras(bundle);
        intent.setClass(context, MoreActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.pages_hot_text_more)
    void hotMore() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("cid", type);
        bundle.putString("hot", "1");
        intent.putExtras(bundle);
        intent.setClass(context, MoreActivity.class);
        context.startActivity(intent);
    }

    /**
     * 延迟线程消息
     */
    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
//                        mLoading = new Loading(
//                                getActivity(), pagseHotMore);
//                        mLoading.setText("正在加载......");
//                        mLoading.show();
                        new asyncTask().execute(1);
                        break;
                    case 2:
//                        mLoading = new Loading(
//                                getActivity(), pagseHotMore);
//                        mLoading.setText("正在加载......");
//                        mLoading.show();
                        new asyncTask().execute(2);
                        break;
                    case 3:
//                        mLoading = new Loading(
//                                getActivity(), pagseHotMore);
//                        mLoading.setText("正在加载......");
//                        mLoading.show();
                        new asyncTask().execute(3);
                        break;
                    case 4:
//                        mLoading = new Loading(
//                                getActivity(), pagseHotMore);
//                        mLoading.setText("正在加载......");
//                        mLoading.show();
                        new asyncTask().execute(4);
                        break;
                }
            }
        };
    }

    @Override
    public void onRefresh() {
//        setDelayMessage(1, 100);
        swipeRefreshLayout.setRefreshing(false);//刷新完成
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            if (newaList != null && newaList.size() > 0) {
                newaList.clear();
            }
            if (hotList != null && hotList.size() > 0) {
                hotList.clear();
            }
            if (imagesUrls != null && imagesUrls.size() > 0) {
                imagesUrls.clear();
            }
            if (imageList != null && imageList.size() > 0) {
                imageList.clear();
            }

            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpPages();
                    httpInfo();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    type = "2";
                    httpPages();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    type = "3";
                    httpPages();
                    bundle.putInt("what", 3);
                    break;
                case 4:
                    type = "4";
                    httpPages();
                    bundle.putInt("what", 4);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            imageUrls = null;
            switch (what) {
                case 1:
                    info();
                    swipeRefreshLayout.setRefreshing(false);//刷新完成
                    newsList();
                    hotList();
                    btnText(namesList);
                    if (imagesUrls.size() > 0) {
                        slideshowView.setVisibility(View.VISIBLE);
                    } else {
                        slideshowView.setVisibility(View.GONE);
                    }

                    imageUrls = new String[imagesUrls.size()];
                    for (int j = 0; j < imagesUrls.size(); j++) {
                        imageUrls[j] = imagesUrls.get(j).getAsString("img_add");
                    }
                    for (int i = 0; i < imageUrls.length; i++) {
                        Map<String, String> image_uri = new HashMap<String, String>();
                        image_uri.put("imageUrls", imageUrls[i]);
                        image_uri.put("imageUris", imageUris[i]);
                        imageList.add(image_uri);
                    }
                    slideshowView.setImageUrls(imageList);
                    break;
                case 2:
                    newsList();
                    hotList();
                    if (imagesUrls.size() > 0) {
                        slideshowView.setVisibility(View.VISIBLE);
                    } else {
                        slideshowView.setVisibility(View.GONE);
                    }
                    imageUrls = new String[imagesUrls.size()];
                    for (int j = 0; j < imagesUrls.size(); j++) {
                        imageUrls[j] = imagesUrls.get(j).getAsString("img_add");
                    }
                    for (int i = 0; i < imagesUrls.size(); i++) {
                        Map<String, String> image_uri = new HashMap<String, String>();
                        image_uri.put("imageUrls", imageUrls[i]);
                        image_uri.put("imageUris", imageUris[i]);
                        imageList.add(image_uri);
                    }
                    slideshowView.setImageUrls(imageList);
                    break;
                case 3:
                    newsList();
                    hotList();
                    if (imagesUrls.size() > 0) {
                        slideshowView.setVisibility(View.VISIBLE);
                    } else {
                        slideshowView.setVisibility(View.GONE);
                    }
                    imageUrls = new String[imagesUrls.size()];
                    for (int j = 0; j < imagesUrls.size(); j++) {
                        imageUrls[j] = imagesUrls.get(j).getAsString("img_add");
                    }
                    for (int i = 0; i < imagesUrls.size(); i++) {
                        Map<String, String> image_uri = new HashMap<String, String>();
                        image_uri.put("imageUrls", imageUrls[i]);
                        image_uri.put("imageUris", imageUris[i]);
                        imageList.add(image_uri);
                    }
                    slideshowView.setImageUrls(imageList);
                    break;
                case 4:
                    newsList();
                    hotList();
                    if (imagesUrls.size() > 0) {
                        slideshowView.setVisibility(View.VISIBLE);
                    } else {
                        slideshowView.setVisibility(View.GONE);
                    }
                    imageUrls = new String[imagesUrls.size()];
                    for (int j = 0; j < imagesUrls.size(); j++) {
                        imageUrls[j] = imagesUrls.get(j).getAsString("img_add");
                    }
                    for (int i = 0; i < imagesUrls.size(); i++) {
                        Map<String, String> image_uri = new HashMap<String, String>();
                        image_uri.put("imageUrls", imageUrls[i]);
                        image_uri.put("imageUris", imageUris[i]);
                        imageList.add(image_uri);
                    }
                    slideshowView.setImageUrls(imageList);
                    break;

            }

        }
    }

    /**
     * 中间的图片
     */
    private void httpInfo() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.getSpecialInfo");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlInfo(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void xmlInfo(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray list = new JSONArray(obj.getString("data"));
            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObj = list.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put("id", jsonObj.getInt("id"));
                cv.put("cid", jsonObj.getInt("cid"));
                cv.put("title", jsonObj.getString("title"));
                cv.put("img", jsonObj.getString("img"));
                imagesList.add(cv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    /**
     * 首次加载
     */
    private void httpPages() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Community.index");
            map.put("cid", type);
            String json = HttpConnectTool.post(map);
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
            JSONObject objData = new JSONObject(obj.getString("data"));
            JSONObject objNews = new JSONObject(objData.getString("new"));
            JSONArray jsonArrNews = new JSONArray(objNews.getString(type));
            JSONObject objHot = new JSONObject(objData.getString("hot"));
            JSONArray jsonArrHot = new JSONArray(objHot.getString(type));
            JSONArray nameData = new JSONArray(objData.getString("cates"));//
            JSONObject objimage = new JSONObject(objData.getString("photo"));
            JSONArray imagesData = new JSONArray(objimage.getString(type));//tu
            if (nameData.length() > 0) {
                for (int i = 0; i < nameData.length(); i++) {
                    JSONObject jsonObj = nameData.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("name", jsonObj.getString("name"));
                    namesList.add(cv);
                }
            }

            if (imagesData.length() > 0) {
                for (int i = 0; i < imagesData.length(); i++) {
                    JSONObject jsonObj = imagesData.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("img_add", jsonObj.getString("img_add"));
                    imagesUrls.add(cv);
                }
            }

            if (jsonArrNews.length() > 0) {
                for (int i = 0; i < jsonArrNews.length(); i++) {
                    JSONObject jsonObj = jsonArrNews.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("file_type", jsonObj.getInt("file_type"));
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("subject", jsonObj.getString("subject"));
                    if (jsonObj.has("video_img")) {
                        cv.put("video_img", jsonObj.getString("video_img"));
                    }
                    cv.put("subject_type", jsonObj.getString("subject_type"));
                    cv.put("tags", jsonObj.getString("tags"));
                    cv.put("nick", jsonObj.getString("nick"));
                    cv.put("icon", jsonObj.getString("icon"));
                    cv.put("title", jsonObj.getString("title"));
                    cv.put("isSign", jsonObj.getInt("isSign"));
                    cv.put("follow", jsonObj.getInt("follow"));
                    newaList.add(cv);
                }
            }

            if (jsonArrHot.length() > 0) {
                for (int i = 0; i < jsonArrHot.length(); i++) {
                    JSONObject jsonObj = jsonArrHot.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("uid", jsonObj.getInt("uid"));//用户id
                    cv.put("id", jsonObj.getInt("id"));//文章id
                    cv.put("file_type", jsonObj.getInt("file_type"));//文件类型 0是文件1是视频
                    cv.put("cid", jsonObj.getInt("cid"));
                    cv.put("subject", jsonObj.getString("subject"));//右侧图像或者视频预览？
                    if(jsonObj.has("video_img")){
                        cv.put("video_img", jsonObj.getString("video_img"));
                    }
                    cv.put("subject_type", jsonObj.getString("subject_type"));
                    cv.put("title", jsonObj.getString("title"));//标题
                    cv.put("tags", jsonObj.getString("tags"));//标签
                    cv.put("nick", jsonObj.getString("nick"));//昵称
                    cv.put("icon", jsonObj.getString("icon"));//头像
                    cv.put("isSign", jsonObj.getInt("isSign"));//是否签约
                    cv.put("follow", jsonObj.getInt("follow"));
                    hotList.add(cv);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 最新内容
     */
    private void newsList() {
        if (newaList.size() > 8) {
            pagseNewsTextMore.setVisibility(View.VISIBLE);
        } else {
            pagseNewsTextMore.setVisibility(View.GONE);
        }
        if (newaList.size() > 0) {
            for (int i = 0; i < newaList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.pages_item_chose, null);
                LinearLayout lay = (LinearLayout) view.findViewById(R.id.pages_iten_layout);
                SimpleDraweeView pagesIcon = (SimpleDraweeView) view.findViewById(R.id.pages_item_icon);//头像
                final SimpleDraweeView pagesSubject = (SimpleDraweeView) view.findViewById(R.id.pages_item_subject);//大图
                TextView pagesTags = (TextView) view.findViewById(R.id.pages_tags_item_text); //标签
                TextView pagesTitle = (TextView) view.findViewById(R.id.pages_title_text);//标题
                ImageView pagesFileM = (ImageView) view.findViewById(R.id.pages_file_m);//视频
                ImageView pagesFileF = (ImageView) view.findViewById(R.id.pages_file_f);//文件
                TextView pagesItemNick = (TextView) view.findViewById(R.id.pages_item_nick_text);//昵称
                TextView pagesSign = (TextView) view.findViewById(R.id.pages_sign_item_text);//签名
                ImageView video = (ImageView) view.findViewById(R.id.play_video);
                Uri imageUri = Uri.parse(newaList.get(i).getAsString("icon"));
                ValidData.load(imageUri, pagesIcon, 30, 30);
                pagesItemNick.setText(newaList.get(i).getAsString("nick"));
                pagesTags.setText(newaList.get(i).getAsString("tags"));
                pagesTitle.setText(newaList.get(i).getAsString("title"));

                if (newaList.get(i).getAsInteger("file_type") == 0) {
                    pagesFileM.setVisibility(View.GONE);
                    pagesFileF.setVisibility(View.VISIBLE);
                } else if (newaList.get(i).getAsInteger("file_type") == 1) {
                    pagesFileM.setVisibility(View.VISIBLE);
                    pagesFileF.setVisibility(View.GONE);
                }

                if (newaList.get(i).getAsInteger("isSign") == 0) {
                    pagesSign.setVisibility(View.GONE);
                } else if (newaList.get(i).getAsInteger("isSign") == 1) {
                    pagesSign.setVisibility(View.VISIBLE);
                }

                if (newaList.get(i).getAsInteger("file_type") == 0) {
                    Uri imageUris = Uri.parse(newaList.get(i).getAsString("subject"));
                    pagesSubject.setImageURI(imageUris);
                    video.setVisibility(View.GONE);
                } else {
                    Uri imageUris = Uri.parse(newaList.get(i).getAsString("video_img"));
                    pagesSubject.setImageURI(imageUris);
                    video.setVisibility(View.VISIBLE);
                }
                final int type = newaList.get(i).getAsInteger("file_type");
                final String str = newaList.get(i).getAsString("nick");
                final Integer id = newaList.get(i).getAsInteger("id");
                final Integer uid = newaList.get(i).getAsInteger("uid");
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
                pagesNewsLayout.addView(view);
            }
        }
    }

    /**
     * 最新内容
     */
    private void hotList() {
        if (hotList.size() > 8) {
            pagesHotTextMore.setVisibility(View.VISIBLE);
        } else {
            pagesHotTextMore.setVisibility(View.GONE);
        }
        if (hotList.size() > 0) {
            for (int i = 0; i < hotList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.pages_item_chose, null);
                LinearLayout lay = (LinearLayout) view.findViewById(R.id.pages_iten_layout);
                SimpleDraweeView pagesIcon = (SimpleDraweeView) view.findViewById(R.id.pages_item_icon);//头像
                final SimpleDraweeView pagesSubject = (SimpleDraweeView) view.findViewById(R.id.pages_item_subject);//大图
                TextView pagesTags = (TextView) view.findViewById(R.id.pages_tags_item_text); //标签
                TextView pagesTitle = (TextView) view.findViewById(R.id.pages_title_text);//标题
                ImageView pagesFileM = (ImageView) view.findViewById(R.id.pages_file_m);//视频
                ImageView pagesFileF = (ImageView) view.findViewById(R.id.pages_file_f);//文件
                TextView pagesItemNick = (TextView) view.findViewById(R.id.pages_item_nick_text);//昵称
                TextView pagesSign = (TextView) view.findViewById(R.id.pages_sign_item_text);//签名
                ImageView video = (ImageView) view.findViewById(R.id.play_video);
                Uri imageUri = Uri.parse(hotList.get(i).getAsString("icon"));
                ValidData.load(imageUri, pagesIcon, 30, 30);
                pagesItemNick.setText(hotList.get(i).getAsString("nick"));
                pagesTags.setText(hotList.get(i).getAsString("tags"));
                pagesTitle.setText(hotList.get(i).getAsString("title"));

                if (hotList.get(i).getAsInteger("file_type") == 0) {
                    pagesFileM.setVisibility(View.GONE);
                    pagesFileF.setVisibility(View.VISIBLE);
                } else if (hotList.get(i).getAsInteger("file_type") == 1) {
                    pagesFileM.setVisibility(View.VISIBLE);
                    pagesFileF.setVisibility(View.GONE);
                }

                if (hotList.get(i).getAsInteger("isSign") == 0) {
                    pagesSign.setVisibility(View.GONE);
                } else if (hotList.get(i).getAsInteger("isSign") == 1) {
                    pagesSign.setVisibility(View.VISIBLE);
                }

                if (hotList.get(i).getAsInteger("file_type") == 0) {
                    Uri imageUris = Uri.parse(hotList.get(i).getAsString("subject"));
                    ValidData.load(imageUris, pagesSubject, 100, 80);
                    video.setVisibility(View.GONE);
                } else {
                    Uri imageUris = Uri.parse(hotList.get(i).getAsString("video_img"));
                    ValidData.load(imageUris, pagesSubject, 100, 80);
                    video.setVisibility(View.VISIBLE);
                }
                final int type = hotList.get(i).getAsInteger("file_type");
                final String str = hotList.get(i).getAsString("nick");
                final Integer id = hotList.get(i).getAsInteger("id");
                final Integer uid = hotList.get(i).getAsInteger("uid");
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
                pagesHotLayout.addView(view);
            }

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 加载最顶端的数据
     */
    private void btnText(List<ContentValues> namesList) {
        if (namesList.size() > 0) {
            if (namesList.get(0).containsKey("name")) {
                radDynamicWu.setText(namesList.get(0).getAsString("name"));
                radDynamicWu.setTag(namesList.get(0).getAsInteger("id"));
                radDynamicWu.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_title));
                radDynamicGu.setText(namesList.get(1).getAsString("name"));
                radDynamicGu.setTag(namesList.get(1).getAsInteger("id"));
                radDynamicGu.setBackgroundColor(getResources().getColor(R.color.white));
                radDynamicYang.setText(namesList.get(2).getAsString("name"));
                radDynamicYang.setTag(namesList.get(2).getAsInteger("id"));
                radDynamicYang.setBackgroundColor(getResources().getColor(R.color.white));
                radDynamicJue.setText(namesList.get(3).getAsString("name"));
                radDynamicJue.setTag(namesList.get(3).getAsInteger("id"));
                radDynamicJue.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

    /**
     * 点击修改样式
     */
    private void btnTextFo() {
        radDynamicWu.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_title));
        radDynamicGu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicYang.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicJue.setBackgroundColor(getResources().getColor(R.color.white));
    }

    /**
     * 点击修改样式
     */
    private void btnTextOne() {
        radDynamicWu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicGu.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_title));
        radDynamicYang.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicJue.setBackgroundColor(getResources().getColor(R.color.white));
    }

    /**
     * 点击修改样式
     */
    private void btnTextTwo() {
        radDynamicWu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicGu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicYang.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_title));
        radDynamicJue.setBackgroundColor(getResources().getColor(R.color.white));
    }

    /**
     * 点击修改样式
     */
    private void btnTextSe() {
        radDynamicWu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicGu.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicYang.setBackgroundColor(getResources().getColor(R.color.white));
        radDynamicJue.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_title));
    }

}
