package cn.com.shequnew.pages.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.com.shequnew.pages.activity.AllCateActivity;
import cn.com.shequnew.pages.activity.MoreActivity;
import cn.com.shequnew.pages.activity.ShopDetailsActivity;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.pages.prompt.Loading;
import cn.com.shequnew.pages.view.SlideShowView;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class CommunityFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.sli_deal)
    SlideShowView sliDeal;
    @BindView(R.id.recommed_deal)
    LinearLayout recommedDeal;
    @BindView(R.id.new_deal)
    LinearLayout newDeal;
    @BindView(R.id.hot_deal)
    LinearLayout hotDeal;
    @BindView(R.id.sw_deal)
    SwipeRefreshLayout swDeal;
    Unbinder unbinder;
    @BindView(R.id.lin_allCate)
    LinearLayout linAllCate;
    @BindView(R.id.comm_new)
    LinearLayout commNew;
    @BindView(R.id.more_recommend)
    TextView moreRecommend;
    @BindView(R.id.more_new_good)
    TextView moreNewGood;
    @BindView(R.id.hot_more)
    TextView hotMore;

    private Context context;
    private List<ContentValues> recommendList = new ArrayList<>();
    private List<ContentValues> netList = new ArrayList<>();
    private List<ContentValues> hotList = new ArrayList<>();
    private List<ContentValues> carouselList = new ArrayList<>();
    private List<ContentValues> allcateList = new ArrayList<>();

    private String[] imageUrls;
    private String[] imageUris = new String[]{
            "http://www.baidu.com",
            "http://www.sina.com.cn",
            "http://www.taobao.com",
            "http://www.tudou.com"};
    private List<Map<String, String>> imageList = new ArrayList<Map<String, String>>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initView();
        initDelay();
        setDelayMessage(1, 100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.community_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        swDeal.setOnRefreshListener(this);

    }


    @OnClick(R.id.more_recommend)
    void moreRecomend() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("hot", "3");
        intent.putExtras(bundle);
        intent.setClass(context, MoreActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.more_new_good)
    void moreNewGood() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("hot", "2");
        intent.putExtras(bundle);
        intent.setClass(context, MoreActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.hot_more)
    void moreHot() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("hot", "4");
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
//                                getActivity(), commNew);
//                        mLoading.setText("正在加载......");
//                        mLoading.show();
                        new asyncTask().execute(1);
                        break;
                }
            }
        };
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            if (recommendList.size() > 0) {
                recommendList.clear();
            }
            if (netList.size() > 0) {
                netList.clear();
            }
            if (hotList.size() > 0) {
                hotList.clear();
            }
            if (hotList.size() > 0) {
                hotList.clear();
            }
            if (carouselList.size() > 0) {
                carouselList.clear();
            }
            if (allcateList.size() > 0) {
                allcateList.clear();
            }
            switch (params[0]) {
                case 1:
                    httpComm();
                    httpAllCate();
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
                    commend();
                    allcate();
                    swDeal.setRefreshing(false);//刷新完成
                    break;
            }

        }
    }


    private void allcate() {
        if (allcateList.size() > 0) {
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.chose_allcate, null);
                TextView commTitle = (TextView) view.findViewById(R.id.call_text);
                commTitle.setText(allcateList.get(i).getAsString("name"));
                final String name = allcateList.get(i).getAsString("name");
                final int type = allcateList.get(i).getAsInteger("id");
                commTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", type);
                        bundle.putString("name", name);
                        intent.putExtras(bundle);
                        intent.setClass(context, AllCateActivity.class);
                        context.startActivity(intent);
                    }
                });
                linAllCate.addView(view);
            }
        }
    }


    private void commend() {
        if (recommendList.size() > 8) {
            moreRecommend.setVisibility(View.VISIBLE);
        } else {
            moreRecommend.setVisibility(View.GONE);
        }

        if (recommendList.size() > 0) {
            for (int i = 0; i < recommendList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.commend_item_list, null);
                LinearLayout comm_layout = (LinearLayout) view.findViewById(R.id.comm_layout);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_images);
                TextView commTitle = (TextView) view.findViewById(R.id.comm_commodity_title);
                TextView commGrd = (TextView) view.findViewById(R.id.comm_commodity_grd);
                SimpleDraweeView commIcon = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_icon);
                TextView commNick = (TextView) view.findViewById(R.id.comm_commodity_nick);
                TextView commPrice = (TextView) view.findViewById(R.id.comm_commodity_price);

                Uri imageUri = Uri.parse(recommendList.get(i).getAsString("good_image"));
                ValidData.load(imageUri, simpleDraweeView, 100, 80);
                Uri imageIcon = Uri.parse(recommendList.get(i).getAsString("user_icon"));
                ValidData.load(imageIcon, commIcon, 30, 30);
                commTitle.setText(recommendList.get(i).getAsString("good_name"));
                commGrd.setText("工期：" + recommendList.get(i).getAsInteger("maf_time") + "天");
                commNick.setText(recommendList.get(i).getAsString("user_nick"));
                commPrice.setText("￥" + recommendList.get(i).getAsString("price"));
                final int id = recommendList.get(i).getAsInteger("id");
                final int uid = recommendList.get(i).getAsInteger("uid");
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
                recommedDeal.addView(view);
            }
        }
        if (netList.size() > 8) {
            moreNewGood.setVisibility(View.VISIBLE);
        } else {
            moreNewGood.setVisibility(View.GONE);
        }

        if (netList.size() > 0) {
            for (int i = 0; i < netList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.commend_item_list, null);
                LinearLayout comm_layout = (LinearLayout) view.findViewById(R.id.comm_layout);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_images);
                TextView commTitle = (TextView) view.findViewById(R.id.comm_commodity_title);
                TextView commGrd = (TextView) view.findViewById(R.id.comm_commodity_grd);
                SimpleDraweeView commIcon = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_icon);
                TextView commNick = (TextView) view.findViewById(R.id.comm_commodity_nick);
                TextView commPrice = (TextView) view.findViewById(R.id.comm_commodity_price);

                Uri imageUri = Uri.parse(netList.get(i).getAsString("good_image"));
                ValidData.load(imageUri, simpleDraweeView, 100, 80);
                Uri imageIcon = Uri.parse(netList.get(i).getAsString("user_icon"));
                ValidData.load(imageIcon, commIcon, 30, 30);
                commTitle.setText(netList.get(i).getAsString("good_name"));
                commGrd.setText("工期：" + netList.get(i).getAsInteger("maf_time") + "天");
                commNick.setText(netList.get(i).getAsString("user_nick"));
                commPrice.setText("￥" + netList.get(i).getAsString("price"));
                final int id = netList.get(i).getAsInteger("id");
                final int uid = netList.get(i).getAsInteger("uid");

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
                newDeal.addView(view);
            }
        }
        if (hotList.size() > 8) {
            hotMore.setVisibility(View.VISIBLE);
        } else {
            hotMore.setVisibility(View.GONE);
        }
        if (hotList.size() > 0) {
            for (int i = 0; i < hotList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.commend_item_list, null);
                LinearLayout comm_layout = (LinearLayout) view.findViewById(R.id.comm_layout);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_images);
                TextView commTitle = (TextView) view.findViewById(R.id.comm_commodity_title);
                TextView commGrd = (TextView) view.findViewById(R.id.comm_commodity_grd);
                SimpleDraweeView commIcon = (SimpleDraweeView) view.findViewById(R.id.comm_commodity_icon);
                TextView commNick = (TextView) view.findViewById(R.id.comm_commodity_nick);
                TextView commPrice = (TextView) view.findViewById(R.id.comm_commodity_price);

                Uri imageUri = Uri.parse(hotList.get(i).getAsString("good_image"));
                ValidData.load(imageUri, simpleDraweeView, 100, 80);
                Uri imageIcon = Uri.parse(hotList.get(i).getAsString("user_icon"));
                ValidData.load(imageIcon, commIcon, 30, 30);
                commTitle.setText(hotList.get(i).getAsString("good_name"));
                commGrd.setText("工期：" + hotList.get(i).getAsInteger("maf_time") + "天");
                commNick.setText(hotList.get(i).getAsString("user_nick"));
                commPrice.setText("￥" + hotList.get(i).getAsString("price"));

                final int id = hotList.get(i).getAsInteger("id");
                final int uid = hotList.get(i).getAsInteger("uid");
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
                hotDeal.addView(view);
            }
        }

        imageUrls = new String[carouselList.size()];
        if (carouselList.size() == 0) {
            sliDeal.setVisibility(View.GONE);
        }

        for (int j = 0; j < carouselList.size(); j++) {
            imageUrls[j] = carouselList.get(j).getAsString("img_add");
        }
        for (int i = 0; i < imageUrls.length; i++) {
            Map<String, String> image_uri = new HashMap<String, String>();
            image_uri.put("imageUrls", imageUrls[i]);
            image_uri.put("imageUris", imageUris[i]);
            imageList.add(image_uri);
        }
        sliDeal.setImageUrls(imageList);
    }

    private void httpAllCate() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.getAllCate");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAllcate(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlAllcate(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray objData = new JSONArray(obj.getString("data"));
            if (objData.length() > 0) {
                for (int i = 0; i < 5; i++) {
                    JSONObject jsonObj = objData.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("name", jsonObj.getString("name"));
                    cv.put("parent", jsonObj.getInt("parent"));
                    cv.put("image", jsonObj.getString("image"));
                    allcateList.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void httpComm() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Trade.getGoods");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlComm(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void xmlComm(String data) {

        try {
            JSONObject obj = new JSONObject(data);
            JSONObject objData = new JSONObject(obj.getString("data"));

            JSONArray jsonComm = new JSONArray(objData.getString("recommend"));
            JSONArray jsonNew = new JSONArray(objData.getString("new"));
            JSONArray jsonHot = new JSONArray(objData.getString("hot"));
            JSONArray jsonCarousel = new JSONArray(objData.getString("carousel"));

            if (jsonComm.length() > 0) {
                for (int i = 0; i < jsonComm.length(); i++) {
                    JSONObject jsonObj = jsonComm.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("good_intro", jsonObj.getString("good_intro"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("price", jsonObj.getString("price"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("origin", jsonObj.getInt("origin"));
                    cv.put("type", jsonObj.getInt("type"));
                    cv.put("recommend", jsonObj.getInt("recommend"));
                    cv.put("hot", jsonObj.getInt("hot"));
                    cv.put("new", jsonObj.getInt("new"));
                    cv.put("user_icon", jsonObj.getString("user_icon"));
                    cv.put("user_nick", jsonObj.getString("user_nick"));
                    recommendList.add(cv);
                }
            }

            if (jsonNew.length() > 0) {
                for (int i = 0; i < jsonNew.length(); i++) {
                    JSONObject jsonObj = jsonNew.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("good_intro", jsonObj.getString("good_intro"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("price", jsonObj.getString("price"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("origin", jsonObj.getInt("origin"));
                    cv.put("type", jsonObj.getInt("type"));
                    cv.put("recommend", jsonObj.getInt("recommend"));
                    cv.put("hot", jsonObj.getInt("hot"));
                    cv.put("new", jsonObj.getInt("new"));
                    cv.put("user_icon", jsonObj.getString("user_icon"));
                    cv.put("user_nick", jsonObj.getString("user_nick"));
                    netList.add(cv);
                }
            }
            if (jsonHot.length() > 0) {
                for (int i = 0; i < jsonHot.length(); i++) {
                    JSONObject jsonObj = jsonHot.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("uid", jsonObj.getInt("uid"));
                    cv.put("good_name", jsonObj.getString("good_name"));
                    cv.put("good_intro", jsonObj.getString("good_intro"));
                    cv.put("good_image", jsonObj.getString("good_image"));
                    cv.put("price", jsonObj.getString("price"));
                    cv.put("maf_time", jsonObj.getInt("maf_time"));
                    cv.put("origin", jsonObj.getInt("origin"));
                    cv.put("type", jsonObj.getInt("type"));
                    cv.put("recommend", jsonObj.getInt("recommend"));
                    cv.put("hot", jsonObj.getInt("hot"));
                    cv.put("new", jsonObj.getInt("new"));
                    cv.put("user_icon", jsonObj.getString("user_icon"));
                    cv.put("user_nick", jsonObj.getString("user_nick"));
                    hotList.add(cv);
                }
            }
            if (jsonCarousel.length() > 0) {
                for (int i = 0; i < jsonCarousel.length(); i++) {
                    JSONObject jsonObj = jsonCarousel.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsonObj.getInt("id"));
                    cv.put("img_add", jsonObj.getString("img_add"));
                    carouselList.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRefresh() {
//        setDelayMessage(1, 100);
        swDeal.setRefreshing(false);//刷新完成
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        System.gc();
    }
}