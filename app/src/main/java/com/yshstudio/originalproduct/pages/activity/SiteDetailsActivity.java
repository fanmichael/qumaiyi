package com.yshstudio.originalproduct.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.umeng.socialize.utils.Log;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.inc.Ini;
import com.yshstudio.originalproduct.pages.adapter.SiteDetailsAdapter;
import com.yshstudio.originalproduct.pages.config.AppContext;
import com.yshstudio.originalproduct.pages.http.APIService;
import com.yshstudio.originalproduct.pages.http.HttpConnectTool;
import com.yshstudio.originalproduct.pages.http.RetrofitUtils;
import com.yshstudio.originalproduct.tools.LogUtils;
import com.yshstudio.originalproduct.tools.SharedPreferenceUtil;

/**
 * 我的地址
 */
public class SiteDetailsActivity extends BaseActivity implements SiteDetailsAdapter.setOnClickLoction {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.site_address_list)
    ListView siteAddressList;

    private Context context;
    private List<ContentValues> address = new ArrayList<>();
    private SiteDetailsAdapter adapter;
    private int updaid;
    private int deleteid;
    private String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_details);
        ButterKnife.bind(this);
        context = this;
        initView();
        initList();
        new asyncTask().execute(1);
    }

    private void initList() {
        adapter = new SiteDetailsAdapter(address, context, this);
        siteAddressList.setAdapter(adapter);
    }


    private void initView() {
        topTitle.setText("我的地址");
        topRegitTitle.setText("新建");
        topRegitTitle.setVisibility(View.VISIBLE);

        Bundle bundle=this.getIntent().getExtras();
        if(bundle.containsKey("type")){
            type=bundle.getString("type");
        }

    }

    @OnClick(R.id.top_regit_title)
    void addAddress() {
        Intent intent = new Intent(context, NewSiteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "add");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            new asyncTask().execute(1);
        }
    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @Override
    public void radio(int posit, int id) {
        updaid = id;
        new asyncTask().execute(3);
    }

    @Override
    public void delete(int posit, int id) {
        deleteid = id;
        new asyncTask().execute(2);
    }

    @Override
    public void update(int posit, int id, int uid, String name, String mobile, String address) {
        Intent intent = new Intent(context, NewSiteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "edit");
        bundle.putInt("id", id);
        bundle.putInt("uid", uid);
        bundle.putString("name", name);
        bundle.putString("mobile", mobile);
        bundle.putString("address", address);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    public void linCh(int posit) {
        if(type.equals("pay")){
            Bundle bundle=new Bundle();
            bundle.putInt("id",address.get(posit).getAsInteger("id"));
            bundle.putInt("uid",address.get(posit).getAsInteger("uid"));
            bundle.putString("name",address.get(posit).getAsString("name"));
            bundle.putString("mobile",address.get(posit).getAsString("mobile"));
            bundle.putString("address",address.get(posit).getAsString("address"));
            Intent intent=new Intent(SiteDetailsActivity.this,IndentActivity.class);
            intent.putExtras(bundle);
            setResult(1,intent);
            destroyActitity();
        }else{
            return;
        }

    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();

            if (address == null) {
                address = new ArrayList<>();
            } else {
                address.clear();
            }

            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpAddressDetails();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpAddressDetailsDelete();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    httpAddressDetailsChose();
                    bundle.putInt("what", 2);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            // removeLoading();
            switch (what) {
                case 1:
                    //初始加载数据
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    new asyncTask().execute(1);
                    break;

            }

        }
    }


    private void httpAddressDetailsChose() {//加载所有数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Address.action");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("id", updaid + "");
            map.put("type", "setdefault");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAddress(json);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void httpAddressDetailsDelete() {//加载所有数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Address.action");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            map.put("id", "" + deleteid);
            map.put("type", "del");
            String json = HttpConnectTool.post(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void httpAddressDetails() {//加载所有数据
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "Address.allAddress");
            map.put("uid", SharedPreferenceUtil.read("id","") + "");
            String json = HttpConnectTool.post(map);
            if (!json.equals("")) {
                xmlAddress(json);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xmlAddress(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsdata = jsonArray.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("id", jsdata.getInt("id"));
                    cv.put("uid", jsdata.getInt("uid"));
                    cv.put("state", jsdata.getInt("state"));
                    cv.put("name", jsdata.getString("name"));
                    cv.put("mobile", jsdata.getString("mobile"));
                    cv.put("address", jsdata.getString("address"));
                    address.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception er) {
            er.printStackTrace();
        }

    }

    /**
     * 请求方式
     * */
    private void httpAdress(){
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "Address.allAddress");
        map.put("uid", SharedPreferenceUtil.read("id","") + "");
        APIService apiService = RetrofitUtils.getApiService();
        rx.Observable<String> observable = apiService.getSalewellPrinterIs(Ini.Url+"?", map);
        observable.compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.logD("ss", "onCompleted ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.logD("ss", "onCompleted "+e);
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.logD("ss", "onCompleted "+s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsdata = jsonArray.getJSONObject(i);
                                    ContentValues cv = new ContentValues();
                                    cv.put("id", jsdata.getInt("id"));
                                    cv.put("uid", jsdata.getInt("uid"));
                                    cv.put("state", jsdata.getInt("state"));
                                    cv.put("name", jsdata.getString("name"));
                                    cv.put("mobile", jsdata.getString("mobile"));
                                    cv.put("address", jsdata.getString("address"));
                                    address.add(cv);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                });


    }






}
